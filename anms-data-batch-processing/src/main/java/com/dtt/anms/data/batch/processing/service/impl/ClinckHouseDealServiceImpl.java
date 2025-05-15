package com.dtt.anms.data.batch.processing.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dtt.anms.data.batch.processing.dao.ClinckHouseDao;
import com.dtt.anms.data.batch.processing.dao.KafkaBatchClickDao;
import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import com.dtt.anms.data.batch.processing.service.ClinckHouseDealService;
import com.dtt.anms.data.batch.processing.utils.TableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: clinckhouse数据处理
 * @author: zhangbin
 * @create: 2024-05-14 18:31
 **/
@Slf4j
@Service
public class ClinckHouseDealServiceImpl implements ClinckHouseDealService {

    @Resource
    ClinckHouseDao clinckHouseDao;

    @Resource
    KafkaBatchClickDao kafkaBatchClickDao;


    @Override
    public void kDataClinckHouseDeal(ConsumerRecords<String, String> consumerRecords, KafkaBatchClick batchClick) {
        String tableName = batchClick.getTableName();
        String tableColum = batchClick.getTableColumn();
        String tableColumMatch = batchClick.getTableColumnMatch();
        //long lastOffset = 0;
        //将配置数据,字段名与kafka消息中传过来的名字匹配情况转化成map,利用map获取匹配数据
        List<String> listColmnMatch = StrUtil.splitTrim(tableColumMatch, ",");
        //最后根据配置,转成最终的jsonArray
        JSONArray jsonArray = new JSONArray();
        Map<String, String> dMapType = new HashMap<String, String>();
        //数据配置，来通过字段名，和json的名字匹配的map
        Map<String, String> dMap = new HashMap<String, String>();
        for (String sMap : listColmnMatch) {
            String[] ds = StrUtil.split(sMap, ":");
            dMap.put(ds[0], ds[1]);
        }
        //将数据配置的字段类型，通过表字段名，和字段类型进行匹配。
        for (String sMap : listColmnMatch) {
            String[] ds = StrUtil.split(sMap, ":");
            dMapType.put(ds[0], ds[2]);
        }

        //将数据库配置的表字段信息转成集合,来对应kakfa中的记录json的key值
        String errMsg = "";
        List<String> listColmn = StrUtil.splitTrim(tableColum, ",");
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            try {
                //log.info("获得的kafka原始数据:" + consumerRecord);
                String msg = consumerRecord.value();
                if(JSONUtil.isJson(msg)) {
                    errMsg = msg;
                    //lastOffset = consumerRecord.offset();
                    //将kafka数据转成jsonobj
                    JSONObject jsonObject = JSONObject.parseObject(msg);

                    JSONObject jsonObjectSfx = new JSONObject();
                    for (String key : listColmn) {
                        //将处理后,可以对应上的json数据插入到新的jsonobj中。
                        jsonObjectSfx.put(key, jsonObject.get(dMap.get(key)));
                    }
                    //将每一个处理好的jsonobj,放入最终提交个clinckhouse使用的jsonarray
                    //key是表字段
                    jsonArray.add(jsonObjectSfx);
                }else {
                    //lastOffset = consumerRecord.offset();
                    log.info("kafka中的数据不是json格式，跳过本条数据处理:"+msg);
                }
            } catch (Exception e) {
                log.error("consumerRecords-ex-1:{},json is:{}", e,errMsg);
            }
        }
        try {
            //如果没有数据，则不执行插入库
            if (jsonArray.size()>0) {
                StringBuffer insertSql = new StringBuffer();
                TableUtil tableUtil = new TableUtil();
                insertSql.append(tableUtil.insertTableClinckBefore(tableName, tableColum));
                insertSql.append(tableUtil.insertTableClinckAfter(tableColum, jsonArray, dMapType));
                //将数据批量插入到clinckhouse
                clinckHouseDao.insertData(insertSql.toString());
                //log.info("topic:{},插入语句为:{}",batchClick.getKafkaTopicName(),insertSql);
            }
            log.info("topic:{},插入的数据条数为:{}",batchClick.getKafkaTopicName(),jsonArray.size());
            //log.info("任务:{} 插入到 clinckhouse 数据量为:{} ", batchClick.getTaskBatchName(), lastOffset - batchClick.getTaskHisNum());
        }catch (Exception e){
            log.error("consumerRecords-ex-2:{},json is:{}", e,errMsg);
        }
    }
}
