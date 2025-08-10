package com.haohaoxuexi.god.oss.service;

import com.haohaoxuexi.god.oss.model.TemporaryCredentials;

import java.io.InputStream;
import java.util.List;

/**
 * OSS服务接口
 */
public interface OssService {

    /**
     * 生成上传临时凭证
     *
     * @param folder           文件夹路径
     * @param fileName        文件名
     * @param durationSeconds 有效期（秒）
     * @return 临时凭证
     */
    TemporaryCredentials generateUploadCredentials(String folder, String fileName, long durationSeconds);

    /**
     * 生成上传临时凭证（默认1小时有效期）
     *
     * @param folder    文件夹路径
     * @param fileName 文件名
     * @return 临时凭证
     */
    TemporaryCredentials generateUploadCredentials(String folder, String fileName);

    /**
     * 生成图片上传临时凭证
     *
     * @param folder    文件夹路径
     * @param fileName 文件名
     * @return 临时凭证
     */
    TemporaryCredentials generateImageUploadCredentials(String folder, String fileName);

    /**
     * 生成图片上传临时凭证（默认图片文件夹）
     *
     * @param fileName 文件名
     * @return 临时凭证
     */
    TemporaryCredentials generateImageUploadCredentials(String fileName);

    /**
     * 下载文件
     *
     * @param objectKey 对象键
     * @return 文件输入流
     */
    InputStream downloadFile(String objectKey);

    /**
     * 通过URL下载文件
     *
     * @param fileUrl 文件URL
     * @return 文件输入流
     */
    InputStream downloadFileByUrl(String fileUrl);

    /**
     * 删除文件
     *
     * @param objectKey 对象键
     * @return 是否删除成功
     */
    boolean deleteFile(String objectKey);

    /**
     * 通过URL删除文件
     *
     * @param fileUrl 文件URL
     * @return 是否删除成功
     */
    boolean deleteFileByUrl(String fileUrl);

    /**
     * 批量删除文件
     *
     * @param objectKeys 对象键列表
     * @return 成功删除的文件数量
     */
    int deleteFiles(List<String> objectKeys);

    /**
     * 检查文件是否存在
     *
     * @param objectKey 对象键
     * @return 文件是否存在
     */
    boolean fileExists(String objectKey);

    /**
     * 通过URL检查文件是否存在
     *
     * @param fileUrl 文件URL
     * @return 文件是否存在
     */
    boolean fileExistsByUrl(String fileUrl);

    /**
     * 获取文件访问URL
     *
     * @param objectKey 对象键
     * @return 文件访问URL
     */
    String getFileUrl(String objectKey);

    /**
     * 获取预签名下载URL
     *
     * @param objectKey       对象键
     * @param durationSeconds 有效期（秒）
     * @return 预签名下载URL
     */
    String getPresignedDownloadUrl(String objectKey, long durationSeconds);

    /**
     * 获取预签名下载URL（默认1小时有效期）
     *
     * @param objectKey 对象键
     * @return 预签名下载URL
     */
    String getPresignedDownloadUrl(String objectKey);

    /**
     * 从URL中提取对象键
     *
     * @param fileUrl 文件URL
     * @return 对象键
     */
    String extractObjectKeyFromUrl(String fileUrl);
}
