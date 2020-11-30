package org.ifinal.finalframework.cache.handler;

import org.ifinal.finalframework.aop.OperationContext;
import org.ifinal.finalframework.aop.OperationHandler;
import org.ifinal.finalframework.cache.Cache;
import org.ifinal.finalframework.cache.CacheLockException;
import org.ifinal.finalframework.cache.annotation.CacheLock;
import org.ifinal.finalframework.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author likly
 * @version 1.0.0
 * @see CacheLock
 * @since 1.0.0
 */
@Component
public class CacheLockOperationHandler extends AbsCacheOperationHandlerSupport implements OperationHandler<Cache, CacheLock> {

    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String LOCK = "lock";

    @Override
    public Void before(Cache cache, OperationContext context) {
        final Logger logger = LoggerFactory.getLogger(context.target().getClass());
        final EvaluationContext evaluationContext = createEvaluationContext(context, null, null);
        final AnnotationAttributes operation = context.annotationAttributes();
        final Object key = generateKey(operation.getStringArray("key"), operation.getString("delimiter"), context.metadata(), evaluationContext);
        if (key == null) {
            throw new IllegalArgumentException("the cache action generate null key, action=" + operation);
        }
        final Object value = Asserts.isEmpty(operation.getString("value")) ? key : generateValue(operation.getString("value"), context.metadata(), evaluationContext);

        Long ttl;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Object expired = generateExpire(operation.getString("expire"), context.metadata(), evaluationContext);

        if (expired != null) {
            if (expired instanceof Date) {
                ttl = ((Date) expired).getTime() - System.currentTimeMillis();
            } else {
                throw new IllegalArgumentException("unSupport expire type: " + expired.getClass());
            }
        } else {
            ttl = operation.getNumber("ttl");
            timeUnit = operation.getEnum("timeUnit");
        }

        final Long sleep = operation.getNumber("sleep");
        context.addAttribute(KEY, key);
        context.addAttribute(VALUE, value);
        int retry = 0;
        do {
            logger.info("==> try to lock: key={},value={},ttl={},timeUnit={},retry={}", key, value, ttl, timeUnit, retry);
            final boolean lock = cache.lock(key, value, ttl, timeUnit);
            logger.info("<== lock result: {}", lock);
            if (lock) {
                context.addAttribute(LOCK, true);
                return null;
            }

            if (sleep != null && sleep > 0L) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    logger.error("retry sleep error,key={},value={}", key, value, e);
                    Thread.currentThread().interrupt();
                }
            }

            retry++;
        } while (retry <= operation.getNumber("retry").intValue());

        context.addAttribute(LOCK, false);
        throw new CacheLockException(String.format("failure to lock key=%s,value=%s", key, value));
    }

    @Override
    public void after(Cache cache, OperationContext context, Object result, Throwable throwable) {
        final Logger logger = LoggerFactory.getLogger(context.target().getClass());

        final Object key = context.getAttribute(KEY);
        final Object value = context.getAttribute(VALUE);
        final Boolean lock = context.getAttribute(LOCK);

        if (Boolean.TRUE.equals(lock)) {
            logger.info("==> try to unlock: key={},value={}", key, value);
            final boolean unlock = cache.unlock(key, value);
            logger.info("<== result: {}", unlock);
        }
    }

}
