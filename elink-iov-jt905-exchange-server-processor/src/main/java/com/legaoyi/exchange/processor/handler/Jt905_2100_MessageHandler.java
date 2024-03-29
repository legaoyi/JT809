package com.legaoyi.exchange.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.processor.util.Constants;
import com.legaoyi.exchange.processor.util.DefaultMessageBuilder;
import com.legaoyi.exchange.processor.util.ExchangeMessage;
import com.legaoyi.exchange.processor.util.ServerRuntimeContext;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "2100" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt905_2100_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt905_2100_MessageHandler.class);

    @Autowired
    @Qualifier("commonDownstreamMessageSendHandler")
    private CommonDownstreamMessageSendHandler commonDownstreamMessageSendHandler;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("******下级平台1200消息总入口，handle 2100 message={}", exchangeMessage);

        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);

        String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);
        String dataType = null;
        if (messageBody != null) {
            dataType = (String) messageBody.get("dataType");
        }

        // 调用消息处理handler
        if (dataType != null) {
            try {
                MessageHandler messageHandler =
                        (MessageHandler) ServerRuntimeContext.getBean(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX.concat(messageId).concat("_").concat(dataType).concat(Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX));
                messageHandler.handle(exchangeMessage);
            } catch (NoSuchBeanDefinitionException e) {
                // 模拟自动回复消息，业务平台需根据业务情况处理，todo
                ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(exchangeMessage);
                if (resp != null) {
                    commonDownstreamMessageSendHandler.handle(resp);
                }
            }
        }
    }

}
