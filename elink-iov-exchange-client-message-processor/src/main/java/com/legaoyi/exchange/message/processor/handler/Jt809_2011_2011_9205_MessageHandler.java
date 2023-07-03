package com.legaoyi.exchange.message.processor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.DefaultMessageBuilder;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

/**
 * 9200 9205消息处理器
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt809_2011_2011_9205" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_2011_2011_9205_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_2011_2011_9205_MessageHandler.class);

    @Autowired
    @Qualifier("commonUpstreamMessageProducer")
    private UpstreamMessageProducer commonUpstreamMessageProducer;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        // 这里业务平台可根据业务进行处理，todo

        // 模拟自动回复
        ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(message);

        logger.info("auto response message:{}", resp);
        commonUpstreamMessageProducer.send(resp);
    }

}
