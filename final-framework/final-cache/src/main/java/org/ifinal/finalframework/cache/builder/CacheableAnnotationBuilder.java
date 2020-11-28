package org.ifinal.finalframework.cache.builder;


import org.ifinal.finalframework.aop.OperationAnnotationBuilder;
import org.ifinal.finalframework.cache.annotation.Cacheable;
import org.ifinal.finalframework.cache.operation.CacheableOperation;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class CacheableAnnotationBuilder extends AbsCacheAnnotationBuilder implements OperationAnnotationBuilder<Cacheable, CacheableOperation> {

    @Override
    public CacheableOperation build(Class<?> type, Cacheable ann) {
        return build((AnnotatedElement) type, ann);
    }

    @Override
    public CacheableOperation build(Method method, Cacheable ann) {
        return build((AnnotatedElement) method, ann);
    }

    private CacheableOperation build(AnnotatedElement ae, Cacheable ann) {
        final String delimiter = getDelimiter(ann.delimiter());
        return CacheableOperation.builder()
                .name(ae.toString())
                .key(parse(ann.key(), delimiter))
                .field(parse(ann.field(), delimiter))
                .delimiter(delimiter)
                .condition(ann.condition())
                .ttl(ann.ttl())
                .expire(ann.expire())
                .timeunit(ann.timeunit())
                .handler(ann.handler())
                .executor(ann.executor())
                .build();

    }
}