package com.senter.agent.server.service;

import com.alibaba.fastjson.JSONObject;
import com.senter.agent.server.utils.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NWService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${SERVER}")
    private String server;
    @Value("${SERVER_FIGURE}")
    private String serverFigure;

    @Tool(description = "获取设备信息，可输入单位名称、线路名称、杆塔名称、SIM卡、状态、设备编码、是否离线、返回条数，排序字段，正序倒序排序" +
            "，返回设备信息", returnDirect = true)
    public String getDeviceList(
            @ToolParam(required = false, description = "单位名称") String companyName,
            @ToolParam(required = false, description = "线路名称") String lineName,
            @ToolParam(required = false, description = "杆塔名称") String towerName,
            @ToolParam(required = false, description = "SIM卡,") String telephone,
            @ToolParam(required = false, description = "状态") String status,
            @ToolParam(required = false, description = "设备编码") String meid,
            @ToolParam(description = "排序，默认asc，升序asc，降序desc") String order,
            @ToolParam(description = "页码，默认1") String page,
            @ToolParam(description = "条数，返回条数，默认20") String rows,
            @ToolParam(description = "token") String token) {
        JSONObject object = new JSONObject();
        object.put("companyName", "senter");
        object.put("meid", meid);
        object.put("status", "OK");
        String jsonString = JSONObject.toJSONString(object);
        log.info("只能是:{}", jsonString);

        return jsonString;
    }

}
