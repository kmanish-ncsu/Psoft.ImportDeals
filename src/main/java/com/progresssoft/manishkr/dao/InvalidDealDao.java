package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.InvalidDeal;

public interface InvalidDealDao
{

    public Integer getMaxId();

    public void persist(InvalidDeal invalidDeal);

    public void merge(InvalidDeal invalidDeal);
}
