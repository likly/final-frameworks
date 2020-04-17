package org.finalframework.data.query;

import org.finalframework.data.mapping.Entity;
import org.finalframework.data.mapping.MappingUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-21 10:34:30
 * @since 1.0
 */
public class AbsQEntity<ID extends Serializable, T> implements QEntity<ID, T> {

    private final List<QProperty<?>> properties = new ArrayList<>();
    private final Map<String, QProperty<?>> pathProperties = new HashMap<>();

    private final Class<T> type;
    private final String table;

    public AbsQEntity(Class<T> type) {
        this(type, null);
    }

    public AbsQEntity(Class<T> type, String table) {
        this.type = type;
        this.table = table;
        this.initProperties();
    }

    protected void initProperties() {
        Entity.from(type)
                .stream()
                .filter(it -> !it.isTransient())
                .forEach(property -> {
                    if (property.isReference()) {

                        final Entity<?> referenceEntity = Entity.from(property.getType());

                        property.getReferenceProperties()
                                .stream()
                                .map(referenceEntity::getRequiredPersistentProperty)
                                .forEach(referenceProperty -> {
                                    addProperty(
                                            QProperty.builder(this, referenceProperty)
                                                    .path(property.getName() + "." + referenceProperty.getName())
                                                    .name(MappingUtils.formatPropertyName(property, referenceProperty))
                                                    .column(MappingUtils.formatColumn(property, referenceProperty))
//                                                    .idProperty(property.isIdProperty())
                                                    .build()
                                    );
                                });


                    } else {
                        addProperty(
                                QProperty.builder(this, property)
                                        .path(property.getName())
                                        .name(property.getName())
                                        .column(MappingUtils.formatColumn(null, property))
                                        .idProperty(property.isIdProperty())
                                        .build()
                        );
                    }
                });
    }

    public void addProperty(QProperty<?> property) {
        this.properties.add(property);
        this.pathProperties.put(property.getPath(), property);
    }

    @Override
    public String getTable() {
        return this.table;
    }

    @Override
    public Class<T> getType() {
        return this.type;
    }

    @Override
    public QProperty<?> getProperty(String path) {
        return pathProperties.get(path);
    }

    @Override
    public Iterator<QProperty<?>> iterator() {
        return properties.iterator();
    }

    @Override
    public Stream<QProperty<?>> stream() {
        return properties.stream();
    }
}
