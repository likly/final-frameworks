package org.ifinal.finalframework.retrofit;


import lombok.Setter;
import org.ifinal.finalframework.retrofit.annotation.Retrofit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */

public class ClassPathRetrofitScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathRetrofitScanner.class);

    private static final String DEFAULT_RETROFIT_BEAN_NAME = "retrofit";

    @Setter
    private Class<? extends Annotation> annotationClass;
    @Setter
    private Class<?> markerInterface;
    @Setter
    private RetrofitFactoryBean<?> retrofitFactoryBean = new RetrofitFactoryBean<>();

    public ClassPathRetrofitScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    @NonNull
    public Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        registerFilters();
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            LOGGER.warn("No Retrofit service was found in '{}' package. Please check your configuration.", Arrays.asList(basePackages));
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder holder : beanDefinitions) {

            try {
                GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Creating RetrofitFactoryBean with name '{}' and '{}' retrofitInterface", holder.getBeanName(), definition.getBeanClassName());
                }

                final Class<?> retrofitService = Class.forName(definition.getBeanClassName());
                final Retrofit retrofit = retrofitService.getAnnotation(Retrofit.class);
                final String retrofitBeanName = StringUtils.hasText(retrofit.value()) ? retrofit.value() : DEFAULT_RETROFIT_BEAN_NAME;

                // the mapper interface is the original class of the bean
                // but, the actual class of the bean is MapperFactoryBean
                if (Objects.nonNull(definition.getBeanClassName())) {
                    Optional.of(definition.getConstructorArgumentValues())
                            .ifPresent(it -> it.addGenericArgumentValue(definition.getBeanClassName()));
                }
                definition.setBeanClass(this.retrofitFactoryBean.getClass());

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("register retrofit: {}", definition.getBeanClassName());
                }

                definition.getPropertyValues().add(DEFAULT_RETROFIT_BEAN_NAME, new RuntimeBeanReference(retrofitBeanName));

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Enabling autowire by type for RetrofitFactoryBean with name '{}'.", holder.getBeanName());
                }
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

        }

    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    private void registerFilters() {
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(@NonNull String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
            addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        }

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

}

