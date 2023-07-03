package com.legaoyi.exchange.message.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "gb32960_2016_05" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Gb32960_2016_2016_05_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Gb32960_2016_2016_05_MessageHandler.class);

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);

        String protocol = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL);
        String protocolVersion = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL_VERSION);
        // String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);

        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_BODY);

        logger.info("******gb32960，平台登入消息,message={}", exchangeMessage);
    }

}
