package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.controller.ImportDealsController;
import com.progresssoft.manishkr.model.DealCount;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class DealCountDaoImpl extends BaseDao implements DealCountDao {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DealCountDaoImpl.class);

    public DealCount findByCurrencyCode(String currency) {
        try{
            DealCount dealCount = (DealCount) getEm()
                    .createQuery("SELECT dc FROM DealCount dc WHERE dc.currency = :currency")
                    .setParameter("currency", currency)
                    .getSingleResult();

            return dealCount;
        }catch(NoResultException ex){
            logger.debug("Currency "+currency+" not found.");
            return null;
        }
    }

    @Override
    public List<DealCount> findAll() {
        try{
            List<DealCount> dealCount = (List<DealCount>) getEm()
                    .createQuery("SELECT dc FROM DealCount dc").getResultList();
            return dealCount;
        }catch(NoResultException ex){
            logger.debug("No currency counts found.");
            return null;
        }
    }

    public void persist(DealCount dealCount){
        logger.debug("Persisting "+dealCount.getCurrency()+" with value "+dealCount.getCount());
        getEm().persist(dealCount);
    }
}
