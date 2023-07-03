package com.legaoyi.exchange.message.processor.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.UpstreamMessageProducer;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;

/***
 * 平台普通下行消息
 * 
 * @author gaoshengbo
 *
 */
@Component("commonUpstreamMessageProducer")
@ConditionalOnProperty(name= {"spring.kafka.bootstrap-servers"})
public class CommonUpstreamMessageKafkaProducer implements UpstreamMessageProducer {

    @Value("${elink.upstream.common.topic}")
    private String topic;
    
    @Autowired
    @Qualifier("upstreamMessageProducer")
    private MQMessageProducer upstreamMessageProducer;

    @Override
    public void send(ExchangeMessage message) throws Exception {
        upstreamMessageProducer.send(topic, message);
    }

}
