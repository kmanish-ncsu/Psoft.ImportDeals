package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealSourceFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class DealSourceFileDaoImpl implements DealSourceFileDao {

    @PersistenceContext
    private EntityManager em;

    public DealSourceFile findByfileName(String fileName) {
        System.out.println("fileName : "+fileName);
        try{
            DealSourceFile dsf = (DealSourceFile) em
                    .createQuery("SELECT dsf FROM DealSourceFile dsf WHERE dsf.sourceFile = :fileName")
                    .setParameter("fileName", fileName)
                    .getSingleResult();

            return dsf;
        }catch(NoResultException ex){
            System.err.println("File "+fileName+" not found.");
            return null;
        }
    }

    public void persist(DealSourceFile dsf){
        em.persist(dsf);
    }

    public void merge(DealSourceFile dsf){
        em.merge(dsf);
    }
}
