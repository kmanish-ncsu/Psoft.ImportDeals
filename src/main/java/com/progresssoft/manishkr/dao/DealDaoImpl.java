package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.Deal;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class DealDaoImpl extends BaseDao implements DealDao {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DealDaoImpl.class);

    public Integer getMaxId(){
        try{
            Integer maxDealId = (Integer) getEm()
                    .createQuery("SELECT COALESCE(MAX(id), 0) FROM Deal ")
                    .getSingleResult();
            return maxDealId;
        }catch(NoResultException ex){
            logger.debug("No results found for SELECT MAX(id) FROM Deal");
            return null;
        }
    }

    public void persist(Deal deal){
        logger.debug("Persisting Deal "+deal.toString());
        getEm().persist(deal);
    }

    public void merge(Deal deal){
        logger.debug("Merging Deal "+deal.toString());
        getEm().merge(deal);
    }
}
