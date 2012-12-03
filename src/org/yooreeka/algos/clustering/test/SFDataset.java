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
package org.yooreeka.algos.clustering.test;

import java.util.Arrays;

import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.util.metrics.NumericDistance;

public class SFDataset {

	private DataPoint[] data;
	private NumericDistance distance;
	private double[][] adjacencyMatrix;

	public SFDataset(DataPoint[] data, NumericDistance distance) {
		this.data = data;
		this.distance = distance;
		this.adjacencyMatrix = calculateAdjacencyMatrix();
	}

	/**
	 * Adjacency matrix for all data instances in the dataset. Each element
	 * represents distance between corresponding elements.
	 * 
	 * @return
	 */
	private double[][] calculateAdjacencyMatrix() {
		int n = data.length;
		double[][] adjMatrix = new double[n][n];

		DataPoint x = null;
		DataPoint y = null;

		for (int i = 0; i < n; i++) {
			x = data[i];
			for (int j = i + 1; j < n; j++) {
				y = data[j];
				adjMatrix[i][j] = distance.getDistance(
						x.getNumericAttrValues(), y.getNumericAttrValues());
				adjMatrix[j][i] = adjMatrix[i][j];
			}
			adjMatrix[i][i] = 0.0;
		}

		return adjMatrix;
	}

	// We might need to move Matrix related methods to separate class
	// eventually.

	public double[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}

	public DataPoint[] getData() {
		return data;
	}

	public void printDistanceMatrix() {
		for (int i = 0, n = adjacencyMatrix.length; i < n; i++) {
			System.out.println(Arrays.toString(adjacencyMatrix[i]));
		}
	}
}
