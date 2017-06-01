package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.Deal;
import com.progresssoft.manishkr.model.DealSourceFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class DealDaoImpl implements DealDao{

    @PersistenceContext
    private EntityManager em;

    public Integer getMaxId(){
        try{
            Integer maxDealId = (Integer) em
                    .createQuery("SELECT COALESCE(MAX(id), 0) FROM Deal ")
                    .getSingleResult();
            return maxDealId;
        }catch(NoResultException ex){
            System.err.println("No results found for SELECT MAX(id) FROM Deal");
            return null;
        }
    }

    public void persist(Deal deal){
        em.persist(deal);
    }

    public void merge(Deal deal){
        em.merge(deal);
    }

}
