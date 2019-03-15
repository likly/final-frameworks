package com.ilikly.finalframework.data.query.builder;

import com.ilikly.finalframework.core.Builder;
import com.ilikly.finalframework.data.query.QProperty;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-06 14:26:19
 * @since 1.0
 */
public interface SqlBuilder<T> extends Builder<String> {


    default String formatProperty(QProperty property) {
        return String.format("%s.%s", property.getTable(), property.getColumn());
    }


}
