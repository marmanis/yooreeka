/*
 *   ________________________________________________________________________________________
 *   
 *   Y O O R E E K A
 *   A library for data mining, machine learning, soft computing, and mathematical analysis
 *   ________________________________________________________________________________________ 
 *    
 *   The Yooreeka project started with the code of the book "Algorithms of the Intelligent Web " 
 *   (Manning 2009). Although the term "Web" prevailed in the title, in essence, the algorithms 
 *   are valuable in any software application.
 *  
 *   Copyright (c) 2007-2009 Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-${year} Marmanis Group LLC and individual contributors as indicated by the @author tags.  
 * 
 *   Certain library functions depend on other Open Source software libraries, which are covered 
 *   by different license agreements. See the NOTICE file distributed with this work for additional 
 *   information regarding copyright ownership and licensing.
 * 
 *   Marmanis Group LLC licenses this file to You under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License.  
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under 
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *   either express or implied. See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package org.yooreeka.util.parsing.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DataEntry;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserException;
import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * 
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class CSVParser implements DocumentParser {

	private CSVFile csvFile;
	
	private boolean verbose=false;	

	private long linesParsed = 0;
	private long linesRead = 0;
	private int lines2Skip=0;

	/**
	 * 
	 */
	public CSVParser(CSVFile f) {
		this.csvFile = f;
	}

	@Override
	public ProcessedDocument parse(AbstractDocument abstractDocument)
			throws DocumentParserException {
		ProcessedDocument processedDocument = null;
		String content = new String(abstractDocument.getDocumentContent(),
				Charset.forName(abstractDocument.getContentCharset()));
		BufferedReader reader = new BufferedReader(new StringReader(content));
		try {
			abstractDocument = parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return processedDocument;
	}

	/**
	 * 
	 * @param bR
	 * @return
	 * @throws IOException
	 */
	public CSVDocument parse(BufferedReader bR) throws IOException {

		long t0 = System.currentTimeMillis();
		
		StringBuilder msg = null;
		if (csvFile.isVerbose()) 	
			msg= new StringBuilder("\nProcessed ");
		
		linesParsed = 0;

		boolean hasMoreLines = true;
		String line;

		while (hasMoreLines) {

			line = bR.readLine();

			if (line == null || line.trim().length() == 0) {

				hasMoreLines = false;

			} else {

				if (linesParsed < lines2Skip) {
					// Skip this line
				} else {
					CSVEntry csvEntry = new CSVEntry(line, getSeparator());
					if (linesRead == 0 && csvFile.hasHeaders()) {
						csvFile.getDoc().setHeaders(csvEntry);
						
						if (isVerbose())
							P.print(csvEntry.toString());
						
					} else {
						if (csvEntry.getData().length != csvFile.getSchema().getNumberOfFields()) {
							
							if (isVerbose()) 
								P.println(csvEntry.toString());
							
						}
						csvFile.getDoc().getCsvData().add(csvEntry);
					}
					linesRead++;
				}
				linesParsed++;
			}
		}
		
		if (csvFile.isVerbose()) {
			msg.append(linesParsed).append(" lines from file: ").append(csvFile.getFile().getAbsolutePath());
			msg.append("\n in "+(System.currentTimeMillis()-t0)+"ms\n");
			P.println(msg.toString());
		}
		
		return csvFile.getDoc();
	}

	@Override
	public DataEntry getDataEntry(int i) {
		return csvFile.getDoc().getCsvData().get(i);
	}

	/**
	 * @return the number of lines parsed
	 */
	public long getLinesParsed() {
		return linesParsed;
	}

	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return csvFile.getSeparator();
	}
	
	/**
	 * @param val sets the number of rows that must be skipped
	 */
	public void skipRows(int val) {
		lines2Skip = val;
	}
	
	/** 
	 * @return the number of lines read
	 */
	public long getLinesRead() {
		return linesRead;
	}
	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * @return the csvFile
	 */
	public CSVFile getCsvFile() {
		return csvFile;
	}

	/**
	 * @return the lines2Skip
	 */
	public int getLines2Skip() {
		return lines2Skip;
	}
}
