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
            Object accessCode = data.get("gnssCenterId");//2019版本
            accessCode = (accessCode == null? data.get("accessCode"):accessCode);//2011版本
            
            Long gnssCenterId = Long.parseLong(String.valueOf(accessCode));
            Integer linkType = Integer.parseInt(String.valueOf(data.get("linkType")));
            Integer result = Integer.parseInt(String.valueOf(data.get("result")));

            if (linkType == 1) {
                mainLinkStateMap.put(gnssCenterId, result);
            } else {
                if (result == 0) {
                    Integer state = mainLinkStateMap.get(gnssCenterId);
                    if (state != null && state == 1) {
                        // 主链路正常，从链路已断开
                        // 这里模拟重新连接从链路，平台可根据业务情况处理,todo
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("gnssCenterId", gnssCenterId);//2019版本
                        map.put("accessCode", gnssCenterId);//2011版本
                        map.put("action", 3);
                        //urgentDownstreamMessageSendHandler
                        //        .handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_LINK_STATUS_MESSAGE, map, "", exchangeMessage.getGatewayId()));
                    }
                }
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        }
    }
}
