package com.haohaoxuexi.gob.mq.config;

import com.haohaoxuexi.gob.mq.producer.StreamProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author loongzhou
 */
@Configuration
public class StreamConfiguration {
    @Bean
    public StreamProducer streamProducer() {
        StreamProducer streamProducer = new StreamProducer();
        return streamProducer;
    }
}
