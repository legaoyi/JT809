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

@Component("commonDownstreamMessageRabbitmqListener")
@RabbitListener(queues = "${rabbitmq.superior.common.queue}")
public class CommonDownstreamMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(CommonDownstreamMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Value("${rabbitmq.superior.common.queue}")
    private String commonDownstreamMessageQueue;

    @RabbitHandler
    public void onMessage(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, DEFAULT_CHARSET);
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            commonDownstreamMessageHandler().handle(json);
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
            commonDownstreamMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @Bean("commonDownstreamMessageQueue")
    public Queue commonDownstreamMessageQueue() {
        return new Queue(commonDownstreamMessageQueue, durable);
    }

    @Bean("commonDownstreamMessageHandler")
    public MQMessageHandler commonDownstreamMessageHandler() {
        DownstreamMqMessageHandler handler = new DownstreamMqMessageHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("downstreamMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
