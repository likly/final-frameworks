package org.ifinal.finalframework.redis.autoconfigure;

import org.ifinal.finalframework.redis.FinalRedisTemplate;
import org.ifinal.finalframework.redis.RedisRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
public class FinalRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FinalRedisTemplate finalRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {

        FinalRedisTemplate template = new FinalRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        RedisRegistry.getInstance().setRedisTemplate(template);
        return template;
    }

}