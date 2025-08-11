package com.haohaoxuexi.gob.file.service;

import java.io.InputStream;

/**
 * 文件 服务
 *
 * @author loongzhou
 */
public interface FileService {

    /**
     * 文件上传
     * @param path
     * @param fileStream
     * @return
     */
    public boolean upload(String path, InputStream fileStream);

}
