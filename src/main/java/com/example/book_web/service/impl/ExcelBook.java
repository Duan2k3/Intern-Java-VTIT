package com.example.book_web.service.impl;

import com.example.book_web.entity.Book;
import com.example.book_web.entity.User;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.CategoryRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class ExcelBook {

    private XSSFWorkbook workbook;
    private Sheet sheet;
    private List<Book> books;

    public ExcelBook(List<Book> books) {
        this.books = books;
        workbook = new XSSFWorkbook();
    }



    public void exportBook(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID","Title","Author","Description","Created_At","Quantity"};
        for(int i = 0 ; i < columns.length ; i++){
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);

        }
        int rowNum = 1;
        for (Book book : books){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(book.getId());
            row.createCell(1).setCellValue(book.getTitle());
            row.createCell(2).setCellValue(book.getAuthors());
            row.createCell(3).setCellValue(book.getDescription());
            row.createCell(4).setCellValue(book.getCreatedAt().toString());
            row.createCell(5).setCellValue(book.getQuantity());

        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=books.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();


    }

}
