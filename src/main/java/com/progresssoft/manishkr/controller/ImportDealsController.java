package com.progresssoft.manishkr.controller;

import com.progresssoft.manishkr.FileAlreadyProcessedException;
import com.progresssoft.manishkr.service.ImportDealsService;
import com.progresssoft.manishkr.util.FileUtil;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/importdeals")
public class ImportDealsController {

    @Autowired
    ImportDealsService importDealsService;

    @Autowired
    FileUtil fileUtil;


    @Value("${unprocessed.file.folder}")
    private String unprocessedFileFolder;

    private static int counter = 0;
//    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ImportDealsController.class);

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ModelAndView all(@RequestParam(value = "sourceFileFolder", required = false) String sourceFileFolder) {
        List<String> unprocessedSourceFiles = fileUtil.getUnprocessedFiles();
        ModelAndView model = new ModelAndView("all");
        if(unprocessedSourceFiles.size() > 0){
            model.addObject("unprocessedSourceFiles",unprocessedSourceFiles);
        }else {
            model.addObject("unprocessedSourceFiles",null);
        }

        return model;

        //        DealSourceFile dsf = importDealsService.getFileDetails("file1");
//        model.addObject("dsf",dsf);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/process")
    protected ModelAndView process(@RequestParam(value = "fileToProcess") String fileToProcess, RedirectAttributes redir){
        System.out.println(" Processing file: "+fileToProcess);
        long startTime = System.nanoTime();
        String fileToProcessMsg;
        try {
            importDealsService.processFile(fileToProcess);
            fileToProcessMsg = fileToProcess+" processed successfully!";
        } catch (FileAlreadyProcessedException e) {
            fileToProcessMsg = fileToProcess+" already processed!";
            e.printStackTrace();//TODO ?
        }
        long estimatedTime = System.nanoTime() - startTime;
//        System.err.println("MOT SIZE>>>>>>>>>>>> "+allRows.size());
        ModelAndView model = new ModelAndView("redirect:/importdeals/all");
        fileToProcessMsg += "Time taken to process: "+(estimatedTime/1000000000)+" seconds" ;
        redir.addFlashAttribute("fileToProcessMsg",fileToProcessMsg);
        return model;
    }



//    @RequestMapping(method = RequestMethod.GET, value = "/filedetails")
//    protected String filedetails(@RequestParam(value = "fileToShowDetail") String fileToShowDetail){
//        return fileToShowDetail+"..........FileDetail";
//    }
}