package com.legaoyi.exchange.message.processor.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.MessageHandler;
import com.legaoyi.exchange.message.processor.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageHandler;

@Component("urgentUpstreamMessageRabbitmqListener")
@ConditionalOnProperty(name = {"elink.upstream.urgent.topic", "spring.rabbitmq.host"})
@RabbitListener(queues = "${elink.upstream.urgent.topic}")
public class UrgentUpstreamMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(UrgentUpstreamMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

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

    @Bean("urgentUpstreamMessageHandler")
    public MQMessageHandler urgentUpstreamMessageHandler() {
        UpstreamMqMessageRabbitmqHandler handler = new UpstreamMqMessageRabbitmqHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("linkStateMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("gatewayRespDownstreamMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("gatewayRestartMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("exchangeUpstreamMessageHandler", MessageHandler.class));
        handlers.add(ServerRuntimeContext.getBean("gnssCenterInfoRequestMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
