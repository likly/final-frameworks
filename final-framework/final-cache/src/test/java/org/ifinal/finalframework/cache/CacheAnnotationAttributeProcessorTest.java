package org.ifinal.finalframework.cache;

import lombok.extern.slf4j.Slf4j;
import org.ifinal.finalframework.cache.annotation.Cacheable;
import org.ifinal.finalframework.util.Reflections;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
class CacheAnnotationAttributeProcessorTest {

    private final CacheAnnotationAttributeProcessor processor = new CacheAnnotationAttributeProcessor();

    @Test
    void cacheable() {
        Method cacheable = Reflections.findRequiredMethod(CacheServiceImpl.class, "cacheable", Integer.class);

        Set<Cacheable> annotations = AnnotatedElementUtils.findAllMergedAnnotations(cacheable, Cacheable.class);


        for (Cacheable annotation : annotations) {
            AnnotationAttributes annotationAttributes = Reflections.getAnnotationAttributes(annotation);
            processor.doProcess(cacheable, annotationAttributes);
            logger.info("find cacheable annotation attributes: {}", annotationAttributes);
        }


    }

}