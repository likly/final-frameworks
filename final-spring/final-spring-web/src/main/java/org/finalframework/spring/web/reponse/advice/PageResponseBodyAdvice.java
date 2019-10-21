package org.finalframework.spring.web.reponse.advice;

import com.github.pagehelper.Page;
import org.finalframework.spring.web.converter.Page2PageConverter;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author likly
 * @version 1.0
 * @date 2019-09-24 23:29:47
 * @since 1.0
 */
@Order(RestAdviceOrdered.DEFAULT_PRECEDENCE)
@RestControllerAdvice
public class PageResponseBodyAdvice extends RestMethodParameterFilter implements ResponseBodyAdvice<Object> {

    private Page2PageConverter page2PageConverter = new Page2PageConverter();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return matches(returnType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Page) {
            return page2PageConverter.convert((Page) body);
        }
        return body;
    }

}