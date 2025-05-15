package com.dtt.anms.syslog.processing.service.impl;

import com.alibaba.fastjson2.JSON;
import com.dtt.anms.syslog.processing.alarm.IOtherAlarmService;
import com.dtt.anms.syslog.processing.entity.AlarmReport;
import com.dtt.anms.syslog.processing.entity.Syslog;
import com.dtt.anms.syslog.processing.entity.SyslogRule;
import com.dtt.anms.syslog.processing.service.IAsyncService;
import com.dtt.anms.syslog.processing.service.SyslogMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Slf4j
public class AsyncServiceImpl implements IAsyncService {

    @Resource
    IOtherAlarmService otherAlarmService;

    @Resource
    SyslogMsgService syslogMsgService;

    @Value(value = "${anms.syslog.kafka.producerrep.alarmTopics}")
    String alarmTopics;

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(Syslog syslog, SyslogRule rule, Map<String, String> parameter) {
        this.execute(syslog, rule, parameter);
    }

    @Override
    public void execute(Syslog syslog, SyslogRule rule, Map<String, String> parameter) {
        try {
            long starttime = System.currentTimeMillis();

            // 数据过滤
            String[] filters = rule.getFilterKeys().split(";");
            for (int i = 0; i < filters.length; i++) {

                if (filters[i] == null) {
                    continue;
                }

                for (Map.Entry<String, String> entry : parameter.entrySet()) {
                    if (entry.getValue() == null || entry.getKey().toUpperCase().indexOf("OTHER") >= 0) {
                        continue;
                    }

                    if (entry.getValue().toUpperCase().indexOf(filters[i].toUpperCase()) >= 0) {
                        log.info("-------------------日志过滤---------------\n关键字：" + filters[i]);
                        return;
                    }
                }
            }

            parameter.put("DEV_IDX", "" + syslog.getDevice().getDeviceId());
            parameter.put("DEV_IP", syslog.getDevice().getDeviceIp());
            parameter.put("DEV_NAME", syslog.getDevice().getDeviceName());



            AlarmReport alarmReport = otherAlarmService.handleAlarm(syslog, rule, parameter);

            //未产生告警
            if (alarmReport == null) {
                return;
            }
            this.write(JSON.toJSONString(alarmReport));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void write(String str) {
        //告警信息发送到kafka
        syslogMsgService.send(alarmTopics, str);
    }
}
