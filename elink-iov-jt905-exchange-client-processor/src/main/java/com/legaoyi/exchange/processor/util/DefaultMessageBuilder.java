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

    public static Map<String, String> messageMap = new HashMap<String, String>();

    static {
        messageMap.put("2101", "2100");
        messageMap.put("2102", "2100");
        messageMap.put("2103", "2100");
        messageMap.put("2104", "2100");

        messageMap.put("2201", "2200");
        messageMap.put("2202", "2200");

        messageMap.put("2301", "2300");
        messageMap.put("2302", "2300");
        messageMap.put("2303", "2300");
        messageMap.put("2304", "2300");
    }

    @SuppressWarnings("unchecked")
    public static ExchangeMessage buildMessage(ExchangeMessage exchangeMessage, String messageId) {
        Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();

        Map<String, Object> messageHeader = (Map<String, Object>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
        messageHeader.put(Constants.MAP_KEY_MESSAGE_ID, messageMap.get(messageId));
        message.put(Constants.MAP_KEY_MESSAGE_HEADER, messageHeader);

        Method method = null;
        try {
            method = DefaultMessageBuilder.class.getDeclaredMethod("build" + messageId + "MessageBody", new Class[] {});
            method.setAccessible(true);
            message.put(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY, method.invoke(DefaultMessageBuilder.class, new Object[] {}));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExchangeMessage resp = new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_UP_MESSAGE, message, "", exchangeMessage.getGatewayId());
        logger.info("******仿真模拟下级平台应答消息,message={}", resp);
        return resp;
    }

    private static Map<String, Object> build2101MessageBody() {
        List<Object> gnssList = new ArrayList<Object>();
        Map<String, Object> gnss = new HashMap<String, Object>();
        gnss.put("vehicleNo", "测A00001");
        gnss.put("vehicleColor", 1);
        gnss.put("encrypt", 0);
        gnss.put("state", 0);
        gnss.put("alarm", 0);
        gnss.put("dateTime", System.currentTimeMillis());
        gnss.put("lng", 32.5566);
        gnss.put("lat", 108.5599);
        gnss.put("vec", 80);
        gnss.put("direction", 150);
        gnssList.add(gnss);

        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2101");
        messageBody.put("gnssList", gnssList);

        return messageBody;
    }

    private static Map<String, Object> build2102MessageBody() {
        List<Object> gnssList = new ArrayList<Object>();
        Map<String, Object> gnss = new HashMap<String, Object>();
        
        gnss.put("encrypt", 0);
        gnss.put("state", 0);
        gnss.put("alarm", 0);
        gnss.put("dateTime", System.currentTimeMillis());
        gnss.put("lng", 33.5566);
        gnss.put("lat", 99.5599);
        gnss.put("vec", 92);
        gnss.put("direction", 126);
        gnssList.add(gnss);

        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "2102");
        messageBody.put("gnssList", gnssList);

        return messageBody;
    }

    private static Map<String, Object> build2103MessageBody() {
        List<Object> runDataList = new ArrayList<Object>();
        Map<String, Object> runData = new HashMap<String, Object>();
        runData.put("vehicleNo", "测A00001");
        runData.put("vehicleColor", 1);
        runData.put("encrypt", 0);
        runData.put("getOnTime", "2021-10-20 10:00:00");
        runData.put("getOnLng", 32.5566);
        runData.put("getOnLat", 108.5599);
        runData.put("getOffTime", "2021-10-20 11:00:00");
        runData.put("getOffLng", 58.5566);
        runData.put("getOffLat", 90.5599);
        runData.put("employeeId", "123456");
        runData.put("serviceEvalIdx", 20);
        
        runData.put("runOdometer", 55);
        runData.put("emptyOdometer",33);
        runData.put("fuelSurcharge", 10);
        runData.put("waitTime", 18);
        runData.put("income", 88);
        runData.put("icFlag", 1);
        runDataList.add(runData);

        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2103");
        messageBody.put("runDataList", runDataList);

        return messageBody;
    }

    private static Map<String, Object> build2104MessageBody() {
        List<Object> runDataList = new ArrayList<Object>();
        Map<String, Object> runData = new HashMap<String, Object>();
        runData.put("encrypt", 0);
        runData.put("getOnTime", "2021-10-20 10:00:00");
        runData.put("getOnLng", 32.5566);
        runData.put("getOnLat", 108.5599);
        runData.put("getOffTime", "2021-10-20 11:00:00");
        runData.put("getOffLng", 58.5566);
        runData.put("getOffLat", 90.5599);
        runData.put("employeeId", "123456");
        runData.put("serviceEvalIdx", 20);
        
        runData.put("runOdometer", 55);
        runData.put("emptyOdometer",33);
        runData.put("fuelSurcharge", 10);
        runData.put("waitTime", 18);
        runData.put("income", 88);
        runData.put("icFlag", 1);
        runDataList.add(runData);

        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "2104");
        messageBody.put("runDataList", runDataList);

        return messageBody;
    }

    private static Map<String, Object> build2201MessageBody() {
        Map<String, Object> sumData = new HashMap<String, Object>();
        sumData.put("N1", "N1");
        sumData.put("N2", "N2");
        sumData.put("N3", "N3");
        sumData.put("N4", "N4");
        sumData.put("N5", "N5");
        
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2201");
        messageBody.put("sumData", sumData);
        return messageBody;
    }

    private static Map<String, Object> build2202MessageBody() {
        Map<String, Object> sumData = new HashMap<String, Object>();
        sumData.put("N1", "N1");
        sumData.put("N2", "N2");
        sumData.put("N3", "N3");
        sumData.put("N4", "N4");
        sumData.put("N5", "N5");
        
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2202");
        messageBody.put("sumData", sumData);
        return messageBody;
    }

    private static Map<String, Object> build2301MessageBody() {
        Map<String, Object> baseData = new HashMap<String, Object>();
        baseData.put("N1", "N1");
        baseData.put("N2", "N2");
        baseData.put("N3", "N3");
        baseData.put("N4", "N4");
        baseData.put("N5", "N5");
        
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2301");
        messageBody.put("baseFlag", "123456");
        messageBody.put("dataOperator", 1);
        messageBody.put("baseData", baseData);
        return messageBody;
    }

    private static Map<String, Object> build2302MessageBody() {
        Map<String, Object> baseData = new HashMap<String, Object>();
        baseData.put("N1", "N1");
        baseData.put("N2", "N2");
        baseData.put("N3", "N3");
        baseData.put("N4", "N4");
        baseData.put("N5", "N5");
        
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2302");
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataOperator", 1);
        messageBody.put("baseData", baseData);
        return messageBody;
    }

    private static Map<String, Object> build2303MessageBody() {
        Map<String, Object> baseData = new HashMap<String, Object>();
        baseData.put("N1", "N1");
        baseData.put("N2", "N2");
        baseData.put("N3", "N3");
        baseData.put("N4", "N4");
        baseData.put("N5", "N5");
        
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2303");
        messageBody.put("employeeId", "123456");
        messageBody.put("dataOperator", 1);
        messageBody.put("baseData", baseData);
        return messageBody;
    }

    private static Map<String, Object> build2304MessageBody() {
        Map<String, Object> baseData = new HashMap<String, Object>();
        baseData.put("N1", "N1");
        baseData.put("N2", "N2");
        baseData.put("N3", "N3");
        baseData.put("N4", "N4");
        baseData.put("N5", "N5");
        
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "2304");
        messageBody.put("areaId", "123456");
        messageBody.put("taxiFareId", "123456");
        messageBody.put("dataOperator", 1);
        messageBody.put("baseData", baseData);
        return messageBody;
    }
}
