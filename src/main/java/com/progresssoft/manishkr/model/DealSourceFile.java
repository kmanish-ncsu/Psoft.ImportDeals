package com.progresssoft.manishkr.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "deal_source_file")
public class DealSourceFile implements Serializable{

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "source_file")
    private String sourceFile;

    @Column(name = "valid_rows")
    private Integer validRows;

    @Column(name = "invalid_rows")
    private Integer invalidRows;

    public DealSourceFile(String sourceFile, Integer validRows, Integer invalidRows) {
        this.sourceFile = sourceFile;
        this.validRows = validRows;
        this.invalidRows = invalidRows;
    }

    public DealSourceFile() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Integer getValidRows() {
        return validRows;
    }

    public void setValidRows(Integer validRows) {
        this.validRows = validRows;
    }

    public Integer getInvalidRows() {
        return invalidRows;
    }

    public void setInvalidRows(Integer invalidRows) {
        this.invalidRows = invalidRows;
    }

    @Override
    public String toString() {
        return "DealSourceFile{" +
                "sourceFile='" + sourceFile + '\'' +
                ", validRows=" + validRows +
                ", invalidRows=" + invalidRows +
                '}';
    }
}
