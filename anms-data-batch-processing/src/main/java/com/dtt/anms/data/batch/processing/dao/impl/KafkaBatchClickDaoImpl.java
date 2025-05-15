package com.dtt.anms.data.batch.processing.dao.impl;

import com.dtt.anms.data.batch.processing.dao.KafkaBatchClickDao;
import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 配置文件处理dao
 * @author: zhangbin
 * @create: 2024-05-17 10:46
 **/
@Service
@Slf4j
public class KafkaBatchClickDaoImpl implements KafkaBatchClickDao {
    @Resource
    @Qualifier("serverJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public List<KafkaBatchClick> getKfBachCli(long id) {
        List<KafkaBatchClick> batchClicks = null;
        try {
            String sql = "select * from kafka_batch_click where task_batch_id = " + id;
            batchClicks = jdbcTemplate.query(sql, new BeanPropertyRowMapper(KafkaBatchClick.class));
        } catch (Exception e) {
            log.error("getKfBachCli-ex:", e);
        }
        return batchClicks;
    }

    @Override
    public List<KafkaBatchClick> getKfBachCli(int from) {
        List<KafkaBatchClick> batchClicks = null;
        try {
            // 使用参数化查询，避免SQL注入
            String sql = "SELECT * FROM kafka_batch_click WHERE status = 1 AND CAST(task_batch_id AS VARCHAR) LIKE CONCAT(?, '%')";

            // 使用 PreparedStatement 来传递参数
            batchClicks = jdbcTemplate.query(
                    sql,
                    new Object[]{String.valueOf(from)},  // 将 int 转换为 String
                    new BeanPropertyRowMapper<>(KafkaBatchClick.class)
            );
        } catch (Exception e) {
            log.error("getKfBachCli-ex:", e);
        }
        return batchClicks;
    }

    @Override
    public KafkaBatchClick getKfBachCliSingle(int id) {
        List<KafkaBatchClick> batchClicks = null;
        try {
            String sql = "select * from kafka_batch_click where status = 1  and task_batch_id  = "+id;
            batchClicks = jdbcTemplate.query(sql, new BeanPropertyRowMapper(KafkaBatchClick.class));
        } catch (Exception e) {
            log.error("getKfBachCli-ex:", e);
        }
        return batchClicks.get(0);
    }

    @Override
    public int updateKfBatch(String sql) {
        return jdbcTemplate.update(sql);
    }
}
