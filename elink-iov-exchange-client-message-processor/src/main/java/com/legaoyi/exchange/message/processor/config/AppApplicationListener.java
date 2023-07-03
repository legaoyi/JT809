package com.legaoyi.exchange.message.processor.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.legaoyi.exchange.message.processor.handler.UpstreamMessageProducer;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageListenerManager;

@SuppressWarnings("rawtypes")
public class AppApplicationListener implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(AppApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            stopServer();
        } else if (event instanceof ApplicationReadyEvent) {
            startServer();
        }
    }

    @SuppressWarnings("unchecked")
    public void startServer() {
        logger.info("*******server started successfully");
        List<Object> gnssCenterInfo = new ArrayList<Object>();

        // 启动从链路
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("ip", "127.0.0.1");
        map.put("port", 6045);
        map.put("gnssCenterId", 9616320);
        map.put("protocol", "jt809_2011");
        map.put("protocolVersion", "1078");
        map.put("userId", 9000630);
        map.put("password", "zgdz0228");
        map.put("versionFlag", "v1.0.0");
        map.put("verifyCode", 888);
        map.put("m1", 60000000);
        map.put("ia1", 70000000);
        map.put("ic1", 80000000);
        gnssCenterInfo.add(map);

        map = new HashMap<String, Object>();
        map.put("ip", "127.0.0.1");
        map.put("port", 6045);
        map.put("gnssCenterId", 9616321);
        map.put("protocol", "jt809_2011");
        map.put("protocolVersion", "1078");
        map.put("userId", 9000631);
        map.put("password", "zgdz0228");
        map.put("versionFlag", "v1.0.0");
        map.put("verifyCode", 888);
        map.put("m1", 60000000);
        map.put("ia1", 70000000);
        map.put("ic1", 80000000);
        gnssCenterInfo.add(map);

        try {
            for (Object info : gnssCenterInfo) {
                Map<String, Object> data = (Map<String, Object>) info;
                data.put("action", 3);
                ExchangeMessage message = new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_LINK_STATUS_MESSAGE, data, "", "");
                ServerRuntimeContext.getBean("urgentUpstreamMessageProducer",UpstreamMessageProducer.class).send(message);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private void stopServer() {
        // 停止接收mq消息
        try {
            ServerRuntimeContext.getBean(MQMessageListenerManager.class).stopAll();
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("*******server stoped successfully");
    }

}
