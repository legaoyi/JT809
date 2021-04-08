package com.legaoyi.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.legaoyi.mq.MQMessageHandler;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.util.ServerRuntimeContext;

@Component("urgentUpstreamMessageRabbitmqListener")
@RabbitListener(queues = "${rabbitmq.subordinate.urgent.queue}")
public class UrgentUpstreamMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(UrgentUpstreamMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Value("${rabbitmq.subordinate.urgent.queue}")
    private String urgentUpstreamMessageQueue;

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @RabbitHandler
    public void onMessage(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, DEFAULT_CHARSET);
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            urgentUpstreamMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @RabbitHandler
    public void onMessage(String json) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            urgentUpstreamMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @Bean("urgentUpstreamMessageQueue")
    public Queue urgentUpstreamMessageQueue() {
        return new Queue(urgentUpstreamMessageQueue, durable);
    }

    @Bean("urgentUpstreamMessageHandler")
    public MQMessageHandler urgentUpstreamMessageHandler() {
        UpstreamMqMessageHandler handler = new UpstreamMqMessageHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("gatewayLinkMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("gatewayRespDownMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("upstreamMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
