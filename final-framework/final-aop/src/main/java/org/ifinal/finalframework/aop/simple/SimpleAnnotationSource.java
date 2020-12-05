package org.ifinal.finalframework.aop.simple;

import org.ifinal.finalframework.aop.AnnotationSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleAnnotationSource implements AnnotationSource<Boolean> {

    private final Map<Object, Boolean> cache = new ConcurrentHashMap<>(1024);

    private final SimpleAnnotationFinder finder;

    public SimpleAnnotationSource(Collection<Class<? extends Annotation>> annotationTypes) {
        this(new SimpleAnnotationFinder(annotationTypes));
    }

    public SimpleAnnotationSource(SimpleAnnotationFinder finder) {
        this.finder = finder;
    }

    @Override
    public Boolean getAnnotations(Method method, Class<?> targetClass) {
        Object cacheKey = getCacheKey(method, targetClass);
        return cache.computeIfAbsent(cacheKey, key -> {

            if (Boolean.TRUE.equals(finder.findAnnotations(method))) {
                return true;
            }

            if (Boolean.TRUE.equals(finder.findAnnotations(targetClass))) {
                return true;
            }

            for (Parameter parameter : method.getParameters()) {
                if (Boolean.TRUE.equals(finder.findAnnotations(parameter))) {
                    return true;
                }
            }

            return false;
        });
    }
}
