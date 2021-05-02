package com.legaoyi.exchange.processor.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("unused")
public class DefaultMessageBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageBuilder.class);

    private static Map<String, String> respMessageMap = new HashMap<String, String>();

    static {
        respMessageMap.put("1001", "1002");
        respMessageMap.put("1003", "1004");

        respMessageMap.put("1207", "9200,9207");
        respMessageMap.put("1208", "9200,9208");
        respMessageMap.put("1209", "9200,9209");
    }

    @SuppressWarnings("unchecked")
    public static ExchangeMessage buildRespMessage(ExchangeMessage exchangeMessage) {
        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);

        String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);
        String dataType = null;
        if (messageBody != null) {
            dataType = (String) messageBody.get("dataType");
        }

        if (dataType != null && !"".equals(dataType)) {
            dataType = respMessageMap.get(dataType);
            if (dataType != null) {
                String[] arr = dataType.split(",");
                messageId = arr[0];
                dataType = arr[1];
            } else {
                messageId = null;
                dataType = null;
            }
        } else {
            messageId = respMessageMap.get(messageId);
        }

        if (messageId == null) {
            return null;
        }

        messageHeader.put(Constants.MAP_KEY_MESSAGE_ID, messageId);
        message.put(Constants.MAP_KEY_MESSAGE_HEADER, messageHeader);
        if (dataType != null && !"".equals(dataType)) {
            messageId = dataType;
        }
        Method method = null;
        try {
            method = DefaultMessageBuilder.class.getDeclaredMethod("build" + messageId+"MessageBody", new Class[] {});
            method.setAccessible(true);
            message.put(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY, method.invoke(DefaultMessageBuilder.class, new Object[] {}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExchangeMessage resp = new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_DOWN_MESSAGE, message, "", exchangeMessage.getGatewayId());
        logger.info("******仿真模拟上级平台应答消息,message={}",resp);
        return resp;
    }

    private static Map<String, Object> build1002MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("result", 0);
        messageBody.put("verifyCode", 789);
        return messageBody;
    }

    private static Map<String, Object> build1004MessageBody() {
        return new HashMap<String, Object>();
    }

    private static Map<String, Object> build9207MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "9207");
        messageBody.put("result", 0);
        return messageBody;
    }

    private static Map<String, Object> build9208MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "9208");
        messageBody.put("result", 0);
        return messageBody;
    }

    private static Map<String, Object> build9209MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "9209");
        messageBody.put("result", 0);
        return messageBody;
    }

}
