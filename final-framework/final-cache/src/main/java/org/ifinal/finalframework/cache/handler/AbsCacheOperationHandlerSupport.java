package org.ifinal.finalframework.cache.handler;


import org.ifinal.finalframework.aop.interceptor.AbsOperationHandlerSupport;
import org.ifinal.finalframework.cache.CacheOperationExpressionEvaluator;
import org.ifinal.finalframework.cache.CacheOperationHandlerSupport;
import org.ifinal.finalframework.cache.interceptor.DefaultCacheOperationExpressionEvaluator;
import org.ifinal.finalframework.context.expression.MethodMetadata;
import org.ifinal.finalframework.util.Asserts;
import org.springframework.expression.EvaluationContext;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class AbsCacheOperationHandlerSupport extends AbsOperationHandlerSupport implements CacheOperationHandlerSupport {

    private final CacheOperationExpressionEvaluator evaluator;
    private Boolean conditionPassing;

    public AbsCacheOperationHandlerSupport() {
        this(new DefaultCacheOperationExpressionEvaluator());
    }

    public AbsCacheOperationHandlerSupport(CacheOperationExpressionEvaluator evaluator) {
        super(evaluator);
        this.evaluator = evaluator;
    }

    @Override
    public Object generateKey(Collection<String> keys, String delimiter, MethodMetadata metadata, EvaluationContext evaluationContext) {
        final List<String> keyValues = keys.stream()
                .map(key -> {
                    if (isExpression(key)) {
                        return evaluator.key(generateExpression(key), metadata.getMethodKey(), evaluationContext);
                    } else {
                        return key;
                    }
                })
                .map(Object::toString)
                .collect(Collectors.toList());


        return String.join(delimiter, keyValues);
    }


    @Override
    public Object generateField(Collection<String> fields, String delimiter, MethodMetadata metadata, EvaluationContext evaluationContext) {
        if (Asserts.isEmpty(fields)) {
            return null;
        }
        List<String> fieldValues = fields.stream()
                .map(field -> {
                    if (isExpression(field)) {
                        return evaluator.field(generateExpression(field), metadata.getMethodKey(), evaluationContext);
                    } else {
                        return field;
                    }
                })
                .map(Object::toString)
                .collect(Collectors.toList());

        return String.join(delimiter, fieldValues);
    }

    @Override
    public Object generateValue(String value, MethodMetadata metadata, EvaluationContext evaluationContext) {
        if (value != null && isExpression(value)) {
            return evaluator.value(generateExpression(value), metadata.getMethodKey(), evaluationContext);
        }
        return value;
    }

    @Override
    public <T> T generateValue(String value, MethodMetadata metadata, EvaluationContext evaluationContext, Class<T> clazz) {
        if (value != null && isExpression(value)) {
            return evaluator.value(generateExpression(value), metadata.getMethodKey(), evaluationContext, clazz);
        }
        throw new IllegalArgumentException("value expression can not evaluator to " + clazz.getCanonicalName());
    }

    @Override
    public boolean isConditionPassing(String condition, MethodMetadata metadata, EvaluationContext evaluationContext) {
        if (this.conditionPassing == null) {
            if (condition != null && isExpression(condition)) {
                this.conditionPassing = evaluator.condition(generateExpression(condition), metadata.getMethodKey(), evaluationContext);
            } else {
                this.conditionPassing = true;
            }
        }
        return this.conditionPassing;
    }

    @Override
    public Object generateExpire(String expire, MethodMetadata metadata, EvaluationContext evaluationContext) {
        if (expire != null && isExpression(expire)) {
            return evaluator.expired(generateExpression(expire), metadata.getMethodKey(), evaluationContext);
        }
        return null;
    }

}
