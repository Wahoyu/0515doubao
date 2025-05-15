package com.dtt.anms.syslog.processing.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtils {

    /*
     * String 转换 Map
     */

    public static Map<String, String> getRequestStringToMap(String requestString) {

        Map<String, String> reMap = new HashMap<String, String>();

        if (requestString == null || requestString.trim().length() == 0) {
            return null;
        }

        String[] requestArray = requestString.trim().split(";");

        for (int i = 0; i < requestArray.length; i++) {

            if (requestArray[i] == null || requestArray[i].length() == 0) {
                continue;
            }


            String[] keyValueArray = requestArray[i].split("=");

            if (keyValueArray.length < 2) {
                continue;
            }

            reMap.put(keyValueArray[0], keyValueArray[1]);
        }

        return reMap;
    }

    /*
     * 判断对象是否为 null
     */

    public static boolean isNull(Object obj) {
        if (obj == null || ((String) obj).trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * String 转换 int 值
     */

    public static int StringToInt(String param) {
        if (isNumeric(param)) {
            return Integer.parseInt(param);
        }
        return -1;
    }

    /*
     * String 转换 long 值
     */

    public static long StringToLong(String param) {
        if (isNumeric(param)) {
            return Long.parseLong(param);
        }
        return -1;
    }

    public static long ObjectToLong(Object obj) {
        try {
            return Long.parseLong(obj.toString().trim());
        } catch (Exception e) {

        }
        return -1;
    }

    public static long ObjectToLong0(Object obj) {
        try {
            return Long.parseLong(obj.toString().trim());
        } catch (Exception e) {

        }
        return 0;
    }

    /*
     * 判断向量是否为 null
     */

    public static boolean isNull(Vector ver) {
        if (ver == null || ver.isEmpty() || ver.size() <= 0) {
            return true;
        }
        return false;
    }

    /*
     * 判断向量是否为 null
     */

    public static boolean isNull(Map map) {
        if (map == null || map.isEmpty() || map.size() <= 0) {
            return true;
        }
        return false;
    }

    /*
     * 判断字符串是否全是数字
     */

    public static boolean isNumeric(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /*
     * return null  与   return ""  是有区别的：
     *
     * null 不发送 清除与活动告警
     *
     * ""   发送清除告警
     */

    public static String isLimitAlarm(Map info, String limit) {

        String split_str = "";

        if (limit.indexOf(">") != -1) {
            split_str = ">";
        } else if (limit.indexOf(">=") != -1) {
            split_str = ">=";
        } else if (limit.indexOf("<") != -1) {
            split_str = "<";
        } else if (limit.indexOf("<=") != -1) {
            split_str = "<=";
        } else if (limit.indexOf("=") != -1) {
            split_str = "=";
        }

        if (split_str == null || split_str.trim().length() == 0) {
            return null;
        }

        String[] str = limit.split(split_str);

        if (str.length == 2 && ToolUtils.isNumeric(str[1])) {

            String method = str[0] == null ? "" : str[0].trim().toUpperCase();

            // 判断方法是否一致
            if (!info.containsKey(method)) {
                return null;
            }

            long value = ToolUtils.ObjectToLong(info.get(method));

            long number = Long.parseLong(str[1]);

            if (split_str.equals(">") && value > number) {
                return value + " " + split_str + " " + number;
            } else if (split_str.equals(">=") && value >= number) {
                return value + " " + split_str + " " + number;
            } else if (split_str.equals("<") && value < number) {
                return value + " " + split_str + " " + number;
            } else if (split_str.equals("<=") && value <= number) {
                return value + " " + split_str + " " + number;
            } else if (split_str.equals("=") && value == number) {
                return value + " " + split_str + " " + number;
            }
        } else {
            return null;
        }

        return "";
    }


}
