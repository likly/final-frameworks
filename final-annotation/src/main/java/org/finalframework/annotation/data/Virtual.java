package org.finalframework.annotation.data;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * Marked the element is a {@literal virtual} property, it's value generated by a {@linkplain Json json}.
 * A {@linkplain Virtual} column is normal annotated on a {@linkplain Method getter} method.
 *
 * <pre class="code">
 *      ALTER TABLE {tableName} ADD {virtual column name} {virtual column type} GENERATED ALWAYS AS JSON_UNQUOTE({column}->{path});
 * </pre>
 *
 * @author likly
 * @version 1.0
 * @date 2020-03-16 22:59
 * @see Column
 * @see Prefix
 * @since 1.0
 */
@Prefix("v_")
@Documented
@Column
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Virtual {

    @AliasFor(annotation = Column.class)
    String value() default "";

    @AliasFor(annotation = Column.class)
    String name() default "";
}
