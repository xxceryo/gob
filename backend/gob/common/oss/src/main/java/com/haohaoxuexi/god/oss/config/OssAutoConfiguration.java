package com.haohaoxuexi.god.oss.config;

import com.haohaoxuexi.god.oss.service.OssService;
import com.haohaoxuexi.god.oss.service.impl.OssServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS自动配置类
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = "aliyun.oss", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OssAutoConfiguration {
    
    /**
     * 配置OSS服务
     */
    @Bean
    @ConditionalOnMissingBean(OssService.class)
    public OssService ossService() {
        return new OssServiceImpl();
    }
}
