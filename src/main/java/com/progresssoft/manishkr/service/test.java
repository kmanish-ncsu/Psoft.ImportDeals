package com.progresssoft.manishkr.service;

import com.progresssoft.manishkr.model.DealSourceFile;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class test {

    public static void main(String[] args) {
//        List<DealSourceFile> list = new ArrayList<>();
//        list.add(new DealSourceFile("INR", 1,2));
//        list.add(new DealSourceFile("USD", 1,2));
//        list.add(new DealSourceFile("USD", 1,2));
//        list.add(new DealSourceFile("EUR", 1,2));
//        list.add(new DealSourceFile("EUR", 1,2));
//        list.add(new DealSourceFile("EUR", 1,2));
//        list.add(new DealSourceFile("EUR", 1,2));
//        list.add(new DealSourceFile("INR", 1,2));
//        System.out.println(Collections.frequency(list,new DealSourceFile("EUR",null,null)));
        long startTime = System.nanoTime();
//        try{
//            System.out.println(Currency.getInstance("INRR").getSymbol());
//        }catch(IllegalArgumentException e){
//            e.printStackTrace();
//        }
        long estimatedTime = System.nanoTime() - startTime;
//        System.err.println("MOT TIME SECONDS >>>>>>>>>>>> "+estimatedTime/1000000);

        DateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
        try {
            Date date = formatter.parse("3/27/17 10:55 ");
//            System.out.println(date);
//            System.out.println(new Timestamp(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AtomicInteger ai = new AtomicInteger(0);
        System.out.println(ai.incrementAndGet());
        System.out.println(ai.incrementAndGet());
        System.out.println(ai.incrementAndGet());
        System.out.println(ai.incrementAndGet());

//        System.out.println("X"+ sdf.format(Date.parse()"")));//Timestamp.valueOf("3/27/17 10:55")

    }
}
