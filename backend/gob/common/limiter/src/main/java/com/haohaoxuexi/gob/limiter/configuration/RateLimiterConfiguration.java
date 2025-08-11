package com.haohaoxuexi.gob.limiter.configuration;

import com.haohaoxuexi.gob.limiter.SlidingWindowRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author loongzhou
 */
@Configuration
public class RateLimiterConfiguration {

    @Bean
    public SlidingWindowRateLimiter slidingWindowRateLimiter(RedissonClient redisson) {
        return new SlidingWindowRateLimiter(redisson);
    }
}
