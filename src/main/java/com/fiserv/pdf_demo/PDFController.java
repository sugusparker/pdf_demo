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
import com.lowagie.text.Header;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
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

    }

    @GetMapping("/jasper2/{format}")
    public void jasper2(@PathVariable String format) throws Exception {
        try {
            String path = System.getProperty("user.dir");
            Map<String, Object> params = new HashMap<String, Object>();
            var resourcePath = Path.of(path, "src/main/resources/UltimoOriginal.jrxml").toString();
            File file = new File(resourcePath);
            String imFile = "src/main/resources/logo2.png";

            URL url = this.getClass().getClassLoader().getResource(Path.of(path, "src/main/resources/logo2.png").toString());

            params.put("logo", url);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, getDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, Path.of(path, "JasperExamplePDF" + getRandomNumberUsingNextInt(1, 10000) + ".pdf").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private JRDataSource getDataSource() {
        //Collection<BeanWithList> coll = new ArrayList<BeanWithList>();
        Collection<Transaction> transactions = (Collection<Transaction>) transactionRepository.findAll();
        return new JRBeanCollectionDataSource(transactions);
    }


    @GetMapping(value = "/itext", produces = MediaType.TEXT_PLAIN_VALUE)
    public String itext() throws IOException {
        PdfWriter writer = new PdfWriter("itext" + getRandomNumberUsingNextInt(1, 1000) + ".pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(20, 20, 20, 20);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Header header = new Header("Fiserv", "Test");
        String imFile = "D:\\pdf_demo\\src\\main\\resources\\logo2.png";
        ImageData data = ImageDataFactory.create(imFile);
        Image image = new Image(data);
        document.add(image);


        //Number of columns and space
        Table table = new Table(new float[]{5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4});
        //Table occupancy on the page
        table.setWidth(UnitValue.createPercentValue(100));
        //table.setFixedPosition(10,1,UnitValue.createPercentValue(100));
        process(table, "id;value;created on;updated on; data1; data2; data3; data4; data5; data6; data7; data8; data9", bold, true);
        //table.addCell(image);
        List<Transaction> myTransaction = (List<Transaction>) (transactionRepository.findAll());
        //Collection<Transaction> transactions = (Collection<Transaction>) transactionRepository.findAll();


        Long start = System.currentTimeMillis();

        for (Transaction transaction : myTransaction) {
            process(table, transaction.toString(), font, false);
        }
        System.out.println("###" + (System.currentTimeMillis() - start));
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

        /*String imFile = "D:\\pdf_demo\\src\\main\\resources\\logo2.png";
        ImageData data = ImageDataFactory.create(imFile);
        Image image = new Image(data);
        table.addCell(image);*/
    }

    private List<Transaction> makeCollection(Iterable<Transaction> iter) {
        List<Transaction> list = new ArrayList<Transaction>();
        for (Transaction item : iter) {
            list.add(item);
        }
        return list;
    }

}
