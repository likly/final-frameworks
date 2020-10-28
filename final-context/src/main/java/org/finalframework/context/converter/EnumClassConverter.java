package org.finalframework.context.converter;

import org.finalframework.auto.spring.factory.annotation.SpringComponent;
import org.finalframework.util.Asserts;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0
 * @date 2020-05-08 15:44:33
 * @since 1.0
 */
@SpringComponent
public class EnumClassConverter implements ClassConverter<Enum<?>, List<Map<String, Object>>> {

    private final Map<Class<?>, EnumConverter<?>> enumConverterMap = new HashMap<>();

    public EnumClassConverter(ObjectProvider<List<EnumConverter<?>>> enumConvertersProvider) {
        final List<EnumConverter<?>> enumConverters = enumConvertersProvider == null ? null : enumConvertersProvider.getIfAvailable();

        if (Asserts.isEmpty(enumConverters)) {
            enumConverterMap.put(Enum.class, new IEnumConverter<>());
        } else {
            for (EnumConverter<?> enumConverter : enumConverters) {
                final EnumTarget annotation = enumConverter.getClass().getAnnotation(EnumTarget.class);
                enumConverterMap.put(annotation.value(), enumConverter);
            }
        }


    }

    @Override
    public List<Map<String, Object>> convert(Class<Enum<?>> source) {
        EnumConverter<?> converter = enumConverterMap.get(source);

        if (converter == null) {
            converter = enumConverterMap.get(Enum.class);
        }

        EnumConverter<Object> finalConverter = (EnumConverter<Object>) converter;
        return Arrays.stream(source.getEnumConstants())
                .map(finalConverter::convert)
                .collect(Collectors.toList());


    }
}
