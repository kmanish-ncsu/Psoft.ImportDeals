package com.progresssoft.manishkr.model;

import javax.persistence.*;
import java.util.Currency;

@Entity
@Table(name = "deal_count")
public class DealCount {

    @Id
    @GeneratedValue
    Integer id;

    public DealCount(String currency, Integer count) {
        this.currency = currency;
        this.count = count;
    }

    @Column(name = "currency", columnDefinition = "char")
    private String currency;

    @Column(name = "count")
    Integer count;

    public DealCount() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        try{
            return Currency.getInstance(currency).getCurrencyCode();
        }catch (IllegalArgumentException ex){
            return currency+"(invalid)";
        }


    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "DealCount{" +
                "currency='" + currency + '\'' +
                ", count=" + count +
                '}';
    }
}
