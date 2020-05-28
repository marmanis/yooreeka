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
package org.yooreeka.util.parsing.common;

import java.io.Serializable;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class DataField implements Serializable {

	private static final long serialVersionUID = 795910732043254883L;
	
	private String name;
	private DataType dataType;
	private int orderIndex;
	private boolean isOrderIndexSet=false;
	
	/**
	 * @return the orderIndex
	 */
	public int getOrderIndex() {
		return orderIndex;
	}
	
	public void setOrderIndex(int val) throws Exception {
		if (!isOrderIndexSet) { 	
			this.orderIndex = val;
			isOrderIndexSet = true;
		} else {
			throw new Exception("APPLICATION ERROR: You cannot reset the order of the field!");
		}
	}

	private boolean isPrimaryKey=false;

	public DataField(String name, DataType dataType) {
		this.name = name;
		this.dataType = dataType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getName() {
		return name;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean validate(String s) {
		boolean isValid = true;

		return isValid;
	}
	
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	
	public void setAsPrimaryKey() {
		isPrimaryKey=true;
	}	
}
