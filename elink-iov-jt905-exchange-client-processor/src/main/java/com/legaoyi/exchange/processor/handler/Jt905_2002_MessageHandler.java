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
 * 9200消息处理器入口
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "2002" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt905_2002_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt905_2002_MessageHandler.class);

    @Autowired
    @Qualifier("commonUpstreamMessageSendHandler")
    private CommonUpstreamMessageSendHandler commonUpstreamMessageSendHandler;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("******2002 message,message={}", exchangeMessage);
        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);
        Integer result = (Integer) messageBody.get("result");

        // 其他消息业务平台根据自身业务进行处理,todo

        // 这里模拟自动上报数据

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Map<String, String> messageMap = DefaultMessageBuilder.messageMap;
                        for (String messageId : messageMap.keySet()) {
                            ExchangeMessage resp = DefaultMessageBuilder.buildMessage(exchangeMessage, messageId);
                            if (resp != null) {
                                commonUpstreamMessageSendHandler.handle(resp);
                            }
                            Thread.sleep(5 * 1000);
                        }
                        Thread.sleep(30 * 1000);
                    } catch (Exception e) {

                    }
                }

            }
        }).start();

    }

}
