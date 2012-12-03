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

public class CosineSimilarityMeasure {

	public double calculate(double[] v1, double[] v2) {
		double a = getDotProduct(v1, v2);
		double b = getNorm(v1) * getNorm(v2);
		return a / b;
	}

	private double getDotProduct(double[] v1, double[] v2) {
		double sum = 0.0;
		for (int i = 0, n = v1.length; i < n; i++) {
			sum += v1[i] * v2[i];
		}
		return sum;
	}

	private double getNorm(double[] v) {
		double sum = 0.0;
		for (int i = 0, n = v.length; i < n; i++) {
			sum += v[i] * v[i];
		}
		return Math.sqrt(sum);
	}
}
