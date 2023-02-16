package com.legaoyi.exchange.message.processor.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("videoPushManager")
public class VideoPushManager {

    private static final Logger logger = LoggerFactory.getLogger(VideoPushManager.class);

    private ExecutorService taskExecutor = Executors.newFixedThreadPool(100);

    private CloseableHttpClient httpClient;

    private String pushIp = "127.0.0.1";// 这里可改成配置

    private int pushPort = 60381;// 这里可改成配置

    public VideoPushManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }

    public void push(String streamUrl) throws Exception {
        logger.info("******stream url={}", streamUrl);
        VideoPushTask pushTask = new VideoPushTask(streamUrl, pushIp, pushPort, httpClient);
        taskExecutor.execute(pushTask);
    }
}
