package com.progresssoft.manishkr.util;

import com.progresssoft.manishkr.exception.FileMoveException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {

    @Value("${unprocessed.file.folder}")
    private String unprocessedFileFolder;

    @Value("${processed.file.folder}")
    private String processedFileFolder;

    public List<String> getUnprocessedFiles(){
        return getFiles(unprocessedFileFolder);
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
        return files;
    }

    public List<String> getProcessedFiles(){
        return getFiles(processedFileFolder);
    }

    public void moveFile(String fileToProcess) throws FileMoveException {
        File file = new File(unprocessedFileFolder+fileToProcess);
        if(!file.renameTo(new File(processedFileFolder+fileToProcess))){
            throw new FileMoveException("File "+fileToProcess+" is processed but could not be moved to processed folder!");
        }
    }

}
