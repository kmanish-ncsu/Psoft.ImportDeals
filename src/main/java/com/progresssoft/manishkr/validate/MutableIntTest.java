package com.progresssoft.manishkr.validate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MutableIntTest {

    MutableInt cut;

    @Before
    public void setUp() {
       cut = new MutableInt();
    }

    @Test
    public void testGet() {
        Assert.assertEquals(1,cut.get());
        cut.increment();
        Assert.assertEquals(2,cut.get());
        cut.increment();
        Assert.assertEquals(3,cut.get());
    }
}
