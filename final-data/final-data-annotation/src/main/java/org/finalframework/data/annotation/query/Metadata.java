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

package org.finalframework.data.annotation.query;


import lombok.Builder;
import lombok.Data;
import org.finalframework.data.annotation.TypeHandler;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * @author likly
 * @version 1.0
 * @date 2020-07-17 17:47:55
 * @see CriterionHandler
 * @see Criterion
 * @since 1.0
 */
@Data
@Builder
public class Metadata implements Serializable {
    @NonNull
    private AndOr andOr;
    @NonNull
    private String query;
    @NonNull
    private String column;
    @NonNull
    private String value;
    @NonNull
    private String path;
    private Class<?> javaType;
    @Nullable
    private Class<? extends TypeHandler> typeHandler;
    private Map<String, String> attributes;
}
