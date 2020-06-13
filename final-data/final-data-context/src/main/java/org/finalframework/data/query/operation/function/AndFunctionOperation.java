/*
 * Copyright (c) 2018-2020.  the original author or authors.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.finalframework.data.query.operation.function;


import org.finalframework.data.query.operation.LogicOperation;
import org.finalframework.data.query.operation.Operation;
import org.finalframework.data.query.operation.function.FunctionOperation;

/**
 * @author likly
 * @version 1.0
 * @date 2020-05-30 12:55:59
 * @since 1.0
 */
public class AndFunctionOperation implements FunctionOperation {
    private final Object value;

    public AndFunctionOperation(Object value) {
        this.value = value;
    }

    @Override
    public Operation operation() {
        return LogicOperation.AND;
    }

    @Override
    public String apply(Object value) {
        return String.format("%s & %s", value, this.value);
    }
}

