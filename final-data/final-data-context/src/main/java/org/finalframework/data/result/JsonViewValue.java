package org.finalframework.data.result;


import java.io.Serializable;

/**
 * @author likly
 * @version 1.0
 * @date 2019-12-06 10:32:29
 * @since 1.0
 */
public class JsonViewValue<T> implements Viewable, Serializable {
    private static final long serialVersionUID = -4251222094454545408L;
    private final T value;
    private final Class<?> view;

    public JsonViewValue(T value, Class<?> view) {
        this.value = value;
        this.view = view;
    }

    public T getValue() {
        return value;
    }

    public Class<?> getView() {
        return view;
    }

}

