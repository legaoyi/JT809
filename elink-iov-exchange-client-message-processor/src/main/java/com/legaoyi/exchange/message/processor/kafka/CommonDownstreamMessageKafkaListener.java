package com.legaoyi.exchange.message.processor.kafka;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.DownstreamMqMessageHandler;
import com.legaoyi.exchange.message.processor.handler.MessageHandler;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageHandler;

@Component("commonUpstreamMessageKafkaListener")
@ConditionalOnProperty(name = {"elink.downstream.common.topic", "spring.kafka.bootstrap-servers"})
public class CommonDownstreamMessageKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(CommonDownstreamMessageKafkaListener.class);

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @KafkaListener(topics = "${elink.downstream.common.topic}")
    public void listen(String message, Acknowledgment ack) {
        ack.acknowledge();
        try {
            if (logger.isInfoEnabled()) {
                logger.info(message);
            }
            commonDownstreamMessageHandler().handle(message);
        } catch (Exception e) {
            logger.error("******handle mq Message error,message={}", message, e);
        }
    }

    @Bean("commonDownstreamMessageHandler")
    public MQMessageHandler commonDownstreamMessageHandler() {
        DownstreamMqMessageHandler handler = new DownstreamMqMessageHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("linkStateMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("gatewayRespUpstreamMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("downstreamMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
