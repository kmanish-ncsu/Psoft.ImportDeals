package com.progresssoft.manishkr.util;

import com.progresssoft.manishkr.exception.FileMoveException;
import com.progresssoft.manishkr.service.ImportDealsServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FileUtil.class);

    @Value("${unprocessed.file.folder}")
    private String unprocessedFileFolder;

    @Value("${processed.file.folder}")
    private String processedFileFolder;

    public List<String> getUnprocessedFiles(){
        logger.debug("fetching Unprocessed files from folder: "+unprocessedFileFolder);
        return getFiles(unprocessedFileFolder);
    }

    public List<String> getProcessedFiles(){
        logger.debug("fetching Processed files from folder: "+processedFileFolder);
        return getFiles(processedFileFolder);
    }

    private List<String> getFiles(String fileFolder) {
        List<String> files = new ArrayList<>();
        File folder = new File(fileFolder);
        File[] listOfFiles = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return !file.isHidden();
            }
        });
        if(listOfFiles != null && listOfFiles.length > 0){
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    files.add(file.getName());
                }
            }
        }
        logger.debug("Found files: "+files+" from folder "+fileFolder);
        return files;
    }

    public void moveFileToprocessedFileFolder(String fileToProcess) throws FileMoveException {
        logger.debug("Moving files from "+unprocessedFileFolder+" to "+processedFileFolder);
        File file = new File(unprocessedFileFolder+fileToProcess);
        if(!file.renameTo(new File(processedFileFolder+fileToProcess))){
            throw new FileMoveException("File "+fileToProcess+" is processed but could not be moved to processed folder!");
        }
    }

    public void setUnprocessedFileFolder(String unprocessedFileFolder) {
        this.unprocessedFileFolder = unprocessedFileFolder;
    }

    public void setProcessedFileFolder(String processedFileFolder) {
        this.processedFileFolder = processedFileFolder;
    }

}
