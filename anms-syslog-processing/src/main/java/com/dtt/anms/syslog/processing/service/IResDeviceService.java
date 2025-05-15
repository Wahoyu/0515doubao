package com.dtt.anms.syslog.processing.service;

import com.dtt.anms.syslog.processing.entity.ResDevice;

public interface IResDeviceService {

    ResDevice getDevice(String ip);
}
