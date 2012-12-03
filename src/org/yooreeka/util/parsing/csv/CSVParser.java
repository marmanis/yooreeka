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

import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DataEntry;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserException;
import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class CSVParser implements DocumentParser {

	/**
	 * 
	 */
	private CSVDocument d;

	private CSVFile csvFile;
	
	private long linesParsed = 0;

	/**
	 * 
	 */
	public CSVParser(CSVFile f) {
		this.csvFile = f;
	}

	@Override
	public DataEntry getDataEntry(int i) {
		return d.getCsvData().get(i);
	}

	public long getLinesParsed() {
		return linesParsed;
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

		d = new CSVDocument();

		linesParsed = 0;

		boolean hasMoreLines = true;
		String line;

		while (hasMoreLines) {

			line = bR.readLine();

			if (line == null) {

				hasMoreLines = false;

			} else {

				CSVEntry csvEntry = new CSVEntry(line, getSeparator());
				if (linesParsed == 0) {
					d.setHeaders(csvEntry); 
				} else {
					d.getCsvData().add(csvEntry);					
				}
				linesParsed++;
			}
		}

		return d;
	}

	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return csvFile.getSeparator();
	}
}
