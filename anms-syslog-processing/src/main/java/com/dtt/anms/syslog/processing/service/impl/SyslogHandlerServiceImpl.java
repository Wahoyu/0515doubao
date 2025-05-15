package com.dtt.anms.syslog.processing.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dtt.anms.syslog.processing.entity.ResDevice;
import com.dtt.anms.syslog.processing.entity.Syslog;
import com.dtt.anms.syslog.processing.entity.SyslogRule;
import com.dtt.anms.syslog.processing.entity.TaskStatus;
import com.dtt.anms.syslog.processing.service.*;
import com.dtt.anms.syslog.processing.utils.NumUtil;
import com.dtt.anms.syslog.processing.utils.SystemUtil;
import com.dtt.anms.syslog.processing.utils.diff_match_patch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zhangbin
 * @create: 2024-04-15 20:37
 **/
@Service
@Slf4j
public class SyslogHandlerServiceImpl implements ISyslogHandlerService {

    @Resource
    ISyslogRuleService syslogRuleService;

    @Resource
    IAsyncService asyncService;

    @Resource
    SyslogMsgService syslogMsgService;

    @Value(value = "${anms.syslog.kafka.producer.topics}")
    String finishTopics;

    @Value(value = "${anms.syslog.kafka.producerrep.alarmTopics}")
    String alarmTopics;

    @Value(value = "${anms.syslog.kafka.producerrep.clinckTopics}")
    String clinckTopics;


    @Resource
    IResDeviceService resDeviceService;

    @Override
    @Async("asyncSyslogExecutor")
    public String handle(String msg, boolean test) {

        String[] syslogMessage = msg.split("-_--_-");
        String ip = syslogMessage[0];
        String text = syslogMessage[1];

        text = text.replaceAll("'", "’");

        //finishTaskData(finishTopics, jsonObject, 1);
        //日志打印处理
        StringBuffer readInfo = new StringBuffer();
        readInfo.append("\n----------------------------收到消息----------------------------");
        readInfo.append("\nIP地址  ：" + ip);
        readInfo.append("\n日志内容：" + msg);

        readInfo.append("\n收到消息时间：" + DateUtil.now());

        //先从缓存中拿设备，没有，查库，然后放入缓存。
        ResDevice device = resDeviceService.getDevice(ip);

        log.info("device:" + JSON.toJSONString(device));

        if (ObjectUtil.isNull(device) || ObjectUtil.isEmpty(device)) {
            readInfo.append("\n是否过滤：是");
            readInfo.append("\n原因：" + ip + "不存在");
            readInfo.append("\n----------------------------------------------------------------");
            log.info(readInfo.toString());
            return readInfo.toString();
        }

        //日志解析
        int type = 0;
        int idx1 = text.indexOf('<');
        if (idx1 >= 0) {
            int idx2 = text.indexOf('>', idx1);
            if (idx2 > 0) {
                type = NumUtil.str2int(text.substring(idx1 + 1, idx2));
            }
        }
        log.info("real type is :" + type);

        while (text.indexOf("  ") >= 0) {
            text = text.replaceAll("  ", " ");
        }


        //日志信息
        Syslog syslog = new Syslog();
        syslog.setIp(ip);
        syslog.setId(device.getDeviceId());
        syslog.setText(text);
        syslog.setTime(DateUtil.parse(DateUtil.now(), "yyyy-MM-dd HH:mm"));
        syslog.setType(type);
        String year = DateUtil.format(new Date(), "YYYY");
        String month = DateUtil.format(new Date(), "MM");
        String day = DateUtil.format(new Date(), "dd");
        String hour = DateUtil.format(new Date(), "HH");
        syslog.setYear(year);
        syslog.setMonth(month);
        syslog.setDay(day);
        syslog.setHour(hour);
        //设备信息
        syslog.setDevice(device);
        readInfo.append("\n厂家ID：" + device.getFactoryId());
        readInfo.append("\n类型ID：" + device.getModelId());
        readInfo.append("\n类型名称：" + device.getModelName());
        readInfo.append("\n设备ID：" + device.getDeviceId());
        readInfo.append("\n设备名称：" + device.getDeviceName());
        readInfo.append("\n设备IP：" + device.getDeviceIp() + "(" + device.getIpAddress() + ")");
        readInfo.append("\n设备时间：" + DateUtil.now());

        //首先需要调用创建表，然后再插入数据
//        syslogService.createTable();
        //现在不是一个设备一个表，直接将数据入库，即可。
        //下面的逻辑可能需要改，看最终入库到哪张表。
//        syslogService.saveSyslogInfo(syslog);
        //readInfo.append("\n入库时间：" + DateUtil.now());
        //由于用批处理程序入库clinkckhouse，所以建表和插入数据不需要。
        //将此部分数据写入到kafka中
        saveDataToKfClink(syslog);

        Long factoryId = syslog.getDevice().getFactoryId();
        List<SyslogRule> syslogRuleList = syslogRuleService.getSyslogRule(factoryId);


        //匹配厂家规则
        SyslogRule rule = null;
        if (syslogRuleList != null && syslogRuleList.size() > 0) {
            readInfo.append("\n厂家规则数量：" + syslogRuleList.size());
            for (SyslogRule syslogRule : syslogRuleList) {
                if (msg.contains(syslogRule.getKeyWord())) {
                    readInfo.append("\nRuleId：" + syslogRule.getRid());
                    readInfo.append("\nKeyWord：" + syslogRule.getKeyWord());
                    readInfo.append("\nAlarmTitle：" + syslogRule.getAlarmTitle());
                    readInfo.append("\nType：" + syslogRule.getType());
                    readInfo.append("\nOpenKey：" + syslogRule.getOpenKey());
                    readInfo.append("\nClearKey：" + syslogRule.getClearKey());
                    readInfo.append("\nSyslogTemplate：" + syslogRule.getSyslogTemplate());
                    readInfo.append("\nAlarmTemplate：" + syslogRule.getAlarmTemplate());
                    readInfo.append("\nAlarmLevel：" + syslogRule.getAlarmLevel());
                    readInfo.append("\nAlarmType：" + syslogRule.getAlarmType());
                    readInfo.append("\nFilterKeys：" + syslogRule.getFilterKeys());

                    rule = syslogRule;
                    break;
                }
            }
        } else {
            readInfo.append("\n厂家规则数量：0");
        }

        readInfo.append("\n规则匹配时间：" + DateUtil.now());

        //发送的报文
        //表示规则匹配成功
        if (rule != null) {
            readInfo.append("syslogRule:" + rule.toString());
            //finishTaskData(finishTopics, new JSONObject(), 2);
            Map<String, String> parameter = diff_match_patch.getKeyValue(msg, rule.getSyslogTemplate());
            readInfo.append("\n解析结果：" + parameter);

            if (test) {
                readInfo.append("\n----------------------------------------------------------------");
                asyncService.execute(syslog, rule, parameter);
            } else {
                asyncService.executeAsync(syslog, rule, parameter);
            }
        } else {
            readInfo.append("syslogRule: 无匹配规则");
        }

        readInfo.append("\n----------------------------------------------------------------");
        log.info(readInfo.toString());
        //finishTaskData(finishTopics, sendData, 4);
        //syslogMsgService.send(proTopics, sendData);
        return null;
    }

