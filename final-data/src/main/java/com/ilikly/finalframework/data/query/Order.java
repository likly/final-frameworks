package com.ilikly.finalframework.data.query;

import com.ilikly.finalframework.data.query.enums.Direction;

/**
 * @author likly
 * @version 1.0
 * @date 2018-10-25 11:15
 * @since 1.0
 */
public interface Order {
    static Order order(QProperty property, Direction direction) {
        return new OrderImpl(property, direction);
    }

    static Order asc(QProperty property) {
        return order(property, Direction.ASC);
    }

    static Order desc(QProperty property) {
        return order(property, Direction.DESC);
    }

    QProperty getProperty();

    Direction getDirection();


}
