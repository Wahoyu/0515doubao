package com.dtt.anms.syslog.processing.entity;

/**
 * @description: 给kafka消息返回响应
 * @author: zhangbin
 * @create: 2024-04-29 20:41
 **/
public class TaskStatus {

    final public static int START = 1;//开始
    final public static int BEGINING = 2;//处理中
    final public static int EXCETION = 3;//异常
    final public static int FINLISH = 4;//完成
    /**
     * 任务ID
     */
    private long taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 记录ID
     */
    private long recordId;
    /**
     * 状态
     */
    private int status;
    /**
     * 时间
     */
    private String time;
    /**
     * 备注
     */
    private String remark;
    /**
     * 服务器IP
     */
    private String server;
    /**
     * 程序路径
     */
    private String path;

    public long getTaskId() {
        return taskId;
    }
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public long getRecordId() {
        return recordId;
    }
    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getServer() {
        return server;
    }
    public void setServer(String server) {
        this.server = server;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setStatusInfo(int status, String message) {
        this.setStatus(status);
        this.setRemark(message);
    }


}

