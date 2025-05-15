package com.dtt.anms.syslog.collect.service;

public interface ISyslogHandlerService {

    String handle(String ip, String msg, int port, boolean test);
}
