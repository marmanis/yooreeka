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
package org.yooreeka.algos.taxis.boosting;

import java.util.Random;

public class WeightBasedRandom {

	private double[] w;

	private Random rnd;

	/**
	 * Creates a new pseudorandom number generator. Distribution and range of
	 * numbers is defined by array of weights.
	 * 
	 * @param w
	 *            weights that define distribution. All weights should add up to
	 *            1.
	 */
	public WeightBasedRandom(double[] w) {
		this.w = w;
		this.rnd = new Random();
	}

	/*
	 * Returns next pseudorandom integer between 0 and w.length distributed
	 * according to weights.
	 */
	public int nextInt() {

		/*
		 * Pseudorandom, uniformly distributed double value between 0.0 and 1.0
		 */
		double x = rnd.nextDouble();

		double cdf = 0.0;

		int y = 0;

		for (int i = 0, n = w.length; i < n; i++) {
			cdf = cdf + w[i];
			y = i;
			if (cdf >= x) {
				break;
			}
		}

		return y;
	}

}
