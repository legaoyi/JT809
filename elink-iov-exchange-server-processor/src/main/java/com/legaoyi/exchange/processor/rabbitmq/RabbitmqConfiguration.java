package com.legaoyi.exchange.processor.rabbitmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.rabbitmq.RabbitmqDirectExchangeMessageProducer;

@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.superior.message.exchange}")
    private String downstreamMessageExchange;

    @Bean("downstreamMessageProducer")
    public MQMessageProducer downstreamMessageProducer() {
        return new RabbitmqDirectExchangeMessageProducer(downstreamMessageExchange);
    }
}
