package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealCount;
import com.progresssoft.manishkr.model.DealSourceFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class DealCountDaoImpl implements DealCountDao{

    @PersistenceContext
    private EntityManager em;

    public DealCount findByCurrencyCode(String currency) {
        try{
            DealCount dealCount = (DealCount) em
                    .createQuery("SELECT dc FROM DealCount dc WHERE dc.currency = :currency")
                    .setParameter("currency", currency)
                    .getSingleResult();

            return dealCount;
        }catch(NoResultException ex){
            System.err.println("Currency "+currency+" not found.");
            return null;
        }
    }

    public void persist(DealCount dealCount){
        em.persist(dealCount);
    }
}
