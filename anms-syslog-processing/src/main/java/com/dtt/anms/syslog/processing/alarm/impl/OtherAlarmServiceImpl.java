package com.dtt.anms.syslog.processing.alarm.impl;


import com.alibaba.fastjson2.JSON;
import com.dtt.anms.syslog.processing.alarm.IOtherAlarmService;
import com.dtt.anms.syslog.processing.config.Constant;
import com.dtt.anms.syslog.processing.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class OtherAlarmServiceImpl extends AlarmServiceImpl implements IOtherAlarmService {

    @Value(value = "${anms.syslog.sleep}")
    int clearAlarmSleep;

    @Override
    public AlarmReport handleAlarm(Syslog syslog, SyslogRule rule, Map<String, String> parameter) {

        long starttime = System.currentTimeMillis();

        StringBuffer info = new StringBuffer();
        info.append("\n--------------------------------------------");
        info.append("\nIP地址  ：" + syslog.getIp());
        info.append("\n日志内容：" + syslog.getText());
        info.append("\n设备ID：" + syslog.getDevice().getDeviceId());

        // 告警状态： 1清除告警  2活动告警
        long alarmState = super.getAlarmState(rule, parameter, clearAlarmSleep);

        // 告警对象名称
        String objectName = super.getAlarmObjectName(parameter);
        info.append("\n对象名称：" + objectName);

        // 告警内容
        String alarmMessage = rule.getAlarmTemplate();
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            alarmMessage = alarmMessage.replaceAll("\\$\\{" + entry.getKey() + "\\}", entry.getValue());
        }

        AlarmReport alarmReport = this.getAlarmSyslog(syslog, rule, parameter, alarmState, objectName, alarmMessage);
        info.append("\n告警："+JSON.toJSONString(alarmReport));
        info.append("\n耗时：" + (System.currentTimeMillis() - starttime) + " 毫秒");
        log.info(info.toString());
        return alarmReport;
    }

    public AlarmReport getAlarmSyslog(Syslog syslog, SyslogRule rule, Map<String, String> parameter, long alarmState, String objectName, String message){
        AlarmReport alarmReport = new AlarmReport();
        //网管ID
        alarmReport.setEmsId(Constant.emsId);
        //网管名称
        alarmReport.setEmsName(Constant.emsName);
        //告警序号
        alarmReport.setSerialId("");
        //设备ID
        alarmReport.setDeviceId(syslog.getDevice().getDeviceId());
        //设备IP
        alarmReport.setDeviceIp(syslog.getDevice().getDeviceIp());
        //设备名称
        alarmReport.setDeviceName(syslog.getDevice().getDeviceName());
        //告警名称
        alarmReport.setAlarmName(rule.getAlarmTitle());
        //告警类型
        alarmReport.setAlarmType(rule.getAlarmType());
        //告警状态 1活动 2清除
        alarmReport.setAlarmStatus(alarmState);
        //告警级别
        alarmReport.setSeverityId(rule.getAlarmLevel());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //告警发生时间
        alarmReport.setHappenTime(sdf.format(new Date()));
        //告警描述
        alarmReport.setAdditionalInfo(message);
        //告警原因
        alarmReport.setProbableCauseDesc(message);
        //告警位置ID
        alarmReport.setPositionId("");
        //告警位置
        alarmReport.setPosition(objectName);
        //备注
        alarmReport.setRemark(syslog.getText());

        return alarmReport;
    }
}
