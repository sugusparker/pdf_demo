package com.fiserv.pdf_demo.service;

import com.fiserv.pdf_demo.Transaction;
import com.fiserv.pdf_demo.TransactionRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private TransactionRepository transactionRepository;


    public String exportReport(String reportFormat) throws IOException, JRException {
        String path = "D:\\pdf_demo";
        String resourcePath = "D:\\pdf_demo\\src\\main\\resources\\MyTestJasper.jrxml";
        List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();
        //Load file and compile
        File file = ResourceUtils.getFile("classpath: MyTestJasper.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        //revisar metodo JRBeanColletionSource casts Iterable to List
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transactions);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Fiserv");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "transaction.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "transaction.pdf");
        }
        return "report generated in path" + path;

    }

}
