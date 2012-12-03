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
package org.yooreeka.algos.clustering.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Group of data points.
 */
public class Cluster {

	private String label;

	private Set<DataPoint> elements;

	// Empty cluster with no elements.
	public Cluster() {
		init("");
	}

	// New cluster that contains all elements from provided clusters.
	public Cluster(Cluster c1, Cluster c2) {
		init("");
		add(c1);
		add(c2);
	}

	public Cluster(Collection<DataPoint> elements) {
		init("");
		for (DataPoint e : elements) {
			add(e);
		}
	}

	public Cluster(DataPoint element) {
		init("");
		add(element);
	}

	public Cluster(String label) {
		init(label);
	}

	public Cluster(String label, Collection<DataPoint> elements) {
		init(label);
		for (DataPoint e : elements) {
			add(e);
		}
	}

	/**
	 * Modifies existing cluster by adding all elements from provided cluster.
	 * 
	 * @param c
	 */
	public void add(Cluster c) {
		for (DataPoint e : c.getElements()) {
			elements.add(e);
		}
	}

	/**
	 * Modifies existing cluster by adding a new element.
	 * 
	 * @param e
	 */
	public void add(DataPoint e) {
		elements.add(e);
	}

	public boolean contains(Cluster c) {
		boolean result = true;
		for (DataPoint e : c.getElements()) {
			if (!contains(e)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public boolean contains(DataPoint e) {
		return elements.contains(e);
	}

	public Cluster copy() {
		Cluster copy = new Cluster();
		for (DataPoint e : this.getElements()) {
			// DataPoint is immutable. No need to create a copy.
			copy.add(e);
		}
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Cluster other = (Cluster) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	/*
	 * Returns number of attributes used to define points in the cluster.
	 */
	public int getDimensionCount() {
		if (elements == null || elements.isEmpty()) {
			return 0;
		}

		return elements.iterator().next().getAttributeCount();
	}

	public Set<DataPoint> getElements() {
		return new LinkedHashSet<DataPoint>(elements);
	}

	public String getElementsAsString() {
		StringBuffer buf = new StringBuffer("{");
		for (DataPoint e : elements) {
			if (buf.length() > 1) {
				buf.append(",\n");
			}
			buf.append(e.getLabel());
		}
		buf.append("}");
		
		return buf.toString();
	}

	public String getLabel() {
		return label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		return result;
	}

	private void init(String label) {
		this.label = label;
		elements = new LinkedHashSet<DataPoint>();
	}

	public int size() {
		return elements.size();
	}

	@Override
	public String toString() {
		return getElementsAsString();
	}

}
