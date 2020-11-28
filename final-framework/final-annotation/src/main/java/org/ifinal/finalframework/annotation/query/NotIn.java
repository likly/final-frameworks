package org.ifinal.finalframework.annotation.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Criterion(NotIn.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotIn {
    String property() default "";

    String[] value() default {
            "   <if test=\"${value} != null\">",
            "       <foreach collection=\"${value}\" item=\"item\" open=\" ${andOr} ${column} NOT IN (\" close=\")\" separator=\",\">#{item}</foreach>",
            "   </if>"
    };

    Class<?> javaType() default Object.class;
}