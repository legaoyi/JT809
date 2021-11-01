package com.legaoyi.exchange.processor.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.processor.util.ExchangeMessage;

/**
 * 网关中间件链路管理消息通知
 * 
 * @author gaoshengbo
 *
 */
@Component("gatewayLinkMessageHandler")
public class GatewayLinkMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(GatewayLinkMessageHandler.class);

    private Map<Long, Integer> mainLinkStateMap = new HashMap<Long, Integer>();

    @Autowired
    @Qualifier("urgentDownstreamMessageSendHandler")
    private UrgentDownstreamMessageSendHandler urgentDownstreamMessageSendHandler;

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (exchangeMessage.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_LINK_STATUS_MESSAGE)) {
            Map<?, ?> data = (Map<?, ?>) exchangeMessage.getMessage();
            logger.info("******链路管理消息,link state message,message={}", data);

            Long gnssCenterId = Long.parseLong(String.valueOf(data.get("gnssCenterId")));
            Integer result = Integer.parseInt(String.valueOf(data.get("result")));

            mainLinkStateMap.put(gnssCenterId, result);
            
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        }
    }
}
