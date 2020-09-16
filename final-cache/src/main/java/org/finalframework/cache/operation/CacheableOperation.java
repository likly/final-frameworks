package org.finalframework.cache.operation;

import org.finalframework.cache.Cache;
import org.finalframework.cache.annotation.Cacheable;
import org.finalframework.cache.handler.CacheableOperationHandler;
import org.finalframework.core.Asserts;
import org.finalframework.spring.aop.Operation;
import org.finalframework.spring.aop.OperationHandler;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-10 22:52:34
 * @see Cacheable
 * @since 1.0
 */
public class CacheableOperation implements Operation {
    private static final String DELIMITER = ":";
    private final String name;
    private final Collection<String> key;
    private final Collection<String> field;
    private final String delimiter;
    private final String condition;
    private final String expire;
    private final Long ttl;
    private final TimeUnit timeunit;
    private final Integer retry;
    private final Long sleep;
    private final Class<? extends OperationHandler> handler;
    private final Class<? extends Cache> executor;

    private CacheableOperation(Builder builder) {
        this.name = Asserts.isBlank(builder.name) ? CacheableOperation.class.getSimpleName() : builder.name;
        this.key = Asserts.isEmpty(builder.key) ? null : builder.key;
        this.field = Asserts.isEmpty(builder.field) ? null : builder.field;
        this.delimiter = Asserts.isEmpty(builder.delimiter) ? DELIMITER : builder.delimiter;
        this.condition = Asserts.isEmpty(builder.condition) ? null : builder.condition;
        this.expire = Asserts.isEmpty(builder.expire) ? null : builder.expire;
        this.ttl = builder.ttl;
        this.timeunit = builder.timeunit;
        this.retry = Asserts.nonNull(builder.retry) && builder.retry > 0 ? builder.retry : null;
        this.sleep = Asserts.nonNull(builder.sleep) && builder.sleep > 0L ? builder.sleep : null;
        this.handler = builder.handler;
        this.executor = builder.executor;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String name() {
        return name;
    }

    @NonNull
    public Collection<String> key() {
        return key;
    }

    @Nullable
    public Collection<String> field() {
        return field;
    }

    @NonNull
    public String delimiter() {
        return delimiter;
    }

    @Nullable
    public String condition() {
        return condition;
    }

    @Nullable
    public String expire() {
        return this.expire;
    }

    @Nullable
    public Long ttl() {
        return this.ttl;
    }

    public TimeUnit timeunit() {
        return this.timeunit;
    }

    @Nullable
    public Integer retry() {
        return this.retry;
    }

    @Nullable
    public Long sleep() {
        return this.sleep;
    }

    @Override
    public Class<? extends OperationHandler> handler() {
        return handler;
    }

    @Override
    public Class<? extends Cache> executor() {
        return this.executor;
    }

    public static class Builder implements org.finalframework.core.Builder<CacheableOperation> {
        private String name;
        private Collection<String> key;
        private Collection<String> field;
        private String delimiter;
        private String condition;
        private String expire;
        private Long ttl;
        private TimeUnit timeunit;
        private Integer retry;
        private Long sleep;
        private Class<? extends OperationHandler> handler;
        private Class<? extends Cache> executor;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder key(Collection<String> key) {
            this.key = key;
            return this;
        }

        public Builder field(Collection<String> field) {
            this.field = field;
            return this;
        }

        public Builder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder expire(String expire) {
            this.expire = expire;
            return this;
        }

        public Builder ttl(Long ttl) {
            this.ttl = ttl;
            return this;
        }

        public Builder timeunit(TimeUnit timeUnit) {
            this.timeunit = timeUnit;
            return this;
        }

        public Builder retry(Integer retry) {
            this.retry = retry;
            return this;
        }

        public Builder sleep(Long sleep) {
            this.sleep = sleep;
            return this;
        }

        public Builder handler(Class<? extends OperationHandler> handler) {
            this.handler = handler == null ? CacheableOperationHandler.class : handler;
            return this;
        }

        public Builder executor(Class<? extends Cache> executor) {
            this.executor = executor == null ? Cache.class : executor;
            return this;
        }

        @Override
        public CacheableOperation build() {
            return new CacheableOperation(this);
        }
    }
}
