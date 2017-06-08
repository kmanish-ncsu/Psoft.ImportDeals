package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealSourceFile;

public interface DealSourceFileDao {
    DealSourceFile findByfileName(String fileName);
    void persist(DealSourceFile dsf);
    }
