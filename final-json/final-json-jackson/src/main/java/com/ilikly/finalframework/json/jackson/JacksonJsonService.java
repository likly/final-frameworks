package com.ilikly.finalframework.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilikly.finalframework.core.Assert;
import com.ilikly.finalframework.json.JsonException;
import com.ilikly.finalframework.json.JsonService;
import lombok.Setter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author likly
 * @version 1.0
 * @date 2018-09-26 21:39
 * @since 1.0
 */
public class JacksonJsonService implements JsonService {

    @Setter
    private ObjectMapper objectMapper;

    public JacksonJsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JacksonJsonService() {
        this(new ObjectMapper());
    }

    @Override
    public String toJson(Object object) throws Throwable {
            return objectMapper.writeValueAsString(object);
    }

    @Override
    public String toJson(Object object, Class<?> view) throws Throwable {
        if (Assert.isNull(view)) return toJson(object);
        return objectMapper.writerWithView(view).writeValueAsString(object);
    }

    @Override
    public <T> T parse(String json, Class<T> classOfT) {
        try {
            return objectMapper.readValue(json, classOfT);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public <T> T parseWithView(String json, Class<T> classOfT, Class<?> view) throws Throwable {
        return objectMapper.readerWithView(view).forType(classOfT).readValue(json);
    }

    @Override
    public <T> T parse(String json, Type typeOfT) throws Throwable {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructType(typeOfT));
    }

    @Override
    public <T> T parseWithView(String json, Type typeOfT, Class<?> view) throws Throwable {
        return objectMapper.readerWithView(view)
                .forType(objectMapper.getTypeFactory().constructType(typeOfT))
                .readValue(json);
    }

    @Override
    public <E, T extends Collection<E>> T parse(String json, Class<T> collectionClass, Class<E> elementClass) throws Throwable {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass));
    }

    @Override
    public <E, T extends Collection<E>> T parseWithView(String json, Class<T> collectionClass, Class<E> elementClass, Class<?> view) throws Throwable {
        return objectMapper.readerWithView(view)
                .forType(objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass))
                .readValue(json);
    }

}
