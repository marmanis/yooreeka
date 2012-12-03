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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;

/**
 * Set of clusters.
 */
public class ClusterSet {

	private Set<Cluster> allClusters = new HashSet<Cluster>();

	public boolean add(Cluster c) {
		return allClusters.add(c);
	}

	public Cluster findClusterByElement(DataPoint e) {
		Cluster cluster = null;
		for (Cluster c : allClusters) {
			if (c.contains(e)) {
				cluster = c;
				break;
			}
		}
		return cluster;
	}

	public List<Cluster> getAllClusters() {
		return new ArrayList<Cluster>(allClusters);
	}

	public boolean remove(Cluster c) {
		return allClusters.remove(c);
	}

	public int size() {
		return allClusters.size();
	}

	// public ClusterSet copy() {
	// ClusterSet clusterSet = new ClusterSet();
	// for(Cluster c : this.allClusters ) {
	// Cluster clusterCopy = c.copy();
	// clusterSet.add(clusterCopy);
	// }
	// return clusterSet;
	// }
}
