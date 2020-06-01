package org.finalframework.data.query.operation.function;


import org.finalframework.data.query.operation.Operation;

/**
 * @author likly
 * @version 1.0
 * @date 2020-05-30 12:54:36
 * @since 1.0
 */
public class MinFunctionOperation implements FunctionOperation {
    @Override
    public Operation operation() {
        return MathOperation.MIN;
    }

    @Override
    public String apply(Object value) {
        return String.format("MIN(%s)", value);
    }
}
