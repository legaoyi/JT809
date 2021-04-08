package com.legaoyi.storer.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.DefaultMessageBuilder;
import com.legaoyi.storer.util.ExchangeMessage;

/**
 * 下级平台主链路登录请求消息
 * @author 高胜波
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1001" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_1001_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_1001_MessageHandler.class);

    @Autowired
    @Qualifier("urgentDownstreamMessageSendHandler")
    private UrgentDownstreamMessageSendHandler urgentDownstreamMessageSendHandler;

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("******下级平台登陆请求消息,handle 1001 message={}", exchangeMessage);
        Map<?, ?> message = (Map<?, ?>) exchangeMessage.getMessage();
        Map<?, ?> messageHeader = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);

        Long gnssCenterId = Long.parseLong(String.valueOf(messageBody.get("gnssCenterId")));
        String userId = String.valueOf(messageBody.get("userId"));
        String password = String.valueOf(messageBody.get("password"));
        String ip = String.valueOf(messageBody.get("ip"));
        Integer port = Integer.parseInt(String.valueOf(messageBody.get("port")));

        //主链路登录请求消息，平台校验用户名密码之后，回复1002消息，todo
        //这里模拟回复成功
        urgentDownstreamMessageSendHandler.handle(DefaultMessageBuilder.buildRespMessage(exchangeMessage));
    }

}
