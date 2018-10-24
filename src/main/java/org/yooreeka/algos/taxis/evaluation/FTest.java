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

public class FTest extends Test {

	private double F = 0.0;

	private ClassifierResults c1;
	private ClassifierResults c2;
	private ClassifierResults c3;

	private double L = 3.0;

	public FTest(ClassifierResults c1, ClassifierResults c2,
			ClassifierResults c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;

		setStatisticSymbol("F");

		// For test size: 500
		// Confidence interval: 0.05
		// Null hypothesis: classifiers are the same
		// Degrees of freedom: L - 1 = 2, 2 * (N - 1) = 2 * 499 = 998
		// F Distribution
		// Rejected if F > 3.08
		//
		// Tabulated values can be found at:
		// http://www.itl.nist.gov/div898/handbook/eda/section3/eda3673.htm

		setThreshold(3.08);

		calculate();
	}

	@Override
	protected void calculate() {

		/*
		 * Classifier accuracies: <Number of Correct classifications> / N
		 */
		double p1 = c1.getAccuracy();
		double p2 = c2.getAccuracy();
		double p3 = c3.getAccuracy();

		/*
		 * Combined accuracy across all classifiers: T / (L * N)
		 */
		double p = calculateCombinedAccuracy();

		/*
		 * Number of test samples.
		 */
		double N = c1.getN();

		/*
		 * SSA
		 */
		double SSA = N * (p1 * p1 + p2 * p2 + p3 * p3 - L * p * p);

		/*
		 * SSB
		 */
		double sumOfjL2 = calculateSumOfjL2();
		double SSB = sumOfjL2 / L - L * N * p;

		/*
		 * SST
		 */
		double SST = N * L * p * (1 - p);

		/*
		 * SSAB
		 */
		double SSAB = SST - SSA - SSB;

		/*
		 * MSA
		 */
		double MSA = SSA / (L - 1);
		/*
		 * MSAB
		 */
		double MSAB = SSAB / ((L - 1) * (N - 1));

		/*
		 * F
		 */
		F = MSA / MSAB;

		setStatisticValue(F);
	}

	/*
	 * Accuracy based on combined results from all classifiers.
	 */
	private double calculateCombinedAccuracy() {
		double nCorrect = c1.getNCorrect() + c2.getNCorrect()
				+ c3.getNCorrect();
		double nAll = c1.getN() + c2.getN() + c3.getN();
		return nCorrect / nAll;
	}

	/*
	 * Calculates sum of jL squares. Where jL is the number of classifiers that
	 * correctly classified instance j.
	 */
	private double calculateSumOfjL2() {
		int n = c1.getN();

		double sumjL2 = 0.0;

		for (int j = 0; j < n; j++) {
			double jL = 0.0;

			if (c1.getResult(j)) {
				jL++;
			}
			if (c2.getResult(j)) {
				jL++;
			}
			if (c3.getResult(j)) {
				jL++;
			}

			sumjL2 += (jL * jL);
		}

		return sumjL2;
	}

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

		print("Confidence Interval     : 0.05");
		print("Degrees of Freedom (1st): 2");
		print("Degrees of Freedom (2nd): 39998");
		print("Statistic threshold     : 3.08");

		printResult();
	}

}
