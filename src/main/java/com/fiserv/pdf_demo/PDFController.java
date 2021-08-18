package com.fiserv.pdf_demo;

import com.fiserv.pdf_demo.service.ReportService;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

@RestController
public class PDFController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReportService service;

    @GetMapping("/jasper/{format}")
    public String jasper(@PathVariable String format) throws Exception {

        return service.exportReport(format);

        /*InputStream is = PDFController.class.getResourceAsStream("my_jasper.jrxml");
        JasperReport report = JasperCompileManager.compileReport(is);
        List<Transaction> myTransactionList = (List<Transaction>) transactionRepository.findAll();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(makeCollection(myTransactionList));

        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<String, Object>(), dataSource);

        JasperExportManager.exportReportToPdfFile(print, "jasper.pdf");
        return "jasper";*/

    }

    @GetMapping(value = "/itext", produces = MediaType.TEXT_PLAIN_VALUE)
    public String itext() throws IOException {
        PdfWriter writer = new PdfWriter("itext.pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        // Creating an ImageData object
        //String imFile = "C:/Users/SugusparkeR/Desktop/logo2.png";
        //ImageData data = ImageDataFactory.create(imFile);
        // Creating an Image object
        //Image image = new Image(data);
        //Scale of the image
        //image.setAutoScale(true);

        //Position of the image
        //image.setFixedPosition(740,500);
        // Adding image to the document
        //document.add(image);

        //Number of columns and space
        Table table = new Table(new float[]{5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4});
        //Table occupancy on the page
        table.setWidth(UnitValue.createPercentValue(100));
        //table.setFixedPosition(10,1,UnitValue.createPercentValue(100));
        process(table, "id;value;created on;updated on; data1; data2; data3; data4; data5; data6; data7; data8; data9", bold, true);

        List<Transaction> myTransactionList = (List<Transaction>) (transactionRepository.findAll());
        for (Transaction transaction : makeCollection(myTransactionList)) {
            process(table, transaction.toString(), font, false);
        }
        document.add(table);
        document.close();
        return "itext";

    }

    private void process(Table table, String line, PdfFont font, boolean isHeader) throws MalformedURLException {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(
                        new Cell().add(
                                new Paragraph(tokenizer.nextToken()).setFont(font)));
            } else {
                table.addCell(
                        new Cell().add(
                                new Paragraph(tokenizer.nextToken()).setFont(font)));
            }
        }

        String imFile = "D:/pdf_demo/logo2.png";
        ImageData data = ImageDataFactory.create(imFile);
        Image image = new Image(data);
        table.addCell(image);
    }

    private List<Transaction> makeCollection(Iterable<Transaction> iter) {
        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction item : iter) {
            list.add(item);
        }
        return list;
    }


}
