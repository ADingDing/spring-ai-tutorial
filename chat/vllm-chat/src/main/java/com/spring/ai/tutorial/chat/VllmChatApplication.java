package com.spring.ai.tutorial.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yingzi
 * @date 2025/5/21 10:37
 */
@SpringBootApplication
public class VllmChatApplication {


    public static void main(String[] args) {
//        System.setProperty("http.proxyHost", "localhost");
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyHost", "192.168.7.215");
//        System.setProperty("http.proxyPort", "8888");

/*        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");*/

        SpringApplication.run(VllmChatApplication.class, args);
    }
}
