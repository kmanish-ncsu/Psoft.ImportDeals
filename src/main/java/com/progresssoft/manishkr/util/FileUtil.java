package com.progresssoft.manishkr.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUtil {

    @Value("${unprocessed.file.folder}")
    private String unprocessedFileFolder;

    @Value("${processed.file.folder}")
    private String processedFileFolder;

    public List<String> getUnprocessedFiles(){
        List<String> files = new ArrayList<>();
        if(unprocessedFileFolder != null){
            File folder = new File(unprocessedFileFolder);
            File[] listOfFiles = folder.listFiles();
            if(listOfFiles != null && listOfFiles.length > 0){
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        files.add(file.getName());
                    }
                }
            }
        }
        return files;
    }

    public String getUnprocessedFilesFolder() {
        return unprocessedFileFolder;
    }

    public void setUnprocessedFileFolder(String unprocessedFileFolder) {
        this.unprocessedFileFolder = unprocessedFileFolder;
    }

    public String getProcessedFileFolder() {
        return processedFileFolder;
    }

    public void setProcessedFileFolder(String processedFileFolder) {
        this.processedFileFolder = processedFileFolder;
    }
}
