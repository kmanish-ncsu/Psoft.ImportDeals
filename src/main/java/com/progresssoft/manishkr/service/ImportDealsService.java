package com.progresssoft.manishkr.service;

import com.progresssoft.manishkr.exception.FileAlreadyProcessedException;
import com.progresssoft.manishkr.exception.FileMoveException;
import com.progresssoft.manishkr.exception.FileParseException;
import com.progresssoft.manishkr.model.DealSourceFile;

public interface ImportDealsService {

    DealSourceFile getFileDetails(String fileName);
    void processFile(String fileToProcess) throws FileAlreadyProcessedException, FileParseException, FileMoveException;
}
