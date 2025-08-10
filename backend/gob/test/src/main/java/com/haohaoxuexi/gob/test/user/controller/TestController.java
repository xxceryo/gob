package com.haohaoxuexi.gob.test.user.controller;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.haohaoxuexi.gob.lock.DistributeLock;
import com.haohaoxuexi.gob.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;


    @GetMapping("/hello")
    @DistributeLock(scene = "test", keyExpression = "#msg")
    @Cached(name = "test", cacheType = CacheType.BOTH, localExpire = 100, timeUnit = TimeUnit.SECONDS, expire = 100)
    public String hello(@RequestParam String msg) {
        return testService.sayHello(msg);
    }

}
