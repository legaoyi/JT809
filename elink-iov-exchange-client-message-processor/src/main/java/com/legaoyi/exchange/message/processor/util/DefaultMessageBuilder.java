package com.legaoyi.exchange.message.processor.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
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
        respMessageMap.put("9205", "1200,1205");
        respMessageMap.put("9206", "1200,1206");
        respMessageMap.put("920A", "1200,120A");
        respMessageMap.put("920B", "1200,120B");

        respMessageMap.put("9301", "1300,1301");
        respMessageMap.put("9302", "1300,1302");

        respMessageMap.put("9401", "1400,1401");

        respMessageMap.put("9501", "1500,1501");
        respMessageMap.put("9502", "1500,1502");
        respMessageMap.put("9503", "1500,1503");
        respMessageMap.put("9504", "1500,1504");
        respMessageMap.put("9505", "1500,1505");

        respMessageMap.put("9601", "1600,1601");

        respMessageMap.put("9801", "1800,1801");
        respMessageMap.put("9802", "1800,1802");

        respMessageMap.put("9902", "1900,1902");

        respMessageMap.put("9A01", "1A00,1A01");
        respMessageMap.put("9A02", "1A00,1A02");
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
            method = DefaultMessageBuilder.class.getDeclaredMethod("build" + messageId + "MessageBody", new Class[] {});
            method.setAccessible(true);
            message.put(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY, method.invoke(DefaultMessageBuilder.class, new Object[] {}));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExchangeMessage resp = new ExchangeMessage(ExchangeMessage.MESSAGEID_EXCHANGE_UPSTREAM_MESSAGE, message, "", exchangeMessage.getGatewayId());
        logger.info("******仿真模拟下级平台应答消息,message={}", resp);
        return resp;
    }

    private static Map<String, Object> build1205MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1205");
        return messageBody;
    }

    private static Map<String, Object> build1206MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1206");
        return messageBody;
    }

    private static Map<String, Object> build120AMessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "120A");
        messageBody.put("driverName", "张三");
        messageBody.put("driverId", "3289902836789987621");
        messageBody.put("licence", "312321312321312");
        messageBody.put("orgName", "交通部");
        return messageBody;
    }

    private static Map<String, Object> build120BMessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "120B");
        messageBody.put("waybill", "张三");
        return messageBody;
    }

    private static Map<String, Object> build1301MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("objectType", 2);
        messageBody.put("objectId", "12345");
        messageBody.put("dataType", "1301");
        messageBody.put("infoId", 1);
        messageBody.put("infoContent", "北京");
        return messageBody;
    }

    private static Map<String, Object> build1302MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "1302");
        messageBody.put("infoId", 1);
        return messageBody;
    }

    private static Map<String, Object> build1401MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1401");
        messageBody.put("supervisionId", 1);
        messageBody.put("result", 0);
        return messageBody;
    }

    private static Map<String, Object> build1501MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1501");
        messageBody.put("result", 0);
        return messageBody;
    }

    private static Map<String, Object> build1502MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1502");
        messageBody.put("photoRspFlag", 1);
        Map<String, Object> gnssData = new HashMap<String, Object>();
        gnssData.put("vehicleColor", 0);
        gnssData.put("encrypt", 1);
        gnssData.put("time", System.currentTimeMillis());
        gnssData.put("lng", 119.0);
        gnssData.put("lat", 32.0);
        gnssData.put("vec1", 50);
        gnssData.put("vec3", 100);
        gnssData.put("direction", 50);
        gnssData.put("altitude", 50);
        gnssData.put("state", 3);
        gnssData.put("alarm", 0);
        messageBody.put("gnssData", gnssData);
        messageBody.put("lensId", 1);
        messageBody.put("type", 1);
        messageBody.put("photo", "终端上传的照片信息byte字节转成十六进制字符串");
        return messageBody;
    }

    private static Map<String, Object> build1503MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1503");
        messageBody.put("msgId", 1);
        messageBody.put("result", 0);
        return messageBody;
    }

    private static Map<String, Object> build1504MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1504");
        messageBody.put("commandType", "01");
        messageBody.put("travelDataInfo", "终端上传的行驶记录信息byte字节转成十六进制字符串");
        return messageBody;
    }

    private static Map<String, Object> build1505MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1505");
        messageBody.put("result", 0);
        return messageBody;
    }

    private static Map<String, Object> build1601MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "测A00001");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1601");
        Map<String, Object> carInfo = new HashMap<String, Object>();
        carInfo.put("OWERS_NAME", "宝钢集团有限公司");
        carInfo.put("VEHICLE_NATIONALITY", "330108");
        carInfo.put("OWERS_TEL", "13516814499");
        carInfo.put("OWERS_ID", "382738");
        carInfo.put("VIN", "测A11111");
        carInfo.put("TRANS_TYPE", "030");
        carInfo.put("VEHICLE_COLOR", "2");
        carInfo.put("VEHICLE_TYPE", "40");
        messageBody.put("carInfo", carInfo);
        return messageBody;
    }

    private static Map<String, Object> build1801MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "桂K609F1");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1801");

        messageBody.put("result", 0);
        messageBody.put("serverIp", "127.0.0.1");
        messageBody.put("serverPort", "26080");
        return messageBody;
    }

    private static Map<String, Object> build1802MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "桂K609F1");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1802");

        messageBody.put("result", 0);
        return messageBody;
    }

    private static Map<String, Object> build1902MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "桂K609F1");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1902");

        messageBody.put("result", 0);

        String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date());
        List<Object> itemList = new ArrayList<Object>();
        int n = getRandom(2, 9);
        for (int i = 0; i < n; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("channelId", 1);

            item.put("startTime", today + "  0" + i + ":00:00");
            item.put("endTime", today + "  0" + (i + 1) + ":00:00");
            item.put("gpsAlarm", 0);
            item.put("aviAlarm", 0);
            item.put("avitemType", 0);
            item.put("streamType", 0);
            item.put("memType", 0);
            item.put("fileSize", 1024 * 1204 * 1024 * getRandom(2, 10));
            itemList.add(item);
        }

        return messageBody;
    }

    private static Map<String, Object> build1A01MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "桂K609F1");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1A01");

        messageBody.put("result", 0);
        messageBody.put("serverIp", "127.0.0.1");
        messageBody.put("serverPort", "26080");
        return messageBody;
    }

    private static Map<String, Object> build1A02MessageBody() {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "桂K609F1");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1A02");

        messageBody.put("result", 0);
        return messageBody;
    }

    public static Map<String, Object> build1202Message() {
        Map<String, Object> messageHeader = new HashMap<String, Object>();
        messageHeader.put("messageId", "1200");
        messageHeader.put("gnssCenterId", "9616320,9616321");
        messageHeader.put("protocol", "jt809_2011");
        messageHeader.put("protocolVersion", "1078");

        Map<String, Object> message = new HashMap<String, Object>();
        message.put(Constants.MAP_KEY_MESSAGE_HEADER, messageHeader);

        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("vehicleNo", "桂K609F1");
        messageBody.put("vehicleColor", 1);
        messageBody.put("dataType", "1202");

        messageBody.put("encrypt", 1);
        messageBody.put("time", new Date().getTime());
        messageBody.put("lng", 121.177961);
        messageBody.put("lat", 31.150808);
        messageBody.put("speed", getRandom(50, 120));
        messageBody.put("dvrSpeed", getRandom(50, 120));
        messageBody.put("mileage", getRandom(80, 180));
        messageBody.put("direction", getRandom(0, 360));
        messageBody.put("altitude", getRandom(50, 1000));
        messageBody.put("state", 3);
        messageBody.put("alarm", getRandom(2, 100) % 45 == 0 ? getRandom(0, Integer.MAX_VALUE) & (1 << getRandom(0, 31)) : 0);
        message.put(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY, messageBody);
        return message;
    }

    public static Map<String, Object> build1701Message() {
        Map<String, Object> messageHeader = new HashMap<String, Object>();
        messageHeader.put("messageId", "1700");
        messageHeader.put("gnssCenterId", "9616320,9616321");
        messageHeader.put("protocol", "jt809_2011");
        messageHeader.put("protocolVersion", "1078");

        Map<String, Object> message = new HashMap<String, Object>();
        message.put(Constants.MAP_KEY_MESSAGE_HEADER, messageHeader);

        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "1701");
        messageBody.put("platformId", "12345678");
        messageBody.put("authorizeCode1", getRandom(0, Integer.MAX_VALUE) + "");
        messageBody.put("authorizeCode2", getRandom(0, Integer.MAX_VALUE) + "");
        message.put(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY, messageBody);
        return message;
    }

    public static Map<String, Object> build1201Message() {
        Map<String, Object> messageHeader = new HashMap<String, Object>();
        messageHeader.put("messageId", "1200");
        messageHeader.put("gnssCenterId", "9616320,9616321");
        messageHeader.put("protocol", "jt809_2011");
        messageHeader.put("protocolVersion", "1078");

        Map<String, Object> messageBody = new HashMap<String, Object>();
        messageBody.put("dataType", "1201");
        messageBody.put("vehicleNo", "桂K609F1");
        messageBody.put("vehicleColor", 1);
        messageBody.put("platformId", "12345678");
        messageBody.put("producerId", "888");
        messageBody.put("terminalModelType", "4");
        messageBody.put("terminalId", "1234567");
        messageBody.put("terminalSimCode", "013109616300");

        Map<String, Object> message = new HashMap<String, Object>();
        message.put(Constants.MAP_KEY_MESSAGE_HEADER, messageHeader);
        message.put(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY, messageBody);
        return message;
    }

    private static int getRandom(int start, int end) {
        return (int) (start + Math.random() * end);
    }
}
