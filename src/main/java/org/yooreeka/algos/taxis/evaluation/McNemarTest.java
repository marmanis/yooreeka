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

public class McNemarTest extends Test {

	private double chi2 = 0.0;

	private ClassifierResults c1;
	private ClassifierResults c2;

	/*
	 * Using 'n??' notation. First '?' represents result for first classifier.
	 * Second '?' represents result for the second classifier. 0 -
	 * misclassification, 1 - correct classification.
	 */

	private int n11 = 0; // both classifiers were correct
	private int n10 = 0; // first is correct, second incorrect
	private int n01 = 0; // first incorrect, second correct
	private int n00 = 0; // both incorrect

	public McNemarTest(ClassifierResults c1, ClassifierResults c2) {
		this.c1 = c1;
		this.c2 = c2;

		setStatisticSymbol("Chi^2");

		// using level of significance 0.05, 1 degree of freedom:
		// reject null hypothesis if chi2 > 3.841
		setThreshold(3.841);

		calculate();
	}

	@Override
	protected void calculate() {
		int n = c1.getN();

		for (int i = 0; i < n; i++) {
			if (c1.getResult(i) && c2.getResult(i)) {
				n11++;
			} else if (c1.getResult(i) && !c2.getResult(i)) {
				n10++;
			} else if (!c1.getResult(i) && c2.getResult(i)) {
				n01++;
			} else {
				n00++;
			}
		}

		double a = Math.abs(n01 - n10) - 1;
		chi2 = a * a / (n01 + n10);

		setStatisticValue(chi2);
	}

	@Override
	public void evaluate() {

		print("_____________________________________________________");
		print("Evaluating classifiers " + c1.getClassifierId() + " and "
				+ c2.getClassifierId() + ":");

		print("_____________________________________________________");
		print(c1.getClassifierId() + " accuracy: " + c1.getAccuracy());
		print(c2.getClassifierId() + " accuracy: " + c2.getAccuracy());
		print("N = " + c1.getN() + ", n00=" + n00 + ", n10=" + n10 + ", n01="
				+ n01 + ", n11=" + n11);
		print("_____________________________________________________");

		print("Confidence Interval             : 0.05");
		print("Degrees of Freedom              : 1");
		print("Statistic threshold (Chi-square): 3.841");

		printResult();
	}

	public int getN00() {
		return n00;
	}

	public int getN10() {
		return n10;
	}

	public int getN11() {
		return n11;
	}
}
