package com.legaoyi.exchange.message.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.DefaultMessageBuilder;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;

/**
 * 上级平台下行消息处理入口
 * 
 * @author gaoshengbo
 *
 */
@Component("downstreamMessageHandler")
public class DownstreamMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(DownstreamMessageHandler.class);

    @Autowired
    @Qualifier("commonUpstreamMessageProducer")
    private UpstreamMessageProducer commonUpstreamMessageProducer;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_EXCHANGE_DOWNSTREAM_MESSAGE)) {
            logger.info("*******收到上级平台消息, message={}", message);
            Map<?, ?> data = (Map<?, ?>) message.getMessage();
            Map<?, ?> messageHeader = (Map<?, ?>) data.get(Constants.MAP_KEY_MESSAGE_HEADER);
            String protocol = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL);
            String protocolVersion = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL_VERSION);
            String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);

            String beanName = Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX.concat(protocol).concat("_").concat(protocolVersion).concat("_").concat(messageId).concat(Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX);
            if (ServerRuntimeContext.getApplicationContext().containsBean(beanName)) {
                MessageHandler messageHandler = ServerRuntimeContext.getBean(beanName, MessageHandler.class);
                messageHandler.handle(message);
            } else {
                // 模拟自动回复消息，业务平台需根据业务情况处理，todo
                ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(message);
                if (resp != null) {
                    commonUpstreamMessageProducer.send(resp);
                }
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
