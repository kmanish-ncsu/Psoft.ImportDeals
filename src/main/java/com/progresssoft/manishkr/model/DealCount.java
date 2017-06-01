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

    public Currency getCurrency() {
        return Currency.getInstance(currency);
    }

    public void setCurrency(Currency currency) {
        this.currency = currency.getCurrencyCode();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
