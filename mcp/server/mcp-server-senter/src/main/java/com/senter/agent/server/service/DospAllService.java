package com.senter.agent.server.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.senter.agent.server.utils.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DospAllService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${SERVER}")
    private String server;
    @Value("${SERVER_FIGURE}")
    private String serverFigure;

//    @Tool(description = "未上报数据设备查询", returnDirect = true)
//    public String getUnReportDevice(
//            @ToolParam(description = "连续天数,默认为1") Integer days,
//            @ToolParam(description = "地区,默认为空") String area,
//            @ToolParam(description = "系统,默认为空") String sys,
//            @ToolParam(description = "系统版本,默认为空") String sysVersion,
//            @ToolParam(description = "软件版本,默认为空") String softwareVersion,
//            @ToolParam(description = "型号,默认为空") String modelName,
//            @ToolParam(description = "起始时间,开始时间,从什么时间开始") String beginReportDate,
//            @ToolParam(description = "终止时间,结束时间,到什么时间结束") String endReportDate,
//            @ToolParam(description = "页数") Integer page,
//            @ToolParam(description = "行数，条数") Integer rows,
//            @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (StringUtils.isEmpty(beginReportDate)) {
//                beginReportDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(endReportDate)) {
//                endReportDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            if (page == null) {
//                page = 1;
//            }
//            if (rows == null) {
//                rows = 10;
//            }
//            JSONObject object = new JSONObject();
//            object.put("beginReportDate", beginReportDate);
//            object.put("endReportDate", endReportDate);
//            object.put("days", days);
//            object.put("page", page);
//            object.put("rows", rows);
//            object.put("area", area);
//            object.put("softwareVersion", softwareVersion);
//            object.put("modelName", modelName);
//            object.put("sys", sys);
//            object.put("sysVersion", sysVersion);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/deviceStatusLast/getUnReportDevice", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "设备异常列表为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("协议", row.get("agreement"));
//                ret.put("操作系统", row.get("sys"));
//                ret.put("软件版本", row.get("softwareVersion"));
//                ret.put("主卡ICCID", row.get("mainCard"));
//                ret.put("设备型号ID", row.get("model"));
//                ret.put("设备编号", row.get("id"));
//                ret.put("DK卡ICCID", row.get("dk"));
//                ret.put("系统版本", row.get("sysVersion"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "查询未上报设备失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "按条件统计异常状态分布,按时间段根据校验字段和比较值做大于、等于、小于、大于等于、小于等于等判断，然后根据分组字段分组，" +
//            "查询统计结果次数超过限制次数的数据", returnDirect = true)
//    public String staAlarm(
//            @ToolParam(description = "校验字段") String deviceStatusDimension,
//            @ToolParam(description = "判断条件,比较,较,大于,小于,等于") String compare,
//            @ToolParam(description = "起始时间,开始时间,从什么时间开始") String stm,
//            @ToolParam(description = "终止时间,结束时间,到什么时间结束") String etm,
//            @ToolParam(description = "比较值,判断值") String threshold,
//            @ToolParam(description = "分组字段,维度") String groupDimension,
//            @ToolParam(description = "次数,次") String thdCount,
//            @ToolParam(description = "是否按设备去重（1:去重）") String noRpdevice,
//            @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (StringUtils.isEmpty(stm)) {
//                stm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(stm)) {
//                etm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            JSONObject object = new JSONObject();
//            object.put("stm", stm);
//            object.put("etm", etm);
//            object.put("deviceStatusDimension", deviceStatusDimension);
//            object.put("compare", compare);
//            object.put("threshold", threshold);
//            object.put("groupDimension", groupDimension);
//            object.put("thdCount", thdCount);
//            object.put("noRpdevice", noRpdevice);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/devicestatus/staAlarm", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "设备异常列表为空";
//            }
//            JSONArray data = retJson.getJSONArray("data");
//            List<JSONObject> retList = new ArrayList<>();
//            data.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("数量", JSON.parseObject(row.toString()).getString("ct"));
//                ret.put("维度", JSON.parseObject(row.toString()).getString("dimension"));
//                retList.add(ret);
//            });
//            return JSON.toJSONString(retList);
//        } catch (Exception e) {
//            return "统计异常状态分布失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "获取时间段内某项指标大于或小于比较值的设备状态文件信息列表", returnDirect = true)
//    public String selStatusAlarmList(
//            @ToolParam(description = "分组字段,维度,校验字段,指标,不要翻译") String deviceStatusDimension,
//            @ToolParam(description = "判断条件,比较,较,大于,小于,等于,包含，不要翻译") String compare,
//            @ToolParam(description = "起始时间,开始时间,从什么时间开始") String stm,
//            @ToolParam(description = "终止时间,结束时间,到什么时间结束") String etm,
//            @ToolParam(description = "比较值,判断值,包含值，不要翻译") String threshold,
//            @ToolParam(description = "页数") Integer page,
//            @ToolParam(description = "行数,记录数") Integer rows,
//            @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (page == null) {
//                page = 1;
//            }
//            if (rows == null) {
//                rows = 10;
//            }
//            if (StringUtils.isEmpty(stm)) {
//                stm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(stm)) {
//                etm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            JSONObject object = new JSONObject();
//            object.put("stm", stm);
//            object.put("etm", etm);
//            object.put("deviceStatusDimension", deviceStatusDimension);
//            object.put("compare", compare);
//            object.put("threshold", threshold);
//            object.put("page", page);
//            object.put("rows", rows);
//
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/devicestatus/alarmList", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "设备异常列表为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("设备编号", row.get("deviceId"));
//                ret.put("meid", row.get("meid"));
//                ret.put("数据所属日期", row.get("dataDate"));
//                ret.put("上传时间", row.get("uploadTime"));
//                ret.put("地区", row.get("area"));
//                ret.put("设备型号", row.get("deviceModel"));
//                ret.put("软件版本", row.get("softwareVersion"));
//                ret.put("协议", row.get("agreement"));
//                ret.put("设备ip", row.get("deviceIp"));
//                ret.put("机芯版本号", row.get("movementVersion"));
//                ret.put("服务器地址及端口", row.get("serverAddressPort"));
//                ret.put("video服务器的ip和端口", row.get("videoIpPort"));
//                ret.put("主卡号", row.get("mainCard"));
//                ret.put("DK卡号", row.get("dkCard"));
//                ret.put("前端分析版本", row.get("analysisVersion"));
//                ret.put("拍照app版本", row.get("cameraAppVersion"));
//                ret.put("MIPI镜头数量", row.get("mipiLenseAmount"));
//                ret.put("MCU版本", row.get("mcuVersion"));
//                ret.put("操作系统", row.get("sys"));
//                ret.put("系统版本", row.get("sysVersion"));
//                ret.put("Modem版本", row.get("modemVersion"));
//                ret.put("Modem重启次数", row.get("modemRestartNumber"));
//                ret.put("网卡打开失败次数", row.get("networkCardErrorNumber"));
//                ret.put("APN类型", row.get("apnType"));
//                ret.put("网络类型", row.get("netType"));
//                ret.put("imei1", row.get("imei1"));
//                ret.put("imei2", row.get("imei2"));
//                ret.put("主卡号状态", Integer.parseInt(row.get("mainCardStatus") + "") == 5 ? "正常" : "不正常");
//                ret.put("DK卡号状态", Integer.parseInt(row.get("dkCardStatus") + "") == 5 ? "正常" : "不正常");
//                ret.put("当前数据卡", Integer.parseInt(row.get("dataCardStatus") + "") == 1 ? "dk卡激活" : "主卡激活");
//                ret.put("TF卡状态", Integer.parseInt(row.get("tfStatus") + "") == 1 ? "正常" : "不正常");
//                ret.put("cer证书是否存在", Integer.parseInt(row.get("certificateExist") + "") == 0 ? "不存在" : "存在");
//                ret.put("VPN版本号", row.get("vpnVersion"));
//                ret.put("切换到CMNET", row.get("cmnetStatus"));
//                ret.put("主卡当日流量数据使用量,单位B", row.get("mainCardDataUsage"));
//                ret.put("信号功率（接收信号的强度指示）,以开机时为准", row.get("receivedSignalStrengthIndicator"));
//                ret.put("dbm,有用信号功率", row.get("referenceSingalReceivingPower"));
//                ret.put("db, 有用信号质量", row.get("referenceSignalRecivedQuality"));
//                ret.put("与服务器断开次数", row.get("serverDisconnectNumber"));
//                ret.put("因为网络异常重启的次数", row.get("rebootCauseNetworkNumber"));
//                ret.put("watchDog失败次数", row.get("watchDogTimerNumber"));
//                ret.put("（多媒体卡使用率）DATA分区占用百分比", row.get("multiMediaCardUsageRate"));
//                ret.put("换机标识,1标识设备是换机设备", row.get("changeDevice"));
//                ret.put("电池电压", row.get("batteryVoltages"));
//                ret.put("电池电量", row.get("batteryLevels"));
//                ret.put("通道信息", row.get("channelInfo"));
//                ret.put("辅机信息", row.get("auxiliaryDeviceInfo"));
//                ret.put("设备与服务器校时时差", row.get("serverTime"));
//                ret.put("预置位总数", row.get("presettingTotal"));
//                ret.put("通道总数", row.get("channelTotal"));
//                ret.put("json文件版本号", row.get("jsonVersion"));
//                ret.put("擦写寿命", row.get("erasureLife"));
//                ret.put("anr次数", row.get("anr"));
//                ret.put("fc次数", row.get("fc"));
//                ret.put("ne次数", row.get("ne"));
//                ret.put("kernel次数", row.get("kernel"));
//                ret.put("电池标准电压", row.get("batteryStandardVoltage"));
//                ret.put("在线日志类型", row.get("logName"));
//                ret.put("正常关机/重启", row.get("rebootNormalNumber"));
//                ret.put("未知原因重启", row.get("rebootCauseUnknownNumber"));
//                ret.put("低电重启", row.get("rebootCauseLowPowerNumber"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//
//        } catch (Exception e) {
//            return "查询异常状态信息列表失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "按维度统计状态文件设备分布情况", returnDirect = true)
//    public String staStatusDimensionCount(
//            @ToolParam(description = "分组字段,维度") String deviceStatusDimension,
//            @ToolParam(description = "设备编号,默认为空") String deviceId,
//            @ToolParam(description = "起始时间,开始时间,从什么时间开始") String stm,
//            @ToolParam(description = "终止时间,结束时间,到什么时间结束") String etm,
//            @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (StringUtils.isEmpty(stm)) {
//                stm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(stm)) {
//                etm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            JSONObject object = new JSONObject();
//            object.put("stm", stm);
//            object.put("etm", etm);
//            object.put("deviceStatusDimension", deviceStatusDimension);
//            object.put("deviceId", deviceId);
//
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/devicestatus/staDimensionCount", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "设备异常列表为空";
//            }
//            JSONArray data = retJson.getJSONArray("data");
//            List<JSONObject> retList = new ArrayList<>();
//            data.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("数量", JSON.parseObject(row.toString()).getString("ct"));
//                ret.put("维度", JSON.parseObject(row.toString()).getString("dimension"));
//                retList.add(ret);
//            });
//            return JSON.toJSONString(retList);
//        } catch (Exception e) {
//            return "按维度统计上报设备数量失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "统计上报文件数量，统计一定时间段内上报的状态文件数量，", returnDirect = true)
//    public String staStatusFileCount(@ToolParam(description = "地区,默认为空") String area,
//                                     @ToolParam(description = "软件版本,默认为空") String sfVersion,
//                                     @ToolParam(description = "型号,默认为空") String modelName,
//                                     @ToolParam(description = "起始时间,开始时间,从什么时间开始") String stm,
//                                     @ToolParam(description = "终止时间,结束时间,到什么时间结束") String etm,
//                                     @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (StringUtils.isEmpty(stm)) {
//                stm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(stm)) {
//                etm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            JSONObject object = new JSONObject();
//            object.put("area", area);
//            object.put("sfVersion", sfVersion);
//            object.put("stm", stm);
//            object.put("etm", etm);
//            object.put("modelName", modelName);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/devicestatus/staCount", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "设备异常列表为空";
//            }
//            return retJson.getString("data");
//        } catch (Exception e) {
//            return "统计上报文件数量失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "查询设备异常信息列表", returnDirect = true)
//    public String list(@ToolParam(description = "地区") String area,
//                       @ToolParam(description = "软件版本,默认为空") String softwareVersion,
//                       @ToolParam(description = "型号,默认为空") String deviceModel,
//                       @ToolParam(description = "设备编号,默认为空") String deviceId,
//
//                       @ToolParam(description = "异常类型，告警类型,默认为空") String ruleName,
//                       @ToolParam(description = "协议,默认为空") String agreement,
//                       @ToolParam(description = "服务地址和端口,默认为空") String serverAddressPort,
//                       @ToolParam(description = "页数") Integer page,
//
//                       @ToolParam(description = "行数,记录数") Integer rows,
//                       @ToolParam(description = "起始时间,开始时间,从什么时间开始") String beginAlarmTime,
//                       @ToolParam(description = "终止时间,结束时间,到什么时间结束") String endAlarmTime,
//                       @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (StringUtils.isEmpty(beginAlarmTime)) {
//                beginAlarmTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(endAlarmTime)) {
//                endAlarmTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            if (page == null) {
//                page = 1;
//            }
//            if (rows == null) {
//                rows = 10;
//            }
//            JSONObject object = new JSONObject();
//            object.put("area", area);
//            object.put("softwareVersion", softwareVersion);
//            object.put("beginAlarmTime", beginAlarmTime);
//            object.put("endAlarmTime", endAlarmTime);
//
//            object.put("deviceId", deviceId);
//            object.put("ruleName", ruleName);
//            object.put("agreement", agreement);
//            object.put("serverAddressPort", serverAddressPort);
//
//            object.put("page", page);
//            object.put("rows", rows);
//            object.put("deviceModel", deviceModel);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/deviceStatusAlarm/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "设备异常列表为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("设备编号", row.get("deviceId"));
//                ret.put("异常名称", row.get("ruleName"));
//                ret.put("预警信息", row.get("alarmInfo"));
//                ret.put("预警时间", row.get("alarmTime"));
//                ret.put("处理状态", row.get("dealStatusName"));
//                ret.put("处理人名称", row.get("dealUserName"));
//                ret.put("处理时间", row.get("dealTime"));
//                ret.put("处理结果", row.get("dealResult"));
//                ret.put("地区", row.get("area"));
//                ret.put("设备型号", row.get("deviceModel"));
//                ret.put("软件版本", row.get("softwareVersion"));
//                ret.put("协议", row.get("agreement"));
//                ret.put("服务器地址及端口", row.get("serverAddressPort"));
//                ret.put("所属日期", row.get("dataDate"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "查询设备异常信息列表失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "按维度统计告警信息", returnDirect = true)
//    public String staAlarmDimensonCount(@ToolParam(description = "地区,默认为空") String area,
//                                        @ToolParam(description = "软件版本,默认为空") String sfVersion,
//                                        @ToolParam(description = "型号,默认为空") String modelName,
//                                        @ToolParam(description = "维度") String deviceDimension,
//                                        @ToolParam(description = "起始时间,开始时间,从什么时间开始") String stm,
//                                        @ToolParam(description = "终止时间,结束时间,到什么时间结束") String etm,
//                                        @ToolParam(description = "异常类型,告警类型") String ruleName,
//                                        @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (StringUtils.isEmpty(stm)) {
//                stm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(stm)) {
//                etm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            JSONObject object = new JSONObject();
//            object.put("area", area);
//            object.put("sfVersion", sfVersion);
//            object.put("stm", stm);
//            object.put("etm", etm);
//            object.put("modelName", modelName);
//            object.put("deviceDimension", deviceDimension);
//            object.put("ruleName", ruleName);
//
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/deviceStatusAlarm/staDimensonCount", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "按维度统计告警查询为空";
//            }
//            JSONArray data = retJson.getJSONArray("data");
//            List<JSONObject> retList = new ArrayList<>();
//            data.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("数量", JSON.parseObject(row.toString()).getString("ct"));
//                ret.put("维度", JSON.parseObject(row.toString()).getString("dimension"));
//                retList.add(ret);
//            });
//            return JSON.toJSONString(retList);
//        } catch (Exception e) {
//            return "按维度统计告警信息失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "统计设备软件版本信息", returnDirect = true)
//    public String staSfVersionList(@ToolParam(description = "地区,默认为空") String area,
//                                   @ToolParam(description = "软件版本,默认为空") String sfVersion,
//                                   @ToolParam(description = "所需记录数") Integer limit,
//                                   @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            if (limit == null) {
//                limit = 10;
//            }
//            if (limit > 100) {
//                return "请求记录数不能查过100";
//            }
//            JSONObject object = new JSONObject();
//            object.put("area", area);
//            object.put("sfVersion", sfVersion);
//            object.put("limit", limit);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/device/staSfVersionList", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "统计上报设备数量查询为空";
//            }
//            JSONArray data = retJson.getJSONArray("data");
//            List<JSONObject> retList = new ArrayList<>();
//            data.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("设备编号", JSON.parseObject(row.toString()).getString("id"));
//                ret.put("软件版本", JSON.parseObject(row.toString()).getString("software_version"));
//                retList.add(ret);
//            });
//            return JSON.toJSONString(retList);
//        } catch (Exception e) {
//            return "统计设备版本失败：" + e.getMessage();
//
//        }
//    }
//
//    @Tool(description = "根据维度统计设备分布信息", returnDirect = true)
//    public String staDeviceDimension(@ToolParam(description = "地区,默认为空") String area,
//                                     @ToolParam(description = "软件版本,默认为空") String sfVersion,
//                                     @ToolParam(description = "型号,默认为空") String modelName,
//                                     @ToolParam(description = "维度") String deviceDimension,
//                                     @ToolParam(description = "排序,序") String orderBy,
//                                     @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            JSONObject object = new JSONObject();
//            object.put("area", area);
//            object.put("sfVersion", sfVersion);
//            object.put("deviceDimension", deviceDimension);
//            object.put("orderBy", orderBy);
//            object.put("modelName", modelName);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/device/staDeviceDimension", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "统计上报设备数量查询为空";
//            }
//            JSONArray data = retJson.getJSONArray("data");
//            List<JSONObject> retList = new ArrayList<>();
//            data.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("数量", JSON.parseObject(row.toString()).getString("ct"));
//                ret.put("维度", JSON.parseObject(row.toString()).getString("dimension"));
//                retList.add(ret);
//            });
//            return JSON.toJSONString(retList);
//        } catch (Exception e) {
//            return "统计维度信息失败：" + e.getMessage();
//
//        }
//    }
//
//    @Tool(description = "查询设备状态文件上报情况", returnDirect = true)
//    public String staDeviceUpload(@ToolParam(description = "地区,默认为空") String area,
//                                  @ToolParam(description = "软件版本,默认为空") String sfVersion,
//                                  @ToolParam(description = "型号,默认为空") String modelName,
//                                  @ToolParam(description = "起始时间,开始时间,从什么时间开始") String stm,
//                                  @ToolParam(description = "终止时间,结束时间,到什么时间结束") String etm,
//                                  @ToolParam(description = "token") String authorization
//    ) {
//        try {
//
//            log.info("统计上报设备数量");
//            if (StringUtils.isEmpty(stm)) {
//                stm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
//            }
//            if (StringUtils.isEmpty(stm)) {
//                etm = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
//            }
//            JSONObject object = new JSONObject();
//            object.put("area", area);
//            object.put("sfVersion", sfVersion);
//            object.put("stm", stm);
//            object.put("etm", etm);
//            object.put("modelName", modelName);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/device/staDeviceUpload", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "统计上报设备数量查询为空";
//            }
//            return retJson.getString("data");
//        } catch (Exception e) {
//            return "统计上报设备数量失败：" + e.getMessage();
//
//        }
//
//    }
//
//    @Tool(description = "统计设备型号列表", returnDirect = true)
//    public String staModelList(@ToolParam(description = "地区,默认是空字符串") String area,
//                               @ToolParam(description = "软件版本，默认是空字符串") String sfVersion,
//                               @ToolParam(description = "所需记录数") Integer limit,
//                               @ToolParam(description = "token") String authorization
//    ) {
//        try {
//            log.info("统计设备型号列表");
//            if (limit == null) {
//                limit = 10;
//            }
//            if (limit > 1000) {
//                return "请求记录数不能查过1000";
//            }
//            JSONObject object = new JSONObject();
//            object.put("area", area);
//            object.put("sfVersion", sfVersion);
//            object.put("limit", limit);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-api/device/staModelList", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                log.info("请重新登陆");
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                log.info("型号列表查询为空");
//                return "型号列表查询为空";
//            }
//            JSONArray data = retJson.getJSONArray("data");
//            List<JSONObject> retList = new ArrayList<>();
//            data.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("设备编号", JSON.parseObject(row.toString()).getString("id"));
//                ret.put("型号", JSON.parseObject(row.toString()).getString("model_name"));
//                retList.add(ret);
//            });
//            return JSON.toJSONString(retList);
//        } catch (Exception e) {
//            return "统计设备型号列表失败：" + e.getMessage();
//        }
//    }
//
//
//    @Tool(description = "通过列表数据生成图表链接,以HTML的img标签格式返回给调用端使用", returnDirect = true)
//    public String getPhoto(@ToolParam(description = "列表数据,类型是list<map<k,v>>") String dataStr) {
//
//        try {
//            log.info("通过列表数据生成图表:" + dataStr);
//            if (StrUtil.isBlank(dataStr)) {
//                return "数据为空，请传入有效数据";
//            }
//            JSONObject object = new JSONObject();
//            object.put("data", JSONArray.parseArray(dataStr));
//            return HttpRequestUtil.postRequest(null, serverFigure, object, restTemplate);
//        } catch (Exception e) {
//            return "通过列表数据生成图表失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "发起设备型号、机型查询,通过型号名称、型号编码及分页信息查询," +
//            "分页返回型号编码、型号名称、型号ID、设备大类、设备类型,型号ID可作为其他接口的关键入参使用", returnDirect = true)
//    public String deviceModelList(
//            @ToolParam(description = "编码") String code,
//            @ToolParam(description = "型号名称，机型名称") String name,
//            @ToolParam(description = "页数，默认1") String page,
//            @ToolParam(description = "每页条数，默认20") String rows,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("code", code);
//            object.put("name", name);
//            if (null == page || null == rows) {
//                object.put("page", 1);
//                object.put("rows", 20);
//            } else {
//                object.put("page", page);
//                object.put("rows", rows);
//            }
//            log.info("deviceModelList:" + object.toJSONString());
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-base/model/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "机型信息查询为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("型号编码", row.get("code"));
//                ret.put("型号名称", row.get("name"));
//                ret.put("设备大类", row.get("majorCategory"));
//                ret.put("设备类型", row.get("minorCategory"));
//                ret.put("设备功能描述", row.get("functionDescription"));
//                ret.put("备注", row.get("remark"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "通过网关查询设备型号失败：" + e.getMessage();
//        }
//    }
//
    @Tool(description = "发起查询设备列表请求,通过设备编码、型号ID、操作系统、软件版本、是否存在异常以及分页信息查询," +
            "分页返回设备型号ID、设备编号、主卡ICCID、DK卡ICCID、操作系统、系统版本、接入状态（在线或离线）、远控连接状态（在线或离线）", returnDirect = true)
    public String deviceList(
            @ToolParam(description = "设备编码,设备SN号,设备唯一码") String deviceId,
            @ToolParam(description = "型号ID,机型ID") String modelId,
            @ToolParam(description = "操作系统") String sys,
            @ToolParam(description = "软件版本") String softwareVersion,
            @ToolParam(description = "是否存在异常,1不存在,2存在") String isAbnormal,
            @ToolParam(description = "页数,默认1") String page,
            @ToolParam(description = "每页条数,默认20") String rows,
            @ToolParam(description = "token") String authorization) {

        try {
            JSONObject object = new JSONObject();
            object.put("deviceId", deviceId);
            object.put("modelId", modelId);
            object.put("sys", sys);
            object.put("softwareVersion", softwareVersion);
            object.put("isAbnormal", isAbnormal);
            if (null == page || null == rows) {
                object.put("page", 1);
                object.put("rows", 20);
            } else {
                object.put("page", page);
                object.put("rows", rows);
            }
            log.info("deviceList:" + object.toJSONString());
            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-base/device/list", object, restTemplate);
            JSONObject retJson = JSONObject.parseObject(retStr);
            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
                return "请重新登陆！";
            } else if (null == retJson || null == retJson.getJSONObject("data")
                    || null == retJson.getJSONObject("data").get("rows")) {
                return "设备信息查询为空";
            }
            JSONObject data = retJson.getJSONObject("data");
            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
            List<JSONObject> retList = new ArrayList<>();
            retRows.forEach(row -> {
                JSONObject ret = new JSONObject();
                ret.put("协议", row.get("agreement"));
                ret.put("操作系统", row.get("sys"));
                ret.put("软件版本", row.get("softwareVersion"));
                ret.put("主卡ICCID", row.get("mainCard"));
                ret.put("设备型号ID", row.get("model"));
                ret.put("设备编号", row.get("id"));
                ret.put("DK卡ICCID", row.get("dk"));
                ret.put("系统版本", row.get("sysVersion"));
                ret.put("远控状态", Integer.parseInt(row.get("controlOnlineStates") + "") == 1 ? "在线" : "离线");
                ret.put("接入状态", Integer.parseInt(row.get("onlineStatus") + "") == 1 ? "在线" : "离线");
                retList.add(ret);
            });

            return JSONObject.toJSONString(retList);
        } catch (Exception e) {
            return "查询设备失败：" + e.getMessage();
        }
    }

