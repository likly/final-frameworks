package org.finalframework.data.query.criterion.function.expression;


import org.finalframework.data.query.criterion.function.SupportTypes;
import org.finalframework.data.query.criterion.function.operation.FunctionOperation;
import org.finalframework.data.query.criterion.function.operation.FunctionOperationExpression;
import org.finalframework.data.query.operation.MathOperation;
import org.finalframework.data.query.operation.Operation;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-28 21:11:49
 * @since 1.0
 */
@SupportTypes
public class SumFunctionOperationExpression implements FunctionOperationExpression<FunctionOperation> {

    @Override
    public Operation operation() {
        return MathOperation.SUM;
    }

    @Override
    public String expression(String target, FunctionOperation criterion) {
        return String.format("SUM(%s)", target);
    }
}