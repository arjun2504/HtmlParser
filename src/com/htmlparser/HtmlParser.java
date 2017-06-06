package com.htmlparser;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class HtmlParser {
	
	private JFrame parserPromptWindow;
	private JPanel mainPanel, titlePanel, htmlReaderPanel, actionPanel;
	private JLabel titleLabel, innerTitle;
	private JButton htmlBrowser;
	
	public HtmlParser() {
		showParser();
	}
	
	private void showParser() {
		
		parserPromptWindow = new JFrame("HTML Parser");
        parserPromptWindow.setSize(600, 300);
        parserPromptWindow.setLocationRelativeTo(null);
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(border);
        
        titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        innerTitle = new JLabel("HTML Parser", SwingConstants.CENTER);
        titlePanel.add(innerTitle, BorderLayout.NORTH);
        
        htmlReaderPanel = new JPanel();
        htmlReaderPanel.setLayout(new BorderLayout());
        htmlBrowser = new JButton();
        htmlReaderPanel.add(htmlBrowser);
        
        
        mainPanel.add(titlePanel);
        mainPanel.add(htmlReaderPanel);
        parserPromptWindow.add(mainPanel);
        parserPromptWindow.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		//new HtmlParser();
		ParseEngine pe = new ParseEngine("sample.html");
		pe.parser();
	}
	
}
