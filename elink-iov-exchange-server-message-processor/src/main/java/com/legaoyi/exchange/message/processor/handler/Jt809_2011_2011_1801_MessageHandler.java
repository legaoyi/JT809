package com.legaoyi.exchange.message.processor.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.exchange.message.processor.util.Constants;
import com.legaoyi.exchange.message.processor.util.ExchangeMessage;

/**
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_PROCESSOR_BEAN_PREFIX + "jt809_2011_2011_1800_1801" + Constants.ELINK_MESSAGE_PROCESSOR_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Jt809_2011_2011_1801_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Jt809_2011_2011_1801_MessageHandler.class);

    @Autowired
    @Qualifier("videoPushManager")
    private VideoPushManager videoPushManager;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        logger.info("*******handle 1801 message={}", exchangeMessage);

        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);

        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);
        String vehicleNo = String.valueOf(messageBody.get("vehicleNo"));
        String vehicleColor = String.valueOf(messageBody.get("vehicleColor"));
        String ip = String.valueOf(messageBody.get("serverIp"));
        Integer port = Integer.parseInt(String.valueOf(messageBody.get("serverPort")));
        Integer result = Integer.parseInt(String.valueOf(messageBody.get("result")));
        int channelId = 1;// todo
        int avitemType = 0;// todo
        String authorizeCode = "32cc1a1ed15d7a30a63447f157790831fd2eb469710be68844b36dbc22966919";// todo
        if (result == 0) {
            // 这里拼1078拉流url,如：http://127.0.0.1:18181/鲁FM3635.2.1.0.32cc1a1ed15d7a30a63447f157790831fd2eb469710be68844b36dbc22966919
            String streamUrl = "http://" + ip + ":" + port + "/" + vehicleNo + "." + vehicleColor + "." + channelId + "." + avitemType + "." + authorizeCode;
            videoPushManager.push(streamUrl);
        }
    }

}
