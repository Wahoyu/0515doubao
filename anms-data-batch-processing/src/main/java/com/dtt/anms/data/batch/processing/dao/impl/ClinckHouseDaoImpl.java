package com.dtt.anms.data.batch.processing.dao.impl;

import com.dtt.anms.data.batch.processing.dao.ClinckHouseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: clinckhouse的数据库操作
 * @author: zhangbin
 * @create: 2024-05-17 10:57
 **/
@Slf4j
@Service
public class ClinckHouseDaoImpl implements ClinckHouseDao {
    @Resource
    @Qualifier("slaveJdbcTemplate")
    JdbcTemplate jdbcTemplateClinck;

    @Override
    public int insertData(String sql) {
        return jdbcTemplateClinck.update(sql);
    }
}
