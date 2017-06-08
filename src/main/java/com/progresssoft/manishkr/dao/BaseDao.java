package com.progresssoft.manishkr.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseDao {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
