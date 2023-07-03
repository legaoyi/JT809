package com.legaoyi.exchange.message.processor.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.DefaultMessageBuilder;
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

    @Autowired
    @Qualifier("urgentUpstreamMessageProducer")
    private UpstreamMessageProducer urgentUpstreamMessageProducer;

    private boolean isMainLinkActive = false;

    private boolean isSubordinateLinkActive = false;

    private boolean running = false;

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (exchangeMessage.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_LINK_STATUS_MESSAGE)) {
            Map<?, ?> data = (Map<?, ?>) exchangeMessage.getMessage();
            logger.info("******链路管理消息,link state message,message={}", data);
            Long gnssCenterId = Long.parseLong(String.valueOf(data.get("gnssCenterId")));
            Integer linkType = Integer.parseInt(String.valueOf(data.get("linkType")));
            Integer result = Integer.parseInt(String.valueOf(data.get("result")));
            if (linkType == 0) {
                // 从链路
                if (result == 0) {
                    // 链路已断开
                    isSubordinateLinkActive = false;
                } else {
                    isSubordinateLinkActive = true;
                }
            } else {
                // 主链路
                if (result == 0) {
                    // 链路已断开
                    isMainLinkActive = false;
                } else {
                    isMainLinkActive = true;
                }
            }
            if (!isMainLinkActive && !isSubordinateLinkActive) {
                // 链路断开之后，自动重连链路,开启主链路（同时会向上级平台发送主链路登录消息）,注意当上级平台禁止连接时会导致死循环，谨慎使用
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("gnssCenterId", gnssCenterId);// 2019版本
                map.put("action", 3);
                // urgentDownMessageSendHandler
                // .handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_LINK_STATUS_MESSAGE, map, "", exchangeMessage.getGatewayId()));
            } else {
                // 模拟位置信息上报
                if (running) {
                    return;
                }
                running = true;
                new Thread(new Runnable() {

                    private long lastTime = 0;

                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(60 * 1000);
                                ExchangeMessage resp = new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_UPSTREAM_MESSAGE, DefaultMessageBuilder.build1202Message(), UUID.randomUUID().toString().replaceAll("-", ""), exchangeMessage.getGatewayId());
                                urgentUpstreamMessageProducer.send(resp);

                                long now = System.currentTimeMillis();
                                if (now - lastTime > 5 * 60 * 1000) {
                                    resp = new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_UPSTREAM_MESSAGE, DefaultMessageBuilder.build1201Message(), UUID.randomUUID().toString().replaceAll("-", ""), exchangeMessage.getGatewayId());
                                    urgentUpstreamMessageProducer.send(resp);

                                    resp = new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_UPSTREAM_MESSAGE, DefaultMessageBuilder.build1701Message(), UUID.randomUUID().toString().replaceAll("-", ""), exchangeMessage.getGatewayId());
                                   urgentUpstreamMessageProducer.send(resp);
                                    lastTime = now;

                                }
                            } catch (Exception e) {
                                logger.error("", e);
                            }
                        }
                    }
                }).start();
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        }
    }
}
