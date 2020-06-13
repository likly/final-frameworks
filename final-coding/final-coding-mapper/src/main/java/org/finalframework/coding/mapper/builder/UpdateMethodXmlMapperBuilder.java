/*
 * Copyright (c) 2018-2020.  the original author or authors.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.finalframework.coding.mapper.builder;

import org.finalframework.coding.entity.Entity;
import org.finalframework.coding.entity.Property;
import org.finalframework.coding.mapper.TypeHandlers;
import org.finalframework.data.query.enums.UpdateOperation;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author likly
 * @version 1.0
 * @date 2019-10-12 10:29:21
 * @since 1.0
 */
public class UpdateMethodXmlMapperBuilder extends AbsMethodXmlMapperBuilder {

    private static final String SQL_UPDATE = "sql-update";

    public UpdateMethodXmlMapperBuilder(TypeHandlers typeHandlers) {
        super(typeHandlers);
    }

    @Override
    public boolean supports(ExecutableElement method) {
        return !method.isDefault() && "update".equals(method.getSimpleName().toString());
    }


    @Override
    protected Element buildMethodElement(ExecutableElement method, Document document, Entity entity) {
        /*
         *      <update id="update">
         *         UPDATE
         *         <include refid="sql-table"/>
         *         <include refid="sql-update"/>
         *         <where>
         *             <choose>
         *                 <when test="ids != null">
         *                     id IN
         *                     <foreach collection="ids" item="id" open="(" separator="," close=")">
         *                         #{id}
         *                     </foreach>
         *                 </when>
         *                 <when test="query != null">
         *                     #{query.sql}
         *                 </when>
         *             </choose>
         *         </where>
         *     </update>
         */
        final Element update = document.createElement("update");
        update.setAttribute("id", method.getSimpleName().toString());
        Element trim = document.createElement("trim");
        trim.setAttribute("prefix", "UPDATE");

        trim.appendChild(include(document, SQL_TABLES));
        trim.appendChild(include(document, SQL_UPDATE));
        trim.appendChild(where(document, whenIdsNotNull(document, entity), whenQueryNotNull(document)));
        update.appendChild(trim);
        return update;
    }

    @Override
    protected Collection<Element> buildMethodFragments(Document document, ExecutableElement method, Entity entity) {
        return Collections.singletonList(appendUpdateSqlFragment(document, entity));
    }

    @SuppressWarnings("all")
    private Element appendUpdateSqlFragment(@NonNull Document document, @NonNull Entity entity) {
        /*
         *      <sql id="sql-update">
         *         <set>
         *             <choose>
         *                 <when test="entity != null and selective == true">
         *                     <if test="entity.property != null">
         *                         column = #{entity.property,javaType=,typeHandler},
         *                     </if>
         *                     <if test="entity.property != null and entity.property.property != null">
         *                         column = #{entity.property.property,javaType=,typeHandler},
         *                     </if>
         *                 </when>
         *                 <when test="entity != null">
         *                     column = #{entity.property,javaType=,typeHandler},
         *                     <if test="entity.property != null">
         *                         column = #{entity.property.property,javaType=,typeHandler},
         *                     </if>
         *                 </when>
         *                 <when test="update != null">
         *                     <if test="update.contains('property')">
         *                         <choose>
         *                             <when test="update.getUpdateSet('property').action.name() == 'EQUAL'">
         *                                 column = #{update.getUpdateSet('property').value,javaType=,typeHandler=},
         *                             </when>
         *                             <when test="update.getUpdateSet('property').action.name() == 'INC'">
         *                                 column = column + 1,
         *                             </when>
         *                             <when test="update.getUpdateSet('property').action.name() == 'INCR'">
         *                                 column = column + #{update.getUpdateSet('property').value},
         *                             </when>
         *                             <when test="update.getUpdateSet('property').action.name() == 'DEC'">
         *                                 column = column - 1,
         *                             </when>
         *                             <when test="update.getUpdateSet('property').action.name() == 'DECR'">
         *                                 column = column - #{update.getUpdateSet('property').value},
         *                             </when>
         *                         </choose>
         *                     </if>
         *                     <if test="update.contains('property') and update.getUpdateSet('property').value.property != null">
         *                          column = #{update.getUpdateSet('property').value.property,javaType=,typeHandler=},
         *                     </if>
         *                 </when>
         *             </choose>
         *         </set>
         *     </sql>
         */
        //<sql id="update">
        final Element sql = document.createElement("sql");
        sql.setAttribute("id", SQL_UPDATE);
        //<set>
        final Element set = document.createElement("set");
        //      <choose>
        //          <when test="entity != null">
        //          <when test="entity != null and selective == true">
        //          <when test="update != null">
        //      </choose>


        Element choose = choose(document, Arrays.asList(
                whenEntityNotNull(document, entity, true),
                whenEntityNotNull(document, entity, false),
                whenUpdateNotNull(document, entity)
        ));

        Property versionProperty = entity.getVersionProperty();

        if (versionProperty != null && !versionProperty.isTransient() && !versionProperty.isReadOnly() && !versionProperty.isFinal() && !versionProperty.isVirtual()) {
            Element trim = document.createElement("trim");
            String column = typeHandlers.formatPropertyColumn(entity, null, versionProperty);
            trim.setAttribute("suffixOverrides", ",");
            trim.setAttribute("suffix", String.format(",%s = %s + 1", column, column));
            trim.appendChild(choose);
            set.appendChild(trim);
        } else {
            set.appendChild(choose);
        }
        sql.appendChild(set);


        //</set>
        //</sql>
        return sql;
    }

