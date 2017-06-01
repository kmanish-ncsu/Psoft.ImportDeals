package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealSourceFile;
import com.progresssoft.manishkr.model.InvalidDeal;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class InvalidDealDaoImpl implements InvalidDealDao{

    @PersistenceContext
    private EntityManager em;

    public Integer getMaxId(){
        try{
            Integer maxInvalidDealId = (Integer) em
                    .createQuery("SELECT COALESCE(MAX(id),0)  FROM InvalidDeal ")
                    .getSingleResult();

            return maxInvalidDealId;
        }catch(NoResultException ex){
            System.err.println("No results found for SELECT MAX(id) FROM InvalidDeal");
            return null;
        }
    }
    public void persist(InvalidDeal invalidDeal){
        em.persist(invalidDeal);
    }

    public void merge(InvalidDeal invalidDeal){
        em.merge(invalidDeal);
    }
}