    @Override
    public void finishTaskData(String topic, JSONObject jsonObject, int Status) {
        //syslog状态反馈
        TaskStatus taskStatus = new TaskStatus();
        //taskStatus.setRecordId(jsonObject.getLong("recordId"));
        //taskStatus.setTaskId(jsonObject.getLong("taskId"));
        //taskStatus.setTaskName(jsonObject.getString("taskName"));
        taskStatus.setTime(DateUtil.now());
        taskStatus.setPath(SystemUtil.getAppPath());
        taskStatus.setServer(SystemUtil.getServerIp());
        syslogMsgService.send(topic, taskStatus.toString());
    }

    public void saveDataToKfClink(Syslog syslog) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", syslog.getDevice().getDeviceId());
            jsonObject.put("device_name", syslog.getDevice().getDeviceName());
            jsonObject.put("device_ip", syslog.getIp());
            jsonObject.put("city_id", syslog.getDevice().getCityId());
            jsonObject.put("city_name", syslog.getDevice().getCityName());
            jsonObject.put("area_id", syslog.getDevice().getAreaId());
            jsonObject.put("area_name", syslog.getDevice().getAreaName());
            jsonObject.put("station_id", syslog.getDevice().getStationId());
            jsonObject.put("station_name", syslog.getDevice().getStationName());
            jsonObject.put("room_id", syslog.getDevice().getRoomId());
            jsonObject.put("room_name", syslog.getDevice().getRoomName());
            jsonObject.put("factory_id", syslog.getDevice().getFactoryId());
            jsonObject.put("factory_name", syslog.getDevice().getFactoryName());
            jsonObject.put("model_id", syslog.getDevice().getModelId());
            jsonObject.put("model_name", syslog.getDevice().getModelName());
            jsonObject.put("log_type", syslog.getType());
            jsonObject.put("log_time", syslog.getTime());
            jsonObject.put("log_desc", syslog.getText());
            jsonObject.put("collect_time", DateUtil.now());
            jsonObject.put("collect_year", DateUtil.format(new Date(), "YYYY"));
            jsonObject.put("collect_month", DateUtil.format(new Date(), "MM"));
            jsonObject.put("collect_day", DateUtil.format(new Date(), "dd"));
            jsonObject.put("collect_hour", DateUtil.format(new Date(), "HH"));
            //将数据写入到kafka收数据的topic中，通过统一处理程序，读取数据，入库。
            syslogMsgService.send(clinckTopics, jsonObject.toJSONString());
        } catch (Exception e) {
            log.error("saveDataToKfClink-ex:", e);
        }
    }
}
