package com.dtt.anms.syslog.processing.dao.impl;

import com.dtt.anms.syslog.processing.dao.IResDeviceDao;
import com.dtt.anms.syslog.processing.entity.ResDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
@Slf4j
@ConditionalOnProperty(value = "anms.syslog.province",havingValue = "ningxia")
public class ResDeviceDaoNXImpl implements IResDeviceDao {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public ResDevice selectByIp(String ip) {
        String sql = "SELECT d.device_id,\n" +
                "       d.device_name,\n" +
                "       d.device_ip,\n" +
                "       d.device_alias,\n" +
                "       d.net_layer_id,\n" +
                "       i.olt_ip as ip_address,\n" +
                "       m.model_id,\n" +
                "       m.model_name,\n" +
                "       f.factory_id,\n" +
                "       f.factory_name,\n" +
                "       g.city_id,\n" +
                "       g.city_name,\n" +
                "       g.area_id,\n" +
                "       g.area_name,\n" +
                "       g.station_id,\n" +
                "       g.station_name,\n" +
                "       g.geo_id AS room_id,\n" +
                "       g.geo_name AS room_name\n" +
                "FROM res_olt i,\n" +
                "     res_device d,\n" +
                "     res_model m,\n" +
                "     res_factory f,\n" +
                "     res_geography g\n" +
                "WHERE i.olt_id = d.device_id\n" +
                "  AND d.model_id = m.model_id\n" +
                "  AND m.factory_id = f.factory_id\n" +
                "  AND d.geo_id = g.geo_id\n" +
                "  AND d.device_ip = '" + ip + "'"
//                "  AND i.olt_ip = '" + ip + "'"
                ;
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ResDevice.class));
        } catch (Exception e) {
            log.error("查询设备信息异常" + sql);
        }
        return null;
    }
}
