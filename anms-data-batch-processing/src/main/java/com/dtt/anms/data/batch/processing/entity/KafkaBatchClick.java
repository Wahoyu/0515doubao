package com.dtt.anms.data.batch.processing.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 任务配置实体类
 * @author: zhangbin
 * @create: 2024-05-14 17:44
 **/
@Data
public class KafkaBatchClick implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private Long taskBatchId;

    /**
     * 任务名称
     */
    private String taskBatchName;

    /**
     * 任务执行时间周期，单位是秒，配置成0，不参与处理
     */
    private Integer taskRangeTime;

    /**
     * 任务执行条数，表示当多少条执行一次，配置成0，不参与处理
     */
    private Integer taskRangeNum;

    /**
     * 本任务上次执行完成的时间
     */
    private Date taskHisTime;

    /**
     * 本任务上次执行的offset偏移量
     */
    private Integer taskHisNum;

    /**
     * kafka的连接信息
     */
    private String kafkaBootstrapServers;

    /**
     * kafka的组id
     */
    private String kafkaGroupId;

    /**
     * kafka会话名称
     */
    private String kafkaTopicName;

    /**
     * 要插入的表名称
     */
    private String tableName;

    /**
     * 要插入表的主键，后续如果有更新，删除再用
     */
    private String tableKeys;

    /**
     * 要插入的表字段信息，逗号分割
     */
    private String tableColumn;

    /**
     * 要插入的表字段与类型匹配信息，逗号分割
     */
    private String tableColumnMatch;

    /**
     * 状态 1启用 2禁用
     */
    private Integer status;

    /**
     * 状态 1启用 2禁用
     */
    private Integer runStatus;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改用户
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    public KafkaBatchClick() {
    }

}
