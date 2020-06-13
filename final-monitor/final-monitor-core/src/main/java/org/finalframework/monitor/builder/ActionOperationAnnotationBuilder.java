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

package org.finalframework.monitor.builder;


import java.lang.reflect.Method;

import org.finalframework.core.Assert;
import org.finalframework.monitor.annotation.MonitorAction;
import org.finalframework.monitor.operation.ActionOperation;
import org.finalframework.spring.annotation.factory.SpringComponent;
import org.finalframework.spring.aop.OperationAnnotationBuilder;
import org.finalframework.spring.aop.annotation.OperationAttribute;

/**
 * @author likly
 * @version 1.0
 * @date 2019-03-27 23:06:28
 * @since 1.0
 */
@SpringComponent
@SuppressWarnings("unused")
public class ActionOperationAnnotationBuilder implements OperationAnnotationBuilder<MonitorAction, ActionOperation> {
    @Override
    public ActionOperation build(Method method, MonitorAction ann) {
        final String name = Assert.isBlank(ann.name()) ? method.getDeclaringClass().getSimpleName() + "#" + method.getName() : ann.name();
        final ActionOperation.Builder builder = ActionOperation.builder()
                .name(name)
                .type(ann.type())
                .action(ann.action())
                .operator(ann.operator())
                .level(ann.level())
                .target(ann.target())
                .point(ann.point())
                .handler(ann.handler())
                .executor(ann.executor());

        final OperationAttribute[] attributes = ann.attributes();

        if (Assert.nonEmpty(attributes)) {
            for (OperationAttribute attribute : attributes) {
                builder.addAttribute(attribute.name(), attribute.value());
            }
        }

        return builder.build();
    }
}
