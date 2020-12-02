package org.ifinal.finalframework.aop.single;

import org.ifinal.finalframework.aop.AnnotationBuilder;
import org.ifinal.finalframework.aop.AnnotationSource;
import org.ifinal.finalframework.aop.AnnotationSourceMethodPoint;
import org.ifinal.finalframework.aop.InterceptorHandler;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class SingleAnnotationBeanFactoryPointcutAdvisor<A extends Annotation, E, T> extends AbstractBeanFactoryPointcutAdvisor {

    private final Pointcut pointcut;

    public SingleAnnotationBeanFactoryPointcutAdvisor(Class<A> annotationType, AnnotationBuilder<A, E> builder, List<InterceptorHandler<T, E>> handlers) {
        this(new SingleAnnotationSource<>(annotationType, builder), handlers);
    }

    public SingleAnnotationBeanFactoryPointcutAdvisor(AnnotationSource<Collection<E>> source, List<InterceptorHandler<T, E>> handlers) {
        this.pointcut = new AnnotationSourceMethodPoint(source);
        setAdvice(new SingleAnnotationMethodInterceptor<>(source, new SingleMethodInvocationDispatcher<T, E>(handlers) {
            @Override
            @NonNull
            protected T getExecutor(E annotation) {
                return SingleAnnotationBeanFactoryPointcutAdvisor.this.getExecutor(annotation);
            }
        }));
    }

    @Override
    @NonNull
    public Pointcut getPointcut() {
        return pointcut;
    }


    @NonNull
    protected abstract T getExecutor(E annotation);


}
