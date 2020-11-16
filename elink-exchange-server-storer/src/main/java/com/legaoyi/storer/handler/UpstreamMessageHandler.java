package com.legaoyi.storer.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.DefaultMessageBuilder;
import com.legaoyi.storer.util.ExchangeMessage;
import com.legaoyi.storer.util.ServerRuntimeContext;

/**
 * 下级平台上行消息处理入口
 * 
 * @author 高胜波
 *
 */
@Component("upstreamMessageHandler")
public class UpstreamMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpstreamMessageHandler.class);

    @Autowired
    @Qualifier("commonDownstreamMessageSendHandler")
    private CommonDownstreamMessageSendHandler commonDownstreamMessageSendHandler;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_EXCHANGE_UP_MESSAGE)) {
            logger.info("*******收到下级平台消息, message={}", message);
            Map<?, ?> data = (Map<?, ?>) message.getMessage();
            Map<?, ?> messageHeader = (Map<?, ?>) data.get(Constants.MAP_KEY_MESSAGE_HEADER);
            String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);

            try {
                // 调用定制化消息处理器处理特定消息
                MessageHandler messageHandler = (MessageHandler) ServerRuntimeContext.getBean(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX.concat(messageId).concat(Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX));
                messageHandler.handle(message);
            } catch (NoSuchBeanDefinitionException e) {
                // 其他消息业务平台根据自身业务进行处理,todo

                // 这里模拟自动回复，
                ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(message);
                if (resp != null) {
                    commonDownstreamMessageSendHandler.handle(resp);
                }
            } catch (Exception e) {
                logger.error(" handler message error,message={}", message, e);
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
