package com.haohaoxuexi.oss;

import com.haohaoxuexi.god.oss.config.OssProperties;
import com.haohaoxuexi.god.oss.model.TemporaryCredentials;
import com.haohaoxuexi.god.oss.service.OssService;
import com.haohaoxuexi.god.oss.service.impl.OssServiceImpl;
import com.haohaoxuexi.god.oss.utils.OssUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OSS服务测试类
 */
@SpringBootTest
public class OssServiceTest {
    
    private OssService ossService;
    private OssProperties ossProperties;
    
    @BeforeEach
    void setUp() {
        // 创建测试配置
        ossProperties = new OssProperties();
        ossProperties.setEndpoint("oss-cn-hangzhou.aliyuncs.com");
        ossProperties.setAccessKeyId("test-access-key-id");
        ossProperties.setAccessKeySecret("test-access-key-secret");
        ossProperties.setBucketName("test-bucket");
        ossProperties.setDefaultFolder("test-uploads");
        
        // 创建服务实例
        ossService = new OssServiceImpl();
    }
    
    @Test
    void testGenerateUploadCredentials() {
        // 测试生成上传凭证
        TemporaryCredentials credentials = ossService.generateUploadCredentials("test-folder", "test.jpg", 3600L);
        
        assertNotNull(credentials);
        assertEquals("test-bucket", credentials.getBucketName());
        assertEquals("oss-cn-hangzhou.aliyuncs.com", credentials.getEndpoint());
        assertTrue(credentials.getObjectKey().startsWith("test-folder/"));
        assertTrue(credentials.getObjectKey().endsWith("test.jpg"));
    }
    
    @Test
    void testGenerateImageUploadCredentials() {
        // 测试生成图片上传凭证
        TemporaryCredentials credentials = ossService.generateImageUploadCredentials("test-folder", "image.png");
        
        assertNotNull(credentials);
        assertEquals("test-bucket", credentials.getBucketName());
        assertTrue(credentials.getObjectKey().startsWith("test-folder/"));
        assertTrue(credentials.getObjectKey().endsWith("image.png"));
    }
    
    @Test
    void testGetFileUrl() {
        // 测试获取文件URL
        String objectKey = "test-folder/test.jpg";
        String fileUrl = ossService.getFileUrl(objectKey);
        
        String expectedUrl = "https://test-bucket.oss-cn-hangzhou.aliyuncs.com/" + objectKey;
        assertEquals(expectedUrl, fileUrl);
    }
    
    @Test
    void testExtractObjectKeyFromUrl() {
        // 测试从URL提取对象键
        String fileUrl = "https://test-bucket.oss-cn-hangzhou.aliyuncs.com/test-folder/test.jpg";
        String objectKey = ossService.extractObjectKeyFromUrl(fileUrl);
        
        assertEquals("test-folder/test.jpg", objectKey);
    }
    
    @Test
    void testOssUtils() {
        // 测试工具类方法
        
        // 测试生成唯一文件名
        String uniqueName = OssUtils.generateUniqueFileName("test.jpg");
        assertNotNull(uniqueName);
        assertTrue(uniqueName.contains("test.jpg"));
        assertTrue(uniqueName.contains("_"));
        
        // 测试提取原始文件名
        String originalName = OssUtils.extractOriginalFileName("test-folder/1704096000000_abc123_test.jpg");
        assertEquals("test.jpg", originalName);
        
        // 测试文件扩展名验证
        boolean isAllowed = OssUtils.isAllowedFileExtension("image.jpg", new String[]{".jpg", ".png"});
        assertTrue(isAllowed);
        
        // 测试文件大小验证
        boolean isValidSize = OssUtils.isAllowedFileSize(1024 * 1024, 10 * 1024 * 1024);
        assertTrue(isValidSize);
        
        // 测试文件大小格式化
        String size = OssUtils.formatFileSize(1024 * 1024);
        assertEquals("1.00 MB", size);
        
        // 测试构建文件URL
        String url = OssUtils.buildFileUrl("oss-cn-hangzhou.aliyuncs.com", "test-bucket", "test-folder/file.jpg");
        assertEquals("https://test-bucket.oss-cn-hangzhou.aliyuncs.com/test-folder/file.jpg", url);
    }
}
