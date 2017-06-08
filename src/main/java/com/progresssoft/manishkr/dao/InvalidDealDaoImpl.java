package com.progresssoft.manishkr.dao;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class InvalidDealDaoImpl extends BaseDao implements InvalidDealDao {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(InvalidDealDaoImpl.class);

    public Integer getMaxId(){
        try{
            Integer maxInvalidDealId = (Integer) getEm()
                    .createQuery("SELECT COALESCE(MAX(id),0)  FROM InvalidDeal ")
                    .getSingleResult();
            logger.debug("Found MAX(id) for InvalidDeal "+maxInvalidDealId);
            return maxInvalidDealId;
        }catch(NoResultException ex){
            logger.debug("No results found for SELECT MAX(id) FROM InvalidDeal");
            return null;
        }
    }
}
