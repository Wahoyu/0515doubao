package com.dtt.anms.syslog.processing.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Syslog implements Serializable {
    private long id;
    private Date time;             // 时间
    private String ip;               // 发送的IP地址
    private int facility;         // 分类
    private int severity;         // 级别
    private String text;             // 内容
    private int type;
    private String year;
    private String month;
    private String day;
    private String hour;

    private ResDevice device;
}
