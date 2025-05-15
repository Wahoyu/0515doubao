package com.dtt.anms.data.batch.processing.dao;

import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;

import java.util.List;

public interface KafkaBatchClickDao {

    List<KafkaBatchClick> getKfBachCli(long id);

    List<KafkaBatchClick> getKfBachCli(int from);

    KafkaBatchClick getKfBachCliSingle(int id);

    int updateKfBatch(String sql);
}