//    @Tool(description = "发起设备状态查询,通过设备状态日期的开始结束时间、设备编码、是否存在短信关键字段查询," +
//            "返回日期范围内每天的设备相关信息,包括设备的基础信息、健康信息、软件版本、网络信息、通道辅机信息、异常信息、电量折线数据等信息", returnDirect = true)
//    public String deviceStatus(
//            @ToolParam(description = "设备状态日期,开始") String beginDateDate,
//            @ToolParam(description = "设备状态日期,结束") String endDateDate,
//            @ToolParam(description = "设备编码,设备SN号,设备唯一码") String deviceId,
//            @ToolParam(description = "是否存在短信,默认为空,1存在,0不存在") String hasSms,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("beginDateDate", beginDateDate);
//            object.put("endDateDate", endDateDate);
//            object.put("deviceId", deviceId);
//            if (StrUtil.isNotBlank(hasSms)) {
//                object.put("hasSms", hasSms);
//            } else {
//                object.put("hasSms", null);
//            }
//            log.info("deviceStatus:" + object.toJSONString());
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-dsm/devicestatus/queryList", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson) {
//                return "查询设备状态信息为空";
//            }
//            List<JSONObject> retRows = (List<JSONObject>) retJson.get("data");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("设备编号", row.get("deviceId"));
//                ret.put("meid", row.get("meid"));
//                ret.put("数据所属日期", row.get("dataDate"));
//                ret.put("上传时间", row.get("uploadTime"));
//                ret.put("地区", row.get("area"));
//                ret.put("设备型号", row.get("deviceModel"));
//                ret.put("软件版本", row.get("softwareVersion"));
//                ret.put("协议", row.get("agreement"));
//                ret.put("设备ip", row.get("deviceIp"));
//                ret.put("机芯版本号", row.get("movementVersion"));
//                ret.put("服务器地址及端口", row.get("serverAddressPort"));
//                ret.put("video服务器的ip和端口", row.get("videoIpPort"));
//                ret.put("主卡号", row.get("mainCard"));
//                ret.put("DK卡号", row.get("dkCard"));
//                ret.put("前端分析版本", row.get("analysisVersion"));
//                ret.put("拍照app版本", row.get("cameraAppVersion"));
//                ret.put("MIPI镜头数量", row.get("mipiLenseAmount"));
//                ret.put("MCU版本", row.get("mcuVersion"));
//                ret.put("操作系统", row.get("sys"));
//                ret.put("系统版本", row.get("sysVersion"));
//                ret.put("Modem版本", row.get("modemVersion"));
//                ret.put("Modem重启次数", row.get("modemRestartNumber"));
//                ret.put("网卡打开失败次数", row.get("networkCardErrorNumber"));
//                ret.put("APN类型", row.get("apnType"));
//                ret.put("网络类型", row.get("netType"));
//                ret.put("imei1", row.get("imei1"));
//                ret.put("imei2", row.get("imei2"));
//                ret.put("主卡号状态", Integer.parseInt(row.get("mainCardStatus") + "") == 5 ? "正常" : "不正常");
//                ret.put("DK卡号状态", Integer.parseInt(row.get("dkCardStatus") + "") == 5 ? "正常" : "不正常");
//                ret.put("当前数据卡", Integer.parseInt(row.get("dataCardStatus") + "") == 1 ? "dk卡激活" : "主卡激活");
//                ret.put("TF卡状态", Integer.parseInt(row.get("tfStatus") + "") == 1 ? "正常" : "不正常");
//                ret.put("cer证书是否存在", Integer.parseInt(row.get("certificateExist") + "") == 0 ? "不存在" : "存在");
//                ret.put("VPN版本号", row.get("vpnVersion"));
//                ret.put("切换到CMNET", row.get("cmnetStatus"));
//                ret.put("主卡当日流量数据使用量,单位B", row.get("mainCardDataUsage"));
//                ret.put("信号功率（接收信号的强度指示）,以开机时为准", row.get("receivedSignalStrengthIndicator"));
//                ret.put("dbm,有用信号功率", row.get("referenceSingalReceivingPower"));
//                ret.put("db, 有用信号质量", row.get("referenceSignalRecivedQuality"));
//                ret.put("与服务器断开次数", row.get("serverDisconnectNumber"));
//                ret.put("因为网络异常重启的次数", row.get("rebootCauseNetworkNumber"));
//                ret.put("watchDog失败次数", row.get("watchDogTimerNumber"));
//                ret.put("（多媒体卡使用率）DATA分区占用百分比", row.get("multiMediaCardUsageRate"));
//                ret.put("换机标识,1标识设备是换机设备", row.get("changeDevice"));
//                ret.put("电池电压", row.get("batteryVoltages"));
//                ret.put("电池电量", row.get("batteryLevels"));
//                ret.put("通道信息", row.get("channelInfo"));
//                ret.put("辅机信息", row.get("auxiliaryDeviceInfo"));
//                ret.put("设备与服务器校时时差", row.get("serverTime"));
//                ret.put("预置位总数", row.get("presettingTotal"));
//                ret.put("通道总数", row.get("channelTotal"));
//                ret.put("json文件版本号", row.get("jsonVersion"));
//                ret.put("擦写寿命", row.get("erasureLife"));
//                ret.put("anr次数", row.get("anr"));
//                ret.put("fc次数", row.get("fc"));
//                ret.put("ne次数", row.get("ne"));
//                ret.put("kernel次数", row.get("kernel"));
//                ret.put("电池标准电压", row.get("batteryStandardVoltage"));
//                ret.put("在线日志类型", row.get("logName"));
//                ret.put("正常关机/重启", row.get("rebootNormalNumber"));
//                ret.put("未知原因重启", row.get("rebootCauseUnknownNumber"));
//                ret.put("低电重启", row.get("rebootCauseLowPowerNumber"));
//                retList.add(ret);
//            });
//
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "查询设备状态信息失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "发起OTA固件版本信息查询,通过机型ID、固件创建时间、固件版本名称查询," +
//            "返回设备型号ID、固件版本、创建人、版本状态等信息", returnDirect = true)
//    public String otaFirmwareList(
//            @ToolParam(description = "机型ID") Integer modelId,
//            @ToolParam(description = "固件创建时间开始") String beginCreateDate,
//            @ToolParam(description = "固件创建时间截止") String endCreateDate,
//            @ToolParam(description = "固件版本名称") String version,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            if(null != modelId && 0 != modelId){
//                object.put("modelId", modelId);
//            }
//            object.put("beginCreateDate", beginCreateDate);
//            object.put("endCreateDate", endCreateDate);
//            object.put("version", version);
//            log.info("otaFirmwareList:" + object.toJSONString());
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-ota/ota/otaFirmware/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "OTA固件版本信息查询为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("机型ID", row.get("modelId"));
//                ret.put("固件版本", row.get("version"));
//                ret.put("检测周期", row.get("cycle"));
//                ret.put("发布时间", row.get("releaseTime"));
//                ret.put("发布说明", row.get("releaseDesc"));
//                ret.put("允许回滚降级", Integer.parseInt(row.get("rollbackFlag") + "") == 1 ? "是" : "否");
//                ret.put("允许整包升级", Integer.parseInt(row.get("fullPkgFlag") + "") == 1 ? "是" : "否");
//                ret.put("版本自升级", Integer.parseInt(row.get("selfUpgradingFlag") + "") == 1 ? "是" : "否");
//                ret.put("编译时间", row.get("buildTime"));
//                ret.put("版本状态", row.get("statusName"));
//                ret.put("备注", row.get("remark"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "获取OTA固件版本信息失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "发起OTA升级记录查询,通过检查升级时间、设备编码、机型ID、固件版本名称、上报类型编码、状态上报时间查询," +
//            "返回检查时间、上报时间、设备编码、上报版本、上班编码、升级开始版本、升级到版本等信息", returnDirect = true)
//    public String getDeviceUpdStatusStatistic(
//            @ToolParam(description = "检查升级开始时间") String beginCheckTime,
//            @ToolParam(description = "检查升级结束时间") String endCheckTime,
//            @ToolParam(description = "设备编码,设备SN号,设备唯一码") String deviceId,
//            @ToolParam(description = "设备机型ID,型号ID") String modelId,
//            @ToolParam(description = "固件版本名称") String firmwareName,
//            @ToolParam(description = "上报类型编码") String reportCode,
//            @ToolParam(description = "状态上报开始时间") String beginReportTIme,
//            @ToolParam(description = "状态上报结束时间") String endReportTime,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("beginCheckTime", beginCheckTime);
//            object.put("endCheckTime", endCheckTime);
//            object.put("deviceId", deviceId);
//            object.put("modelId", modelId);
//            object.put("firmwareName", firmwareName);
//            object.put("reportCode", reportCode);
//            object.put("beginReportTIme", beginReportTIme);
//            object.put("endReportTime", endReportTime);
//            log.info("getDeviceUpdStatusStatistic:" + object.toJSONString());
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-ota/ota/otaUpgradeTask/getDeviceUpdStatusStatistic", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "OTA升级记录查询为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("机型", row.get("modelName"));
//                ret.put("升级版本", row.get("endVer"));
//                ret.put("原版本", row.get("beginVer"));
//                ret.put("任务号", row.get("taskId"));
//                ret.put("升级类型", row.get("pkgType"));
//                ret.put("设备编号", row.get("deviceId"));
//                ret.put("检查时间", row.get("checkTime"));
//                ret.put("上报编码", row.get("reportCode"));
//                ret.put("上报版本", row.get("reportVersion"));
//                ret.put("上报时间", row.get("reportTime"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "OTA升级记录查询失败：" + e.getMessage();
//        }
//    }

