package org.ifinal.finalframework.aop.single;

import org.ifinal.finalframework.aop.AnnotationBuilder;
import org.ifinal.finalframework.aop.AnnotationFinder;
import org.ifinal.finalframework.aop.AnnotationParser;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class SingleAnnotationParser<A extends Annotation, E> implements AnnotationParser<Collection<E>>, Serializable {

    private final AnnotationFinder<Collection<A>> finder;
    private final AnnotationBuilder<A, E> builder;


    public SingleAnnotationParser(Class<A> annotationType, AnnotationBuilder<A, E> builder) {
        this(new SingleAnnotationFinder<>(annotationType), builder);
    }

    public SingleAnnotationParser(AnnotationFinder<Collection<A>> finder, AnnotationBuilder<A, E> builder) {
        this.finder = finder;
        this.builder = builder;
    }

    @Override
    public Collection<E> parseAnnotations(Class<?> clazz) {
        return finder.findAnnotations(clazz)
                .stream()
                .map(annotation -> builder.build(clazz, annotation))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<E> parseAnnotations(Method method) {
        return finder.findAnnotations(method)
                .stream()
                .map(annotation -> builder.build(method, annotation))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<E> parseAnnotations(Parameter parameter, Integer index) {
        return finder.findAnnotations(parameter)
                .stream()
                .map(annotation -> builder.build(parameter, index, annotation))
                .collect(Collectors.toList());
    }
}
