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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.yooreeka.util.parsing.common.DataField;
import org.yooreeka.util.parsing.common.DataType;

/**
 * 
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class CSVFile {

	private File file;

	private String separator;

	private CSVDocument doc;
	
	// Whether a CSV file has Headers
	private boolean hasHeaders;

	public CSVFile(String fileName, boolean hasHeaders, CSVSchema schema) {

		this.hasHeaders = hasHeaders;

		file = new File(fileName);
	}

	public CSVEntry getHeaders() {

		CSVEntry e = null;

		if (doc.hasHeaders()) {
			e = doc.getHeaders();
		}

		return e;
	}

	public boolean hasHeaders() {
		return hasHeaders;
	}

	public CSVDocument read() throws IOException {

		FileReader fReader = new FileReader(file);
		BufferedReader bReader = new BufferedReader(fReader);

		CSVParser csvParser = new CSVParser(this);
		doc = csvParser.parse(bReader);

		bReader.close();
		
		return doc;
	}

	/**
	 * @return the doc
	 */
	public CSVDocument getDoc() {
		return doc;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		CSVSchema s = new CSVSchema();

		DataField f1 = new DataField("Order Id", DataType.LONG);
		s.addColumn(f1);

		DataField f2 = new DataField("Order Status", DataType.STRING);
		s.addColumn(f2);

		DataField f3 = new DataField("Order  Amount", DataType.DOUBLE);
		s.addColumn(f3);

		DataField f4 = new DataField("Product Id", DataType.STRING);
		s.addColumn(f4);

		CSVFile f = new CSVFile(args[0], true, s);
		f.read();
	}

	/**
	 * @return the separatorChar
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separatorChar the separatorChar to set
	 */
	public void setSeparator(String val) {
		separator = val;
	}
}
