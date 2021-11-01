package com.legaoyi.exchange.processor.rabbitmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.rabbitmq.RabbitmqDirectExchangeMessageProducer;

@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.subordinate.message.exchange}")
    private String upstreamMessageExchange;

    @Bean("upstreamMessageProducer")
    public MQMessageProducer upstreamMessageProducer() {
        return new RabbitmqDirectExchangeMessageProducer(upstreamMessageExchange);
    }
}
