package com.progresssoft.manishkr.service;

import com.progresssoft.manishkr.dao.DealCountDao;
import com.progresssoft.manishkr.dao.DealSourceFileDao;
import com.progresssoft.manishkr.exception.FileAlreadyProcessedException;
import com.progresssoft.manishkr.exception.FileMoveException;
import com.progresssoft.manishkr.exception.FileParseException;
import com.progresssoft.manishkr.model.Deal;
import com.progresssoft.manishkr.model.DealCount;
import com.progresssoft.manishkr.model.DealSourceFile;
import com.progresssoft.manishkr.model.InvalidDeal;
import com.progresssoft.manishkr.util.FileUtil;
import com.progresssoft.manishkr.validate.DealsFileValidator;
import com.progresssoft.manishkr.validate.MutableInt;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ImportDealsServiceImpl implements ImportDealsService{

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(ImportDealsServiceImpl.class);

    public static final int MULTI_INSERT_SIZE = 1000;

    @Autowired
    private DealSourceFileDao dealSourceFileDao;

    @Autowired
    private DealsFileValidator dealsFileValidator;

    @Autowired
    private DealCountDao dealCountDao;

    @Autowired
    private FileUtil fileUtil;

    @PersistenceContext
    private EntityManager em;

    private BufferedReader bufferedReader = null;

    @Value("${unprocessed.file.folder}")
    private String unprocessedFileFolder;

    @Value("${processed.file.folder}")
    private String processedFileFolder;

    CsvParserSettings csvParserSettings;

    CsvParser csvParser;

    public DealSourceFile getFileDetails(String fileName){
        return getDealSourceFileDao().findByfileName(fileName);
    }

    public void processFile(String fileToProcess) throws FileAlreadyProcessedException, FileParseException, FileMoveException {
        List<String[]> allRows = parseSourceFile(fileToProcess);

        List<Deal> deals = new ArrayList<>();
        List<InvalidDeal> invalidDeals = new ArrayList<>();
        Map<String, MutableInt> freq = new HashMap<>();

        DealSourceFile dsf = saveSourceFile(fileToProcess);
        getDealsFileValidator().validate(allRows, dsf, deals, invalidDeals, freq);
        persistDeals(deals);
        persistInvalidDeals(invalidDeals);
        updateDealCounts(freq);
        dsf.setValidRows(deals.size());
        dsf.setInvalidRows(invalidDeals.size());
        getFileUtil().moveFileToprocessedFileFolder(fileToProcess);
        try {getBufferedReader().close();} catch (IOException e) {e.printStackTrace();}
    }

    private void updateDealCounts(Map<String, MutableInt> freq) {
        DealCount dc;
        for(Map.Entry<String, MutableInt> entry : freq.entrySet()){
            dc = getDealCountDao().findByCurrencyCode(entry.getKey());
            if(dc == null){
                logger.debug("CURRENCY "+entry.getKey()+" fetched was null");
                dc = new DealCount(entry.getKey(), entry.getValue().get());
                getDealCountDao().persist(dc);
            }else{
                logger.debug("CURRENCY "+entry.getKey()+" was found");
              dc.setCount(dc.getCount()+ entry.getValue().get());
            }
        }
    }

    private void persistInvalidDeals(List<InvalidDeal> invalidDeals) {
        logger.debug("Persisting "+ invalidDeals.size()+" Deals");
        StringBuilder query;
        int dealsize;
        int count;
        query = new StringBuilder();
        dealsize = invalidDeals.size();
        count = 0;
        for(InvalidDeal d : invalidDeals){
            query.append("(").append(d.getId()).append(",'").append(d.getDealId()).append("','").append(d.getFromCurrency()).append("','").append(d.getToCurrency()).append("','").append(d.getDealTimestamp()).append("',").append(d.getAmount()).append(",").append(d.getSourceFile().getId()).append("),");
            count++;
            if ( (count % MULTI_INSERT_SIZE) == 0) {
                multiInsert(query, d);
                query = new StringBuilder();
            }else{
                if(count == dealsize){
                    query.setLength(query.length() - 1);
                    query.insert(0,d.getMultiInsertSql());
                    getEm().createNativeQuery(query.toString()).executeUpdate();
                }
            }

        }
    }

    private void multiInsert(StringBuilder query, InvalidDeal d) {
        query.setLength(query.length() - 1);
        query.insert(0,d.getMultiInsertSql());
        getEm().createNativeQuery(query.toString()).executeUpdate();
    }

    private void persistDeals(List<Deal> deals) {
        logger.debug("Persisting "+ deals.size()+" Deals");
        StringBuilder query = new StringBuilder();
        int dealsize = deals.size();
        int count = 0;
        for(Deal d : deals){
            query.append("(").append(d.getId()).append(",'").append(d.getDealId()).append("','").append(d.getFromCurrency()).append("','").append(d.getToCurrency()).append("','").append(d.getDealTimestamp()).append("',").append(d.getAmount()).append(",").append(d.getSourceFile().getId()).append("),");
            count++;
            if ( (count % MULTI_INSERT_SIZE) == 0) {
                query.setLength(query.length() - 1);
                query.insert(0,d.getMultiInsertSql());
                getEm().createNativeQuery(query.toString()).executeUpdate();
                query = new StringBuilder();
            }else{
                if(count == dealsize){
                    query.setLength(query.length() - 1);
                    query.insert(0,d.getMultiInsertSql());
                    getEm().createNativeQuery(query.toString()).executeUpdate();
                }
            }

        }
    }

    @Override
    public List<DealCount> getCurrencyCount() {
        return getDealCountDao().findAll();
    }

    public List<String[]> parseSourceFile(String fileToProcess) throws FileParseException {
        logger.debug("Parsing DealSourceFile "+fileToProcess);
        csvParserSettings = new CsvParserSettings();
        csvParserSettings.getFormat().setLineSeparator("\n");
        csvParser = new CsvParser(csvParserSettings);
        setBufferedReader(getReader(getUnprocessedFileFolder()+fileToProcess));
        try{
            return csvParser.parseAll(getBufferedReader());
        }catch (Exception e){
            throw new FileParseException("Error parsing file "+fileToProcess);
        }
    }

    private DealSourceFile saveSourceFile(String fileToProcess) throws FileAlreadyProcessedException {
        logger.debug("Saving DealSourceFile "+fileToProcess);
        DealSourceFile dsf = new DealSourceFile();
        dsf.setSourceFile(fileToProcess);
        DealSourceFile check = getFileDetails(dsf.getSourceFile());
        if(check == null){
            getDealSourceFileDao().persist(dsf);
            return dsf;
        }else{
            throw new FileAlreadyProcessedException("File "+dsf.getSourceFile()+" already processed!");
        }
    }

    private BufferedReader getReader(String csvFile) throws FileParseException {
        BufferedReader br2;
        try {
            br2 =  new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"));
        } catch(Exception ex){
            throw new FileParseException("Error reading file "+csvFile);
        }
        return br2;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public DealSourceFileDao getDealSourceFileDao() {
        return dealSourceFileDao;
    }

    public void setDealSourceFileDao(DealSourceFileDao dealSourceFileDao) {
        this.dealSourceFileDao = dealSourceFileDao;
    }

    public DealsFileValidator getDealsFileValidator() {
        return dealsFileValidator;
    }

    public void setDealsFileValidator(DealsFileValidator dealsFileValidator) {
        this.dealsFileValidator = dealsFileValidator;
    }

    public DealCountDao getDealCountDao() {
        return dealCountDao;
    }

    public void setDealCountDao(DealCountDao dealCountDao) {
        this.dealCountDao = dealCountDao;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public void setFileUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public String getUnprocessedFileFolder() {
        return unprocessedFileFolder;
    }

    public void setUnprocessedFileFolder(String unprocessedFileFolder) {
        this.unprocessedFileFolder = unprocessedFileFolder;
    }

    public void setProcessedFileFolder(String processedFileFolder) {
        this.processedFileFolder = processedFileFolder;
    }

}
