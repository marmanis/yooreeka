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
package org.yooreeka.algos.clustering.hierarchical;

/**
 * Basic implementation of Prim's algorithm to build Minimal Spanning Tree
 * (MST).
 * 
 */
public class MST {

	class Edge {

		private int i;
		private int j;
		private double w;

		Edge(int i, int j, double w) {
			this.i = i;
			this.j = j;
			this.w = w;
		}

		public int getI() {
			return i;
		}

		public int getJ() {
			return j;
		}

		public double getW() {
			return w;
		}

	}

	/** The adjacency matrix of the graph */
	private double[][] adjM;

	public MST() {
	}

	public double[][] buildMST(double[][] adjM) {

		this.adjM = adjM;

		// Marks nodes that belong to MST. Initial MST has only one node.
		boolean[] allV = new boolean[adjM.length];
		allV[0] = true;

		// Adjacency matrix defining MST
		double[][] mst = new double[adjM.length][adjM.length];
		for (int i = 0, n = mst.length; i < n; i++) {
			for (int j = 0; j < n; j++) {
				/*
				 * Using -1 to indicate that there is no edge between nodes i
				 * and j. Can't use 0 because it is a valid distance.
				 */
				mst[i][j] = -1;
			}
		}

		Edge e = null;
		while ((e = findMinimumEdge(allV)) != null) {
			allV[e.getJ()] = true;
			mst[e.getI()][e.getJ()] = e.getW();
			mst[e.getJ()][e.getI()] = e.getW();
		}

		return mst;
	}

	private Edge findMinimumEdge(boolean[] mstV) {
		Edge e = null;
		double minW = Double.POSITIVE_INFINITY;
		int minI = -1;
		int minJ = -1;

		for (int i = 0, n = adjM.length; i < n; i++) {
			// part of MST
			if (mstV[i] == true) {
				for (int j = 0, k = adjM.length; j < k; j++) {
					// not part of MST
					if (mstV[j] == false) {
						if (minW > adjM[i][j]) {
							minW = adjM[i][j];
							minI = i;
							minJ = j;
						}
					}
				}
			}
		}

		if (minI > -1) {
			e = new Edge(minI, minJ, minW);
		}

		return e;
	}
}
