package com.legaoyi.exchange.processor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.processor.util.ExchangeMessage;
/***
 * 中间件网关应答平台下行消息处理结果
 * @author gaoshengbo
 *
 */
@Component("gatewayRespDownMessageHandler")
public class GatewayRespDownMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(GatewayRespDownMessageHandler.class);

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_RESP_MESSAGE)) {
            logger.info("******中间件应答平台下发消息，message={}",message);
            //平台可根据应答结果处理消息，todo
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
