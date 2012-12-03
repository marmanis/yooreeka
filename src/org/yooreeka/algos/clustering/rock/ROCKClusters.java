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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.config.YooreekaConfigurator;

/**
 * Set of clusters and link data for ROCK implementation.
 */
public class ROCKClusters {

	private static final Logger LOG = Logger.getLogger(ROCKClusters.class.getName());

	/*
	 * Used to assign unique IDs to clusters.
	 */
	private int nextKey;

	/*
	 * Provides ID -> Cluster mapping.
	 */
	private Map<Integer, Cluster> clusterMap;

	/*
	 * Provides ID -> Similar Clusters mapping.
	 */
	private Map<Integer, List<SimilarCluster>> similarClustersMap;

	/*
	 * Goodness measure between two clusters. It is used to determine cluster
	 * eligibility for merge.
	 */
	private MergeGoodnessMeasure goodnessMeasure;

	/*
	 * Links between data points and clusters.
	 */
	private LinkMatrix linkMatrix;

	public ROCKClusters(List<Cluster> initialClusters, LinkMatrix linkMatrix,
			MergeGoodnessMeasure goodnessMeasure) {

		LOG.setLevel(YooreekaConfigurator.getLevel(ROCKClusters.class.getName()));
		
		this.linkMatrix = linkMatrix;
		clusterMap = new HashMap<Integer, Cluster>();
		nextKey = 0;
		this.goodnessMeasure = goodnessMeasure;

		for (Cluster c : initialClusters) {
			addCluster(c);
		}
		calculateClusterSimilarities();
	}

	public int addCluster(Cluster c) {
		int key = nextKey;
		clusterMap.put(key, c);
		nextKey++;
		return key;
	}

	public void calculateClusterSimilarities() {
		similarClustersMap = new HashMap<Integer, List<SimilarCluster>>();
		for (Integer clusterKey : getAllKeys()) {
			List<SimilarCluster> similarClusters = new LinkedList<SimilarCluster>();
			Cluster cluster = getCluster(clusterKey);
			for (Integer similarClusterKey : getAllKeys()) {
				if (clusterKey != similarClusterKey) {
					Cluster similarCluster = getCluster(similarClusterKey);
					int nLinks = linkMatrix.getLinks(cluster, similarCluster);
					if (nLinks > 0) {
						double goodness = goodnessMeasure.g(nLinks,
								cluster.size(), similarCluster.size());
						similarClusters.add(new SimilarCluster(
								similarClusterKey, goodness));
					}
				}
			}
			setSimilarClusters(clusterKey, similarClusters);
		}
	}

	/**
	 * Finds a pair of cluster indexes with the best goodness measure.
	 */
	public List<Integer> findBestMergeCandidates() {
		Integer bestKey = null;
		SimilarCluster bestSimilarCluster = null;
		Double bestGoodness = Double.NEGATIVE_INFINITY;
		for (Map.Entry<Integer, List<SimilarCluster>> e : similarClustersMap
				.entrySet()) {
			List<SimilarCluster> similarClusters = e.getValue();
			if (similarClusters != null && similarClusters.size() > 0) {
				SimilarCluster topSimilarCluster = similarClusters.get(0);
				if (topSimilarCluster.getGoodness() > bestGoodness) {
					bestGoodness = topSimilarCluster.getGoodness();
					bestKey = e.getKey();
					bestSimilarCluster = topSimilarCluster;
				}
			}
		}
		List<Integer> bestMergeCandidates = new ArrayList<Integer>();
		if (bestKey != null) {
			bestMergeCandidates.add(bestKey);
			bestMergeCandidates.add(bestSimilarCluster.getClusterKey());
		}
		return bestMergeCandidates;
	}

	public Collection<Cluster> getAllClusters() {
		return clusterMap.values();
	}

	public Set<Integer> getAllKeys() {
		return new HashSet<Integer>(clusterMap.keySet());
	}

	public Cluster getCluster(Integer key) {
		return clusterMap.get(key);
	}

	public double mergeBestCandidates() {
		List<Integer> mergeCandidates = findBestMergeCandidates();

		double goodness = Double.NaN;

		if (mergeCandidates.size() > 1) {

			Integer key1 = mergeCandidates.get(0);
			Integer key2 = mergeCandidates.get(1);
			goodness = similarClustersMap.get(key1).get(0).getGoodness();

			mergeClusters(key1, key2);
		}

		return goodness;
	}

	public Integer mergeClusters(Integer key1, Integer key2) {

		Cluster cluster1 = getCluster(key1);
		Cluster cluster2 = getCluster(key2);
		Cluster cluster3 = new Cluster(cluster1, cluster2);
		removeCluster(key1);
		removeCluster(key2);
		Integer key3 = addCluster(cluster3);

		calculateClusterSimilarities();

		return key3;
	}

	public Cluster removeCluster(Integer key) {
		return clusterMap.remove(key);
	}

	private void setSimilarClusters(Integer key, List<SimilarCluster> list) {
		SimilarCluster.sortByGoodness(list);
		similarClustersMap.put(key, list);
	}

	public int size() {
		return clusterMap.size();
	}
}
