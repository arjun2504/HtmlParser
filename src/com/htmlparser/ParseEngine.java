package com.htmlparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseEngine {
	
	private String filePath;
	
	ParseEngine(String inputFile) {
		this.filePath = inputFile;
	}
	
	public void parser() throws IOException {
		
		File file = new File(this.filePath);
		Document doc = Jsoup.parse(file, "UTF-8");
		Elements newsHeadlines = doc.select("font");
		int errorFlag = 0;	//red
		String errorColor = "red", warningColor = "#ffd000";
		int warningFlag = 0;	//#ffd000
		String color, colorMemory = "red";
		
		
		ArrayList<Report> errorList = new ArrayList<Report>();
		ArrayList<Report> warningList = new ArrayList<Report>();
		
		String errorType = "Error";
		
		ListIterator li = newsHeadlines.listIterator();
		int i = 0;
		
		while(li.hasNext()) {
			Element e = (Element) li.next();
			String text = e.text();
			
			color = e.attr("color");
			if(color.equals(errorColor) || color.equals(warningColor)) {
				colorMemory = color;
			}
			
			if(text.startsWith("Line ")) {
				
				int lineNumber = getLineNumber(text);
				String attributeId = getId(text, "attribute");
				String entityId = getId(text, "entity");
				String productId = getId(text, "product");
				String description = getMask(text);
				
				Report report = new Report();
				report.setLineNumber(lineNumber);
				if(attributeId != null) report.setAttributeId(attributeId);
				if(entityId != null) report.setEntityId(entityId);
				if(productId != null) report.setProductId(productId);
				
				if(colorMemory.equals(errorColor)) {
					report.setReportType("Error");
					report.setDescription(description);
					errorList.add(report);
				} else if(colorMemory.equals(warningColor)) {
					report.setReportType("Warning");
					warningList.add(report);
				}
				
				
				
				
			}
			
			if(i == 300) break;
			i++;
		}
		
		ExcelExporter errorExporter = new ExcelExporter("Error");
		errorExporter.export(errorList);
		ExcelExporter warningExporter = new ExcelExporter("Warning");
		warningExporter.export(warningList);
	}
	
	private String getId(String text, String type) {
		
		int placeholder = text.lastIndexOf(type + " with ID '");
		int lastIndex = placeholder + type.length() + 10;
		
		String attributeId = null;
		
		if(placeholder != -1) {
		
			int finalIndex = lastIndex + 1;
			
			for(int i=lastIndex+1; i<text.length(); i++) {
				if(text.charAt(i) == '\'') {
					finalIndex = i;
					break;
				}
			}
			
			attributeId = text.substring(lastIndex, finalIndex);
		}
		
		return attributeId;
	}
	
	private int getLineNumber(String text) {
		
		//5 is the starting index
		int index = 5;
		char nowChar = text.charAt(index);
		String lineNumber = "";
		
		while( Character.isDigit(nowChar) ) {
			
			lineNumber += text.charAt(index);
			
			index++;
			nowChar = text.charAt(index);
		}
		
		return Integer.parseInt(lineNumber);
	}
	
	public String getMask(String text) {
		Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(text);
		m.find();
		return m.group(1);
	}
	
}
