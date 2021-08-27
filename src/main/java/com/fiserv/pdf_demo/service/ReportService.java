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
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private TransactionRepository transactionRepository;


    public String exportReport(String reportFormat) throws IOException, JRException {
        String path = System.getProperty("user.dir");
        List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();
        //Load file and compile
        var resourcePath = Path.of(path, "src/main/resources/UltimoOriginal.jrxml").toString();
        File file = new File(resourcePath);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        //check method JRBeanColletionSource casts Iterable to List
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transactions);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Fiserv");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, Path.of(path, "JasperExampleHTML.html").toString());
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, Path.of(path, "JasperExamplePDF.pdf").toString());
        }
        return "report generated in path " + path;

    }

}
