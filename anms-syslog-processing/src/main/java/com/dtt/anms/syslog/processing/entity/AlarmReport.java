package com.dtt.anms.syslog.processing.entity;

import lombok.Data;

@Data
public class AlarmReport {
    /**
     * EMS ID   syslog 1, 轮训告警 2, trap 告警 3
     */
    String emsId;
    /**
     * EMS 名称  syslogAlarm/statusAlarm/trapAlarm
     */
    String emsName;
    /**
     * 序列号
     */
    String serialId;
    /**
     * 设备ID（补充）
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
     * 设备别名（补充）
     */
    String deviceAlias;
    /**
     * 网络层ID（补充）
     */
    Long netLayerId;
    String netLayerName;
    /**
     * 厂家ID （补充）
     */
    Long factoryId;
    /**
     * 厂家名称（补充）
     */
    String factoryName;
    /**
     * 型号ID（补充）
     */
    Long modelId;
    /**
     * 型号名称（补充）
     */
    String modelName;
    /**
     * 地理位置ID（补充）
     */
    Long cityId;
    String cityName;
    /**
     * 区域ID（补充）
     */
    Long areaId;
    String areaName;
    /**
     * 局站ID（补充）
     */
    Long stationId;
    String stationName;
    /**
     * 机房ID（补充）
     */
    Long roomId;
    String roomName;
    /**
     * 告警名称
     */
    String alarmName;
    /**
     * 告警类型
     */
    Long alarmType;
    /**
     * 告警状态 1活动 2清除
     */
    Long alarmStatus;
    /**
     * 告警级别
     */
    Long severityId;
    /**
     * 告警发生时间
     */
    String happenTime;
    /**
     * 告警描述
     */
    String additionalInfo;
    /**
     * 告警原因
     */
    String probableCauseDesc;
    /**
     * 告警位置ID
     */
    String positionId;
    /**
     * 告警位置
     */
    String position;
    /**
     * 备注
     */
    String remark;
}
