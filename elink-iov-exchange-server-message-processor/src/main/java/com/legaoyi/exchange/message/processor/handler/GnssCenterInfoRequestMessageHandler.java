package com.legaoyi.exchange.message.processor.handler;

import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("gnssCenterInfoRequestMessageHandler")
public class GnssCenterInfoRequestMessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("commonDownstreamMessageProducer")
    private MQMessageProducer commonDownstreamMessageProducer;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_EXCHANGE_GNSS_CENTER_INFO_REQUEST_MESSAGE)) {
            List<?> list = new ArrayList<Object>();// 补充下级平台信息,todo
            if (list != null && !list.isEmpty()) {
                commonDownstreamMessageProducer.send(message.getGatewayId(), new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_GNSS_CENTER_INFO_RESP_MESSAGE, list, null, message.getGatewayId()));
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
