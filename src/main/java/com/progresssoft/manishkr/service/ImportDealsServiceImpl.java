package com.progresssoft.manishkr.service;

import com.progresssoft.manishkr.exception.FileAlreadyProcessedException;
import com.progresssoft.manishkr.exception.FileMoveException;
import com.progresssoft.manishkr.exception.FileParseException;
import com.progresssoft.manishkr.dao.DealCountDao;
import com.progresssoft.manishkr.dao.DealDao;
import com.progresssoft.manishkr.dao.DealSourceFileDao;
import com.progresssoft.manishkr.dao.InvalidDealDao;
import com.progresssoft.manishkr.model.Deal;
import com.progresssoft.manishkr.model.DealCount;
import com.progresssoft.manishkr.model.DealSourceFile;
import com.progresssoft.manishkr.model.InvalidDeal;
import com.progresssoft.manishkr.util.FileUtil;
import com.progresssoft.manishkr.validate.DealsFileValidator;
import com.progresssoft.manishkr.validate.MutableInt;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.*;
import java.util.*;

@Service
@Transactional
public class ImportDealsServiceImpl implements ImportDealsService{

    @Autowired
    private DealSourceFileDao dealSourceFileDao;

    @Autowired
    private DealDao dealDao;

    @Autowired
    private InvalidDealDao invalidDealDao;

    @Autowired
    DealsFileValidator dealsFileValidator;

    @Autowired
    DealCountDao dealCountDao;

    @Autowired
    FileUtil fileUtil;

    @PersistenceContext
    private EntityManager em;

    BufferedReader bufferedReader = null;

    @Value("${unprocessed.file.folder}")
    private String unprocessedFileFolder;

    @Value("${processed.file.folder}")
    private String processedFileFolder;

    public DealSourceFile getFileDetails(String fileName){
        return dealSourceFileDao.findByfileName(fileName);
    }

    public void processFile(String fileToProcess) throws FileAlreadyProcessedException, FileParseException, FileMoveException {
        List<String[]> allRows = parseSourceFile(unprocessedFileFolder, fileToProcess);

        List<Deal> deals = new ArrayList<>();
        List<InvalidDeal> invalidDeals = new ArrayList<>();
        Map<String, MutableInt> freq = new HashMap<>();

        // insert file into deal_source_file table
        DealSourceFile dsf = new DealSourceFile();
        dsf.setSourceFile(fileToProcess);
        saveSourceFile(dsf);
        dealsFileValidator.validate(allRows, dsf, deals, invalidDeals, freq);
        // validate all rows, create 2 lists (valid,invalid)
        System.err.println("DEALS>>>>>>>>>>>"+deals.size());
        System.err.println("INVALID DEALS>>>>>>>>>>>"+invalidDeals.size());

//        for(Deal d : deals){
//            dealDao.persist(d);
//            count++;
//            if ( (count % 50) == 0) {
//                em.flush();
//                em.clear();
//            }
//        }
//
//        count = 0;
//        for(InvalidDeal d : invalidDeals){
//            invalidDealDao.persist(d);
//            count++;
//            if ( (count % 50) == 0) {
//                em.flush();
//                em.clear();
//            }
//        }

        StringBuilder query = new StringBuilder();
        int dealsize = deals.size();
        int count = 0;
        for(Deal d : deals){
            query.append("(").append(d.getId()).append(",'").append(d.getDealId()).append("','").append(d.getFromCurrency()).append("','").append(d.getToCurrency()).append("','").append(d.getDealTimestamp()).append("',").append(d.getAmount()).append(",").append(d.getSourceFile().getId()).append("),");
            count++;
            if ( (count % 1000) == 0) {
                query.setLength(query.length() - 1);
                query.insert(0,"insert into deal values ");
                em.createNativeQuery(query.toString()).executeUpdate();
                query = new StringBuilder();
            }else{
                if(count == dealsize){
                    query.setLength(query.length() - 1);
                    query.insert(0,"insert into deal values ");
                    em.createNativeQuery(query.toString()).executeUpdate();
                }
            }

        }
        query = new StringBuilder();
        dealsize = invalidDeals.size();
        count = 0;
        for(InvalidDeal d : invalidDeals){
            query.append("(").append(d.getId()).append(",'").append(d.getDealId()).append("','").append(d.getFromCurrency()).append("','").append(d.getToCurrency()).append("','").append(d.getDealTimestamp()).append("',").append(d.getAmount()).append(",").append(d.getSourceFile().getId()).append("),");
            count++;
            if ( (count % 1000) == 0) {
                query.setLength(query.length() - 1);
                query.insert(0,"insert into invalid_deal values ");
                em.createNativeQuery(query.toString()).executeUpdate();
                query = new StringBuilder();
            }else{
                if(count == dealsize){
                    query.setLength(query.length() - 1);
                    query.insert(0,"insert into invalid_deal values ");
                    em.createNativeQuery(query.toString()).executeUpdate();
                }
            }

        }
        //count valid deals per currency (insert into)
        DealCount dc;
        for(Map.Entry<String, MutableInt> entry : freq.entrySet()){
            //read code from db and update after adding count
            dc = dealCountDao.findByCurrencyCode(entry.getKey());
            if(dc == null){
                System.err.println("CURRENCY "+entry.getKey()+" fetched was null");
                dc = new DealCount(entry.getKey(), entry.getValue().get());
                dealCountDao.persist(dc);
            }else{
              dc.setCount(dc.getCount()+ entry.getValue().get());
            }
        }
        //update DSF with valid invalid rows
        dsf.setValidRows(deals.size());
        dsf.setInvalidRows(invalidDeals.size());

        //TODO move the file to processed file folder
        fileUtil.moveFile(fileToProcess);
        try {getBufferedReader().close();} catch (IOException e) {e.printStackTrace();}
    }

    private List<String[]> parseSourceFile(String unprocessedFileFolder, String fileToProcess) throws FileParseException {
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        CsvParser parser = new CsvParser(settings);
        setBufferedReader(getReader(unprocessedFileFolder+fileToProcess));
        try{
            return parser.parseAll(getBufferedReader());
        }catch (Exception e){
            throw new FileParseException("Error parsing file "+fileToProcess);
        }
    }

    private void saveSourceFile(DealSourceFile dsf) throws FileAlreadyProcessedException {
        DealSourceFile check = getFileDetails(dsf.getSourceFile());
        if(check == null){
            dealSourceFileDao.persist(dsf);
        }else{
            throw new FileAlreadyProcessedException("File "+dsf.getSourceFile()+" already processed!");
        }
    }

    private BufferedReader getReader(String csvFile) {
        BufferedReader br2 = null;
        try {
            br2 =  new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));
        } catch(Exception ex){
            System.out.println("fix"+ex);
        }
        return br2;
    }

    private BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    private void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }
}
