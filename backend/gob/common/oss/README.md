# OSS Starter 模块

## 概述

这是一个基于Spring Boot的OSS（对象存储服务）starter模块，提供了完整的阿里云OSS操作功能。该模块**只提供核心服务方法**，不包含REST API控制器，你可以在各个业务模块中直接使用这些方法来实现具体的业务逻辑。

## 主要特性

- 🚀 **开箱即用**：引入依赖即可使用，无需额外配置
- 🔐 **临时凭证生成**：为前端提供安全的临时上传凭证
- 📁 **文件管理**：支持文件上传、下载、删除、存在性检查等操作
- 🌐 **预签名URL**：生成临时访问链接，支持直接下载
- ⚙️ **自动配置**：Spring Boot自动配置，支持条件启用
- 🛠️ **工具类**：提供丰富的文件操作工具方法
- 🔧 **纯服务层**：只提供核心方法，不包含Web层，更加灵活

## 快速开始

### 1. 添加依赖

在你的项目中添加OSS模块依赖：

```xml
<dependency>
    <groupId>com.haohaoxuexi</groupId>
    <artifactId>oss</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置属性

在 `application.yml` 中添加配置：

```yaml
aliyun:
  oss:
    enabled: true
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: ${OSS_ACCESS_KEY_ID:your-access-key-id}
    access-key-secret: ${OSS_ACCESS_KEY_SECRET:your-access-key-secret}
    bucket-name: ${OSS_BUCKET_NAME:your-bucket-name}
    default-folder: uploads
```

### 3. 直接使用

```java
@Autowired
private OssService ossService;

// 生成上传凭证
TemporaryCredentials credentials = ossService.generateUploadCredentials("images", "photo.jpg");

// 下载文件
InputStream inputStream = ossService.downloadFile("images/photo.jpg");

// 获取预签名下载URL
String downloadUrl = ossService.getPresignedDownloadUrl("images/photo.jpg");
```

## 核心服务类

### OssService 接口

主要的OSS服务接口，提供以下方法：

#### 临时凭证生成
- `generateUploadCredentials(folder, fileName, durationSeconds)` - 生成指定文件夹的上传凭证
- `generateUploadCredentials(folder, fileName)` - 生成上传凭证（默认1小时有效期）
- `generateImageUploadCredentials(folder, fileName)` - 生成图片上传凭证
- `generateImageUploadCredentials(fileName)` - 生成图片上传凭证（默认图片文件夹）

#### 文件操作
- `downloadFile(objectKey)` - 下载文件
- `downloadFileByUrl(fileUrl)` - 通过URL下载文件
- `deleteFile(objectKey)` - 删除文件
- `deleteFileByUrl(fileUrl)` - 通过URL删除文件
- `deleteFiles(objectKeys)` - 批量删除文件
- `fileExists(objectKey)` - 检查文件是否存在
- `fileExistsByUrl(fileUrl)` - 通过URL检查文件是否存在

#### URL生成
- `getFileUrl(objectKey)` - 获取文件访问URL
- `getPresignedDownloadUrl(objectKey, durationSeconds)` - 生成预签名下载URL
- `getPresignedDownloadUrl(objectKey)` - 生成预签名下载URL（默认1小时）

#### 工具方法
- `extractObjectKeyFromUrl(fileUrl)` - 从URL中提取对象键

## 在业务模块中使用

### 1. 直接注入使用
```java
@Service
public class UserService {
    
    @Autowired
    private OssService ossService;
    
    public void uploadUserAvatar(String fileName, InputStream inputStream) {
        // 生成上传凭证
        TemporaryCredentials credentials = ossService.generateUploadCredentials("avatars", fileName);
        
        // 处理业务逻辑...
    }
}
```

### 2. 继承扩展
```java
@Service
public class CustomOssService extends OssServiceImpl {
    
    @Override
    public TemporaryCredentials generateUploadCredentials(String folder, String fileName, long durationSeconds) {
        // 添加自定义逻辑
        TemporaryCredentials credentials = super.generateUploadCredentials(folder, fileName, durationSeconds);
        
        // 自定义处理...
        return credentials;
    }
}
```

### 3. 组合使用
```java
@Service
public class FileService {
    
