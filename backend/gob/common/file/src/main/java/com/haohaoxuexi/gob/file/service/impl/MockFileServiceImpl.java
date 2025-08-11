package com.haohaoxuexi.gob.file.service.impl;

import com.haohaoxuexi.gob.file.service.FileService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * oss 服务
 *
 * @author loongzhou
 */
@Slf4j
@Setter
public class MockFileServiceImpl implements FileService {


    @Override
    public boolean upload(String path, InputStream fileStream) {
        return true;
    }

}
