package com.dtt.anms.syslog.processing.service;

import com.alibaba.fastjson2.JSONObject;

public interface ISyslogHandlerService {

    String handle(String msg, boolean test);

    void finishTaskData (String topic, JSONObject jsonObject, int Status);
}
