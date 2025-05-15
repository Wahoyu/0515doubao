package com.dtt.anms.syslog.processing.alarm;



import com.dtt.anms.syslog.processing.entity.SyslogRule;

import java.util.Map;

public interface IAlarmService {

    Long getAlarmState(SyslogRule rule, Map<String, String> parameter, int clearAlarmSleep);

    String getAlarmObjectName(Map<String, String> parameter);

    //long getAlarmLevel(List<AlarmLevel> alarmLevels, long value, long state);

    //Alarm getAlarm(Syslog syslog, SyslogRule rule, Map<String, String> parameter, long alarmLevel, long alarmState, String objectId, String objectName, String message);

    //Alarm getAlarmSyslog(Syslog syslog, SyslogRule rule, Map<String, String> parameter, long alarmLevel, long alarmState, String objectId, String objectName, String message);

}
