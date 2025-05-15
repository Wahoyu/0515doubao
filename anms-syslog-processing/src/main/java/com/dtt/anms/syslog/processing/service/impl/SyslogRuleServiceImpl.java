package com.dtt.anms.syslog.processing.service.impl;

import com.dtt.anms.syslog.processing.dao.ISyslogRuleDAO;
import com.dtt.anms.syslog.processing.entity.SyslogRule;
import com.dtt.anms.syslog.processing.service.ISyslogRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SyslogRuleServiceImpl implements ISyslogRuleService {

    @Resource
    ISyslogRuleDAO syslogRuleDAO;

    @Override
    @Cacheable(value = "syslogRuleCache", key = "#factoryId" , unless = "#result == null")
    public List<SyslogRule> getSyslogRule(Long factoryId) {
        return syslogRuleDAO.getSysLogRule(factoryId);
    }
}
