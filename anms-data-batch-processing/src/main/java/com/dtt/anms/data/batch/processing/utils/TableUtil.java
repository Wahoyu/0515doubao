package com.dtt.anms.data.batch.processing.utils;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhangbind
 * Date: 2024-05-18
 * Time: 9:54
 */
public class TableUtil {

    /**
     * @param tablename
     * @param column
     * @return 通过配置参数，组装clinckhouse的插入语句的前段
     */
    public String insertTableClinckBefore(String tablename, String column) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("insert into ");
        stringBuffer.append(tablename + " ");
        stringBuffer.append("( " + column + " )");
        stringBuffer.append(" values ");
        return stringBuffer.toString();
    }

    /**
     * @param tcolmn
     * @param jsonArray
     * @return 处理clinckhouse的插入语句，后半部分的插入语句组合
     */
    public String insertTableClinckAfter(String tcolmn, JSONArray jsonArray, Map<String, String> dMapType) {
        StringBuffer stringBuffer = new StringBuffer();

        List<String> listColmn = StrUtil.splitTrim(tcolmn, ",");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            stringBuffer.append("(");
            for (int j = 0; j < listColmn.size(); j++) {
                String tbKey = listColmn.get(j);
                if (StrUtil.equalsIgnoreCase("N", dMapType.get(tbKey))) {
                    stringBuffer.append(jsonObject.getLong(listColmn.get(j)));
                } else if (StrUtil.equalsIgnoreCase("F", dMapType.get(tbKey))) {
                    stringBuffer.append(jsonObject.getFloat(listColmn.get(j)));
                } else {
                    stringBuffer.append("'" + jsonObject.getString(listColmn.get(j)) + "'");
                }
                if (j != listColmn.size() - 1) {
                    stringBuffer.append(",");
                }
            }
            stringBuffer.append(")");
            if (i != jsonArray.size() - 1) {
                stringBuffer.append(",");
            }
        }

        return stringBuffer.toString();
    }

}
