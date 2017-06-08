package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealSourceFile;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class DealSourceFileDaoImpl extends BaseDao implements DealSourceFileDao {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DealSourceFileDaoImpl.class);

    public DealSourceFile findByfileName(String fileName) {
        try{
            DealSourceFile dsf = (DealSourceFile) getEm()
                    .createQuery("SELECT dsf FROM DealSourceFile dsf WHERE dsf.sourceFile = :fileName")
                    .setParameter("fileName", fileName)
                    .getSingleResult();
            logger.debug("File "+fileName+" found.");
            return dsf;
        }catch(NoResultException ex){
            logger.debug("File "+fileName+" not found.");
            return null;
        }
    }

    public void persist(DealSourceFile dsf){
        logger.debug("Persisting DealSourceFile "+dsf.toString());
        getEm().persist(dsf);
    }

}
