package com.progresssoft.manishkr.controller;

import com.progresssoft.manishkr.exception.FileAlreadyProcessedException;
import com.progresssoft.manishkr.exception.FileMoveException;
import com.progresssoft.manishkr.exception.FileParseException;
import com.progresssoft.manishkr.service.ImportDealsService;
import com.progresssoft.manishkr.util.FileUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/importdeals")
public class ImportDealsController {

    @Autowired
    private ImportDealsService importDealsService;

    @Autowired
    private FileUtil fileUtil;


    @Value("${unprocessed.file.folder}")
    private String unprocessedFileFolder;

    private static int counter = 0;
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ImportDealsController.class);

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ModelAndView all(@RequestParam(value = "sourceFileFolder", required = false) String sourceFileFolder) {
        List<String> unprocessedSourceFiles = fileUtil.getUnprocessedFiles();
        List<String> processedSourceFiles = fileUtil.getProcessedFiles();
        ModelAndView model = new ModelAndView("all");
        if(unprocessedSourceFiles.size() > 0){
            model.addObject("unprocessedSourceFiles",unprocessedSourceFiles);
        }else {
            model.addObject("unprocessedSourceFiles",null);
        }
        if(processedSourceFiles.size() > 0){
            model.addObject("processedSourceFiles",processedSourceFiles);
        }else {
            model.addObject("processedSourceFiles",null);
        }
        return model;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/process")
    protected ModelAndView process(@RequestParam(value = "fileToProcess") String fileToProcess, RedirectAttributes redir){
        logger.info(" Processing file: "+fileToProcess);
        long startTime = System.nanoTime();
        String fileToProcessMsg;
        ModelAndView model = new ModelAndView("redirect:/importdeals/all");
        try {
            importDealsService.processFile(fileToProcess);
            fileToProcessMsg = fileToProcess+" processed successfully!";
            long estimatedTime = System.nanoTime() - startTime;
            fileToProcessMsg += "Time taken to process: "+(estimatedTime/1000000000)+" seconds" ;
            logger.info(fileToProcessMsg);
        } catch (FileAlreadyProcessedException e) {
            fileToProcessMsg = fileToProcess+" already processed!";
        } catch (FileParseException e) {
            fileToProcessMsg = fileToProcess+" could not be processed!";
        } catch (FileMoveException e) {
            fileToProcessMsg = fileToProcess+" could not be moved!";
        }

        redir.addFlashAttribute("fileToProcessMsg",fileToProcessMsg);
        return model;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/currencycount")
    protected ModelAndView currencyCount(){
        ModelAndView mav = new ModelAndView("currencycount");
        mav.addObject("currencycount",importDealsService.getCurrencyCount());
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/filedetails")
    protected ModelAndView filedetails(@RequestParam(value = "file") String file){
        ModelAndView mav = new ModelAndView("filedetails");
        mav.addObject("filedetails",importDealsService.getFileDetails(file));
        return mav;
    }
}