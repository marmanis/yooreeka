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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.model.Dendrogram;
import org.yooreeka.config.YooreekaConfigurator;

/** A hierarchical agglomerative clustering algorithm based on single link */
public class SingleLinkAlgorithm {

	private static final Logger LOG = Logger.getLogger(SingleLinkAlgorithm.class.getName());

	public static void main(String[] args) {
		// Define data
		DataPoint[] elements = new DataPoint[5];
		elements[0] = new DataPoint("A", new double[] {});
		elements[1] = new DataPoint("B", new double[] {});
		elements[2] = new DataPoint("C", new double[] {});
		elements[3] = new DataPoint("D", new double[] {});
		elements[4] = new DataPoint("E", new double[] {});

		double[][] a = new double[][] { { 0, 1, 2, 2, 3 }, { 1, 0, 2, 4, 3 },
				{ 2, 2, 0, 1, 5 }, { 2, 4, 1, 0, 3 }, { 3, 3, 5, 3, 0 } };

		SingleLinkAlgorithm ca = new SingleLinkAlgorithm(elements, a);
		Dendrogram dnd = ca.cluster();
		dnd.printAll();
		// dnd.print(3);
	}
	private DataPoint[] elements;

	private double[][] a;

	// Hierarchical Agglomerative Algorithm
	public SingleLinkAlgorithm(DataPoint[] elements, double[][] adjacencyMatrix) {

		LOG.setLevel(YooreekaConfigurator.getLevel(SingleLinkAlgorithm.class.getName()));

		this.elements = elements;
		this.a = adjacencyMatrix;
	}

	// Implements Single Link Technique
	private List<Cluster> buildClusters(double distanceThreshold) {
		boolean[] usedElementFlags = new boolean[elements.length];
		List<Cluster> clusters = new ArrayList<Cluster>();
		for (int i = 0, n = a.length; i < n; i++) {
			List<DataPoint> clusterPoints = new ArrayList<DataPoint>();
			for (int j = i, k = a.length; j < k; j++) {
				if (a[i][j] <= distanceThreshold
						&& usedElementFlags[j] == false) {
					clusterPoints.add(elements[j]);
					usedElementFlags[j] = true;
				}
			}
			if (clusterPoints.size() > 0) {
				Cluster c = new Cluster(clusterPoints);
				clusters.add(c);
			}
		}
		return clusters;
	}

	public Dendrogram cluster() {
		Dendrogram dnd = new Dendrogram("Distance");
		double d = 0;

		// initially load all elements as individual clusters
		List<Cluster> initialClusters = new ArrayList<Cluster>();
		for (DataPoint e : elements) {
			Cluster c = new Cluster(e);
			initialClusters.add(c);
		}

		dnd.addLevel(String.valueOf(d), initialClusters);

		d = 1.0;

		int k = initialClusters.size();

		while (k > 1) {
			int oldK = k;
			List<Cluster> clusters = buildClusters(d);
			k = clusters.size();
			if (oldK != k) {
				dnd.addLevel(String.valueOf(d), clusters);
			}

			d = d + 1;
		}
		return dnd;
	}
}
