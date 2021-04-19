package org.ifinal.finalframework.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;

import org.ifinal.finalframework.json.Json;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public class Object2JsonRedisSerializer implements RedisSerializer<Object> {

    public static final Object2JsonRedisSerializer UTF_8 = new Object2JsonRedisSerializer();

    private final Charset charset;

    public Object2JsonRedisSerializer(final Charset charset) {
        this.charset = charset;
    }

    public Object2JsonRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(final Object o) {
        return o == null ? null : Json.toJson(o).getBytes(charset);
    }

    @Override
    public Object deserialize(final byte[] bytes) {
        return bytes == null ? null : new String(bytes, charset);
    }

}