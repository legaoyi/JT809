package com.legaoyi.exchange.processor.handler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.processor.util.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;
/***
 * 平台紧急下行消息，如链路控制消息
 * @author gaoshengbo
 *
 */
@Component("urgentUpstreamMessageSendHandler")
public class UrgentUpstreamMessageSendHandler extends MessageHandler {

    @Autowired
    @Qualifier("upstreamMessageProducer")
    private MQMessageProducer upstreamMessageProducer;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        upstreamMessageProducer.send(message.getGatewayId().concat(".urgent"), message.toString());
        if (this.getSuccessor() != null) {
            this.getSuccessor().handle(message);
        }
    }

}
