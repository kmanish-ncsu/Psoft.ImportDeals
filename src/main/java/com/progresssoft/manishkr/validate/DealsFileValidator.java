package com.progresssoft.manishkr.validate;

import com.progresssoft.manishkr.dao.DealDao;
import com.progresssoft.manishkr.dao.InvalidDealDao;
import com.progresssoft.manishkr.model.Deal;
import com.progresssoft.manishkr.model.DealSourceFile;
import com.progresssoft.manishkr.model.InvalidDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DealsFileValidator {

//    @Value("${timestamp.format}")
//    private String timestampFormat;

    @Autowired
    DealDao dealDao;

    @Autowired
    InvalidDealDao invalidDealDao;

    private DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");

    public void validate(List<String[]> allDeals, DealSourceFile dsf, List<Deal> deals, List<InvalidDeal> invalidDeals, Map<String, MutableInt> freq){
        if(allDeals == null) {return;}



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
            BigDecimal amount = new BigDecimal(currDeal[4]);

            if(dealId == null || !validFromCurrency || !validToCurrency || amount == null || timestamp == null){
                invalidDeals.add(new InvalidDeal(atomicInvalidDealId.incrementAndGet(),dealId,currDeal[1],currDeal[2],timestamp,amount,dsf));
                System.err.println("INVALID DDALS "+invalidDeals.toString());
            }else{
                deals.add(new Deal(atomicDealId.incrementAndGet(),dealId,currDeal[1],currDeal[2],timestamp,amount,dsf));
            }

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
