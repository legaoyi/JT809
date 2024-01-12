package com.legaoyi.exchange.message.processor.handler.jt905;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.MessageHandler;
import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

/**
 * 下级平台实时上传车辆定位消息
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt905_2014_2014_2101" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt905_2100_2101_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt905_2100_2101_MessageHandler.class);

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        //位置信息处理
        logger.info("*******handle 2101 message={}", exchangeMessage);

        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_BODY);

        //这里可以根据业务需要，进行处理，以及把消息保存数据库，todo。注意最好是批量保存数据以提高性能
    }

}
