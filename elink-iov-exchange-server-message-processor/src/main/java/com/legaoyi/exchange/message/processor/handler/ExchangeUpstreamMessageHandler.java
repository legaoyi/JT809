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
import com.legaoyi.mq.MQMessageProducer;

/**
 * 下级平台上行消息处理入口
 * 
 * @author gaoshengbo
 *
 */
@Component("exchangeUpstreamMessageHandler")
public class ExchangeUpstreamMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeUpstreamMessageHandler.class);

    @Autowired
    @Qualifier("commonDownstreamMessageProducer")
    private MQMessageProducer commonDownstreamMessageProducer;

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (exchangeMessage.getMessageId().equals(ExchangeMessage.MESSAGEID_EXCHANGE_UPSTREAM_MESSAGE)) {
            logger.info("*******收到下级平台消息, message={}", exchangeMessage);
            Map<?, ?> message = (Map<?, ?>) exchangeMessage.getMessage();
            Map<?, ?> messageHeader = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
            String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);
            // long gnssCenterId = Long.parseLong(String.valueOf(messageHeader.get(Constants.MAP_KEY_GNSS_CENTER_ID)));
            String protocol = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL);
            String protocolVersion = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL_VERSION);
            try {
                // 调用定制化消息处理器处理特定消息
                String beanName = Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX.concat(protocol).concat("_").concat(protocolVersion).concat("_").concat(messageId).concat(Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX);
                if (ServerRuntimeContext.getApplicationContext().containsBean(beanName)) {
                    MessageHandler messageHandler = ServerRuntimeContext.getBean(beanName, MessageHandler.class);
                    messageHandler.handle(exchangeMessage);
                } else {
                    // 其他消息业务平台根据自身业务进行处理,todo
                    // 这里模拟自动回复，
                    ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(exchangeMessage);
                    if (resp != null) {
                        commonDownstreamMessageProducer.send(resp.getGatewayId(), resp);
                    }
                }
            } catch (Exception e) {
                logger.error(" handler message error,message={}", exchangeMessage, e);
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        }
    }
}
