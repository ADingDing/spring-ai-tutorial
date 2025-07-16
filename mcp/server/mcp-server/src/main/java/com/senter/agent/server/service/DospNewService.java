package com.senter.agent.server.service;

import com.alibaba.fastjson.JSONObject;
import com.senter.agent.server.utils.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 2806
 */
@Slf4j
@Service
public class DospNewService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${SERVER}")
    private String SERVER;

    @Tool(description = "通过条件(时间、型号、软件版本、排序方式)查询设备远控次数，返回设备编码及对应次数", returnDirect = true)
    public String recordStatistic(@ToolParam(description = "远控时间范围开始") String beginCreateTime,
                                  @ToolParam(description = "远控时间范围结束") String endCreateTime,
                                  @ToolParam(description = "型号、机型") String deviceModel,
                                  @ToolParam(description = "软件版本") String softwareVersion,
                                  @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                  @ToolParam(description = "返回条数") String retLimit,
                                  @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("beginCreateTime", beginCreateTime);
            object.put("endCreateTime", endCreateTime);
            object.put("deviceModel", deviceModel);
            object.put("softwareVersion", softwareVersion);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);

            log.info("recordStatistic:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/accessApi/recordStatistic", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "远控操作记录查询为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("设备编号", row.get("device_id"));
                ret.put("远控次数", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "通过条件(时间、型号、软件版本、排序方式)查询设备远控次数失败：" + e.getMessage();
        }
    }

   /*  @Tool(description = "查询不同统计类型的短信发送次数，统计类型：1发送内容；2发送用户；3发送卡号", returnDirect = true)
    public String sendCountByType(@ToolParam(description = "统计类型：1发送内容；2发送用户；3发送卡号") Integer countType,
                                  @ToolParam(description = "短信发送时间范围开始") String beginSendTime,
                                  @ToolParam(description = "短信发送时间范围结束") String endSendTime,
                                  @ToolParam(description = "短信发送内容") String sendContent,
                                  @ToolParam(description = "终端号码，发送号码，发送卡号，卡号") String terminalNum,
                                  @ToolParam(description = "发送状态") String status,
                                  @ToolParam(description = "运营商") String operator,
                                  @ToolParam(description = "发送人") String userName,
                                  @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                  @ToolParam(description = "返回条数") String retLimit,
                                  @ToolParam(description = "Authorization") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("countType", countType);
            object.put("beginSendTime", beginSendTime);
            object.put("endSendTime", endSendTime);
            object.put("sendContent", sendContent);
            object.put("terminalNum", terminalNum);
            object.put("status", status);
            object.put("operator", operator);
            object.put("userName", userName);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            log.info("sendCountByType:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/smsApi/sendCountByType", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "查询短信发送记录为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            if (null == countType) {
                return "查询短信发送记录为空";
            } else if (1 == countType) {
                retRows.forEach(row -> {
                    JSONObject ret = new JSONObject();
                    ret.put("发送内容", row.get("send_content"));
                    ret.put("发送次数", row.get("cou"));
                    retList.add(ret);
                });
            } else if (2 == countType) {
                retRows.forEach(row -> {
                    JSONObject ret = new JSONObject();
                    ret.put("发送人", row.get("user_name"));
                    ret.put("状态", row.get("statusStr"));
                    ret.put("发送次数", row.get("cou"));
                    retList.add(ret);
                });
            } else if (3 == countType) {
                retRows.forEach(row -> {
                    JSONObject ret = new JSONObject();
                    ret.put("终端号码", row.get("terminal_num"));
                    ret.put("发送次数", row.get("cou"));
                    retList.add(ret);
                });
            }
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "查询不同统计类型的短信发送次数失败：" + e.getMessage();
        }
    }

    @Tool(description = "ota升级信息统计,查询不同地区、型号、固件版本、设备编号的设备的OTA升级次数", returnDirect = true)
    public String otaStatistic(@ToolParam(description = "机型、型号，默认为空") String model,
                               @ToolParam(description = "ota升级时间范围开始") String beginCheckTime,
                               @ToolParam(description = "ota升级时间范围结束") String endCheckTime,
                               @ToolParam(description = "设备编号，默认为空") String deviceId,
                               @ToolParam(description = "上报状态，默认为空") String reportCode,
                               @ToolParam(description = "固件版本、版本号，默认为空") String reportSwVersion,
                               @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                               @ToolParam(description = "返回条数") String retLimit,
                               @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("model", model);
            object.put("beginCheckTime", beginCheckTime);
            object.put("endCheckTime", endCheckTime);
            object.put("deviceId", deviceId);
            object.put("reportCode", reportCode);
            object.put("reportSwVersion", reportSwVersion);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            log.info("otaStatistic:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/otaApi/otaStatistic", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "ota升级信息统计查询为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("地区", row.get("area"));
                ret.put("机型", row.get("model"));
                ret.put("版本号", row.get("report_sw_version"));
                ret.put("设备编号", row.get("device_id"));
                ret.put("升级次数", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "ota升级信息统计失败：" + e.getMessage();
        }
    }


    @Tool(description = "统计设备上图数量列表", returnDirect = true)
    public String devicestatusStatistic(@ToolParam(description = "机型、型号") String deviceModel,
                                        @ToolParam(description = "上图统计时间范围开始") String beginDataDate,
                                        @ToolParam(description = "上图统计时间范围结束") String endDataDate,
                                        @ToolParam(description = "设备编号") String deviceId,
                                        @ToolParam(description = "软件版本") String softwareVersion,
                                        @ToolParam(description = "地区") String area,
                                        @ToolParam(description = "排序，默认desc，升序asc，降序desc") String sortByStr,
                                        @ToolParam(description = "返回条数") String retLimit,
                                        @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("deviceModel", deviceModel);
            object.put("beginDataDate", beginDataDate);
            object.put("endDataDate", endDataDate);
            object.put("deviceId", deviceId);
            object.put("area", area);
            object.put("softwareVersion", softwareVersion);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            log.info("devicestatusStatistic:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/devicestatusStatistic", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "统计设备上图数量列表查询为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("设备编号", row.get("device_id"));
                ret.put("上图数量", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "统计设备上图数量列表失败：" + e.getMessage();
        }
    }

    @Tool(description = "异常出现次数统计", returnDirect = true)
    public String deviceAlarmStatistic(@ToolParam(description = "机型、型号") String deviceModel,
                                       @ToolParam(description = "统计时间范围开始") String beginAlarmTime,
                                       @ToolParam(description = "统计时间范围结束") String endAlarmTime,
                                       @ToolParam(description = "系统，安卓：Android、鸿蒙：Harmony") String sys,
                                       @ToolParam(description = "地区") String area,
                                       @ToolParam(description = "返回条数") String retLimit,
                                       @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                       @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("deviceModel", deviceModel);
            object.put("beginAlarmTime", beginAlarmTime);
            object.put("endAlarmTime", endAlarmTime);
            object.put("area", area);
            object.put("sys", sys);
            object.put("retLimit", retLimit);
            object.put("sortByStr", sortByStr);
            log.info("deviceAlarmStatistic:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/deviceAlarmStatistic", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "异常出现次数统计为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("异常名称", row.get("rule_name"));
                ret.put("异常数量", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "异常出现次数统计失败：" + e.getMessage();
        }
    }


    @Tool(description = "根据区域统计异常类型的设备数量", returnDirect = true)
    public String deviceAlarmByAreaStatistic(
            @ToolParam(description = "统计时间范围开始") String beginAlarmTime,
            @ToolParam(description = "统计时间范围结束") String endAlarmTime,
            @ToolParam(description = "省份、省、自治区、特别行政区") String province,
            @ToolParam(description = "市、自治州、盟") String city,
            @ToolParam(description = "市辖区、县级市、县、自治县、旗、自治旗、林区") String district,
            @ToolParam(description = "返回条数") String retLimit,
            @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
            @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("beginAlarmTime", beginAlarmTime);
            object.put("endAlarmTime", endAlarmTime);
            object.put("province", province);
            object.put("city", city);
            object.put("district", district);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            log.info("deviceAlarmByAreaStatistic:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/deviceAlarmByAreaStatistic", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "根据区域统计异常类型的设备数量统计为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                if (null != row.get("province")) {
                    ret.put("省", row.get("province"));
                }
                if (null != row.get("city")) {
                    ret.put("市", row.get("city"));
                }
                if (null != row.get("district")) {
                    ret.put("区", row.get("district"));
                }
                ret.put("异常名称", row.get("rule_name"));
                ret.put("异常数量", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "根据区域统计异常类型的设备数量失败：" + e.getMessage();
        }
    }

    @Tool(description = "账号异常处理次数统计", returnDirect = true)
    public String deviceAlarmDealStatistic(@ToolParam(description = "机型、型号") String deviceModel,
                                           @ToolParam(description = "统计时间范围开始") String beginAlarmTime,
                                           @ToolParam(description = "统计时间范围结束") String endAlarmTime,
                                           @ToolParam(description = "处理人") String dealUserName,
                                           @ToolParam(description = "异常名称") String ruleName,
                                           @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                           @ToolParam(description = "返回条数") String retLimit,
                                           @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("beginAlarmTime", beginAlarmTime);
            object.put("endAlarmTime", endAlarmTime);
            object.put("deviceModel", deviceModel);
            object.put("dealUserName", dealUserName);
            object.put("ruleName", ruleName);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/deviceAlarmDealStatistic", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "账号异常处理次数统计统计为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("处理人", row.get("deal_user_name"));
                ret.put("异常数量", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "账号异常处理次数统计数量失败：" + e.getMessage();
        }
    }


    @Tool(description = "每日新增异常设备数", returnDirect = true)
    public String newDeviceAlarmEachDay(@ToolParam(description = "机型、型号") String deviceModel,
                                        @ToolParam(description = "统计时间范围开始") String beginAlarmTime,
                                        @ToolParam(description = "统计时间范围结束") String endAlarmTime,
                                        @ToolParam(description = "异常名称") String ruleName,
                                        @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                        @ToolParam(description = "返回条数") String retLimit,
                                        @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("beginAlarmTime", beginAlarmTime);
            object.put("endAlarmTime", endAlarmTime);
            object.put("deviceModel", deviceModel);
            object.put("ruleName", ruleName);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/newDeviceAlarmEachDay", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "每日新增异常设备数统计为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("日期", row.get("dataDate"));
                ret.put("异常数量", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "每日新增异常设备数失败：" + e.getMessage();
        }
    }


    @Tool(description = "连续指定日期某些查询条件下的设备异常数据", returnDirect = true)
    public String continuareDeviceAlarm(@ToolParam(description = "机型、型号") String deviceModel,
                                        @ToolParam(description = "统计时间范围开始") String beginAlarmTime,
                                        @ToolParam(description = "统计时间范围结束") String endAlarmTime,
                                        @ToolParam(description = "处理状态：未处理0，已处理1") String dealStatus,
                                        @ToolParam(description = "连续出现天数") String rnStr,
                                        @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                        @ToolParam(description = "返回条数") String retLimit,
                                        @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("beginAlarmTime", beginAlarmTime);
            object.put("endAlarmTime", endAlarmTime);
            object.put("deviceModel", deviceModel);
            object.put("dealStatus", dealStatus);
            object.put("rn", rnStr);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            log.info("continuareDeviceAlarm:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/continuareDeviceAlarm", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "连续指定日期某些查询条件下的设备异常数据统计为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("设备编码", row.get("device_id"));
                ret.put("连续日期", row.get("alldate"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "连续指定日期某些查询条件下的设备异常数据失败：" + e.getMessage();
        }
    }

    @Tool(description = "统计异常设备及异常数，返回异常设备编码、设备型号、数量", returnDirect = true)
    public String countDeviceAlarm(@ToolParam(description = "机型、型号，默认为空") String deviceModel,
                                   @ToolParam(description = "统计时间范围开始") String beginAlarmTime,
                                   @ToolParam(description = "统计时间范围结束") String endAlarmTime,
                                   @ToolParam(description = "处理状态：未处理0，已处理1") String dealStatus,
                                   @ToolParam(description = "统计异常数量范围开始") String havingStart,
                                   @ToolParam(description = "统计异常数量范围结束") String havingEnd,
                                   @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                   @ToolParam(description = "返回条数") String retLimit,
                                   @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("beginAlarmTime", beginAlarmTime);
            object.put("endAlarmTime", endAlarmTime);
            object.put("dealStatus", dealStatus);
            object.put("deviceModel", deviceModel);
            object.put("havingStart", havingStart);
            object.put("havingEnd", havingEnd);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            log.info("countDeviceAlarm:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/countDeviceAlarm", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "统计异常设备及异常数统计为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("设备编码", row.get("device_id"));
                ret.put("设备型号", row.get("device_model"));
                ret.put("异常数量", row.get("cou"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "统计异常设备及异常数失败：" + e.getMessage();
        }
    }

    @Tool(description = "连续指定日期某些查询条件下的经历过OTA的异常设备", returnDirect = true)
    public String continuareDeviceAlarmWithOta(@ToolParam(description = "机型、型号") String deviceModel,
                                               @ToolParam(description = "统计时间范围开始") String beginAlarmTime,
                                               @ToolParam(description = "统计时间范围结束") String endAlarmTime,
                                               @ToolParam(description = "处理状态：未处理0，已处理1") String dealStatus,
                                               @ToolParam(description = "连续出现天数") String rnStr,
                                               @ToolParam(description = "排序，默认asc，升序asc，降序desc") String sortByStr,
                                               @ToolParam(description = "上报编码，默认为空") String reportCode,
                                               @ToolParam(description = "固件版本，默认为空") String firmwareName,
                                               @ToolParam(description = "返回条数") String retLimit,
                                               @ToolParam(description = "token") String authorization) {
        try {
            JSONObject object = new JSONObject();
            object.put("beginAlarmTime", beginAlarmTime);
            object.put("endAlarmTime", endAlarmTime);
            object.put("dealStatus", dealStatus);
            object.put("deviceModel", deviceModel);
            object.put("reportCode", reportCode);
            object.put("rn", rnStr);
            object.put("firmwareName", firmwareName);
            object.put("sortByStr", sortByStr);
            object.put("retLimit", retLimit);
            String retStr = HttpRequestUtil.postRequest(authorization, SERVER + "/dosp-api/devicestatusApi/continuareDeviceAlarmWithOta", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.get("data")) {
                return "连续指定日期某些查询条件下的经历过OTA的异常设备统计为空";
            }
            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("设备编码", row.get("device_id"));
                ret.put("连续日期", row.get("alldate"));
                retList.add(ret);
            });
            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "连续指定日期某些查询条件下的经历过OTA的异常设备失败：" + e.getMessage();
        }
    }*/


}
