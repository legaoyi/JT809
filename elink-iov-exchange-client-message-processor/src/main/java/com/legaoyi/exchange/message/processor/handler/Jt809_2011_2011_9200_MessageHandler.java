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
 * 9200消息处理器入口
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt809_2011_2011_9200" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_2011_2011_9200_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_2011_2011_9200_MessageHandler.class);

    @Autowired
    @Qualifier("commonUpstreamMessageProducer")
    private UpstreamMessageProducer commonUpstreamMessageProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("******上级平台9200 消息总入口，message={}", exchangeMessage);
        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);

        String protocol = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL);
        String protocolVersion = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL_VERSION);
        // String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);

        String dataType = null;
        if (messageBody != null) {
            dataType = (String) messageBody.get("dataType");
        }

        // 调用消息处理handler
        if (dataType != null) {
            String beanName = Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX.concat(protocol).concat("_").concat(protocolVersion).concat("_").concat(dataType).concat(Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX);
            if (ServerRuntimeContext.getApplicationContext().containsBean(beanName)) {
                MessageHandler messageHandler = ServerRuntimeContext.getBean(beanName, MessageHandler.class);
                messageHandler.handle(exchangeMessage);
            } else {
                // 模拟自动回复消息，业务平台需根据业务情况处理，todo
                ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(exchangeMessage);
                if (resp != null) {
                    commonUpstreamMessageProducer.send(resp);
                }
            }
        }
    }

}
