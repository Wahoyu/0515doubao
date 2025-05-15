package com.dtt.anms.syslog.processing.alarm.impl;

import com.dtt.anms.syslog.processing.alarm.IAlarmService;
import com.dtt.anms.syslog.processing.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class AlarmServiceImpl implements IAlarmService {


    private String obj_name = "OBJ_NAME";
    private String obj_state = "OBJ_STATE";
    private String obj_idx = "OBJ_IDX";

    private String dev_idx = "${DEV_IDX}";
    private String dev_ip = "${DEV_IP}";
    private String dev_name = "${DEV_NAME}";
    private String obj_other = "${OBJ_OTHER}";
    private String obj_light = "${OBJ_LIGHT}";

    @Override
    public Long getAlarmState(SyslogRule rule, Map<String, String> parameter, int clearAlarmSleep) {

        //告警状态
        String objState = parameter.containsKey(obj_state) ? parameter.get(obj_state) : "";

        /**
         * 0 未识别 1 活动告警  2 清除告警
         */
        long clear = 0;

        try {

            /**
             * 告警状态判断逻辑
             *
             * 1 清除与活动并存， 当状态内容中包含clearKey关键字则清除告警，反之活动告警
             * 2 活动告警
             * 3 清除告警
             */

            if (rule.getType() == 1) {
                if (objState.toUpperCase().indexOf(rule.getClearKey().toUpperCase()) >= 0) {
                    clear = 2;
                } else if (objState.toUpperCase().indexOf(rule.getOpenKey().toUpperCase()) >= 0) {
                    clear = 1;
                }
            } else if (rule.getType() == 2) {
                clear = 1;
            } else if (rule.getType() == 3) {
                clear = 2;
            }


            /*if (rule.getType() == 2) {//活动告警
                return clear;
            }

            // 判断是否是清除告警
            if (rule.getType() == 1 && objState.toUpperCase().indexOf(rule.getClearKey().toUpperCase()) >= 0) {
                clear = 1;
            } else if (rule.getType() == 3) {
                clear = 1;
            } else if (!("".equalsIgnoreCase(objState)) && objState.toUpperCase().indexOf(rule.getClearKey().toUpperCase()) >= 0) {
                clear = 1;
            }

            if (clear == 1) {
                Thread.sleep(clearAlarmSleep);
            }*/
        } catch (Exception e) {
            log.error("AlarmState error:", e);
        }

        return clear;
    }

    @Override
    public String getAlarmObjectName(Map<String, String> parameter) {
        //告警对象名称
        return parameter.containsKey(obj_name) ? parameter.get(obj_name) : "";
    }
}
