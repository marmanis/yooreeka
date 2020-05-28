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
package org.yooreeka.algos.search.ranking;

import org.yooreeka.util.internet.crawling.util.ValueToIndexMapping;

// Sub-stochastic matrix - some rows will have all zeros
public class PageRankMatrixH {

	private ValueToIndexMapping indexMapping = new ValueToIndexMapping();

	double[][] matrix;

	private int numberOfPagesWithNoLinks = 0;

	public PageRankMatrixH(int nPages) {
		matrix = new double[nPages][nPages];
	}

	/**
	 * Just associate page url with an index. Used for pages that have no
	 * outlinks.
	 */
	public void addLink(String pageUrl) {
		indexMapping.getIndex(pageUrl);
	}

	public void addLink(String fromPageUrl, String toPageUrl) {
		addLink(fromPageUrl, toPageUrl, 1);
	}

	public void addLink(String fromPageUrl, String toPageUrl, double weight) {
		int i = indexMapping.getIndex(fromPageUrl);
		int j = indexMapping.getIndex(toPageUrl);

		try {

			matrix[i][j] = weight;

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("fromPageUrl:" + fromPageUrl + ", toPageUrl: "
					+ toPageUrl);
		}
	}

	public void calculate() {

		for (int i = 0, n = matrix.length; i < n; i++) {

			double rowSum = 0;

			for (int j = 0, k = matrix.length; j < k; j++) {

				rowSum += matrix[i][j];
			}

			if (rowSum > 0) {

				for (int j = 0, k = matrix.length; j < k; j++) {

					if (matrix[i][j] > 0) {

						matrix[i][j] = matrix[i][j] / rowSum;
					}
				}

			} else {

				numberOfPagesWithNoLinks++;
			}
		}
	}

	/**
	 * A <B>dangling node</B> corresponds to a web page that has no outlinks.
	 * These nodes result in a H row that has all its values equal to 0.
	 */
	public int[] getDangling() {

		int n = getSize();
		int[] d = new int[n];

		boolean foundOne = false;

		for (int i = 0; i < n; i++) {

			for (int j = 0; j < n; j++) {

				if (matrix[i][j] > 0) {
					foundOne = true;
					break;
				}
			}

			if (foundOne) {
				d[i] = 0;
			} else {
				d[i] = 1;
			}

			foundOne = false;
		}

		return d;
	}

	/**
	 * @return the indexMapping
	 */
	public ValueToIndexMapping getIndexMapping() {
		return indexMapping;
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public int getNumberOfPagesWithNoLinks() {
		return this.numberOfPagesWithNoLinks;
	}

	public int getSize() {
		return matrix.length;
	}

	public void print() {

		StringBuilder txt = new StringBuilder("H Matrix\n\n");

		for (int i = 0, n = matrix.length; i < n; i++) {
			txt.append("Index: ").append(i);
			txt.append("  -->  ");
			txt.append("Page ID: ").append(indexMapping.getValue(i));
			txt.append("\n");
		}

		txt.append("\n").append("\n");

		for (int i = 0, n = matrix.length; i < n; i++) {

			for (int j = 0, k = matrix.length; j < k; j++) {

				txt.append(" ");
				txt.append(matrix[i][j]);

				if (j < k - 1) {
					txt.append(", ");
				} else {
					txt.append("\n");
				}
			}
		}

		System.out.println(txt.toString());
	}
}
