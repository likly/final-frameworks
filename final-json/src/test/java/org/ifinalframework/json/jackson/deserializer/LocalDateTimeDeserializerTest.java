/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ifinalframework.json.jackson.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

/**
 * LocalDateTimeDeserializerTest.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class LocalDateTimeDeserializerTest {

    private final LocalDateTimeDeserializer deserializer = new LocalDateTimeDeserializer();

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext context;

    @Test
    void deserialize() throws IOException {

        when(jsonParser.isNaN()).thenReturn(false);
        Date now = new Date();
        when(jsonParser.getValueAsLong()).thenReturn(now.getTime());

        when(context.getAttribute(ZoneId.class)).thenReturn(null);

        LocalDateTime localDateTime = deserializer.deserialize(jsonParser, context);

        assertEquals(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), now.getTime());

    }

    @Test
    void handledType() {
        assertEquals(LocalDateTime.class, deserializer.handledType());
    }

}
