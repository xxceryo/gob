package com.haohaoxuexi.gob.lock.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @author xxceryo
 */
@Configuration
@EnableMethodCache(basePackages = "com.haohaoxuexi")
public class CacheConfiguration {
}
