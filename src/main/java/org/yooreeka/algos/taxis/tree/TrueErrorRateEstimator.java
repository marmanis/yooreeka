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
package org.yooreeka.algos.taxis.tree;

/*
 * Estimates true error rate for tree pruning. Based on 
 * heuristic for C4.5. 
 */
public class TrueErrorRateEstimator {

	/*
	 * Default value.
	 */
	private double z = 0.69; // for confidence: 0.25 or 25%

	/**
	 * Calculates true error rate for a node using error observed on training
	 * data. C4.5 uses upper confidence limit for error rate to represent true
	 * error rate.
	 * 
	 * @param n
	 *            total number of training samples at the node
	 * @param e
	 *            number of misclassified samples at the node
	 * @return
	 */
	public double errorRate(double n, double e) {
		/*
		 * Observed error rate based on our training data.
		 */
		double oe = e / n;

		/*
		 * Calculating upper confidence limit to use an estimate of the error
		 * rate
		 */
		double tmp1 = oe / n - (oe * oe) / n + (z * z) / (4 * n * n);
		double numerator = oe + (z * z) / (2 * n) + z * Math.sqrt(tmp1);
		double denominator = 1 + (z * z) / n;

		return numerator / denominator;
	}
}
