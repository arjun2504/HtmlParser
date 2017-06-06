package com.htmlparser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ListIterator;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelExporter {
	
//	HashMap<String, String> data = new HashMap<String, String>();
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	HSSFRow headingRow;
	String fileName;
	String reportType;
	String endTime;
	
	ExcelExporter(String reportType, String endTime) {
		this.reportType = reportType;
		this.endTime = endTime;
		
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet();
		headingRow = sheet.createRow(0);
		String[] columnHeading = { "S.No.", "Line", "Entity ID", 
				"Product ID", "Attribute ID", "Type", 
				this.reportType + " Description", "Start Time",
				"End Time", "File Name", reportType
				};
		
		for(int i=0; i<11; i++) {
			headingRow.createCell(i).setCellValue(columnHeading[i]);
		}
		
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh.mm");
		Date date = new Date();
		this.fileName = reportType + " Report " + df.format(date) + ".xls";
		
	}
	
	public void export(ArrayList<Report> reportList) {
		int rowNum = 1;
		ListIterator<Report> li = reportList.listIterator();
		while(li.hasNext()) {
			Report report = (Report) li.next();
			
			HSSFRow row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(rowNum);
			row.createCell(1).setCellValue("Line " + report.getLineNumber());
			row.createCell(2).setCellValue(report.getEntityId());
			row.createCell(3).setCellValue(report.getProductId());
			row.createCell(4).setCellValue(report.getAttributeId());
			row.createCell(5).setCellValue(report.getReportType());
			row.createCell(6).setCellValue(report.getDescription());
			row.createCell(7).setCellValue(report.getStartTime());
			row.createCell(8).setCellValue(this.endTime);
			row.createCell(9).setCellValue(report.getFileName());
			row.createCell(10).setCellValue(report.getStatement());
		
			rowNum++;
		}
		
		save();
	}

	private void save() {
		
		try {
			FileOutputStream fos = new FileOutputStream(this.fileName);
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
