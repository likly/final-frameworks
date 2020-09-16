package org.finalframework.data.query;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author likly
 * @version 1.0
 * @date 2020-06-04 16:45:10
 * @since 1.0
 */
class LimitTest {

    private static final Logger logger = LoggerFactory.getLogger(LimitTest.class);

    @Test
    void apply() {

        final StringBuilder builder = new StringBuilder();

        Limit.limit(20L, 20L).apply(builder, "query.limit");

        logger.info(builder.toString());

    }
}