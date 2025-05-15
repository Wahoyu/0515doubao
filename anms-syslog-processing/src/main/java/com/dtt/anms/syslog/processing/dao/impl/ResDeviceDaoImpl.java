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
@ConditionalOnProperty(value = "pess.alarm.accessControlStatus.enable",havingValue = "neimeng",matchIfMissing = true)
public class ResDeviceDaoImpl implements IResDeviceDao {

    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public ResDevice selectByIp(String ip) {

        String sql = "select d.device_id, d.device_name, d.device_ip, d.device_alias, d.net_layer_id, i.ip_address, m.model_id, m.model_name, f.factory_id, f.factory_name,g.city_id,g.city_name,g.area_id,g.area_name,g.station_id,g.station_name,g.geo_id as room_id,g.geo_name as room_name from res_device_ip i, res_device d,res_model m,res_factory f,res_geography g where i.device_id = d.device_id and d.model_id = m.model_id and m.factory_id = f.factory_id and d.geo_id = g.geo_id and i.ip_address ='" + ip + "'";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ResDevice.class));
        } catch (Exception e) {
            log.error("查询设备信息异常" + sql);
        }
        return null;
    }
}
