package com.haohaoxuexi.god.oss.utils;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * OSS工具类
 */
public class OssUtils {
    
    /**
     * 生成唯一的文件名
     *
     * @param originalFileName 原始文件名
     * @return 唯一文件名
     */
    public static String generateUniqueFileName(String originalFileName) {
        if (!StringUtils.hasText(originalFileName)) {
            return UUID.randomUUID().toString();
        }
        
        String extension = "";
        String nameWithoutExtension = originalFileName;
        
        if (originalFileName.contains(".")) {
            int lastDotIndex = originalFileName.lastIndexOf(".");
            extension = originalFileName.substring(lastDotIndex);
            nameWithoutExtension = originalFileName.substring(0, lastDotIndex);
        }
        
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return timestamp + "_" + uuid + "_" + nameWithoutExtension + extension;
    }
    
    /**
     * 从对象键中提取原始文件名
     *
     * @param objectKey 对象键
     * @return 原始文件名
     */
    public static String extractOriginalFileName(String objectKey) {
        if (!StringUtils.hasText(objectKey)) {
            return "";
        }
        
        String[] parts = objectKey.split("/");
        if (parts.length > 0) {
            String fileName = parts[parts.length - 1];
            // 移除时间戳和UUID前缀
            if (fileName.contains("_")) {
                String[] nameParts = fileName.split("_");
                if (nameParts.length > 2) {
                    StringBuilder originalName = new StringBuilder();
                    for (int i = 2; i < nameParts.length; i++) {
                        if (i > 2) {
                            originalName.append("_");
                        }
                        originalName.append(nameParts[i]);
                    }
                    return originalName.toString();
                }
            }
            return fileName;
        }
        return "";
    }
    
    /**
     * 验证文件扩展名是否允许
     *
     * @param fileName 文件名
     * @param allowedExtensions 允许的扩展名数组
     * @return 是否允许
     */
    public static boolean isAllowedFileExtension(String fileName, String[] allowedExtensions) {
        if (!StringUtils.hasText(fileName) || allowedExtensions == null || allowedExtensions.length == 0) {
            return false;
        }
        
        String extension = getFileExtension(fileName);
        if (!StringUtils.hasText(extension)) {
            return false;
        }
        
        for (String allowedExtension : allowedExtensions) {
            if (extension.equalsIgnoreCase(allowedExtension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名（包含点号）
     */
    public static String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    /**
     * 验证文件大小是否在允许范围内
     *
     * @param fileSize 文件大小（字节）
     * @param maxSize 最大允许大小（字节）
     * @return 是否允许
     */
    public static boolean isAllowedFileSize(long fileSize, long maxSize) {
        return fileSize > 0 && fileSize <= maxSize;
    }
    
    /**
     * 格式化文件大小
     *
     * @param bytes 字节数
     * @return 格式化后的文件大小字符串
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    /**
     * 构建完整的文件URL
     *
     * @param endpoint OSS端点
     * @param bucketName 存储桶名称
     * @param objectKey 对象键
     * @return 完整的文件URL
     */
    public static String buildFileUrl(String endpoint, String bucketName, String objectKey) {
        if (!StringUtils.hasText(endpoint) || !StringUtils.hasText(bucketName) || !StringUtils.hasText(objectKey)) {
            return "";
        }
        
        return "https://" + bucketName + "." + endpoint + "/" + objectKey;
    }
    
    /**
     * 从URL中提取对象键
     *
     * @param fileUrl 文件URL
     * @param bucketName 存储桶名称
     * @param endpoint OSS端点
     * @return 对象键
     */
    public static String extractObjectKeyFromUrl(String fileUrl, String bucketName, String endpoint) {
        if (!StringUtils.hasText(fileUrl) || !StringUtils.hasText(bucketName) || !StringUtils.hasText(endpoint)) {
            return fileUrl;
        }
        
        String baseUrl = "https://" + bucketName + "." + endpoint + "/";
        if (fileUrl.startsWith(baseUrl)) {
            return fileUrl.substring(baseUrl.length());
        }
        
        return fileUrl;
    }
}
