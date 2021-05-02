package com.legaoyi.exchange.processor.rabbitmq;

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

import com.legaoyi.exchange.processor.handler.MessageHandler;
import com.legaoyi.exchange.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageHandler;

@Component("urgentDownstreamMessageRabbitmqListener")
@RabbitListener(queues = "${rabbitmq.superior.urgent.queue}")
public class UrgentDownstreamMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(UrgentDownstreamMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Value("${rabbitmq.superior.urgent.queue}")
    private String urgentDownstreamMessageQueue;

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
            urgentDownstreamMessageHandler().handle(json);
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
            urgentDownstreamMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @Bean("urgentDownstreamMessageQueue")
    public Queue urgentDownstreamMessageQueue() {
        return new Queue(urgentDownstreamMessageQueue, durable);
    }

    @Bean("urgentDownstreamMessageHandler")
    public MQMessageHandler urgentDownstreamMessageHandler() {
        DownstreamMqMessageHandler handler = new DownstreamMqMessageHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("gatewayLinkMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("gatewayRespDownMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("downstreamMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
