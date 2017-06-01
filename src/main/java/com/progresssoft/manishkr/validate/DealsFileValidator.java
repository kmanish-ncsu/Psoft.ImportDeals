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
            String fromCurrency = getCurrency(currDeal[1]);

            MutableInt count = freq.get(fromCurrency);
            if (count == null) {
                freq.put(fromCurrency, new MutableInt());
            }
            else {
                count.increment();
            }

            String toCurrency = getCurrency(currDeal[2]);
            Timestamp timestamp = getTimeStamp(currDeal[3]);
            BigDecimal amount = new BigDecimal(currDeal[4]);

            if(dealId == null || fromCurrency == null || toCurrency == null || amount == null || timestamp == null){
                invalidDeals.add(new InvalidDeal(atomicInvalidDealId.incrementAndGet(),dealId,fromCurrency,toCurrency,timestamp,amount,dsf));
            }else{
                deals.add(new Deal(atomicDealId.incrementAndGet(),dealId,fromCurrency,toCurrency,timestamp,amount,dsf));
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

    private String getCurrency(String currency){
        try{
            return Currency.getInstance(currency).getCurrencyCode();
        }catch (IllegalArgumentException ex){
            return null;
        }
    }
}
