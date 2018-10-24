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
package org.yooreeka.algos.clustering.partitional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.utils.ObjectToIndexMapping;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.metrics.NumericDistance;
import org.yooreeka.util.metrics.EuclideanDistance;

public class NearestNeighborAlgorithm {

	private static final Logger LOG = Logger.getLogger(NearestNeighborAlgorithm.class.getName());
	
	public static void main(String[] args) {

		DataPoint[] elements = new DataPoint[5];
		elements[0] = new DataPoint("A", new double[] {});
		elements[1] = new DataPoint("B", new double[] {});
		elements[2] = new DataPoint("C", new double[] {});
		elements[3] = new DataPoint("D", new double[] {});
		elements[4] = new DataPoint("E", new double[] {});

		double[][] a = new double[][] { { 0, 1, 2, 2, 3 }, { 1, 0, 2, 4, 3 },
				{ 2, 2, 0, 1, 5 }, { 2, 4, 1, 0, 3 }, { 3, 3, 5, 3, 0 } };

		double threshold = 2;

		NearestNeighborAlgorithm nn = new NearestNeighborAlgorithm(elements, a,
				threshold);

		nn.run();
	}

	/*
	 * All elements for clustering.
	 */
	private DataPoint[] allDataPoints;

	/*
	 * Matrix with distances between elements.
	 */
	private double[][] a;

	/*
	 * Threshold value that is used to determine if elements will be added to
	 * one of the existing clusters or if a new cluster will be created.
	 */
	private double t = 0.5;

	/*
	 * List of clusters.
	 */
	private List<Cluster> allClusters;

	/*
	 * Distance metric that will be used to calculate distance between elements.
	 */
	private NumericDistance dist = new EuclideanDistance();

	/*
	 * DataPoint -> Index mapping. Used to access data in distance matrix.
	 */
	ObjectToIndexMapping<DataPoint> idxMapping = null;

	private boolean verbose = true;

	public NearestNeighborAlgorithm(DataPoint[] dataPoints, double t) {
		this(dataPoints, null, t);
	}

	/**
	 * 
	 * @param dataPoints
	 *            elements to cluster. Element order should correspond to
	 *            elements in distance matrix.
	 * @param a
	 *            matrix showing distance between elements. Can be null.
	 * @param t
	 *            threshold value for new cluster creation.
	 */
	public NearestNeighborAlgorithm(DataPoint[] dataPoints, double[][] a,
			double t) {
		
		LOG.setLevel(YooreekaConfigurator.getLevel(NearestNeighborAlgorithm.class.getName()));
		
		this.t = t;
		this.allDataPoints = dataPoints;
		this.a = a;
		this.allClusters = new ArrayList<Cluster>();

		/*
		 * Create DataPoint -> Index mapping for all data points.
		 */
		idxMapping = new ObjectToIndexMapping<DataPoint>();

		for (int i = 0, n = dataPoints.length; i < n; i++) {
			idxMapping.getIndex(dataPoints[i]);
		}

	}

	private void assignPointToCluster(DataPoint x) {

		/* find min distance between current point and all clusters */
		double minNNDist = Double.POSITIVE_INFINITY;
		Cluster closestCluster = null;
		for (Cluster c : allClusters) {
			double nnDist = getNNDistance(c, x);
			if (nnDist < minNNDist) {
				minNNDist = nnDist;
				closestCluster = c;
			}
		}

		/* Assign point to cluster based on calculated distance and threshold */
		if (minNNDist <= t) {
			closestCluster.add(x);
		} else {
			/* Best distance exceeds the threshold - create a new cluster. */
			Cluster newCluster = new Cluster();
			newCluster.add(x);
			allClusters.add(newCluster);
		}
	}

	private void calculateDistanceMatrix() {
		a = new double[allDataPoints.length][allDataPoints.length];
		for (int i = 0, n = allDataPoints.length; i < n; i++) {
			DataPoint x = allDataPoints[i];
			for (int j = i + 1; j < n; j++) {
				DataPoint y = allDataPoints[j];
				a[i][j] = dist.getDistance(x.getNumericAttrValues(),
						y.getNumericAttrValues());
				a[j][i] = a[i][j];
			}
			a[i][i] = 0.0;
		}
	}

	public List<Cluster> getAllClusters() {
		return allClusters;
	}

	/**
	 * Calculates distance between cluster and element using Nearest Neighbor
	 * approach.
	 */
	private double getNNDistance(Cluster c, DataPoint x) {

		double nnDist = Double.POSITIVE_INFINITY;

		if (c.contains(x)) {
			nnDist = 0.0;
		} else {
			int i = idxMapping.getIndex(x);
			for (DataPoint y : c.getElements()) {
				int j = idxMapping.getIndex(y);
				double xyDist = a[i][j];
				nnDist = Math.min(nnDist, xyDist);
			}
		}

		return nnDist;
	}

	private void printResults() {
		System.out.println("Nearest Neighbor Clustering with t = " + t);
		System.out.println("Clusters:");
		for (Cluster c : allClusters) {
			System.out.println(c.getElementsAsString());
		}
	}

	public void run() {

		if (allDataPoints == null || allDataPoints.length == 0) {
			return;
		}

		if (a == null) {
			calculateDistanceMatrix();
		}

		for (int i = 0, n = allDataPoints.length; i < n; i++) {
			assignPointToCluster(allDataPoints[i]);
		}

		if (verbose) {
			printResults();
		}
	}

	public void setDistance(NumericDistance dist) {
		this.dist = dist;
	}
}