//    @Tool(description = "发起短信发送记录查询,通过发送时间、发送内容（模糊查询）、发送号码（可以查询多个,逗号分隔）、发送人进行查询," +
//            "返回发送内容、发送情况状态、发送号码、发送时间、发送人及运营商等信息", returnDirect = true)
//    public String smsCommonSend(
//            @ToolParam(description = "发送开始时间") String beginSendTime,
//            @ToolParam(description = "发送截止时间") String endSendTime,
//            @ToolParam(description = "发送内容") String sendContent,
//            @ToolParam(description = "终端号码,发送号码") String terminalNums,
//            @ToolParam(description = "发送人") String userName,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("beginSendTime", beginSendTime);
//            object.put("endSendTime", endSendTime);
//            object.put("sendContent", sendContent);
//            object.put("terminalNums", terminalNums);
//            object.put("userName", userName);
//            log.info("smsCommonSend:" + object.toJSONString());
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-sms/sms/smsCommonSend/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "短信发送记录为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("发送时间", row.get("sendTime"));
//                ret.put("终端号码", row.get("terminalNum"));
//                ret.put("状态码", row.get("status"));
//                ret.put("发送情况", row.get("sendState"));
//                ret.put("发送内容", row.get("sendContent"));
//                ret.put("发送人", row.get("userName"));
//                ret.put("运营商", row.get("operator"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "短信发送记录失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "发起短信接收记录查询,通过接收时间、终端号码、关联账户进行查询," +
//            "返回接收时间、终端号码（接收号码）、接收内容、运营商等信息", returnDirect = true)
//    public String smsCommonReceive(
//            @ToolParam(description = "接收开始时间") String beginReceiveTime,
//            @ToolParam(description = "接收截止时间") String endReceiveTime,
//            @ToolParam(description = "终端号码") String terminalIds,
//            @ToolParam(description = "关联账户") String relationUser,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("beginReceiveTime", beginReceiveTime);
//            object.put("endReceiveTime", endReceiveTime);
//            object.put("terminalIds", terminalIds);
//            object.put("relationUser", relationUser);
//            log.info("smsCommonReceive:" + object.toJSONString());
////            return HttpRequestUtil.postRequest(authorization, server + "/dosp-sms/sms/smsCommonReceive/list", object, restTemplate);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-sms/sms/smsCommonReceive/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "短信接收记录为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("接收时间", row.get("receiveTime"));
//                ret.put("终端号码", row.get("terminalId"));
//                ret.put("接收内容", row.get("msgContent"));
//                ret.put("运营商", row.get("operator"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "短信接收记录失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "发起短信定时发送记录查询,通过定时日期、发送状态（0未发送,2发送中,1已发送）、发送人、终端号码、发送内容进行查询," +
//            "返回计划时间、终端号码、发送内容、发送状态（0未发送,2发送中,1已发送）、发送人等信息", returnDirect = true)
//    public String smsMessagePlan(
//            @ToolParam(description = "定时日期") String planTimeStr,
//            @ToolParam(description = "状态（默认为空，0未发送，2发送中，1已发送）") String status,
//            @ToolParam(description = "终端号码") String terminalNum,
//            @ToolParam(description = "发送内容") String sendContent,
//            @ToolParam(description = "发送人") String userName,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("planTimeStr", planTimeStr);
//            object.put("status", status);
//            object.put("terminalNum", terminalNum);
//            object.put("userName", userName);
//            log.info("smsMessagePlan:" + object.toJSONString());
////            return HttpRequestUtil.postRequest(authorization, server + "/dosp-sms/sms/smsMessagePlan/list", object, restTemplate);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-sms/sms/smsMessagePlan/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "定时发送记录为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("计划发送时间", row.get("planTime"));
//                ret.put("实际发送时间", row.get("realTime"));
//                ret.put("终端号码", row.get("terminalNum"));
//                ret.put("发送内容", row.get("sendContent"));
//                ret.put("发送人", row.get("userName"));
//                ret.put("发送状态", row.get("statusName"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "定时发送记录失败：" + e.getMessage();
//        }
//    }
//
//    @Tool(description = "发起设备异常信息查询", returnDirect = true)
//    public String deviceStatusAlarm(
//            @ToolParam(description = "预警时间开始") String beginAlarmTime,
//            @ToolParam(description = "预警时间结束") String endAlarmTime,
//            @ToolParam(description = "设备型号名称") String deviceModel,
//            @ToolParam(description = "设备编码,设备SN号,设备唯一码") String deviceId,
//            @ToolParam(description = "处理状态,1是已处理,0是未处理") String dealStatus,
//            @ToolParam(description = "软件版本") String softwareVersion,
//            @ToolParam(description = "协议") String agreement,
//            @ToolParam(description = "区域") String area,
//            @ToolParam(description = "异常id列表") String[] ruleIds,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("beginAlarmTime", beginAlarmTime);
//            object.put("endAlarmTime", endAlarmTime);
//            object.put("deviceModel", deviceModel);
//            object.put("deviceId", deviceId);
//            object.put("ruleIds", ruleIds);
//            object.put("softwareVersion", softwareVersion);
//            object.put("agreement", agreement);
//            object.put("area", area);
//            log.info("deviceStatusAlarm:" + object.toJSONString());
////            return HttpRequestUtil.postRequest(authorization, server + "/dosp-dsm/devicestatus/deviceStatusAlarm/list", object, restTemplate);
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-dsm/devicestatus/deviceStatusAlarm/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "设备异常列表为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("设备编号", row.get("deviceId"));
//                ret.put("异常名称", row.get("ruleName"));
//                ret.put("预警信息", row.get("alarmInfo"));
//                ret.put("预警时间", row.get("alarmTime"));
//                ret.put("处理状态", row.get("dealStatusName"));
//                ret.put("处理人名称", row.get("dealUserName"));
//                ret.put("处理时间", row.get("dealTime"));
//                ret.put("处理结果", row.get("dealResult"));
//                ret.put("地区", row.get("area"));
//                ret.put("设备型号", row.get("deviceModel"));
//                ret.put("软件版本", row.get("softwareVersion"));
//                ret.put("协议", row.get("agreement"));
//                ret.put("服务器地址及端口", row.get("serverAddressPort"));
//                ret.put("所属日期", row.get("dataDate"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "设备异常列表失败：" + e.getMessage();
//        }
//    }

