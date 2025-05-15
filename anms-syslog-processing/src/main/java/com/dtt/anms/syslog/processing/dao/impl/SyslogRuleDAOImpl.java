package com.dtt.anms.syslog.processing.dao.impl;


import com.dtt.anms.syslog.processing.dao.ISyslogRuleDAO;
import com.dtt.anms.syslog.processing.entity.SyslogRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
@Slf4j
public class SyslogRuleDAOImpl implements ISyslogRuleDAO {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public List<SyslogRule> getSysLogRule(Long factoryId) {

        String sql = "select t.rule_id as rid, t.is_use as is_use,t.alarm_title as alarm_title,t.type as type, t.key_word as key_word, t.alarm_template as alarm_template,t.alarm_type as alarm_type,t.alarm_level as alarm_level,t.open_key as open_key,t.clear_key as clear_key,t.obj_rgx as obj_rgx,t.syslog_template as syslog_template,t.filter_keys as filter_keys,t.remark as remark,t.factory_id as factory_id from res_syslog_rule t where t.is_use = 1 and t.factory_id = " + factoryId + " order by t.key_word desc";
        try {
            log.debug(sql);
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper(SyslogRule.class));
        } catch (Exception e) {
            log.error("查询 syslog 规则异常", e);
        }
        return null;
    }


}