    private Element whenEntityNotNull(@NonNull Document document, @NonNull Entity entity, boolean selective) {
        //        <when test="entity != null and selective == true">
        //              <choose>
        //                  <when test="view != null and view == 'view'.toString()">
        //                  </when>
        //              </choose>
        //          </when>
        final String test = selective ? "entity != null and selective == true" : "entity != null";

        final List<Element> views = entity.getViews().stream().map(view -> buildEntitySelectiveViewElement(document, entity, selective, view)).collect(Collectors.toList());
        views.add(buildEntitySelectiveViewElement(document, entity, selective, null));
        final Element choose = choose(document, views);

        return whenOrOtherwise(document, test, choose);
    }

    private Element buildEntitySelectiveViewElement(@NonNull Document document, @NonNull Entity entity, boolean selective, @Nullable TypeElement view) {

        final Element whenOrOtherwise = document.createElement("when");

        final String test = view == null ? "view == null" : String.format("view != null and view.getCanonicalName() == '%s'.toString()", view.getQualifiedName().toString());
        whenOrOtherwise.setAttribute("test", test);

        entity.stream()
                .filter(it -> it.isModifiable() && (view == null || it.hasView(view)))
                .forEach(property -> {
                    if (property.isReference()) {
                        /**
                         * <if test="entity.property != null and entity.property.property != null">
                         *     column = #{entity.property.property,javaType=,typeHandler},
                         * </if>
                         */
                        final TypeElement multiType = property.getJavaTypeElement();
                        final Entity multiEntity = property.toEntity();
                        List<String> properties = property.referenceProperties();
                        properties.stream()
                                .map(multiEntity::getProperty)
                                .map(multiProperty -> {

//                                    ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(multiProperty);
//                                    final String column = columnGenerator.generateWriteColumn(property.getTable(), property, multiProperty);
//                                    final String value = columnGenerator.generateWriteValue(property, multiProperty, "entity");

                                    final String column = typeHandlers.formatPropertyWriteColumn(entity, property, multiProperty);
                                    final String value = typeHandlers.formatPropertyValues(property, multiProperty, "entity");


                                    final Element ifPropertyNotNull = document.createElement("if");
                                    final String ifTest = selective ? String.format("entity.%s != null and entity.%s.%s != null", property.getName(), property.getName(), multiProperty.getName())
                                            : String.format("entity.%s != null", property.getName());
                                    ifPropertyNotNull.setAttribute("test", ifTest);
                                    ifPropertyNotNull.appendChild(textNode(document, String.format("%s = %s,", column, value)));
                                    return ifPropertyNotNull;
                                }).forEach(whenOrOtherwise::appendChild);
                    } else {
                        /**
                         * <if test="entity.property != null">
                         *     column = #{entity.property,javaType=,typeHandler=}
                         * </if>
                         */

//                        ColumnGenerator columnGenerator = Utils.getPropertyColumnGenerator(property);
//                        final String column = columnGenerator.generateWriteColumn(property.getTable(), null, property);
//                        final String value = columnGenerator.generateWriteValue(null, property, "entity");

                        final String column = typeHandlers.formatPropertyWriteColumn(entity, null, property);
                        final String value = typeHandlers.formatPropertyValues(null, property, "entity");

                        if (selective) {
                            final Element ifPropertyNotNull = document.createElement("if");
                            final String ifTest = String.format("entity.%s != null", property.getName());
                            ifPropertyNotNull.setAttribute("test", ifTest);
                            ifPropertyNotNull.appendChild(textNode(document, String.format("%s = %s,", column, value)));
                            whenOrOtherwise.appendChild(ifPropertyNotNull);
                        } else {
                            whenOrOtherwise.appendChild(textNode(document, String.format("%s = %s,", column, value)));
                        }
                    }

                });

        return whenOrOtherwise;
    }

