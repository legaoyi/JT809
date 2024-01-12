package com.legaoyi.exchange.message.processor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

@Component("gatewayRestartMessageHandler")
public class GatewayRestartMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(GatewayRestartMessageHandler.class);

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (exchangeMessage.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_RESTART)) {
            logger.info("******网关重启消息, message={}", exchangeMessage);
            //平台可根据应答结果处理消息，todo
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        }
    }
}
