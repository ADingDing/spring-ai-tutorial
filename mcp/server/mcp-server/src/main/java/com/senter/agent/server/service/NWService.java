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

        try {
            JSONObject object = new JSONObject();
            object.put("companyName", companyName);
            object.put("lineName", lineName);
            object.put("towerName", towerName);
            object.put("telephone", telephone);
            object.put("status", status);
            object.put("meid", meid);
            object.put("page", StringUtils.isNotBlank(page) ? Integer.parseInt(page) : 1);
            object.put("rows", StringUtils.isNotBlank(rows) ? Integer.parseInt(rows) : 20);
            object.put("order", order);
            log.info("获取设备列表传参:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest("", server + "/deviceCapture/getDeviceList", object, restTemplate, token);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("statusMessage") && "登录信息失效".equals(retJson.getString("statusMessage"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "设备列表为空";
            }
            return retJson.getJSONArray("data").toJSONString();
        } catch (Exception e) {
            return "获取设备列表失败：" + e.getMessage();
        }
    }

}
