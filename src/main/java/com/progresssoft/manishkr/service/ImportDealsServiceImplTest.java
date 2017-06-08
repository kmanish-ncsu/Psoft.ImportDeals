package com.progresssoft.manishkr.service;

import com.progresssoft.manishkr.dao.DealCountDao;
import com.progresssoft.manishkr.dao.DealSourceFileDao;
import com.progresssoft.manishkr.exception.FileParseException;
import com.progresssoft.manishkr.model.DealCount;
import com.progresssoft.manishkr.model.DealSourceFile;
import com.progresssoft.manishkr.util.FileUtil;
import com.progresssoft.manishkr.validate.DealsFileValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImportDealsServiceImplTest {

    @Spy
    ImportDealsServiceImpl cut;

    @Mock
    DealSourceFileDao dealSourceFileDao;

    @Mock
    DealsFileValidator dealsFileValidator;

    @Mock
    DealCountDao dealCountDao;

    @Mock
    FileUtil fileUtil;

    @Mock
    EntityManager em;

    @Mock
    BufferedReader bufferedReader = null;

    @Before
    public void setUp() throws SQLException {
        cut.setEm(em);
        cut.setDealCountDao(dealCountDao);
        cut.setDealsFileValidator(dealsFileValidator);
        cut.setDealSourceFileDao(dealSourceFileDao);
        cut.setFileUtil(fileUtil);
        cut.setProcessedFileFolder("/Users/manishkr/ProgressSoft/unprocessed/");
        cut.setUnprocessedFileFolder("/Users/manishkr/ProgressSoft/processed/");
    }

    @Test
    public void testgetFileDetails(){
        DealSourceFile dsf = new DealSourceFile("sourceFile",90,10);
        when(dealSourceFileDao.findByfileName(anyString())).thenReturn(dsf);

        DealSourceFile result = cut.getFileDetails("sourceFile");

        Assert.assertNotNull(result);
        Assert.assertEquals("sourceFile",result.getSourceFile());
        Assert.assertEquals(new Integer(90), result.getValidRows());
        Assert.assertEquals(new Integer(10), result.getInvalidRows());

        when(dealSourceFileDao.findByfileName(anyString())).thenReturn(null);
        result = cut.getFileDetails("wrongSourceFile");
        Assert.assertNull(result);
    }

    @Test
    public void testgetCurrencyCount() {
        List<DealCount> dealCounts = new ArrayList<>();
        dealCounts.add(new DealCount("INR",10));
        dealCounts.add(new DealCount("USD",20));
        dealCounts.add(new DealCount("AED",30));
        when(dealCountDao.findAll()).thenReturn(dealCounts);

        List<DealCount> result = cut.getCurrencyCount();
        Assert.assertNotNull(result);
        Assert.assertEquals(3,result.size());
    }

    @Test(expected = FileParseException.class)
    public void testprocessFileWithFileParseException() throws Exception {
        cut.processFile("fileToProcess");
    }

    @Test
    public void testprocessFile() throws Exception {
        List<String[]> parsedSrcFile = new ArrayList<>();
        parsedSrcFile.add(new String[]{"67-8522525","JPY","UAH","3/4/15 1:49","5617.73"});
        parsedSrcFile.add(new String[]{"77-8522525","AED","UAH","3/4/17 12:33","245.43"});
        parsedSrcFile.add(new String[]{"87-8522525","INR","AED","3/1/16 2:49","517.9"});
        doReturn(bufferedReader).when(cut).getBufferedReader();
        doReturn(dealsFileValidator).when(cut).getDealsFileValidator();
        doReturn(parsedSrcFile).when(cut).parseSourceFile(anyString());
        cut.processFile("DUMMY");
    }

}
