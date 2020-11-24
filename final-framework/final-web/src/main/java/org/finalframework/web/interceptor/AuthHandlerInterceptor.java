package org.finalframework.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.finalframework.annotation.IUser;
import org.finalframework.annotation.auth.Auth;
import org.finalframework.context.user.UserContextHolder;
import org.finalframework.util.Reflections;
import org.finalframework.web.auth.AuthService;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/15 16:16:17
 * @since 1.0
 */
@Slf4j
@Component
@ConditionalOnBean(AuthService.class)
@SuppressWarnings("rawtypes")
public class AuthHandlerInterceptor implements AsyncHandlerInterceptor {


    private final Map<Class<? extends Annotation>, AuthService> authServices = new HashMap<>();

    public AuthHandlerInterceptor(ObjectProvider<AuthService<?>> authServiceProvider) {
        for (AuthService<?> authService : authServiceProvider) {
            Class<?> targetClass = AopUtils.getTargetClass(authService);
            Class authAnnotation = Reflections.findParameterizedInterfaceArgumentClass(targetClass, AuthService.class, 0);
            logger.info("register auth service of {} for @{}", targetClass, authAnnotation);
            authServices.put(authAnnotation, authService);
        }
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Object handler) throws Exception {
        Auth auth = findHandlerAuth(handler, Auth.class);
        if (auth != null) {
            IUser user = UserContextHolder.getUser();
            Class<? extends Annotation> annotation = auth.value();
            if (Auth.class.equals(annotation)) {
                authServices.get(annotation).auth(user, auth);
            } else {
                authServices.get(annotation).auth(user, findHandlerAuth(handler, annotation));
            }
        }


        return true;
    }

    private <A extends Annotation> A findHandlerAuth(@NonNull Object handler, Class<A> ann) {
        if (handler instanceof HandlerMethod) {
            A annotation = AnnotatedElementUtils.findMergedAnnotation(((HandlerMethod) handler).getMethod(), ann);
            if (annotation != null) {
                return annotation;
            }
            return AnnotatedElementUtils.findMergedAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(), ann);
        }

        return null;
    }

}
