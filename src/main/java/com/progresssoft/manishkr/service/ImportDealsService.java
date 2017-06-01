package com.progresssoft.manishkr.service;

import com.progresssoft.manishkr.FileAlreadyProcessedException;
import com.progresssoft.manishkr.model.DealSourceFile;

public interface ImportDealsService {

    public DealSourceFile getFileDetails(String fileName);
    public void processFile(String fileToProcess) throws FileAlreadyProcessedException;
}
