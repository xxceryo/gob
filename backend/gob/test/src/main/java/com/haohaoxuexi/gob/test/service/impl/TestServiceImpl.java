package com.haohaoxuexi.gob.test.service.impl;

import com.haohaoxuexi.gob.test.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String msg) {
        return "Hello" + msg;
    }
}
