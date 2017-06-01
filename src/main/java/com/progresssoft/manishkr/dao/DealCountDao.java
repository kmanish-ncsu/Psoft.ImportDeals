package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealCount;

public interface DealCountDao {

    public DealCount findByCurrencyCode(String currency);

    public void persist(DealCount dealCount);

}
