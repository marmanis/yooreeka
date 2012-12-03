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
package org.yooreeka.algos.clustering.rock;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.utils.ObjectToIndexMapping;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.metrics.SimilarityMeasure;

/**
 * Calculates number of links between data points.
 */
public class LinkMatrix {

	private static final Logger LOG = Logger.getLogger(LinkMatrix.class.getName());
	
	private double th;
	double[][] pointSimilarityMatrix;
	int[][] pointNeighborMatrix;
	int[][] pointLinkMatrix;
	private ObjectToIndexMapping<DataPoint> objToIndexMapping;

	public LinkMatrix(DataPoint[] points, double[][] similarityMatrix, double th) {
		init(points, similarityMatrix, th);
	}

	public LinkMatrix(DataPoint[] points, SimilarityMeasure pointSim, double th) {

		double[][] similarityMatrix = calculatePointSimilarities(points,
				pointSim);
		init(points, similarityMatrix, th);
	}

	/*
	 * Calculates similarity matrix for all points.
	 */
	private double[][] calculatePointSimilarities(DataPoint[] points,
			SimilarityMeasure pointSim) {

		int n = points.length;
		double[][] simMatrix = new double[n][n];
		for (int i = 0; i < n; i++) {
			DataPoint itemX = points[i];
			String[] attributesX = itemX.getTextAttrValues();
			for (int j = i + 1; j < n; j++) {
				DataPoint itemY = points[j];
				String[] attributesY = itemY.getTextAttrValues();
				simMatrix[i][j] = pointSim.similarity(attributesX, attributesY);
				simMatrix[j][i] = simMatrix[i][j];
			}
			simMatrix[i][i] = 1.0;
		}

		return simMatrix;
	}

	/**
	 * Calculates number of links between two clusters. Number of links between
	 * two clusters is the sum of links between all point pairs( p1, p2) where
	 * p1 belongs to the first cluster and p2 belongs to the other cluster.
	 * 
	 * @param clusterX
	 * @param clusterY
	 * 
	 * @return link count between two clusters.
	 */
	public int getLinks(Cluster clusterX, Cluster clusterY) {
		Set<DataPoint> itemsX = clusterX.getElements();
		Set<DataPoint> itemsY = clusterY.getElements();

		int linkSum = 0;

		for (DataPoint x : itemsX) {
			for (DataPoint y : itemsY) {
				linkSum += getLinks(x, y);
			}
		}
		return linkSum;
	}

	public int getLinks(DataPoint p1, DataPoint p2) {
		int i = objToIndexMapping.getIndex(p1);
		int j = objToIndexMapping.getIndex(p2);
		return pointLinkMatrix[i][j];
	}

	private void init(DataPoint[] points, double[][] similarityMatrix, double th) {

		LOG.setLevel(YooreekaConfigurator.getLevel(LinkMatrix.class.getName()));
		
		this.th = th;

		objToIndexMapping = new ObjectToIndexMapping<DataPoint>();

		// Create DataPoint <-> Index mapping.
		for (DataPoint point : points) {
			objToIndexMapping.getIndex(point);
		}

		pointSimilarityMatrix = similarityMatrix;

		// Identify neighbors: a[i][j] == 1 if (i,j) are neighbors and 0
		// otherwise.
		int n = points.length;

		pointNeighborMatrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				if (pointSimilarityMatrix[i][j] >= th) {
					pointNeighborMatrix[i][j] = 1;
				} else {
					pointNeighborMatrix[i][j] = 0;
				}
				pointNeighborMatrix[j][i] = pointNeighborMatrix[i][j];
			}
			pointNeighborMatrix[i][i] = 1;
		}

		// Calculate number of links between points
		pointLinkMatrix = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				pointLinkMatrix[i][j] = nLinksBetweenPoints(
						pointNeighborMatrix, i, j);
				pointLinkMatrix[j][i] = pointLinkMatrix[i][j];
			}
		}

	}

	private int nLinksBetweenPoints(int[][] neighbors, int indexX, int indexY) {
		int nLinks = 0;
		for (int i = 0, n = neighbors.length; i < n; i++) {
			nLinks += neighbors[indexX][i] * neighbors[i][indexY];
		}
		return nLinks;
	}

	public void printPointLinkMatrix() {
		System.out
				.println("Point Link matrix (th=" + String.valueOf(th) + "):");
		for (int i = 0; i < pointLinkMatrix.length; i++) {
			System.out.println(Arrays.toString(pointLinkMatrix[i]));
		}
	}

	public void printPointNeighborMatrix() {
		System.out.println("Point Neighbor matrix (th=" + String.valueOf(th)
				+ "):");
		for (int i = 0; i < pointNeighborMatrix.length; i++) {
			System.out.println(Arrays.toString(pointNeighborMatrix[i]));
		}
	}

	public void printSimilarityMatrix() {
		System.out.println("Point Similarity matrix:");
		for (int i = 0; i < pointSimilarityMatrix.length; i++) {
			System.out.println(Arrays.toString(pointSimilarityMatrix[i]));
		}
	}

}
