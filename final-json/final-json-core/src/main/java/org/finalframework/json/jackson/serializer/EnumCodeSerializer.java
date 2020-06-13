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

package org.finalframework.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.finalframework.data.annotation.IEnum;

import java.io.IOException;

/**
 * 枚举{@link IEnum}码序列化器，将枚举序列化为其{@link IEnum#getCode()}所描述的值。
 *
 * @author likly
 * @version 1.0
 * @date 2019-08-26 16:38:26
 * @since 1.0
 */
public class EnumCodeSerializer extends JsonSerializer<IEnum> {
    public static final EnumCodeSerializer instance = new EnumCodeSerializer();

    @Override
    public void serialize(IEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(value.getCode());
    }
}
