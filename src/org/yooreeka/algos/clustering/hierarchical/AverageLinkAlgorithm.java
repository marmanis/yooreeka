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

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.utils.ObjectToIndexMapping;

/** A hierarchical agglomerative clustering algorithm based on the average link */
public class AverageLinkAlgorithm {

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

		AverageLinkAlgorithm ca = new AverageLinkAlgorithm(elements, a);
		Dendrogram dnd = ca.cluster();
		dnd.printAll();
	}
	private DataPoint[] elements;
	private double[][] a;

	private ClusterSet allClusters;

	public AverageLinkAlgorithm(DataPoint[] elements, double[][] adjacencyMatrix) {
		this.elements = elements;
		this.a = adjacencyMatrix;
		this.allClusters = new ClusterSet();
	}

	public Dendrogram cluster() {

		Dendrogram dnd = new Dendrogram("Distance");
		double d = 0.0;

		// initially load all elements as individual clusters
		for (DataPoint e : elements) {
			Cluster c = new Cluster(e);
			allClusters.add(c);
		}

		dnd.addLevel(String.valueOf(d), allClusters.getAllClusters());

		d = 1.0;

		while (allClusters.size() > 1) {
			int K = allClusters.size();
			mergeClusters(d);
			// it is possible that there were no clusters to merge for current
			// d.
			if (K > allClusters.size()) {
				dnd.addLevel(String.valueOf(d), allClusters.getAllClusters());
				K = allClusters.size();
			}

			d = d + 0.5;
		}
		return dnd;
	}

	private void mergeClusters(double distanceThreshold) {
		int nClusters = allClusters.size();

		ObjectToIndexMapping<Cluster> idxMapping = new ObjectToIndexMapping<Cluster>();

		double[][] clusterDistances = new double[nClusters][nClusters];

		for (int i = 0, n = a.length; i < n; i++) {
			for (int j = i + 1, k = a.length; j < k; j++) {
				double d = a[i][j];
				if (d > 0) {
					DataPoint e1 = elements[i];
					DataPoint e2 = elements[j];
					Cluster c1 = allClusters.findClusterByElement(e1);
					Cluster c2 = allClusters.findClusterByElement(e2);
					if (!c1.equals(c2)) {
						int ci = idxMapping.getIndex(c1);
						int cj = idxMapping.getIndex(c2);
						clusterDistances[ci][cj] += d;
						clusterDistances[cj][ci] += d;
					}
				}
			}
		}

		boolean[] merged = new boolean[clusterDistances.length];
		for (int i = 0, n = clusterDistances.length; i < n; i++) {
			for (int j = i + 1, k = clusterDistances.length; j < k; j++) {
				Cluster ci = idxMapping.getObject(i);
				Cluster cj = idxMapping.getObject(j);
				int ni = ci.size();
				int nj = cj.size();
				clusterDistances[i][j] = clusterDistances[i][j] / (ni * nj);
				clusterDistances[j][i] = clusterDistances[i][j];
				// merge clusters if distance is below the threshold
				if (merged[i] == false && merged[j] == false) {
					if (clusterDistances[i][j] <= distanceThreshold) {
						allClusters.remove(ci);
						allClusters.remove(cj);
						Cluster mergedCluster = new Cluster(ci, cj);
						allClusters.add(mergedCluster);
						merged[i] = true;
						merged[j] = true;
					}
				}
			}
		}
	}
}
