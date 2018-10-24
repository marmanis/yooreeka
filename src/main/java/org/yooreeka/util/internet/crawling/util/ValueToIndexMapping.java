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
package org.yooreeka.util.internet.crawling.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps string values to an index. This class is used for mapping strings to
 * arrays or matrices. Index is zero-based.
 */
public class ValueToIndexMapping implements java.io.Serializable {
	/**
	 * Unique identifier for serialization
	 */
	private static final long serialVersionUID = -2077767183898369580L;

	/*
	 * Index value that will be returned for the next new string value.
	 */
	private int nextIndex = 0;

	/*
	 * Maintains mapping from value to index.
	 */
	private Map<String, Integer> valueMapping = new HashMap<String, Integer>();

	/*
	 * Maintains mapping from index to value.
	 */
	private Map<Integer, String> indexMapping = new HashMap<Integer, String>();

	public ValueToIndexMapping() {
		// empty
	}

	/**
	 * Returns index assigned to the value. For new values new index will be
	 * assigned and returned.
	 */
	public int getIndex(String value) {
		Integer index = valueMapping.get(value);
		if (index == null) {
			index = nextIndex;
			valueMapping.put(value, index);
			indexMapping.put(index, value);
			nextIndex++;
		}
		return index;
	}

	/**
	 * Current number of elements.
	 */
	public int getSize() {
		return valueMapping.size();
	}

	/**
	 * Returns value mapped to the index or null if mapping doesn't exist.
	 */
	public String getValue(int index) {
		return indexMapping.get(index);
	}
}
