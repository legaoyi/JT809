package com.legaoyi.exchange.message.processor.handler.jt809;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.handler.MessageHandler;
import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;

/**
 * 下级平台实时上传车辆定位消息
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt809_2011_2011_1201" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_2011_2011_1201_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_2011_2011_1201_MessageHandler.class);

    @Autowired
    @Qualifier("commonDownstreamMessageProducer")
    private MQMessageProducer commonDownstreamMessageProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        // 车辆注册信息
        logger.info("*******handle 1201 message={}", exchangeMessage);

        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_BODY);

        // 这里可以根据业务需要，进行处理，以及把消息保存数据库，可以下发9601请求车辆静态信息
        Map<String, Object> respMessageHeader = new HashMap<String, Object>();
        respMessageHeader.putAll(messageHeader);
        respMessageHeader.put(Constants.MAP_KEY_MESSAGE_ID, "9600");

        Map<String, Object> respMessageBody = new HashMap<String, Object>();
        respMessageBody.put(Constants.MAP_KEY_DATA_TYPE, "9601");
        respMessageBody.put(Constants.MAP_KEY_VEHICLE_NO, messageBody.get(Constants.MAP_KEY_VEHICLE_NO));
        respMessageBody.put(Constants.MAP_KEY_VEHICLE_COLOR, messageBody.get(Constants.MAP_KEY_VEHICLE_COLOR));

        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put(Constants.MAP_KEY_MESSAGE_HEADER, respMessageHeader);
        resp.put(Constants.MAP_KEY_MESSAGE_BODY, respMessageBody);
        commonDownstreamMessageProducer.send(exchangeMessage.getGatewayId(), new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_DOWNSTREAM_MESSAGE, resp, null, exchangeMessage.getGatewayId()));

    }

}
