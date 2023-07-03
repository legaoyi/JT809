package com.legaoyi.exchange.message.processor.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.UpstreamMessageProducer;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;

/***
 * 平台紧急下行消息，如链路控制消息
 * 
 * @author gaoshengbo
 *
 */
@Component("urgentUpstreamMessageProducer")
@ConditionalOnProperty(name= {"spring.rabbitmq.host"})
public class UrgentUpstreamMessageRabbitmqProducer implements UpstreamMessageProducer {

    @Autowired
    @Qualifier("upstreamMessageProducer")
    private MQMessageProducer upstreamMessageProducer;

    @Override
    public void send(ExchangeMessage message) throws Exception {
        upstreamMessageProducer.send("urgent", message);
    }

}