//    @Tool(description = "设备统计信息查询", returnDirect = true)
//    public String staDevice(
//            @ToolParam(description = "统计类型,0设备总数、异常设备数等统计信息,1预警告警相关统计信息") String collectType,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("collectType", collectType);
//            return HttpRequestUtil.postRequest(authorization, server + "/dosp-dsm/report/reportDeviceStatus/staDevice", object, restTemplate);
//        } catch (Exception e) {
//            return "短信发送记录失败：" + e.getMessage();
//        }
//    }

    @Tool(description = "设备七日统计信息查询", returnDirect = true)
    public String staDeviceStatus(
            @ToolParam(description = "统计类型,0设备上报数、设备故障数、设备预警数统计信息,1异常统计信息") String collectType,
            @ToolParam(description = "token") String authorization) {

        try {
            JSONObject object = new JSONObject();
            object.put("collectType", collectType);
            return HttpRequestUtil.postRequest(authorization, server + "/dosp-dsm/report/reportDeviceStatus/staDeviceStatus", object, restTemplate);
        } catch (Exception e) {
            return "短信发送记录失败：" + e.getMessage();
        }
    }

//    @Tool(description = "发起远控操作记录查询,通过操作日期、设备编号、功能名称、操作账号发起查询," +
//            "返回设备编号、系统类型、功能编号、功能名称、操作人信息、操作时间等信息", returnDirect = true)
//    public String accessRecord(
//            @ToolParam(description = "操作时间开始") String beginCreateTime,
//            @ToolParam(description = "操作时间结束") String endCreateTime,
//            @ToolParam(description = "设备编码,设备SN号,设备唯一码") String deviceId,
//            @ToolParam(description = "功能名称") String funcName,
//            @ToolParam(description = "操作账号") String createName,
//            @ToolParam(description = "token") String authorization) {
//
//        try {
//            JSONObject object = new JSONObject();
//            object.put("beginCreateTime", beginCreateTime);
//            object.put("endCreateTime", endCreateTime);
//            object.put("deviceId", deviceId);
//            object.put("funcName", funcName);
//            object.put("createName", createName);
//            log.info("accessRecord" + object.toJSONString());
//
//            String retStr = HttpRequestUtil.postRequest(authorization, server + "/dosp-access/record/list", object, restTemplate);
//            JSONObject retJson = JSONObject.parseObject(retStr);
//            if (null != retJson && null != retJson.getString("msgKey") && "TOKEN_NOT_LOGIN".equals(retJson.getString("msgKey"))) {
//                return "请重新登陆！";
//            } else if (null == retJson || null == retJson.getJSONObject("data")
//                    || null == retJson.getJSONObject("data").get("rows")) {
//                return "远控操作记录查询为空";
//            }
//            JSONObject data = retJson.getJSONObject("data");
//            List<JSONObject> retRows = (List<JSONObject>) data.get("rows");
//            List<JSONObject> retList = new ArrayList<>();
//            retRows.forEach(row -> {
//                JSONObject ret = new JSONObject();
//                ret.put("设备编号", row.get("deviceId"));
//                ret.put("设备系统类型", row.get("deviceSys"));
//                ret.put("功能编号", row.get("funcCode"));
//                ret.put("功能名称", row.get("funcName"));
//                ret.put("操作信息", row.get("info"));
//                ret.put("操作人id", row.get("createId"));
//                ret.put("操作人名称", row.get("createName"));
//                ret.put("操作时间", row.get("createTime"));
//                ret.put("操作", row.get("func"));
//                ret.put("操作结果", row.get("result"));
//                retList.add(ret);
//            });
//            return JSONObject.toJSONString(retList);
//        } catch (Exception e) {
//            return "远控操作记录查询失败：" + e.getMessage();
//        }
//    }


}
