package org.ifinal.finalframework.web.autoconfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.ifinal.auto.spring.factory.annotation.SpringApplicationListener;
import org.ifinal.finalframework.util.Asserts;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 自定义参数解析器配置器。
 * <p>由于 {@link RequestMappingHandlerAdapter}的配置的 {@link HandlerMethodArgumentResolver}在默认的配置之后， 因此当参数列表中使用{@link
 * java.util.Map}或其子类作为参数时，会被默认的 {@link MapMethodProcessor}所解析，走不到自定义的参数解析器， 因此在 {@link ApplicationReadyEvent}事件中，将自定义的 {@link
 * HandlerMethodArgumentResolver}置于默认的之前。</p>
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@SpringApplicationListener
public class RequestMappingHandlerAdapterAutoConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {

            if (event.getSpringApplication().getWebApplicationType() != WebApplicationType.SERVLET) {
                return;
            }

            ConfigurableApplicationContext applicationContext = event.getApplicationContext();
            final RequestMappingHandlerAdapter requestMappingHandlerAdapter = applicationContext
                .getBean(RequestMappingHandlerAdapter.class);
            configureHandlerMethodArgumentResolver(applicationContext, requestMappingHandlerAdapter);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * @param context context
     * @param adapter adapter
     * @see RequestMappingHandlerAdapter#setArgumentResolvers(List)
     */
    private void configureHandlerMethodArgumentResolver(final ApplicationContext context,
        final RequestMappingHandlerAdapter adapter) {

        final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

        //自定义参数解析器不为空，将自定义的参数解析器置于默认的之前
        context.getBeanProvider(HandlerMethodArgumentResolver.class)
            .stream()
            .forEachOrdered(argumentResolvers::add);

        // 获取默认的参数解析器
        final List<HandlerMethodArgumentResolver> defaultArgumentResolvers = adapter.getArgumentResolvers();
        if (Asserts.nonEmpty(defaultArgumentResolvers)) {
            for (HandlerMethodArgumentResolver argumentResolver : defaultArgumentResolvers) {
                if (argumentResolver instanceof AbstractMessageConverterMethodArgumentResolver) {
                    Optional.ofNullable(ReflectionUtils.findField(argumentResolver.getClass(), "messageConverters"))
                        .ifPresent(messageConverters -> {
                            ReflectionUtils.makeAccessible(messageConverters);
                            ReflectionUtils
                                .setField(messageConverters, argumentResolver, adapter.getMessageConverters());
                        });

                }
                argumentResolvers.add(argumentResolver);
            }
        }

        adapter.setArgumentResolvers(argumentResolvers);
    }

}
