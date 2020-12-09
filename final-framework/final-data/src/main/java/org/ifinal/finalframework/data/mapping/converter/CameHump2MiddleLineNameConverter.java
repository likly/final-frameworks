package org.ifinal.finalframework.data.mapping.converter;

import org.ifinal.finalframework.data.annotation.NameConverter;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class CameHump2MiddleLineNameConverter implements NameConverter {
    @Override
    public String convert(String name) {
        StringBuilder sb = new StringBuilder(name);
        int temp = 0;//定位
        for (int i = 1; i < name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i)) && !Character.isUpperCase(name.charAt(i - 1))) {
                sb.insert(i + temp, "-");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }
}
