package com.dtt.anms.syslog.processing.service;



import com.dtt.anms.syslog.processing.entity.Syslog;
import com.dtt.anms.syslog.processing.entity.SyslogRule;

import java.util.Map;

public interface IAsyncService {
    /**
     * 执行异步任务
     */
    void executeAsync(Syslog syslog, SyslogRule rule, Map<String, String> parameter);

    void execute(Syslog syslog, SyslogRule rule, Map<String, String> parameter);
}
