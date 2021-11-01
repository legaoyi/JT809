package com.legaoyi.exchange.processor.handler;

import com.legaoyi.exchange.processor.util.ExchangeMessage;

public abstract class MessageHandler {

    private MessageHandler successor;

    public MessageHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(MessageHandler successor) {
        this.successor = successor;
    }

    public abstract void handle(ExchangeMessage message) throws Exception;

}
