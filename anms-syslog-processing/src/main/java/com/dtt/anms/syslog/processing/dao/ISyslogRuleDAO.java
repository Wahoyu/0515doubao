package com.dtt.anms.syslog.processing.dao;

import com.dtt.anms.syslog.processing.entity.SyslogRule;

import java.util.List;

public interface ISyslogRuleDAO {

    List<SyslogRule> getSysLogRule(Long factoryId);

}
