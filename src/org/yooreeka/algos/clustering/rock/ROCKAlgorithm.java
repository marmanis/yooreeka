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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.model.Dendrogram;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.metrics.JaccardCoefficient;
import org.yooreeka.util.metrics.SimilarityMeasure;

public class ROCKAlgorithm {

	private static final Logger LOG = Logger.getLogger(ROCKAlgorithm.class.getName());
	
	public static void main(String[] args) {
		// Define data
		DataPoint[] elements = new DataPoint[4];
		elements[0] = new DataPoint("Doc1", new String[] { "book" });
		elements[1] = new DataPoint("Doc2", new String[] { "water", "sun", "sand", "swim" });
		elements[2] = new DataPoint("Doc3", new String[] { "water", "sun", "swim", "read" });
		elements[3] = new DataPoint("Doc4", new String[] { "read", "sand" });

		int k = 1;
		double th = 0.2;
		ROCKAlgorithm rock = new ROCKAlgorithm(elements, k, th);
		Dendrogram dnd = rock.cluster();
		dnd.printAll();
	}
	private DataPoint[] points;
	private int k;

	private double th;

	private SimilarityMeasure similarityMeasure;

	private LinkMatrix linkMatrix;

	/**
	 * 
	 * @param k
	 *            desired number of clusters.
	 * @param th
	 *            threshold value to identify neighbors among points.
	 */
	public ROCKAlgorithm(DataPoint[] points, int k, double th) {
		
		LOG.setLevel(YooreekaConfigurator.getLevel(ROCKAlgorithm.class.getName()));
		
		this.points = points;
		this.k = k;
		this.th = th;
		this.similarityMeasure = new JaccardCoefficient();
		// this.similarityMeasure = new CosineSimilarity();
		this.linkMatrix = new LinkMatrix(points, similarityMeasure, th);
	}

	public Dendrogram cluster() {

		// Create a new cluster out of every point.
		List<Cluster> initialClusters = new ArrayList<Cluster>();
		for (int i = 0, n = points.length; i < n; i++) {
			Cluster cluster = new Cluster(points[i]);
			initialClusters.add(cluster);
		}
		double g = Double.POSITIVE_INFINITY;
		Dendrogram dnd = new Dendrogram("Goodness");
		dnd.addLevel(String.valueOf(g), initialClusters);

		MergeGoodnessMeasure goodnessMeasure = new MergeGoodnessMeasure(th);

		ROCKClusters allClusters = new ROCKClusters(initialClusters,
				linkMatrix, goodnessMeasure);

		int nClusters = allClusters.size();
		while (nClusters > k) {
			int nClustersBeforeMerge = nClusters;
			g = allClusters.mergeBestCandidates();
			nClusters = allClusters.size();
			if (nClusters == nClustersBeforeMerge) {
				// there are no linked clusters to merge
				break;
			}
			dnd.addLevel(String.valueOf(g), allClusters.getAllClusters());
		}

		System.out.println("Number of clusters: "
				+ allClusters.getAllClusters().size());
		return dnd;
	}

	public int getK() {
		return k;
	}

	public LinkMatrix getLinkMatrix() {
		return linkMatrix;
	}

	public SimilarityMeasure getSimilarityMeasure() {
		return similarityMeasure;
	}

	public double getTh() {
		return th;
	}

}
