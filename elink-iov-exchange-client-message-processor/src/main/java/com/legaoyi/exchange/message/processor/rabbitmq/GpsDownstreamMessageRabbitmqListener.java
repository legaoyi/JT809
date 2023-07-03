package com.legaoyi.exchange.message.processor.rabbitmq;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.DownstreamMqMessageHandler;
import com.legaoyi.exchange.message.processor.handler.MessageHandler;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageHandler;

@Component("gpsDownstreamMessageRabbitmqListener")
@ConditionalOnProperty(name= {"elink.downstream.gps.topic","spring.rabbitmq.host"})
@RabbitListener(queues = "${elink.downstream.gps.topic}")
public class GpsDownstreamMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(GpsDownstreamMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Value("${elink.downstream.gps.topic}")
    private String gpsDownstreamMessageQueue;

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
            gpsDownstreamMessageHandler().handle(json);
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
            gpsDownstreamMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @Bean("gpsDownstreamMessageQueue")
    public Queue gpsDownstreamMessageQueue() {
        return new Queue(gpsDownstreamMessageQueue, durable);
    }

    @Bean("gpsDownstreamMessageHandler")
    public MQMessageHandler gpsDownstreamMessageHandler() {
        DownstreamMqMessageHandler handler = new DownstreamMqMessageHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("downstreamMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
