package com.legaoyi.exchange.message.processor.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.DefaultMessageBuilder;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageProducer;

@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt809_2011_2011_1200" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_2011_2011_1200_MessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("commonDownstreamMessageProducer")
    private MQMessageProducer commonDownstreamMessageProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);

        String protocol = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL);
        String protocolVersion = (String) messageHeader.get(Constants.MAP_KEY_PROTOCOL_VERSION);
        // String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);

        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_BODY);

        String dataType = null;
        if (messageBody != null) {
            dataType = (String) messageBody.get(Constants.MAP_KEY_DATA_TYPE);
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
                    commonDownstreamMessageProducer.send(resp.getGatewayId(), resp);
                }
            }
        }
    }

}
