package com.dtt.anms.syslog.processing.service;

import com.dtt.anms.syslog.processing.entity.SyslogRule;

import java.util.List;

public interface ISyslogRuleService {

    List<SyslogRule> getSyslogRule(Long factoryId);
}
