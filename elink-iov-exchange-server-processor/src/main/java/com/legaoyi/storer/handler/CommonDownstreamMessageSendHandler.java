package com.legaoyi.storer.handler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.util.ExchangeMessage;
/***
 * 平台普通下行消息
 * @author 高胜波
 *
 */
@Component("commonDownstreamMessageSendHandler")
public class CommonDownstreamMessageSendHandler extends MessageHandler {

    @Autowired
    @Qualifier("downstreamMessageProducer")
    private MQMessageProducer downstreamMessageProducer;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        downstreamMessageProducer.send(message.getGatewayId().concat(".common"), message.toString());
        if (this.getSuccessor() != null) {
            this.getSuccessor().handle(message);
        }
    }

}