package com.legaoyi.exchange.message.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

/**
 * 网关中间件链路管理消息通知
 * 
 * @author gaoshengbo
 *
 */
@Component("linkStateMessageHandler")
public class LinkStateMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(LinkStateMessageHandler.class);

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (exchangeMessage.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_LINK_STATUS_MESSAGE)) {
            Map<?, ?> data = (Map<?, ?>) exchangeMessage.getMessage();
            logger.info("******链路管理消息,link state message,message={}", data);
            long gnssCenterId = Long.parseLong(String.valueOf(data.get(Constants.MAP_KEY_GNSS_CENTER_ID)));
            Integer linkType = Integer.parseInt(String.valueOf(data.get("linkType")));
            Integer result = Integer.parseInt(String.valueOf(data.get("result")));

        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        }
    }
}
