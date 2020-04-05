package org.finalframework.spring.aop.interceptor;


import org.finalframework.spring.annotation.factory.SpringComponent;
import org.finalframework.spring.aop.OperationConfiguration;
import org.finalframework.spring.aop.OperationSource;
import org.finalframework.spring.aop.OperationSourcePointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.data.util.Lazy;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-27 00:20:16
 * @since 1.0
 */
@SpringComponent
public class AnnotationPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationPointcutAdvisor.class);
    private final Lazy<Pointcut> pointcut;

    protected AnnotationPointcutAdvisor(OperationConfiguration configuration) {
        final Set<Class<? extends Annotation>> annoTypes = configuration.getOperationAnnotations();
        final BaseOperationInvocationHandler handler = new BaseOperationInvocationHandler(configuration);
        final OperationSource source = new AnnotationOperationSource(annoTypes, configuration);
        this.setAdvice(new OperationInterceptor(source, handler));
        this.pointcut = Lazy.of(new OperationSourcePointcut(source));
        logger.debug("AnnotationPointcutAdvisor: {}", annoTypes);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut.get();
    }
}
