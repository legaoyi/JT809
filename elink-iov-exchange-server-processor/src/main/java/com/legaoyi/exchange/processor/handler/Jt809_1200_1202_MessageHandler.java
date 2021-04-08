package com.legaoyi.exchange.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.processor.util.Constants;
import com.legaoyi.exchange.processor.util.ExchangeMessage;

/**
 * 下级平台实时上传车辆定位消息
 * @author 高胜波
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1200_1202" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_1200_1202_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_1200_1202_MessageHandler.class);


    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        //位置信息处理
        logger.info("*******handle 1202 message={}", exchangeMessage);

        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);

        //这里可以根据业务需要，进行处理，以及把消息保存数据库，todo。注意最好是批量保存数据以提高性能
    }

}
