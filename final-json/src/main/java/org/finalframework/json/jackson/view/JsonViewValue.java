package org.finalframework.json.jackson.view;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

/**
 * @author likly
 * @version 1.0
 * @date 2019-12-06 10:32:29
 * @see JsonView
 * @since 1.0
 */
@Data
public class JsonViewValue implements Viewable<Object> {
    private final Object value;
    private final Class<?> view;

    public JsonViewValue(Object value, Class<?> view) {
        this.value = value;
        this.view = view;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Class<?> getView() {
        return view;
    }

}

