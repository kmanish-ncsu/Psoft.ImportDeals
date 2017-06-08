package com.progresssoft.manishkr.util;

import com.progresssoft.manishkr.exception.FileMoveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilTest {

    @Spy
    FileUtil cut;

    @Before
    public void setUp() {
//       cut = new FileUtil();
       cut.setProcessedFileFolder(anyString());
       cut.setUnprocessedFileFolder(anyString());
    }

    @Test
    public void testgetUnprocessedFiles() {
        List<String> unprocessedFiles = new ArrayList<>();
        unprocessedFiles.add("file1");
        unprocessedFiles.add("file2");
        doReturn(unprocessedFiles).when(cut).getUnprocessedFiles();
        List<String> unprocessedFile = cut.getUnprocessedFiles();
        Assert.assertNotNull(unprocessedFile);
    }

    @Test
    public void testgetprocessedFiles() {
        List<String> processedFiles = new ArrayList<>();
        processedFiles.add("file3");
        processedFiles.add("file4");
//        doReturn(processedFiles).when(cut).getProcessedFiles();
        List<String> processedFile = cut.getProcessedFiles();
        Assert.assertNotNull(processedFile);
    }

    @Test(expected = FileMoveException.class)
    public void testmoveFileToprocessedFileFolder() throws FileMoveException {
        cut.moveFileToprocessedFileFolder(anyString());
    }
}
