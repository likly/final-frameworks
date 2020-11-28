package org.ifinal.finalframework.example.web.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ifinal.finalframework.annotation.IUser;
import org.ifinal.finalframework.annotation.auth.Auth;
import org.ifinal.finalframework.annotation.data.PrimaryKey;
import org.ifinal.finalframework.context.exception.ForbiddenException;
import org.ifinal.finalframework.web.auth.AuthService;
import org.ifinal.finalframework.web.auth.TokenService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Component
public class AuthTokenService implements TokenService, AuthService<Auth> {
    @Override
    public void auth(@Nullable IUser<?> user, @NonNull Auth auth, @NonNull Object handler) {
        logger.info("try auth ...");

        if (user == null) {
            throw new ForbiddenException("未登录！");
        }

    }

    @Nullable
    @Override
    public IUser<?> token(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        logger.info("try token");

        if (request.getParameterMap().containsKey("unlogin")) {
            return null;
        }


        return new User(Long.MIN_VALUE, "user");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User implements IUser<Long> {
        @PrimaryKey
        private Long id;
        private String name;
    }
}