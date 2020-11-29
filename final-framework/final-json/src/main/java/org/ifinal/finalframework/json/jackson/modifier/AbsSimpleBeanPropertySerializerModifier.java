package org.ifinal.finalframework.json.jackson.modifier;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import org.springframework.lang.NonNull;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbsSimpleBeanPropertySerializerModifier<T> extends AbsBeanPropertySerializerModifier {



    @Override
    public boolean support(@NonNull BeanPropertyDefinition property) {
        return support(property.getRawPrimaryType());
    }


    protected abstract boolean support(Class<?> clazz);
}
