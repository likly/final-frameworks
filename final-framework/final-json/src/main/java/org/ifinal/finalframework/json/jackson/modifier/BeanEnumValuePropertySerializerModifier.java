package org.ifinal.finalframework.json.jackson.modifier;


import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.ifinal.finalframework.auto.service.annotation.AutoService;
import org.ifinal.finalframework.data.annotation.EnumValue;
import org.ifinal.finalframework.json.jackson.serializer.EnumValueDescSerializer;
import org.ifinal.finalframework.json.jackson.serializer.EnumValueNameSerializer;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@AutoService(BeanSerializerModifier.class)
public class BeanEnumValuePropertySerializerModifier extends AbsBeanPropertySerializerModifier {

    private static final String ENUM_NAME_PROPERTY_SUFFIX = "Name";
    private static final String ENUM_DESC_PROPERTY_SUFFIX = "Desc";

    @Override
    public boolean support(BeanPropertyDefinition property) {
        return property.getField() != null && property.getField().hasAnnotation(EnumValue.class);
    }

    @Override
    public Collection<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, BeanPropertyDefinition property, BeanPropertyWriter writer) {
        BeanPropertyWriter enumNamePropertyWriter = buildEnumNamePropertyWriter(beanDesc, property, writer);
        BeanPropertyWriter enumDescPropertyWriter = buildEnumDescPropertyWriter(beanDesc, property, writer);
        return Arrays.asList(enumNamePropertyWriter, enumDescPropertyWriter);
    }

    private BeanPropertyWriter buildEnumNamePropertyWriter(BeanDescription beanDesc, BeanPropertyDefinition property, BeanPropertyWriter writer) {
        BeanPropertyWriter enumNamePropertyWriter = new BeanPropertyWriter(property,
                writer.getMember(), beanDesc.getClassAnnotations(), property.getPrimaryType(),
                new EnumValueNameSerializer(property.getField().getAnnotation(EnumValue.class)), writer.getTypeSerializer(), writer.getSerializationType(),
                writer.willSuppressNulls(), null, property.findViews());

        setNameValue(enumNamePropertyWriter, enumNamePropertyWriter.getName() + ENUM_NAME_PROPERTY_SUFFIX);
        return enumNamePropertyWriter;
    }

    private BeanPropertyWriter buildEnumDescPropertyWriter(BeanDescription beanDesc, BeanPropertyDefinition property, BeanPropertyWriter writer) {
        BeanPropertyWriter enumDescriptionPropertyWriter = new BeanPropertyWriter(property,
                writer.getMember(), beanDesc.getClassAnnotations(), property.getPrimaryType(),
                new EnumValueDescSerializer(property.getField().getAnnotation(EnumValue.class)), writer.getTypeSerializer(), writer.getSerializationType(),
                writer.willSuppressNulls(), null, property.findViews());

        setNameValue(enumDescriptionPropertyWriter, enumDescriptionPropertyWriter.getName() + ENUM_DESC_PROPERTY_SUFFIX);
        return enumDescriptionPropertyWriter;
    }
}

