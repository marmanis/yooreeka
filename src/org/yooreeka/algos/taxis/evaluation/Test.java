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

public abstract class Test {

	private String statisticSymbol;

	protected double statisticValue;
	private double threshold;
	public Test() {
		super();
	}

	protected abstract void calculate();

	public abstract void evaluate();

	public String getStatisticSymbol() {
		return statisticSymbol;
	}

	public double getStatisticValue() {
		return statisticValue;
	}

	public double getThreshold() {
		return threshold;
	}

	protected boolean isDifferent(double statistic, double threshold) {
		if (statistic > threshold) {
			return true;
		} else {
			return false;
		}
	}

	protected void print(String val) {
		System.out.print("      ");
		System.out.println(val);
	}

	protected void printResult() {

		boolean btmp = isDifferent(statisticValue, threshold);

		String tmp;

		if (btmp) {
			tmp = " > ";
		} else {
			tmp = " <= ";
		}

		print("________________________________________________________");

		print(statisticSymbol + " value is " + statisticValue + "which is "
				+ tmp + threshold);

		print("The two classifiers are different: "
				+ String.valueOf(btmp).toUpperCase());
	}

	protected void setStatisticSymbol(String statisticSymbol) {
		this.statisticSymbol = statisticSymbol;
	}

	protected void setStatisticValue(double statisticValue) {
		this.statisticValue = statisticValue;
	}

	protected void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}