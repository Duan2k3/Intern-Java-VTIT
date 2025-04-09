package com.example.book_web.service.impl;

import com.example.book_web.entity.Book;
import com.example.book_web.entity.Category;
import com.example.book_web.repository.BookRepository;
import com.example.book_web.repository.CategoryRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelExporter {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public ExcelExporter(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public void exportBooks(HttpServletResponse response) throws IOException {
        List<Book> books = bookRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Book List");
        writeHeader(sheet, workbook);
        writeData(sheet, books);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=books.xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeHeader(Sheet sheet, Workbook workbook) {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        String[] headers = {"ID", "Title", "Author", "Description", "Created At", "Quantity"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private void writeData(Sheet sheet, List<Book> books) {
        int rowCount = 1;


        for (Book book : books) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(book.getId());
            row.createCell(1).setCellValue(book.getTitle());
            row.createCell(2).setCellValue(book.getAuthors());
            row.createCell(3).setCellValue(book.getDescription());
            row.createCell(4).setCellValue(book.getCreatedAt().toString());
            row.createCell(5).setCellValue(book.getQuantity());
        }
    }

    public void ImportToExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() == 0) continue;

            Book book = new Book();


            book.setTitle(getCellValue(row.getCell(1)));


            book.setAuthors(getCellValue(row.getCell(2)));


            book.setDescription(getCellValue(row.getCell(3)));


            Cell quantityCell = row.getCell(5);
            if (quantityCell != null) {
                switch (quantityCell.getCellType()) {
                    case NUMERIC:
                        book.setQuantity((int) quantityCell.getNumericCellValue());
                        break;
                    case STRING:
                        try {
                            book.setQuantity(Integer.parseInt(quantityCell.getStringCellValue().trim()));
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid quantity in row " + row.getRowNum() + ": " + quantityCell.getStringCellValue());
                            book.setQuantity(0);
                        }
                        break;
                    case BLANK:
                        book.setQuantity(0); // Default for blank cells
                        break;
                    default:
                        System.err.println("Unexpected cell type for quantity in row " + row.getRowNum() + ": " + quantityCell.getCellType());
                        book.setQuantity(0);
                }
            } else {
                System.err.println("Quantity cell missing in row " + row.getRowNum());
                book.setQuantity(0); // Default if cell is null
            }


        Cell categoriesCell = row.getCell(6);
        if (categoriesCell != null && categoriesCell.getCellType() == CellType.STRING) {
            String categoriesString = categoriesCell.getStringCellValue();
            List<Long> categoryIds = List.of(categoriesString.split(","))
                    .stream()
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            book.setCategories(categories);
        }

        bookRepository.save(book);
        }

        workbook.close();
        inputStream.close();
    }


    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
