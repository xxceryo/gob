package com.haohaoxuexi.god.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.haohaoxuexi.god.oss.config.OssProperties;
import com.haohaoxuexi.god.oss.model.TemporaryCredentials;
import com.haohaoxuexi.god.oss.service.OssService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * OSS服务实现类
 */
@Service
public class OssServiceImpl implements OssService {
    
    private static final Logger logger = LoggerFactory.getLogger(OssServiceImpl.class);
    
    @Autowired
    private OssProperties ossProperties;
    
    private OSS ossClient;
    
    /**
     * 获取OSS客户端
     */
    private OSS getOssClient() {
        if (ossClient == null) {
            ossClient = new OSSClientBuilder().build(
                ossProperties.getEndpoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret()
            );
        }
        return ossClient;
    }
    
    @Override
    public TemporaryCredentials generateUploadCredentials(String folder, String fileName, long durationSeconds) {
        try {
            // 生成唯一的对象键
            String objectKey = generateObjectKey(folder, fileName);
            
            // 生成预签名上传URL作为临时凭证
            OSS ossClient = getOssClient();
            Date expiration = new Date(System.currentTimeMillis() + durationSeconds * 1000);
            String uploadUrl = ossClient.generatePresignedUrl(ossProperties.getBucketName(), objectKey, expiration).toString();
            
            // 创建临时凭证对象（这里使用预签名URL的方式）
            TemporaryCredentials credentials = new TemporaryCredentials();
            credentials.setAccessKeyId(ossProperties.getAccessKeyId());
            credentials.setAccessKeySecret(ossProperties.getAccessKeySecret());
            credentials.setSecurityToken(""); // 预签名URL不需要安全令牌
            credentials.setExpiration(expiration);
            credentials.setBucketName(ossProperties.getBucketName());
            credentials.setObjectKey(objectKey);
            credentials.setEndpoint(ossProperties.getEndpoint());
            credentials.setRegion(extractRegionFromEndpoint(ossProperties.getEndpoint()));
            
            return credentials;
        } catch (Exception e) {
            logger.error("生成上传凭证失败", e);
            throw new RuntimeException("生成上传凭证失败", e);
        }
    }
    
    @Override
    public TemporaryCredentials generateUploadCredentials(String folder, String fileName) {
        // 默认有效期1小时
        return generateUploadCredentials(folder, fileName, 3600L);
    }
    
    @Override
    public TemporaryCredentials generateImageUploadCredentials(String folder, String fileName) {
        // 图片上传使用默认文件夹
        String targetFolder = StringUtils.hasText(folder) ? folder : "images";
        return generateUploadCredentials(targetFolder, fileName);
    }
    
    @Override
    public TemporaryCredentials generateImageUploadCredentials(String fileName) {
        // 使用默认图片文件夹
        return generateUploadCredentials("images", fileName);
    }
    
    @Override
    public InputStream downloadFile(String objectKey) {
        try {
            OSS ossClient = getOssClient();
            OSSObject ossObject = ossClient.getObject(ossProperties.getBucketName(), objectKey);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            logger.error("下载文件失败: {}", objectKey, e);
            throw new RuntimeException("下载文件失败: " + objectKey, e);
        }
    }
    
    @Override
    public InputStream downloadFileByUrl(String fileUrl) {
        String objectKey = extractObjectKeyFromUrl(fileUrl);
        return downloadFile(objectKey);
    }
    
    @Override
    public boolean deleteFile(String objectKey) {
        try {
            OSS ossClient = getOssClient();
            ossClient.deleteObject(ossProperties.getBucketName(), objectKey);
            return true;
        } catch (Exception e) {
            logger.error("删除文件失败: {}", objectKey, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteFileByUrl(String fileUrl) {
        String objectKey = extractObjectKeyFromUrl(fileUrl);
        return deleteFile(objectKey);
    }
    
    @Override
    public int deleteFiles(List<String> objectKeys) {
        if (objectKeys == null || objectKeys.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        for (String objectKey : objectKeys) {
            if (deleteFile(objectKey)) {
                successCount++;
            }
        }
        return successCount;
    }
    
    @Override
    public boolean fileExists(String objectKey) {
        try {
            OSS ossClient = getOssClient();
            return ossClient.doesObjectExist(ossProperties.getBucketName(), objectKey);
        } catch (Exception e) {
            logger.error("检查文件是否存在失败: {}", objectKey, e);
            return false;
        }
    }
    
    @Override
    public boolean fileExistsByUrl(String fileUrl) {
        String objectKey = extractObjectKeyFromUrl(fileUrl);
        return fileExists(objectKey);
    }
    
    @Override
    public String getFileUrl(String objectKey) {
        return "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/" + objectKey;
    }
    
    @Override
    public String getPresignedDownloadUrl(String objectKey, long durationSeconds) {
        try {
            OSS ossClient = getOssClient();
            Date expiration = new Date(System.currentTimeMillis() + durationSeconds * 1000);
            return ossClient.generatePresignedUrl(ossProperties.getBucketName(), objectKey, expiration).toString();
        } catch (Exception e) {
            logger.error("生成预签名下载URL失败: {}", objectKey, e);
            throw new RuntimeException("生成预签名下载URL失败", e);
        }
    }
    
    @Override
    public String getPresignedDownloadUrl(String objectKey) {
        // 默认有效期1小时
        return getPresignedDownloadUrl(objectKey, 3600L);
    }
    
    @Override
    public String extractObjectKeyFromUrl(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            return null;
        }
        
        try {
            // 从完整URL中提取对象键
            String baseUrl = "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/";
            if (fileUrl.startsWith(baseUrl)) {
                return fileUrl.substring(baseUrl.length());
            }
            
            // 如果URL格式不匹配，尝试其他方式解析
            String[] parts = fileUrl.split("/");
            if (parts.length > 3) {
                StringBuilder objectKey = new StringBuilder();
                for (int i = 3; i < parts.length; i++) {
                    if (i > 3) {
                        objectKey.append("/");
                    }
                    objectKey.append(parts[i]);
                }
                return objectKey.toString();
            }
            
            return fileUrl;
        } catch (Exception e) {
            logger.warn("从URL提取对象键失败: {}", fileUrl, e);
            return fileUrl;
        }
    }
    
    /**
     * 生成唯一的对象键
     */
    private String generateObjectKey(String folder, String fileName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String extension = "";
        
        if (fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        
        return folder + "/" + timestamp + "_" + uuid + "_" + fileName + extension;
    }
    
    /**
     * 从端点中提取区域信息
     */
    private String extractRegionFromEndpoint(String endpoint) {
        if (endpoint.contains(".")) {
            String[] parts = endpoint.split("\\.");
            if (parts.length > 0) {
                return parts[0];
            }
        }
        return "cn-hangzhou";
    }
    
    /**
     * 销毁资源
     */
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}
