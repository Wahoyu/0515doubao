package com.dtt.anms.syslog.processing.dao;

import com.dtt.anms.syslog.processing.entity.ResDevice;

public interface IResDeviceDao {
    ResDevice selectByIp(String ip);
}
