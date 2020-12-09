package org.ifinal.finalframework.json;

import lombok.extern.slf4j.Slf4j;
import org.ifinal.finalframework.data.annotation.AbsEntity;
import org.ifinal.finalframework.data.annotation.YN;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
class JsonTest {
    @Test
    void enum2Json() {
        logger.info(Json.toJson(YN.YES));
        assertEquals(YN.YES.getCode().toString(), Json.toJson(YN.YES));
        System.out.println(Json.toJson(YN.class));
        assertEquals(YN.YES, Json.toObject(YN.YES.getCode().toString(), YN.class));

    }

    @Test
    void entityJson() {
        AbsEntity entity = new AbsEntity();
        entity.setId(1L);
        entity.setCreated(LocalDateTime.now());
        System.out.println(Json.toJson(entity));
        assertEquals(1L, entity.getId());
    }
}
