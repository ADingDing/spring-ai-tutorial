package com.senter.agent.server.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class HttpRequestUtil {


    /**
     * post请求
     *
     * @param authorization
     * @param url
     * @param object
     * @return
     */
    public static String postRequest(String authorization, String url, JSONObject object, RestTemplate restTemplate) {
        log.info("token:" + authorization);
        log.info("url:" + url);
        log.info("param:" + JSON.toJSONString(object));

        HttpHeaders headers = new HttpHeaders();
        if (StrUtil.isNotBlank(authorization)) {
            headers.set("Authorization", authorization);
        }
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        // 创建请求体，这里以JSON字符串为例
        HttpEntity<String> request = new HttpEntity<String>(object.toJSONString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        log.info(body);
        return body;
    }

    public static String postRequest(String authorization, String url, JSONObject object, RestTemplate restTemplate, String cookie) {
        log.info("url:{}，param:{}，token：{}", url, JSON.toJSONString(object), authorization);

        HttpHeaders headers = new HttpHeaders();
        if (StrUtil.isNotBlank(authorization)) {
            headers.set("Authorization", authorization);
        }
        if (StrUtil.isNotBlank(cookie)) {
            headers.set("Cookie", cookie);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建请求体，这里以JSON字符串为例
        HttpEntity<String> request = new HttpEntity<String>(object.toJSONString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = responseEntity.getBody();
        log.info(body);
        return body;
    }

}
