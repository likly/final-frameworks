package org.finalframework.data.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 创建时间
 *
 * @author likly
 * @version 1.0
 * @date 2018-10-15 15:14
 * @see Column
 * @see Created
 * @see LastModifier
 * @see LastModified
 * @since 1.0
 */
@Column
@Default
@Documented
@Index(Integer.MAX_VALUE - 100)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Creator {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}