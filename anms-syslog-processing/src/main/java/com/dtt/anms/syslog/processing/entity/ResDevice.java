package com.dtt.anms.syslog.processing.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResDevice implements Serializable {
    /**
     * 设备ID
     */
    Long deviceId;
    /**
     * 设备IP
     */
    String deviceIp;
    /**
     * 设备名称
     */
    String deviceName;
    /**
     * 接口地址
     */
    String ipAddress;
    /**
     * 设备描述
     */
    String deviceAlias;
    /**
     * 厂家ID
     */
    Long factoryId;
    /**
     * 厂家名称
     */
    String factoryName;
    /**
     * 型号ID
     */
    Long modelId;
    /**
     * 型号名称
     */
    String modelName;
    /**
     * 地理位置ID
     */
    Long cityId;
    String cityName;
    /**
     * 区域ID
     */
    Long areaId;
    String areaName;
    /**
     * 局站ID
     */
    Long stationId;
    String stationName;
    /**
     * 机房ID
     */
    Long roomId;
    String roomName;
}
