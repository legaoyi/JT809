package com.legaoyi.exchange.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.processor.util.Constants;
import com.legaoyi.exchange.processor.util.DefaultMessageBuilder;
import com.legaoyi.exchange.processor.util.ExchangeMessage;

/**
 * 下级平台主链路登录请求消息
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "2001" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt905_2001_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt905_2001_MessageHandler.class);

    @Autowired
    @Qualifier("urgentDownstreamMessageSendHandler")
    private UrgentDownstreamMessageSendHandler urgentDownstreamMessageSendHandler;

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("******下级平台登陆请求消息,handle 2001 message={}", exchangeMessage);
        Map<?, ?> message = (Map<?, ?>) exchangeMessage.getMessage();
        Map<?, ?> messageHeader = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);

        Object accessCode = messageHeader.get("gnssCenterId");
        
        Long gnssCenterId = Long.parseLong(String.valueOf(accessCode));
        
        String userId = String.valueOf(messageBody.get("userId"));
        String connectTime = String.valueOf(messageBody.get("connectTime"));
        String mac = String.valueOf(messageBody.get("mac"));
        //主链路登录请求消息，平台校验用户名密码之后，回复1002消息，todo
        
        
        //这里模拟回复成功
        urgentDownstreamMessageSendHandler.handle(DefaultMessageBuilder.buildRespMessage(exchangeMessage));
    }

}
