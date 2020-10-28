package org.finalframework.web.resolver;

import org.finalframework.auto.spring.factory.annotation.SpringArgumentResolver;
import org.finalframework.context.exception.BadRequestException;
import org.finalframework.json.Json;
import org.finalframework.util.Asserts;
import org.finalframework.web.resolver.annotation.RequestJsonParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-31 11:22:47
 * @see RequestJsonParam
 * @see RequestJsonParamHandler
 * @since 1.0
 */
@SpringArgumentResolver
public final class RequestJsonParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(RequestJsonParamHandlerMethodArgumentResolver.class);

    @NonNull
    private final Charset defaultCharset = Charset.defaultCharset();

    /**
     * return {@code true} if the {@linkplain MethodParameter parameter} is annotated by {@link RequestJsonParam}, otherwise {@code false}.
     *
     * @param parameter the method parameter
     * @return {@code true} if the {@linkplain MethodParameter parameter} is annotated by {@link RequestJsonParam}, otherwise {@code false}.
     */
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        // only support the method parameter annotated by @RequestJsonParam
        return parameter.hasParameterAnnotation(RequestJsonParam.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        // find the @RequestJsonParam annotation
        final RequestJsonParam requestJsonParam = Objects.requireNonNull(parameter.getParameterAnnotation(RequestJsonParam.class), "requestJsonParam annotation is null");

        final String contentType = webRequest.getHeader("content-type");
        if (Objects.nonNull(contentType) && contentType.startsWith("application/json")) {

            final Object nativeRequest = webRequest.getNativeRequest();
            if (nativeRequest instanceof HttpServletRequest) {
                ServletServerHttpRequest inputMessage = new ServletServerHttpRequest((HttpServletRequest) nativeRequest);
                Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
                final String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
                if (Asserts.nonEmpty(body)) {
                    logger.debug("==> jsonBody: {}", body);
                    return parseJson(body, parameter);
                }
            }

            return null;

        } else {
            final String parameterName = getParameterName(requestJsonParam, parameter);
            String value = webRequest.getParameter(parameterName);
            if (Asserts.isBlank(value) && requestJsonParam.required()) {
                throw new BadRequestException(String.format("parameter %s is required", parameterName));
            }

            if (Asserts.isBlank(value) && !ValueConstants.DEFAULT_NONE.equals(requestJsonParam.defaultValue())) {
                value = requestJsonParam.defaultValue();
            }

            if (Asserts.isBlank(value)) {
                return null;
            }
            logger.debug("==> RequestJsonParam: name={},value={}", parameterName, value);
            return parseJson(value, parameter);
        }

    }

    private Object parseJson(String json, MethodParameter parameter) {
        final Class<?> type = parameter.getParameterType();
        final RequestJsonParamHandler<?> requestJsonParamHandler = RequestJsonParamHandlerRegistry.getInstance().getRequestJsonParamHandler(type);
        if (requestJsonParamHandler != null) {
            return requestJsonParamHandler.convert(json);
        }
        return Json.toObject(json, parameter.getGenericParameterType());
    }

    /**
     * 获取指定的参数名，如果{@link RequestJsonParam#value()}未指定，则使用{@link MethodParameter#getParameterName()}。
     */
    private String getParameterName(RequestJsonParam requestJsonParam, MethodParameter parameter) {
        return Asserts.isEmpty(requestJsonParam.value()) ? parameter.getParameterName() : requestJsonParam.value().trim();
    }

    private Charset getContentTypeCharset(@Nullable MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        } else {
            return getDefaultCharset();
        }
    }

    @NonNull
    public Charset getDefaultCharset() {
        return this.defaultCharset;
    }

}
