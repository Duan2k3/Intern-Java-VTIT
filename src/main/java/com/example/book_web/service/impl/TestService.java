package com.example.book_web.service.impl;

import com.example.book_web.entity.Book;
import com.example.book_web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestService {

    private final BookRepository bookRepository;

    public byte[] exportBooksToPdf() throws JRException {
        List<Book> books = bookRepository.findAll();

        // Load file jrxml từ resources
        InputStream reportStream = getClass().getResourceAsStream("/reports/Book.jrxml");
        if (reportStream == null) {
            throw new RuntimeException("Không tìm thấy file reports/Book.jrxml");
        }

        // Compile report
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(books);

        Map<String, Object> parameters = new HashMap<>();

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export ra byte[]
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
