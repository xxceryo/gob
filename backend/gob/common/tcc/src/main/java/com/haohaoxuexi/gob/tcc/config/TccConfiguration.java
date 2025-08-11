package com.haohaoxuexi.gob.tcc.config;

import com.haohaoxuexi.gob.tcc.service.TransactionLogService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author loongzhou
 */
@Configuration
@MapperScan("com.haohaoxuexi.gob.tcc.mapper")
public class TccConfiguration {

    @Bean
    public TransactionLogService transactionLogService() {
        return new TransactionLogService();
    }
}
