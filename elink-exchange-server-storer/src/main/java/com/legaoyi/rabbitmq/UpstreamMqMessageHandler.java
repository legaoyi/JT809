package com.legaoyi.rabbitmq;

import java.util.Iterator;
import java.util.List;

import com.legaoyi.mq.MQMessageHandler;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.util.ExchangeMessage;
import com.legaoyi.storer.util.JsonUtil;

/***
 * 下级平台上行消息处理入口
 * 
 * @author 高胜波
 *
 */
public class UpstreamMqMessageHandler implements MQMessageHandler {

    private List<MessageHandler> handlers;

    public void setHandlers(List<MessageHandler> handlers) {
        this.handlers = handlers;
        if (handlers != null) {
            Iterator<MessageHandler> it = handlers.iterator();
            MessageHandler handler = it.next();
            while (it.hasNext()) {
                MessageHandler successor = it.next();
                handler.setSuccessor(successor);
                handler = successor;
            }
        }
    }

    public void handle(String message) throws Exception {
        if (handlers != null && !handlers.isEmpty()) {
            handlers.get(0).handle(JsonUtil.convertStringToObject(message, ExchangeMessage.class));
        }
    }

    @Override
    public void handle(byte[] bytes) throws Exception {

    }
}
