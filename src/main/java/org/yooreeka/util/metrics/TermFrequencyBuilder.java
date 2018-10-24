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
package org.yooreeka.util.metrics;

import java.util.HashMap;
import java.util.Map;

public class TermFrequencyBuilder {

	/**
	 * Calculates term frequency vectors based on two sets of terms.
	 */
	public static double[][] buildTermFrequencyVectors(String[] x, String[] y) {

		// create a set of terms with flags
		Map<String, Integer> allAttributes = new HashMap<String, Integer>();
		for (String s : x) {
			// set flags to indicate that this term is present only in x[]
			allAttributes.put(s, 0x01);
		}
		for (String s : y) {
			if (!allAttributes.containsKey(s)) {
				// set flags to indicate that this term is present only in y[]
				allAttributes.put(s, 0x02);
			} else {
				// set flags to indicate that this term is present in x[] and
				// y[]
				allAttributes.put(s, 0x03);
			}
		}

		// create term frequency vectors
		int n = allAttributes.size();
		double[] termFrequencyForX = new double[n];
		double[] termFrequencyForY = new double[n];
		int i = 0;
		for (Map.Entry<String, Integer> e : allAttributes.entrySet()) {
			// 0x01 - x[] only ,
			// 0x02 - y[] only,
			// 0x03 - x[] and y[]
			int flags = e.getValue();
			termFrequencyForX[i] = flags & 0x01;
			termFrequencyForY[i] = flags >> 1;
			i++;
		}

		return new double[][] { termFrequencyForX, termFrequencyForY };
	}

}
