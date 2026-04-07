package com.mphasis.employeeservice.controller;


import com.lowagie.text.*;
import java.awt.Color;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mphasis.employeeservice.dto.EmployeeResponseDTO;
import com.mphasis.employeeservice.service.EmployeeService;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStreamWriter;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/export")
public class ExportController {

    private final EmployeeService employeeService;

    public ExportController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/csv/{table}")
    public void exportCsv(@PathVariable String table, HttpServletResponse response) throws Exception {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + table + ".csv");

        List<EmployeeResponseDTO> rows = employeeService.getEmployees();

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(response.getOutputStream()))) {

            if (!rows.isEmpty()) {
                // header
                writer.writeNext(new String[]{"ID", "Name", "Email", "Phone Number", "Address", "Date of Birth"});


                // writer.writeNext(rows.get(0).keySet().toArray(new String[0]));

                // rows
                for (EmployeeResponseDTO emp : rows) {
                    writer.writeNext(new String[]{
                            String.valueOf(emp.getId()),
                            emp.getName(),
                            emp.getEmail(),
                            emp.getPhNo(),
                            emp.getAddress(),
                            emp.getDateOfBirth()
                    });
                }
            }

            writer.flush();
        }
    }


    @GetMapping("/excel/{table}")
    public void exportExcel(@PathVariable String table, HttpServletResponse response) throws Exception {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + table + ".xlsx");

        List<EmployeeResponseDTO> rows = employeeService.getEmployees();

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Employees");

            int rowIndex = 0;

            // header row
            Row header = sheet.createRow(rowIndex++);

            String[] columns = {"ID", "Name", "Email", "Phone Number", "Address", "Date of Birth"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);

            }

            // Data rows
            for (EmployeeResponseDTO emp : rows) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(emp.getId());
                row.createCell(1).setCellValue(emp.getName());
                row.createCell(2).setCellValue(emp.getEmail());
                row.createCell(3).setCellValue(emp.getPhNo());
                row.createCell(4).setCellValue(emp.getAddress());
                row.createCell(5).setCellValue(emp.getDateOfBirth());
            }


            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());

        }
    }

    @GetMapping("/pdf/{table}")
    public void exportPdf(@PathVariable String table, HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + table + ".pdf");

        List<EmployeeResponseDTO> rows = employeeService.getEmployees();

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Title
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Employee Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // spacing

        // Table (6 columns)
        PdfPTable tablePdf = new PdfPTable(6);
        tablePdf.setWidthPercentage(100);

        // Headers
        String[] headers = {"ID", "Name", "Email", "Phone Number", "Address", "Date of Birth"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            tablePdf.addCell(cell);
        }

        // Data rows
        for (EmployeeResponseDTO emp : rows) {
            tablePdf.addCell(String.valueOf(emp.getId()));
            tablePdf.addCell(emp.getName());
            tablePdf.addCell(emp.getEmail());
            tablePdf.addCell(emp.getPhNo());
            tablePdf.addCell(emp.getAddress());
            tablePdf.addCell(emp.getDateOfBirth());
        }

        document.add(tablePdf);

        document.close();
    }
}
