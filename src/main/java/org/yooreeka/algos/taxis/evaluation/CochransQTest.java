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
package org.yooreeka.algos.taxis.evaluation;

public class CochransQTest extends Test {

	private double q = 0.0;

	private ClassifierResults c1;
	private ClassifierResults c2;
	private ClassifierResults c3;

	private double L = 3.0;

	public CochransQTest(ClassifierResults c1, ClassifierResults c2,
			ClassifierResults c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;

		setStatisticSymbol("Q");

		// Confidence interval: 0.05
		// Null hypothesis: classifiers are the same
		// Degrees of freedom L - 1 = 2
		// Rejected if q > 5.991
		setThreshold(5.991);

		calculate();
	}

	@Override
	protected void calculate() {
		int n = c1.getN();

		/*
		 * Total number of correct classifications among all classifiers.
		 */
		double T = calculateT();

		double T2 = 0.0;

		for (int i = 0; i < n; i++) {
			double x = 0.0;

			if (c1.getResult(i)) {
				x++;
			}
			if (c2.getResult(i)) {
				x++;
			}
			if (c3.getResult(i)) {
				x++;
			}

			T2 += (x * x);
		}

		double sum = 0.0;
		sum = (double) c1.getNCorrect() * c1.getNCorrect()
				+ (double) c2.getNCorrect() * c2.getNCorrect()
				+ (double) c3.getNCorrect() * c3.getNCorrect();

		double a = L * sum;

		q = (L - 1) * (a - T * T) / (L * T - T2);

		setStatisticValue(q);
	}

	/*
	 * Calculates total number of correct classifications among all classifiers.
	 */
	private int calculateT() {
		return c1.getNCorrect() + c2.getNCorrect() + c3.getNCorrect();
	}

	// public boolean different() {
	// return isDifferent(q,getThreshold());
	// }
	//
	@Override
	public void evaluate() {
		print("_____________________________________________________");

		print("Evaluating classifiers " + c1.getClassifierId() + ", "
				+ c2.getClassifierId() + ", " + c3.getClassifierId() + ":");
		print("_____________________________________________________");
		print(c1.getClassifierId() + " accuracy: " + c1.getAccuracy());
		print(c2.getClassifierId() + " accuracy: " + c2.getAccuracy());
		print(c3.getClassifierId() + " accuracy: " + c3.getAccuracy());
		print("_____________________________________________________");

		print("Confidence Interval             : 0.05");
		print("Degrees of Freedom              : 2");
		print("Statistic threshold (chi-square): 5.991");

		// printResult("Q",q,different());
	}
}