    @Autowired
    private OssService ossService;
    
    public void processFile(String fileName) {
        // 使用OSS服务
        if (ossService.fileExists("processed/" + fileName)) {
            // 处理逻辑...
        }
    }
}
```

### 4. 实现REST API
```java
@RestController
@RequestMapping("/api/files")
public class FileController {
    
    @Autowired
    private OssService ossService;
    
    @PostMapping("/upload/credentials")
    public ResponseEntity<Map<String, Object>> getUploadCredentials(
            @RequestParam String fileName,
            @RequestParam String folder) {
        
        try {
            TemporaryCredentials credentials = ossService.generateUploadCredentials(folder, fileName);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", credentials);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 错误处理...
        }
    }
    
    @GetMapping("/download/{objectKey}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String objectKey) {
        try {
            InputStream inputStream = ossService.downloadFile(objectKey);
            // 处理下载逻辑...
        } catch (Exception e) {
            // 错误处理...
        }
    }
}
```

## 工具类

### OssUtils

提供常用的工具方法：

```java
// 生成唯一文件名
String uniqueName = OssUtils.generateUniqueFileName("original.jpg");
// 输出: 1704096000000_abc123_original.jpg

// 提取原始文件名
String originalName = OssUtils.extractOriginalFileName("uploads/1704096000000_abc123_original.jpg");
// 输出: original.jpg

// 验证文件扩展名
boolean isAllowed = OssUtils.isAllowedFileExtension("image.jpg", new String[]{".jpg", ".png", ".gif"});

// 格式化文件大小
String size = OssUtils.formatFileSize(1024 * 1024); // 输出: 1.00 MB

// 构建文件URL
String url = OssUtils.buildFileUrl("oss-cn-hangzhou.aliyuncs.com", "bucket", "uploads/file.jpg");
```

## 配置说明

### 配置属性

| 属性名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `aliyun.oss.enabled` | boolean | true | 是否启用OSS模块 |
| `aliyun.oss.endpoint` | String | oss-cn-hangzhou.aliyuncs.com | OSS访问端点 |
| `aliyun.oss.access-key-id` | String | - | 访问密钥ID |
| `aliyun.oss.access-key-secret` | String | - | 访问密钥Secret |
| `aliyun.oss.bucket-name` | String | - | 存储桶名称 |
| `aliyun.oss.default-folder` | String | uploads | 默认文件夹路径 |

### 重要说明

**本starter模块不包含REST API控制器**，只提供核心的OSS操作方法。你需要在各个业务模块中：

1. **注入OssService**：直接使用OSS操作方法
2. **实现业务逻辑**：根据具体需求调用相应方法
3. **提供API接口**：在业务模块中实现具体的REST接口

## 环境配置

### 开发环境
```yaml
aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: dev-access-key
    access-key-secret: dev-access-secret
    bucket-name: dev-bucket
```

### 生产环境
```yaml
aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: ${OSS_ACCESS_KEY_ID}
    access-key-secret: ${OSS_ACCESS_KEY_SECRET}
    bucket-name: ${OSS_BUCKET_NAME}
```

## 注意事项

1. **安全性**：生产环境请使用环境变量或配置中心管理敏感信息
2. **权限控制**：确保OSS访问密钥具有最小必要权限
3. **错误处理**：建议在业务代码中添加适当的异常处理
4. **资源管理**：OSS客户端会自动管理连接池，无需手动关闭
5. **无Web层**：本starter不包含控制器，需要在业务模块中实现具体的API接口

## 故障排除

### 常见问题

1. **连接超时**: 检查网络连接和防火墙设置
2. **权限错误**: 验证AccessKey和权限配置
3. **存储桶不存在**: 确认存储桶名称和区域配置正确
4. **文件上传失败**: 检查文件大小和格式限制

### 日志调试

启用DEBUG日志级别：

```yaml
logging:
  level:
    com.haohaoxuexi.oss: DEBUG
```

## 版本历史

- v1.0.0 - 初始版本，提供基础OSS操作功能
- 支持临时凭证生成、文件上传下载、预签名URL等功能
- **纯服务层设计**，不包含Web控制器

## 许可证

本项目采用 MIT 许可证。
