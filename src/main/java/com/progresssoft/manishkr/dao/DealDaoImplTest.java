package com.progresssoft.manishkr.dao;

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
public class DealDaoImplTest {

    DealDaoImpl cut;

    @Mock
    EntityManager mockEm;


    @Before
    public void setUp() throws SQLException {
        cut = new DealDaoImpl();
        cut.setEm(mockEm);
    }

    @Test
    public void testgetMaxId() {
        Query mockedQuery = mock(Query.class);
        when(this.cut.getEm().createQuery("SELECT COALESCE(MAX(id), 0) FROM Deal ")).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(50);
        Integer result = cut.getMaxId();
        Assert.assertEquals(new Integer(50),result);
    }

    @Test
    public void testgetMaxIdNoResultException() {
        Query mockedQuery = mock(Query.class);
        when(this.cut.getEm().createQuery("SELECT COALESCE(MAX(id), 0) FROM Deal ")).thenThrow(new NoResultException());
        Integer result = cut.getMaxId();
        Assert.assertNull(null,result);
    }

}
