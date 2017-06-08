package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealCount;

import java.util.List;

public interface DealCountDao {

    DealCount findByCurrencyCode(String currency);
    List<DealCount> findAll();
    void persist(DealCount dealCount);

}
