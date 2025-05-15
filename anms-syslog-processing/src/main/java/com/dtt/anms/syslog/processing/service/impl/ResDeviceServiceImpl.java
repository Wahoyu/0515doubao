package com.dtt.anms.syslog.processing.service.impl;

import com.dtt.anms.syslog.processing.dao.IResDeviceDao;
import com.dtt.anms.syslog.processing.entity.ResDevice;
import com.dtt.anms.syslog.processing.service.IResDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ResDeviceServiceImpl implements IResDeviceService {

    @Resource
    IResDeviceDao resDeviceDao;

    @Override
    @Cacheable(value = "deviceCache", key = "#ip")
    public ResDevice getDevice(String ip) {
        return resDeviceDao.selectByIp(ip);
    }
}
