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
/**
 * 上级平台下行消息处理入口
 * @author gaoshengbo
 *
 */
@Component("downstreamMessageHandler")
public class DownstreamMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(DownstreamMessageHandler.class);

    @Autowired
    @Qualifier("commonUpstreamMessageSendHandler")
    private CommonUpstreamMessageSendHandler commonUpstreamMessageSendHandler;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_EXCHANGE_DOWN_MESSAGE)) {
            Map<?, ?> data = (Map<?, ?>) message.getMessage();
            Map<?, ?> messageHeader = (Map<?, ?>) data.get(Constants.MAP_KEY_MESSAGE_HEADER);
            String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);

            try {
                //调用定制化消息处理器处理特定消息
                MessageHandler messageHandler = (MessageHandler) ServerRuntimeContext.getBean(
                        Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX.concat(messageId).concat(Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX));
                messageHandler.handle(message);
            } catch (NoSuchBeanDefinitionException e) {
                //其他消息业务平台根据自身业务进行处理,todo
                
                //这里模拟自动回复，
                ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(message);
                if (resp != null) {
                    commonUpstreamMessageSendHandler.handle(resp);
                }
            } catch (Exception e) {
                logger.error("handler message error,message={}", message, e);
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
