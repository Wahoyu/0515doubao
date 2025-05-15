package com.dtt.anms.syslog.processing.alarm;



import com.dtt.anms.syslog.processing.entity.AlarmReport;
import com.dtt.anms.syslog.processing.entity.Syslog;
import com.dtt.anms.syslog.processing.entity.SyslogRule;

import java.util.Map;

public interface IOtherAlarmService {
    AlarmReport handleAlarm(Syslog syslog, SyslogRule rule, Map<String,String> parameter);
    //Alarm handleAlarm2(Syslog syslog, SyslogRule rule, Map<String,String> parameter);
}