    private Element whenUpdateNotNull(@NonNull Document document, @NonNull Entity entity) {
        //        <when test="update != null">
        final Element whenUpdateNotNull = document.createElement("when");
        whenUpdateNotNull.setAttribute("test", "update != null");


        final Element foreach = document.createElement("foreach");

        foreach.setAttribute("collection", "update");
        foreach.setAttribute("item", "item");
        foreach.setAttribute("separator", ",");


        final Element choose = document.createElement("choose");

        Stream.of(
                whenOrOtherwise(document, "item.operation.name() == 'EQUAL'", textNode(document, "${item.updateTarget} = ${item.updateValue}")),
                whenOrOtherwise(document, "item.operation.name() == 'INC'", textNode(document, "${item.updateTarget} = ${item.updateTarget} + 1")),
                whenOrOtherwise(document, "item.operation.name() == 'INCR'", textNode(document, "${item.updateTarget} = ${item.updateTarget} + ${item.updateValue}")),
                whenOrOtherwise(document, "item.operation.name() == 'DEC'", textNode(document, "${item.updateTarget} = ${item.updateTarget} - 1")),
                whenOrOtherwise(document, "item.operation.name() == 'DECR'", textNode(document, "${item.updateTarget} = ${item.updateTarget} - ${item.updateValue}"))
        ).forEach(choose::appendChild);


        foreach.appendChild(choose);


        whenUpdateNotNull.appendChild(foreach);


//        entity.stream().filter(Property::isModifiable)
//                .forEach(property -> {
//
//                    /*
//                     * <if test="update.contains('property') and update['property'].value.property != null">
//                     *      column = #{update[property].value.property,javaType=,typeHandler=}
//                     * </if>
//                     */
//                    if (property.isReference()) {
//                        final Entity multiEntity = property.toEntity();
//                        List<String> properties = property.referenceProperties();
//                        properties.stream()
//                                .map(multiEntity::getProperty)
//                                .map(multiProperty -> {
//                                    final TypeElement javaType = multiProperty.getJavaTypeElement();
//                                    final TypeElement typeHandler = Optional.ofNullable(property.getTypeHandler()).orElse(typeHandlers.getTypeHandler(property));
//
//                                    final Element ifUpdateContains = document.createElement("if");
//
//                                    final String name = multiProperty.isIdProperty() ? property.getName()
//                                            : property.getName() + multiProperty.getName().substring(0, 1).toUpperCase() + multiProperty.getName().substring(1);
//
//                                    final String column = typeHandlers.formatPropertyWriteColumn(property, multiProperty);
//                                    final String ifTest = String.format("update['%s'] != null", name);
//                                    ifUpdateContains.setAttribute("test", ifTest);
//
//                                    List<Element> whenElements = Arrays.stream(UpdateOperation.values())
//                                            .map(operation -> {
//                                                final String whenTest = String.format("update['%s'].operation.name() == '%s'", name, operation.name());
//                                                String updateSql = null;
//                                                switch (operation) {
//                                                    case EQUAL:
//                                                        updateSql = typeHandler == null ?
//                                                                String.format("%s = #{update[%s].value},", column, name)
//                                                                : String.format("%s = #{update[%s].value,javaType=%s,typeHandler=%s},",
//                                                                column, name, javaType.getQualifiedName().toString(), typeHandler.getQualifiedName().toString());
//                                                        break;
//                                                    case INC:
//                                                        updateSql = String.format("%s = %s + 1,", column, column);
//                                                        break;
//                                                    case INCR:
//                                                        updateSql = String.format("%s = %s + #{update[%s].value},", column, column, name);
//                                                        break;
//                                                    case DEC:
//                                                        updateSql = String.format("%s = %s - 1,", column, column);
//                                                        break;
//                                                    case DECR:
//                                                        updateSql = String.format("%s = %s - #{update[%s].value},", column, column, name);
//                                                        break;
//                                                }
//                                                return whenOrOtherwise(document, whenTest, textNode(document, updateSql));
//                                            }).collect(Collectors.toList());
//                                    ifUpdateContains.appendChild(choose(document, whenElements));
//                                    return ifUpdateContains;
//                                }).forEach(whenUpdateNotNull::appendChild);
//
//                    } else {
//                        final TypeElement javaType = property.getJavaTypeElement();
//                        final TypeElement typeHandler = Optional.ofNullable(property.getTypeHandler()).orElse(typeHandlers.getTypeHandler(property));
//                        final Element ifUpdateContains = document.createElement("if");
//                        final String updatePath = property.getName();
//                        final String ifTest = String.format("update['%s'] != null", updatePath);
//                        ifUpdateContains.setAttribute("test", ifTest);
//
//                        ifUpdateContains.setAttribute("test", ifTest);
//                        final String multiColumn = typeHandlers.formatPropertyWriteColumn(null, property);
//
//                        List<Element> whenElements = Arrays.stream(UpdateOperation.values())
//                                .map(operation -> {
//                                    final String whenTest = String.format("update['%s'].operation.name() == '%s'", updatePath, operation.name());
//                                    String updateSql = null;
//                                    switch (operation) {
//                                        case EQUAL:
//                                            updateSql = typeHandler == null ?
//                                                    String.format("%s = #{update[%s].value},", multiColumn, updatePath)
//                                                    : String.format("%s = #{update[%s].value,javaType=%s,typeHandler=%s},",
//                                                    multiColumn, updatePath, javaType.getQualifiedName().toString(), typeHandler.getQualifiedName().toString());
//                                            break;
//                                        case INC:
//                                            updateSql = String.format("%s = %s + 1,", multiColumn, multiColumn);
//                                            break;
//                                        case INCR:
//                                            updateSql = String.format("%s = %s + #{update[%s].value},", multiColumn, multiColumn, updatePath);
//                                            break;
//                                        case DEC:
//                                            updateSql = String.format("%s = %s - 1,", multiColumn, multiColumn);
//                                            break;
//                                        case DECR:
//                                            updateSql = String.format("%s = %s - #{update[%s].value},", multiColumn, multiColumn, updatePath);
//                                            break;
//                                    }
//                                    return whenOrOtherwise(document, whenTest, textNode(document, updateSql));
//                                }).collect(Collectors.toList());
//                        ifUpdateContains.appendChild(choose(document, whenElements));
//
//                        whenUpdateNotNull.appendChild(ifUpdateContains);
//                    }
//                });
        //        </when>
        return whenUpdateNotNull;
    }
}
