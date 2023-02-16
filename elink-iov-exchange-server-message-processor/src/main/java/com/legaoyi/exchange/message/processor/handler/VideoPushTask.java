package com.legaoyi.exchange.message.processor.handler;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoPushTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(VideoPushTask.class);

    private String url = null;

    private String ip = "127.0.0.1";

    private int port = 6037;

    private CloseableHttpClient httpClient;

    private HttpContext context;

    public VideoPushTask(String url, String pushIp, int pushPort, CloseableHttpClient httpClient) {
        this.url = url;
        this.ip = pushIp;
        this.port = pushPort;
        this.httpClient = httpClient;
        this.context = new BasicHttpContext();
    }

    public void run() {
        BufferedInputStream in = null;
        Socket conn = null;
        OutputStream out = null;
        try {
            HttpGet httpGet = new HttpGet(this.url);
            CloseableHttpResponse response = httpClient.execute(httpGet, context);
            in = new BufferedInputStream(response.getEntity().getContent());
            byte[] buff = new byte[512];
            int bytesRead;
            conn = new Socket(ip, port);
            out = conn.getOutputStream();
            while ((bytesRead = in.read(buff, 0, buff.length)) != -1) {
                out.write(buff, 0, bytesRead);
                out.flush();
            }
        } catch (Exception e) {
            logger.error("*******push stream error,url={}", url, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {

            }
        }
    }
}
