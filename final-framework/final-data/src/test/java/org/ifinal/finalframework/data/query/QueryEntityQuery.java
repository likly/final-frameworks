package org.ifinal.finalframework.data.query;


import lombok.Data;
import org.ifinal.finalframework.query.annotation.Equal;
import org.ifinal.finalframework.query.annotation.Or;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@Or
public class QueryEntityQuery {
    @Equal
    private String name;
    @Equal
    private Integer age;
}

