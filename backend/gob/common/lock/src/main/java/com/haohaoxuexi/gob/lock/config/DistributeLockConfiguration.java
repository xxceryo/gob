package com.haohaoxuexi.gob.lock.config;


import com.haohaoxuexi.gob.lock.DistributeLockAspect;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author xxceryo
 */
public class DistributeLockConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DistributeLockAspect distributeLockAspect(RedissonClient redisson){
        return new DistributeLockAspect(redisson);
    }
}
