package com.progresssoft.manishkr.dao;

import com.progresssoft.manishkr.model.DealCount;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DealCountDaoImplTest {

    DealCountDaoImpl cut;

    @Mock
    EntityManager mockEm;

    List<DealCount> dealCounts;

    DealCount singleDealCount;

    @Before
    public void setUp() throws SQLException {
        cut = new DealCountDaoImpl();
        cut.setEm(mockEm);
        dealCounts = new ArrayList<>();
        singleDealCount = new DealCount();
    }

    @Test
    public void testFindAll() {
        Query mockedQuery = mock(Query.class);
        when(this.cut.getEm().createQuery("SELECT dc FROM DealCount dc")).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenReturn(dealCounts);
        dealCounts.add(new DealCount("INR",10));
        dealCounts.add(new DealCount("USD",20));
        dealCounts.add(new DealCount("AED",30));
        List<DealCount> result = cut.findAll();
        Assert.assertNotNull(result);
        Assert.assertEquals(3,result.size());
    }

    @Test
    public void testFindAllNoResultsExceptionResultingInNull() {
        Query mockedQuery = mock(Query.class);
        when(this.cut.getEm().createQuery("SELECT dc FROM DealCount dc")).thenReturn(mockedQuery);
        when(mockedQuery.getResultList()).thenThrow(new NoResultException());
        List<DealCount> result = cut.findAll();
        Assert.assertNull(result);
    }

    @Test
    public void testFindByCurrencyCode() {
        DealCount dc = new DealCount("AED",100);
        Query mockedQuery = mock(Query.class);
        when(this.cut.getEm().createQuery("SELECT dc FROM DealCount dc WHERE dc.currency = :currency")).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("currency","AED")).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(dc);
        DealCount result = cut.findByCurrencyCode("AED");
        Assert.assertNotNull(result);
        Assert.assertEquals("AED",result.getCurrency());
        Assert.assertEquals(new Integer(100),result.getCount());
    }

    @Test
    public void testFindByCurrencyCodeReturnsNull() {
        Query mockedQuery = mock(Query.class);
        when(this.cut.getEm().createQuery("SELECT dc FROM DealCount dc WHERE dc.currency = :currency")).thenThrow(new NoResultException());
        DealCount result = cut.findByCurrencyCode("AED");
        Assert.assertNull(result);
    }
}
