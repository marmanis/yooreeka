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

import org.yooreeka.util.parsing.common.DataEntry;

/**
 * A <tt>CSVEntry</tt> is simply an array of <tt>String</tt>s. The default
 * separator is the comma character, i.e. ",".
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class CSVEntry extends DataEntry {

	public static final String DEFAULT_SEPARATOR = ",";
	private String separator;
	
	private String[] data;

	public CSVEntry(String csvLine) {
		this(csvLine,null);
	}
	
	public CSVEntry(String csvLine, String sepChar) {

		if (sepChar == null) {
			setSeparator(CSVEntry.DEFAULT_SEPARATOR);
		} else {
			setSeparator(sepChar);
		}
		
		data = csvLine.trim().split(getSeparator());
	}

	public String getEntryAt(int i) {
		return data[i];
	}
	
	public String[] getData() {
		return data;
	}

	@Override
	public DataEntry getDataEntry() {

		return this;
	}

	@Override
	public String toString() {

		return toString(CSVEntry.DEFAULT_SEPARATOR);
	}

	public String toString(String printSeparator) {
		StringBuilder sb = new StringBuilder();
		int i=1;

		for (String s : data) {
			if (i<data.length) {
				sb.append(s).append(printSeparator);
			} else {
				sb.append(s);
			}
			i++;
		}
		return sb.toString();
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
	public void setSeparator(String separatorChar) {
		this.separator = separatorChar;
	}
}
