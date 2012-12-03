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

public class Diff2PropTest extends Test {

	double z = 0.0;

	private ClassifierResults c1;
	private ClassifierResults c2;

	public Diff2PropTest(ClassifierResults c1, ClassifierResults c2) {
		this.c1 = c1;
		this.c2 = c2;

		setStatisticSymbol("|z|");

		/*
		 * Confidence interval: 0.05 Null hypothesis - classifiers are the same
		 * Null hypothesis is rejected if |z| > 1.96
		 */
		setThreshold(1.96);

		calculate();
	}

	@Override
	protected void calculate() {

		double n = c1.getN();
		double p = 0.5 * (c1.getAccuracy() + c2.getAccuracy());
		double a = c1.getAccuracy() - c2.getAccuracy();
		double b = (2.0 * p * (1 - p)) / n;
		z = a / Math.sqrt(b);

		setStatisticValue(Math.abs(z));
	}

	@Override
	public void evaluate() {

		print("_____________________________________________________");
		print("Evaluating classifiers " + c1.getClassifierId() + " and "
				+ c2.getClassifierId() + ":");

		print("_____________________________________________________");
		print(c1.getClassifierId() + " accuracy: " + c1.getAccuracy());
		print(c2.getClassifierId() + " accuracy: " + c2.getAccuracy());
		print("_____________________________________________________");

		print("Confidence Interval             : 0.05");
		print("Statistic threshold (Std Normal): 1.96");

		printResult();
	}
}
