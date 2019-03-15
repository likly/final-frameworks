package com.ilikly.finalframework.spring.web.converter;


import com.ilikly.finalframework.data.entity.enums.IEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-11 15:56:02
 * @since 1.0
 */
public class String2EnumConverterFactory implements ConverterFactory<String, IEnum> {

    @Override
    public <T extends IEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new String2EnumConverter<>(targetType);
    }
}
