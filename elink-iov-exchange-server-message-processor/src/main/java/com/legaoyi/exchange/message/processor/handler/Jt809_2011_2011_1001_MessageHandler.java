package com.legaoyi.exchange.message.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.DefaultMessageBuilder;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;

/**
 * 下级平台主链路登录请求消息
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt809_2011_2011_1001" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_2011_2011_1001_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_2011_2011_1001_MessageHandler.class);

    @Autowired
    @Qualifier("commonDownstreamMessageProducer")
    private MQMessageProducer commonDownstreamMessageProducer;

    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("******下级平台登陆请求消息,handle 1001 message={}", exchangeMessage);
        Map<?, ?> message = (Map<?, ?>) exchangeMessage.getMessage();
        Map<?, ?> messageHeader = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);

        long gnssCenterId = Long.parseLong(String.valueOf(messageHeader.get(Constants.MAP_KEY_GNSS_CENTER_ID)));

        String userId = String.valueOf(messageBody.get("userId"));
        String password = String.valueOf(messageBody.get("password"));
        String ip = String.valueOf(messageBody.get("ip"));
        Integer port = Integer.parseInt(String.valueOf(messageBody.get("port")));

        // 主链路登录请求消息，平台校验用户名密码之后，回复1002消息，todo
        // 这里模拟回复成功
        ExchangeMessage resp = DefaultMessageBuilder.buildRespMessage(exchangeMessage);
        commonDownstreamMessageProducer.send(resp.getGatewayId(), resp);
    }

}
