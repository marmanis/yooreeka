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
package org.yooreeka.algos.clustering.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps object values to an index. Index is zero-based.
 */
public class ObjectToIndexMapping<T> implements java.io.Serializable {

	private static final long serialVersionUID = 2031098306406708902L;

	/*
	 * Index value that will be returned for the next new value.
	 */
	private int nextIndex = 0;

	/*
	 * Maintains mapping from object to index.
	 */
	private Map<T, Integer> objMapping = new HashMap<T, Integer>();

	/*
	 * Maintains mapping from index to value.
	 */
	private Map<Integer, T> indexMapping = new HashMap<Integer, T>();

	public ObjectToIndexMapping() {
		// empty
	}

	/**
	 * Returns index assigned to the value. For new values new index will be
	 * assigned and returned.
	 */
	public int getIndex(T value) {
		Integer index = objMapping.get(value);
		if (index == null) {
			index = nextIndex;
			objMapping.put(value, index);
			indexMapping.put(index, value);
			nextIndex++;
		}
		return index;
	}

	/**
	 * Returns value mapped to the index or null if mapping doesn't exist.
	 */
	public T getObject(int index) {
		return indexMapping.get(index);
	}

	/**
	 * Current number of elements.
	 */
	public int getSize() {
		return objMapping.size();
	}
}
