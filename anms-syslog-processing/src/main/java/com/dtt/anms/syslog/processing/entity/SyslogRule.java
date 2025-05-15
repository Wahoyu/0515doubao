package com.dtt.anms.syslog.processing.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SyslogRule implements Serializable {
    private Long rid; // 自动增长ID
    private Long factoryId;
    private boolean isIse; // 1标识激活
    private String alarmTitle; // 名称
    private Long type; // 1open\clear一起，2open、3clear
    private String keyWord; // 匹配之前，先判断是否包含这个字符串，如果包含了再进行匹配，空就不判断
    private String syslogTemplate;// 匹配的字符串，现在简单只包含三个 :
    // {OBJ_NAME} {OBJ_STATE}
    // ，现在不需要{OBJ_IDX}，太复杂了
    // 其它都是原字符，这样简单
    private String alarmTemplate; // 告警模板
    private Long alarmType; // 告警类型
    private Long alarmLevel; // 告警级别
    private String openKey; // open状态的关键字
    private String clearKey; // clear状态的关键字

    private String objRjx = "(\\w|\\.)+?/*?\\d*?/*?\\d*?"; // 有默认值

    private String regex; // 根据以上字段自动计算

    private String filterKeys; // 过滤关键字 多个关键字用分号隔开 主要用于 {OBJ_NAME} {OBJ_STATE}

    private String remark;

    @Override
    public String toString() {
        return "SyslogRule{" +
                "rid=" + rid +
                ", factoryId=" + factoryId +
                ", isIse=" + isIse +
                ", alarmTitle='" + alarmTitle + '\'' +
                ", type=" + type +
                ", keyWord='" + keyWord + '\'' +
                ", syslogTemplate='" + syslogTemplate + '\'' +
                ", alarmTemplate='" + alarmTemplate + '\'' +
                ", alarmType=" + alarmType +
                ", alarmLevel=" + alarmLevel +
                ", openKey='" + openKey + '\'' +
                ", clearKey='" + clearKey + '\'' +
                ", objRjx='" + objRjx + '\'' +
                ", regex='" + regex + '\'' +
                ", filterKeys='" + filterKeys + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
