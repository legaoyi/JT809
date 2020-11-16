package com.legaoyi.storer.handler;

import com.legaoyi.storer.util.ExchangeMessage;

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
