package org.finalframework.data.query.criterion.function.expression;


import org.finalframework.data.query.criterion.function.SupportTypes;
import org.finalframework.data.query.criterion.function.operation.FunctionOperationExpression;
import org.finalframework.data.query.criterion.function.operation.SimpleFunctionOperation;
import org.finalframework.data.query.operation.JsonOperation;
import org.finalframework.data.query.operation.Operation;

/**
 * @author likly
 * @version 1.0
 * @date 2020-03-21 14:01:37
 * @since 1.0
 */
@SupportTypes
public class JsonLengthFunctionOperationExpression<T> implements FunctionOperationExpression<SimpleFunctionOperation> {

    @Override
    public Operation operation() {
        return JsonOperation.JSON_LENGTH;
    }

    @Override
    public String expression(String target, SimpleFunctionOperation criterion) {
        return String.format("JSON_LENGTH(%s)", target);
    }

}
