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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.yooreeka.algos.clustering.utils.Attributes;
import org.yooreeka.util.metrics.EuclideanDistance;

/**
 * A Thing to be clustered. Defined by a set of attributes.
 */
public class DataPoint {

	/**
	 * Descriptive label or name. We also use it as unique ID for the instance.
	 */
	private String label;

	/**
	 * Collection of attributes that define this point.
	 */
	private Attribute[] attributes;

	/*
	 * Values derived from attributes.
	 */
	private String[] attributeNames;
	private double[] numericAttributeValues;
	private String[] textAttributeValues;

	public DataPoint(String label, Attribute[] attributes) {
		init(label, attributes);
	}

	/**
	 * Creates a new point with numerical attributes. Attribute names are
	 * auto-generated.
	 */
	public DataPoint(String label, double[] attrValues) {
		// create attributes with auto-generated names
		init(label, Attributes.createAttributes(attrValues));
	}

	public DataPoint(String label, String[] attrValues) {
		// create attributes with auto-generated names
		init(label, Attributes.createAttributes(attrValues));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DataPoint other = (DataPoint) obj;
		if (!Arrays.equals(attributes, other.attributes))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	public int getAttributeCount() {
		return numericAttributeValues.length;
	}

	public String[] getAttributeNames() {
		return attributeNames;
	}

	public Attribute[] getAttributes() {
		return attributes;
	}

	public String getLabel() {
		return label;
	}

	public double[] getNumericAttrValues() {
		return numericAttributeValues;
	}

	public double getR() {

		EuclideanDistance euclid = new EuclideanDistance();

		int n = attributes.length;

		double[] x = new double[n];

		for (int i = 0; i < n; i++) {
			x[i] = 0d;
		}

		return euclid.getDistance(x, this.numericAttributeValues);
	}

	public String[] getTextAttrValues() {
		return textAttributeValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(attributes);
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	private void init(String label, Attribute[] attributes) {
		this.label = label;
		this.attributes = attributes;
		this.attributeNames = Attributes.getNames(attributes);
		if (Attributes.allText(attributes)) {
			this.textAttributeValues = Attributes.getTextValues(attributes);
		} else {
			this.textAttributeValues = null;
		}
		if (Attributes.allNumeric(attributes)) {
			this.numericAttributeValues = Attributes
					.getNumericValues(attributes);
		} else {
			this.numericAttributeValues = null;
		}
	}

	public String toShortString() {
		List<String> attrValues = new ArrayList<String>();
		for (Attribute a : attributes) {
			if (a.isNumeric()) {
				attrValues.add(String.valueOf(a.getNumericValue()));
			} else {
				attrValues.add(a.getTextValue());
			}
		}
		return label + "(" + attrValues.toString() + ")";
	}

	@Override
	public String toString() {
		return label + "(" + Arrays.toString(attributes) + ")";
	}

}
