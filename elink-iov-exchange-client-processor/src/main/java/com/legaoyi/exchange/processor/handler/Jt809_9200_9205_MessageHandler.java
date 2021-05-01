package com.legaoyi.exchange.processor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.processor.util.Constants;
import com.legaoyi.exchange.processor.util.DefaultMessageBuilder;
import com.legaoyi.exchange.processor.util.ExchangeMessage;

/**
 * 9200 9205消息处理器
 * 
 * @author 高胜波
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "9200_9205" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_9200_9205_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_9200_9205_MessageHandler.class);

    @Autowired
    @Qualifier("commonUpstreamMessageSendHandler")
    private CommonUpstreamMessageSendHandler commonUpstreamMessageSendHandler;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        logger.info("handle 9205 message={}", message);
        // 这里业务平台可根据业务进行处理，todo

        // 模拟自动回复
        commonUpstreamMessageSendHandler.handle(DefaultMessageBuilder.buildRespMessage(message));
    }

}
