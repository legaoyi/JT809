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

import com.legaoyi.exchange.message.processor.handler.MessageHandler;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageHandler;

@Component("mediaUpstreamMessageKafkaListener")
@ConditionalOnProperty(name = {"elink.upstream.media.topic", "spring.kafka.bootstrap-servers"})
public class MediaUpstreamMessageKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(MediaUpstreamMessageKafkaListener.class);

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @KafkaListener(topics = "${elink.upstream.media.topic}")
    public void listen(String message, Acknowledgment ack) {
        ack.acknowledge();
        try {
            if (logger.isInfoEnabled()) {
                logger.info(message);
            }
            mediaUpstreamMessageHandler().handle(message);
        } catch (Exception e) {
            logger.error("******handle mq Message error,message={}", message, e);
        }
    }

    @Bean("mediaUpstreamMessageHandler")
    public MQMessageHandler mediaUpstreamMessageHandler() {
        UpstreamMqMessageKafkaHandler handler = new UpstreamMqMessageKafkaHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("exchangeUpstreamMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
