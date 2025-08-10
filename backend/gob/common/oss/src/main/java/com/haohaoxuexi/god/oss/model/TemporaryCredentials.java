package com.haohaoxuexi.god.oss.model;

import java.util.Date;

/**
 * 临时访问凭证
 */
public class TemporaryCredentials {
    
    /**
     * 临时访问密钥ID
     */
    private String accessKeyId;
    
    /**
     * 临时访问密钥Secret
     */
    private String accessKeySecret;
    
    /**
     * 安全令牌
     */
    private String securityToken;
    
    /**
     * 过期时间
     */
    private Date expiration;
    
    /**
     * 存储桶名称
     */
    private String bucketName;
    
    /**
     * 对象键（文件路径）
     */
    private String objectKey;
    
    /**
     * OSS端点
     */
    private String endpoint;
    
    /**
     * 区域
     */
    private String region;

    public TemporaryCredentials() {}

    public TemporaryCredentials(String accessKeyId, String accessKeySecret, String securityToken, 
                              Date expiration, String bucketName, String objectKey, String endpoint, String region) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
        this.expiration = expiration;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.endpoint = endpoint;
        this.region = region;
    }

    // Getters and Setters
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

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "TemporaryCredentials{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", securityToken='" + securityToken + '\'' +
                ", expiration=" + expiration +
                ", bucketName='" + bucketName + '\'' +
                ", objectKey='" + objectKey + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
