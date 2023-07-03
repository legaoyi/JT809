package com.legaoyi.exchange.message.processor.handler;

import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

public interface UpstreamMessageProducer {

    public void send(ExchangeMessage message) throws Exception;
}
