package com.legaoyi.exchange.message.processor.handler.jt905;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.MessageHandler;
import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageProducer;

@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt905_2014_2014_2100" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt905_2100_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt905_2100_MessageHandler.class);

    @Autowired
    @Qualifier("commonDownstreamMessageProducer")
    private MQMessageProducer commonDownstreamMessageProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("******下级平台1200消息总入口，handle 2100 message={}", exchangeMessage);

        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        String protocol = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL);
        String protocolVersion = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL_VERSION);

        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_BODY);

        String dataType = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);
        if (messageBody != null) {
            dataType = (String) messageBody.get("dataType");
        }

        // 调用消息处理handler
        if (dataType != null) {
            String beanName = Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX.concat(protocol).concat("_").concat(protocolVersion).concat("_").concat(dataType).concat(Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX);
            if (ServerRuntimeContext.getApplicationContext().containsBean(beanName)) {
                MessageHandler messageHandler = (MessageHandler) ServerRuntimeContext.getBean(beanName);
                messageHandler.handle(exchangeMessage);
            } else {
                // todo
            }
        }
    }

}
