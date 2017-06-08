package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealSourceFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DealSourceFileDaoImplTest {

    DealSourceFileDaoImpl cut;

    @Mock
    EntityManager mockEm;

    DealSourceFile dsf;

    @Before
    public void setUp() throws SQLException {
        cut = new DealSourceFileDaoImpl();
        cut.setEm(mockEm);
        dsf = new DealSourceFile("sourceFile",90,10);
    }

    @Test
    public void testfindByfileName() {
        Query mockedQuery = mock(Query.class);
        when(this.cut.getEm().createQuery("SELECT dsf FROM DealSourceFile dsf WHERE dsf.sourceFile = :fileName")).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("fileName","sourceFile")).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(dsf);
        DealSourceFile result = cut.findByfileName("sourceFile");
        Assert.assertNotNull(result);
        Assert.assertEquals("sourceFile",result.getSourceFile());
        Assert.assertEquals(new Integer(90),result.getValidRows());
        Assert.assertEquals(new Integer(10),result.getInvalidRows());
    }

    @Test
    public void testfindByfileNameNoResultException() {
        when(this.cut.getEm().createQuery("SELECT dsf FROM DealSourceFile dsf WHERE dsf.sourceFile = :fileName")).thenThrow(new NoResultException());
        DealSourceFile result = cut.findByfileName("invalidsourceFile");
        Assert.assertNull(null,result);
    }

}
