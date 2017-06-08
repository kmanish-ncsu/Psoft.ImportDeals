package com.progresssoft.manishkr.validate;

import com.progresssoft.manishkr.controller.ImportDealsController;
import com.progresssoft.manishkr.dao.DealDao;
import com.progresssoft.manishkr.dao.InvalidDealDao;
import com.progresssoft.manishkr.model.Deal;
import com.progresssoft.manishkr.model.DealSourceFile;
import com.progresssoft.manishkr.model.InvalidDeal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DealsFileValidator {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DealsFileValidator.class);

    @Autowired
    DealDao dealDao;

    @Autowired
    InvalidDealDao invalidDealDao;

    private DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");

    public void validate(List<String[]> allDeals, DealSourceFile dsf, List<Deal> deals, List<InvalidDeal> invalidDeals, Map<String, MutableInt> freq){
        if(allDeals == null || allDeals.size() == 0) {return;}

        AtomicInteger atomicDealId = new AtomicInteger(dealDao.getMaxId());
        AtomicInteger atomicInvalidDealId = new AtomicInteger(invalidDealDao.getMaxId());
        for(String[] currDeal : allDeals){
            String dealId = currDeal[0];
            boolean validFromCurrency = validateCurrency(currDeal[1]);
            boolean validToCurrency = validateCurrency(currDeal[2]);

            MutableInt count = freq.get(currDeal[1]);
            if (count == null) {freq.put(currDeal[1], new MutableInt());}
            else {count.increment();}


            Timestamp timestamp = getTimeStamp(currDeal[3]);
            BigDecimal amount = getBigDecimal(currDeal[4]);

            if(dealId == null || !validFromCurrency || !validToCurrency || amount == null || timestamp == null){
                invalidDeals.add(new InvalidDeal(atomicInvalidDealId.incrementAndGet(),dealId,currDeal[1],currDeal[2],timestamp,amount,dsf));
            }else{
                deals.add(new Deal(atomicDealId.incrementAndGet(),dealId,currDeal[1],currDeal[2],timestamp,amount,dsf));
            }
        }
    }

    private BigDecimal getBigDecimal(String s) {
        try{
            return new BigDecimal(s);
        }catch(NullPointerException e){
            return null;
        }
    }

    private Timestamp getTimeStamp(String dateString) {
        try {
            Date date = formatter.parse(dateString);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    private boolean validateCurrency(String currency){
        try{
            Currency.getInstance(currency).getCurrencyCode();
            return true;
        }catch (IllegalArgumentException ex){
            return false;
        }
    }
}
