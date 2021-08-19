package com.fiserv.pdf_demo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Transaction {

    @Id
    private String id;
    private BigDecimal value;
    private LocalDate createdOn;
    private LocalDate lastUpdated;
    private String data1;
    private String data2;
    private String data3;
    private String data4;
    private String data5;
    private String data6;
    private String data7;
    private String data8;
    private String data9;

    @Override
    public String toString() {
        return "" + id + ";" + value + ";" + createdOn + ";" + lastUpdated;
    }
}

