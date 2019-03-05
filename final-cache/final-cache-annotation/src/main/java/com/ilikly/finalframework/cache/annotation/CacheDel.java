package com.ilikly.finalframework.cache.annotation;

import java.lang.annotation.*;

/**
 * @author likly
 * @version 1.0
 * @date 2018-10-31 18:21
 * @since 1.0
 */
@CacheAnnotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheDel {

    String[] key();

    String[] field() default {};

    String delimiter() default ":";

    String condition() default "";

    boolean beforeInvocation() default false;

}
