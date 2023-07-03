package com.legaoyi.exchange.message.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

/**
 * 下级平台实时上传车辆定位消息
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt809_2011_2011_1601" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_2011_2011_1601_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_2011_2011_1601_MessageHandler.class);

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        // 车辆静态信息
        logger.info("*******handle 1601 message={}", exchangeMessage);

        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_BODY);

    }

}
