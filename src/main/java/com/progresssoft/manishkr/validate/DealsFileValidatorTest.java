package com.progresssoft.manishkr.validate;

import com.progresssoft.manishkr.dao.DealDao;
import com.progresssoft.manishkr.dao.InvalidDealDao;
import com.progresssoft.manishkr.model.Deal;
import com.progresssoft.manishkr.model.DealSourceFile;
import com.progresssoft.manishkr.model.InvalidDeal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DealsFileValidatorTest {

    @InjectMocks
    DealsFileValidator cut;

    @Mock
    DealDao dealDao;

    @Mock
    InvalidDealDao invalidDealDao;

    List<String[]> allDeals;
    DealSourceFile dsf;
    List< Deal > deals;
    List< InvalidDeal > invalidDeals;
    Map<String, MutableInt> freq;

    @Before
    public void setUp() {
        allDeals = new ArrayList<>();
        deals = new ArrayList<>();
        invalidDeals = new ArrayList<>();
    }

    @Test
    public void testValidateEmptyDeals() {
        dsf = new DealSourceFile("sourceFile", 90, 10);
        freq = new HashMap<>();
        cut.validate(allDeals, dsf, deals, invalidDeals, freq);
        Assert.assertEquals(0,deals.size());
        Assert.assertEquals(0,invalidDeals.size());
    }

    @Test
    public void testValidate() {
        allDeals.add(new String[]{"67-8522525","JPY","UAH","3/4/15 1:49","5617.73"});
        allDeals.add(new String[]{"77-8522525","AED","UAH","3/4/17 12:33","245.43"});
        allDeals.add(new String[]{"87-8522525","INR","AED","3/1/16 2:49","517.9"});
        allDeals.add(new String[]{"87-8522525","INRR","AED","3/1/16 2:49","517.9"});
        allDeals.add(new String[]{"87-8522525","INRR","AED","XXX","517.9"});
        allDeals.add(new String[]{null,"INRR","AED","XXX","517.9"});
        allDeals.add(new String[]{"87-85225","INRR","AED","XXX",null});
        dsf = new DealSourceFile("sourceFile", 90, 10);
        freq = new HashMap<>();

        when(dealDao.getMaxId()).thenReturn(10);
        when(invalidDealDao.getMaxId()).thenReturn(5);
        cut.validate(allDeals, dsf, deals, invalidDeals, freq);
        Assert.assertEquals(3,deals.size());
        Assert.assertEquals(4,invalidDeals.size());
    }
}
