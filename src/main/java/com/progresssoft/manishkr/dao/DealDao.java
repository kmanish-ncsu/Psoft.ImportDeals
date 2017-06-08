package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.Deal;

public interface DealDao {

    public Integer getMaxId();

    public void persist(Deal deal);

    public void merge(Deal deal);

}
