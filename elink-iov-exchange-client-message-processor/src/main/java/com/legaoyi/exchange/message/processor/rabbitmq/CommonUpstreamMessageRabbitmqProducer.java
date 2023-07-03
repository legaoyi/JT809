package com.legaoyi.exchange.message.processor.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ConditionalOnProperty(name= {"spring.rabbitmq.host"})
public class CommonUpstreamMessageRabbitmqProducer implements UpstreamMessageProducer {

    @Autowired
    @Qualifier("upstreamMessageProducer")
    private MQMessageProducer upstreamMessageProducer;

    @Override
    public void send(ExchangeMessage message) throws Exception {
        upstreamMessageProducer.send("common", message);
    }

}
