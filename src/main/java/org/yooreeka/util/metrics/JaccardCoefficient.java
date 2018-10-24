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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculates Jaccard coefficient for two sets of items.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 */
public class JaccardCoefficient implements SimilarityMeasure {

	private static final long serialVersionUID = -5051498381470492495L;

	public JaccardCoefficient() {
		// empty
	}

	public double similarity(List<String> x, List<String> y) {

		if (x.size() == 0 || y.size() == 0) {
			return 0.0;
		}

		Set<String> unionXY = new HashSet<String>(x);
		unionXY.addAll(y);

		Set<String> intersectionXY = new HashSet<String>(x);
		intersectionXY.retainAll(y);

		return (double) intersectionXY.size() / (double) unionXY.size();
	}
	
	public double similarity(String[] x, String[] y) {
		double sim = 0.0d;
		if ((x != null && y != null) && (x.length > 0 || y.length > 0)) {
			sim = similarity(Arrays.asList(x), Arrays.asList(y));
		} else {
			throw new IllegalArgumentException(
					"The arguments x and y must be not NULL and either x or y must be non-empty.");
		}
		return sim;
	}

}
