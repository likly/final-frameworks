package org.finalframework.auto.query.processor;


import org.finalframework.auto.data.Entity;
import org.finalframework.auto.data.EntityFactory;
import org.finalframework.auto.data.Property;
import org.finalframework.auto.query.Utils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-26 14:07:01
 * @since 1.0
 */
public final class QEntityFactory {

    private static final String ENTITY_PREFIX = "Q";


    private QEntityFactory() {
    }

    public static QEntity create(ProcessingEnvironment processingEnv, String packageName, Entity entity) {
        final String entityName = entity.getSimpleName();
        final QEntity.Builder builder = new QEntity.Builder(entity);
        builder.packageName(packageName)
                .name(ENTITY_PREFIX + entityName);
        entity.stream()
                .filter(property -> !property.isTransient())
                .forEach(property -> {
                    if (property.isReference()) {
                        TypeElement multiElement = property.getJavaTypeElement();
                        Entity multiEntity = EntityFactory.create(processingEnv, multiElement);
                        final List<String> properties = property.referenceProperties();
                        properties.stream()
                                .map(multiEntity::getRequiredProperty)
                                .map(multiProperty -> buildProperty(property, multiProperty))
                                .forEach(builder::addProperty);
                    } else {
                        builder.addProperty(buildProperty(null, property));
                    }

                });

        return builder.build();
    }


    private static QProperty buildProperty(@Nullable Property referenceProperty, @NonNull Property property) {
        if (referenceProperty == null) {
            return QProperty.builder(property.getName(), Utils.formatPropertyName(null, property))
                    .type(property.getJavaTypeElement())
                    .idProperty(property.isIdProperty())
                    .column(Utils.formatPropertyColumn(null, property))
                    .insertable(property.isWriteable())
                    .updatable(property.isModifiable())
                    .selectable(!property.isTransient() && !property.isWriteOnly() && !property.isVirtual())
                    .build();
        } else {
            final String path = referenceProperty.getName() + "." + property.getName();
            final String name = Utils.formatPropertyName(referenceProperty, property);
            return QProperty.builder(path, name)
                    .column(Utils.formatPropertyColumn(referenceProperty, property))
                    .type(property.getJavaTypeElement())
                    .idProperty(false)
                    .insertable(property.isWriteable())
                    .updatable(property.isModifiable())
                    .selectable(!referenceProperty.isTransient() && !referenceProperty.isWriteOnly() && !referenceProperty.isVirtual())
                    .build();
        }

    }
}

