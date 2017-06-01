package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealSourceFile;

public interface DealSourceFileDao {
    public DealSourceFile findByfileName(String fileName);
    public void persist(DealSourceFile dsf);
    public void merge(DealSourceFile dsf);
    }
