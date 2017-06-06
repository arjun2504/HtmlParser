package com.htmlparser;

import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HtmlParser {
	
	private JFrame mainFrame;
	private JPanel mainPanel, titlePanel, browserPanel, actionPanel;
	private JLabel titleLabel;
	
	public HtmlParser() {
		showParser();
	}
	
	private void showParser() {
		
		mainFrame = new JFrame("HTML Parser");
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		
		
		mainFrame.add(mainPanel);
		mainFrame.setVisible(true);
		mainFrame.setSize(600, 300);
	}

	public static void main(String[] args) throws IOException {
		//new HtmlParser();
		ParseEngine pe = new ParseEngine("sample.html");
		pe.parser();
	}
	
}
