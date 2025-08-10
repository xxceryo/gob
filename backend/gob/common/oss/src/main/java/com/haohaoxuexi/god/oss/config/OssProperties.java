package com.haohaoxuexi.god.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OSS配置属性
 */
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {
    
    /**
     * 阿里云OSS访问端点
     */
    private String endpoint;
    
    /**
     * 阿里云OSS访问密钥ID
     */
    private String accessKeyId;
    
    /**
     * 阿里云OSS访问密钥Secret
     */
    private String accessKeySecret;
    
    /**
     * 阿里云OSS存储桶名称
     */
    private String bucketName;
    
    /**
     * 默认文件夹路径
     */
    private String defaultFolder = "uploads";
    
    /**
     * 是否启用OSS
     */
    private boolean enabled = true;
    
    /**
     * 连接超时时间（毫秒）
     */
    private int connectionTimeout = 30000;
    
    /**
     * 请求超时时间（毫秒）
     */
    private int requestTimeout = 60000;
    
    /**
     * 最大连接数
     */
    private int maxConnections = 200;

    // Getters and Setters
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDefaultFolder() {
        return defaultFolder;
    }

    public void setDefaultFolder(String defaultFolder) {
        this.defaultFolder = defaultFolder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }
}
