package com.fiserv.pdf_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class Provider {

    @Autowired
    private TransactionRepository transactionRepository;

    @PostConstruct
    public void initializeData() {
        for (int i = 0; i < 1000; i++) {
            Transaction transaction = new Transaction();
            transaction.setId(UUID.randomUUID().toString());
            transaction.setValue(BigDecimal.valueOf(Math.random() * 10));
            transaction.setCreatedOn(LocalDate.now());
            transaction.setLastUpdated(LocalDate.now());
            transaction.setData1("20$");
            transaction.setData2("30$");
            transaction.setData3("40$");
            transaction.setData4("50$");
            transaction.setData5("60$");
            transaction.setData6("70$");
            transaction.setData7("80$");
            transaction.setData8("90$");
            transaction.setData9("100$");

            transactionRepository.save(transaction);
        }
    }
}

