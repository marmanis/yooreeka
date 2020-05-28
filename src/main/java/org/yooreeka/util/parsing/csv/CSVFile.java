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

import org.yooreeka.util.C;
import org.yooreeka.util.P;

/**
 * TODO: If the <tt>CSVFile</tt> has headers then we can infer the fields and their order.
 * The problem with automatically defining the fields is that the data field type can be ill-defined
 * or insufficient (e.g. knowing that a data field is an email address can help us apply additional validations) 
 * or inefficient (e.g. integers represented as doubles). 
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class CSVFile {

	private boolean isVerbose=false;
	
	private File file;

	private String separator;

	private CSVDocument doc;
	
	private CSVSchema schema;
	
	// Whether a CSV file has Headers
	private boolean hasHeaders;

	public CSVFile(String fileName, boolean hasHeaders, CSVSchema schema) {

		this.schema = schema;
		
		this.hasHeaders = hasHeaders;

		file = new File(fileName);
		
		//Initialize the document that corresponds to the file
		doc = new CSVDocument();
		doc.setSchema(schema);
	}

	public CSVEntry getHeaders() {

		CSVEntry e = null;

		if (doc.hasHeaders()) {
			e = doc.getHeaders();
		}

		return e;
	}
	
	public void printHeaders() {
		P.hline();
		CSVEntry e = getHeaders();
		if (e != null)
			P.println(e.toString());
		P.hline();
	}

	public boolean hasHeaders() {
		return hasHeaders;
	}

	public CSVDocument read() throws IOException {
		return read(C.ZERO_INT);
	}
	
	public CSVDocument read(int skipRows) throws IOException {

		FileReader fReader = new FileReader(file);
		BufferedReader bReader = new BufferedReader(fReader);

		CSVParser csvParser = new CSVParser(this);
		csvParser.skipRows(skipRows);
		csvParser.parse(bReader);
		
		bReader.close();
		
		return doc;
	}

	/**
	 * @return the doc
	 */
	public CSVDocument getDoc() {
		return doc;
	}

	public CSVEntry query(int id) {
		return doc.getCsvData().get(id);
	}
	
	
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		CSVSchema s = new CSVSchema();
//
//		DataField f1 = new DataField("Order Id", DataType.LONG);
//		s.addColumn(f1);
//
//		DataField f2 = new DataField("Order Status", DataType.STRING);
//		s.addColumn(f2);
//
//		DataField f3 = new DataField("Order  Amount", DataType.DOUBLE);
//		s.addColumn(f3);
//
//		DataField f4 = new DataField("Product Id", DataType.STRING);
//		s.addColumn(f4);
//
//		CSVFile f = new CSVFile(args[0], true, s);
//		f.read();
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

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
	public void isVerbose(boolean val) {
		isVerbose=val;
	}
	
	public boolean isVerbose() {
		return isVerbose;
	}

	/**
	 * @return the schema
	 */
	public CSVSchema getSchema() {
		return schema;
	}
}
