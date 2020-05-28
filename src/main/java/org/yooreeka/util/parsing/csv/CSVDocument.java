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

import java.util.ArrayList;
import java.util.ListIterator;

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * A <tt>CSVDocument</tt> is an <tt>ArrayList</tt> of <tt>CSVEntry</tt>s
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class CSVDocument extends ProcessedDocument {

	private CSVEntry headers;
	
	private boolean hasHeaders;
	private ArrayList<CSVEntry> csvData;
	
	private CSVSchema schema;

	public CSVDocument() {
		csvData = new ArrayList<CSVEntry>();
	}
	
	public CSVDocument(ArrayList<CSVEntry> data) {
		csvData = data;
	}

	public CSVEntry getHeaders() {
		return headers;
	}
	
	public boolean hasHeaders() {
		return	hasHeaders;
	}
	
	public void hasHeaders(boolean val) {
		hasHeaders = val;
	}

	/**
	 * @return the csvData
	 */
	public ArrayList<CSVEntry> getCsvData() {
		return csvData;
	}
		
	public void print(String printSeparator) {
		if (hasHeaders()) {
			P.hline();
			P.println(getHeaders().toString(printSeparator));
		}
		P.hline();

		ListIterator<CSVEntry> elements = csvData.listIterator();
		while (elements.hasNext()) {
			CSVEntry e = elements.next();
			P.println(e.toString(printSeparator));
		}
		P.hline();
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(CSVEntry headers) {
		this.headers = headers;
	}

	/**
	 * @return the schema
	 */
	public CSVSchema getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(CSVSchema schema) {
		this.schema = schema;
	}
}
