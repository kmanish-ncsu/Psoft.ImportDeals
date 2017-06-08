package com.progresssoft.manishkr.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "deal")
@Cacheable(value = false)
public class Deal {

    @Id
    private Integer id;

    @Column(name = "deal_id")
    private String dealId;

    @Column(name = "from_currency", columnDefinition = "char")
    private String fromCurrency;

    @Column(name = "to_currency", columnDefinition = "char")
    private String toCurrency;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date dealTimestamp;

    @Column(name = "amount")
    private BigDecimal amount;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "source_file")
    private DealSourceFile sourceFile;

    public Deal(Integer id,String dealId, String fromCurrency, String toCurrency, Timestamp dealTimestamp, BigDecimal amount, DealSourceFile sourceFile) {
        this.id = id;
        this.dealId = dealId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.dealTimestamp = dealTimestamp;
        this.amount = amount;
        this.sourceFile = sourceFile;
    }

    public Deal() {
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Date getDealTimestamp() {
        return dealTimestamp;
    }

    public void setDealTimestamp(Date dealTimestamp) {
        this.dealTimestamp = dealTimestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DealSourceFile getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(DealSourceFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMultiInsertSql(){
        return "insert into deal values ";
    }

    @Override
    public String toString() {
        return "Deal{" +
                "dealId='" + dealId + '\'' +
                ", fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", amount=" + amount +
                ", sourceFile=" + sourceFile +
                '}';
    }

}
