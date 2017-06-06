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
		Elements fontElements = doc.select("font");
		String errorColor = "#ff0000";
		String warningColor = "#ffd000";
		String color;
		String colorMemory = "";
		String startTime = "", endTime = "";
		String processFileName = "";
		
		boolean startMetaData = true, finishMetaData = true; 
		
		
		ArrayList<Report> errorList = new ArrayList<Report>();
		ArrayList<Report> warningList = new ArrayList<Report>();
		
		
		ListIterator li = fontElements.listIterator();
		
		
		while(li.hasNext()) {
			Element e = (Element) li.next();
			
			String text = e.text();
			
			color = e.attr("color");
			
			if(color.equals(errorColor) || color.equals(warningColor)) {
				colorMemory = color;
			}
			
			if(startMetaData && getId(text, "meta") != null) {
				startTime = getMask(text);
				processFileName = getId(text, "meta");
				System.out.println(startTime);
				System.out.println(processFileName);
				
				startMetaData = false;
			}
			
			
			if(text.startsWith("Line ") && color.equals("#000000")) {
				
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
				
				report.setFileName(processFileName);
				report.setStartTime(startTime);
				report.setEndingTime(endTime);
				report.setStatement(text);
				
				if(colorMemory.equals(errorColor)) {
					report.setReportType("Error");
					report.setDescription(description);
					errorList.add(report);
				} else if(colorMemory.equals(warningColor)) {
					report.setReportType("Warning");
					warningList.add(report);
				}
				
				
			} else if(finishMetaData && text.contains("Import completed (")) {
				endTime = getMask(text);
				System.out.println(endTime);
				
				finishMetaData = false;
			}
			
			//if(i == 300) break;
			//i++;
		}
		
		ExcelExporter errorExporter = new ExcelExporter("Error", endTime);
		errorExporter.export(errorList);
		ExcelExporter warningExporter = new ExcelExporter("Warning", endTime);
		warningExporter.export(warningList);
	}
	
	private String getId(String text, String type) {
		
		
		int placeholder;
		int lastIndex;
		
		if(!type.equals("meta")) {
			placeholder = text.lastIndexOf(type + " with ID '");
			lastIndex = placeholder + type.length() + 10;
		} else {
			placeholder = text.lastIndexOf("Processing the file)(s) '");
			lastIndex = placeholder + 25;
		}
		
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
		String lastDescr = null;
		while( m.find() ) {
			lastDescr = m.group(1);
		}
		return lastDescr;
	}
	
}
