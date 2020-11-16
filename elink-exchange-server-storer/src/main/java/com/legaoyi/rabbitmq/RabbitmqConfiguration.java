package com.legaoyi.rabbitmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.legaoyi.mq.MQMessageProducer;

@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.superior.message.exchange}")
    private String downstreamMessageExchange;

    @Bean("downstreamMessageProducer")
    public MQMessageProducer downstreamMessageProducer() {
        return new RabbitmqDirectExchangeMessageProducer(downstreamMessageExchange);
    }
}
