package com.haohaoxuexi.gob.mq.consumer;


import com.alibaba.fastjson2.JSON;
import com.haohaoxuexi.gob.mq.param.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;

import static com.haohaoxuexi.gob.mq.producer.StreamProducer.*;

/**
 * MQ消费基类
 * @author loongzhou
 */
@Slf4j
public class AbstractStreamConsumer {

    /**
     * 从msg中解析出消息对象
     *
     * @param msg
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getMessage(Message<MessageBody> msg, Class<T> type) {
        String messageId = msg.getHeaders().get(ROCKET_MQ_MESSAGE_ID, String.class);
        String tag = msg.getHeaders().get(ROCKET_TAGS, String.class);
        String topic = msg.getHeaders().get(ROCKET_MQ_TOPIC, String.class);
        Object object = JSON.parseObject(msg.getPayload().getBody(), type);
        log.info("Received Message topic:{} messageId:{},object:{}，tag:{}", topic, messageId, JSON.toJSONString(object), tag);
        return (T) object;
    }
}
