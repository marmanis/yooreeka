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
package org.yooreeka.algos.clustering.rock;

/**
 * Goodness measure for merging two clusters.
 */
public class MergeGoodnessMeasure {

	/*
	 * Threshold value that was used to identify neighbors among points.
	 */
	private double linkThreshold;

	/*
	 * Intermediate value that is used in calculation of goodness measure and
	 * stays the same for different clusters.
	 */
	private double p;

	public MergeGoodnessMeasure(double th) {
		this.linkThreshold = th;
		this.p = 1.0 + 2.0 * f(th);
	}

	/**
	 * This is just one of the possible implementations.
	 * 
	 * @param linkThreshold
	 *            threshold value that was used to identify neighbors among
	 *            points.
	 */
	private double f(double th) {

		/*
		 * This implementation assumes that linkThreshold was a threshold for
		 * similarity measure (as opposed to dissimilarity/distance).
		 */
		return (1.0 - th) / (1.0 + th);
	}

	public double g(int nLinks, int nX, int nY) {
		double a = Math.pow(nX + nY, p);
		double b = Math.pow(nX, p);
		double c = Math.pow(nY, p);

		return nLinks / (a - b - c);
	}

	/**
	 * @return the linkThreshold
	 */
	public double getTh() {
		return linkThreshold;
	}

	/**
	 * @param linkThreshold
	 *            the linkThreshold to set
	 */
	public void setTh(double th) {
		this.linkThreshold = th;
	}
}
