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

public class CosineSimilarity implements SimilarityMeasure {

	private static final long serialVersionUID = -3470234210362615980L;

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

	public double sim(double[] v1, double[] v2) {
		double a = getDotProduct(v1, v2);
		double b = getNorm(v1) * getNorm(v2);
		return a / b;
	}

	/**
	 * Calculates cosine similarity between two sets of terms by converting them
	 * into term frequency vectors. It should be clear that, unlike numerical
	 * vectors, the definition of this similarity is to a large extent
	 * arbitrary.
	 */
	public double similarity(String[] x, String[] y) {

		double[][] termFrequencyVectors = TermFrequencyBuilder
				.buildTermFrequencyVectors(x, y);

		double[] termFrequencyForX = termFrequencyVectors[0];
		double[] termFrequencyForY = termFrequencyVectors[1];

		return sim(termFrequencyForX, termFrequencyForY);
	}

}
