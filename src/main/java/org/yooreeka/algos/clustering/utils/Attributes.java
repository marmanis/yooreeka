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
package org.yooreeka.algos.clustering.utils;

import org.yooreeka.algos.clustering.model.Attribute;

/*
 * Utility methods to simplify operations on attributes.
 */
public class Attributes {

	public static boolean allNumeric(Attribute[] attributes) {
		boolean allNumeric = true;
		for (Attribute a : attributes) {
			if (a.isNumeric() == false) {
				allNumeric = false;
				break;
			}
		}
		return allNumeric;
	}

	public static boolean allText(Attribute[] attributes) {
		boolean allText = true;
		for (Attribute a : attributes) {
			if (a.isText() == false) {
				allText = false;
				break;
			}
		}
		return allText;
	}

	public static Attribute[] createAttributes(double[] attrValues) {
		int n = attrValues.length;
		Attribute[] attrs = new Attribute[n];
		for (int i = 0; i < n; i++) {
			String attrName = "a-" + i;
			Attribute a = new Attribute(attrName, attrValues[i]);
			attrs[i] = a;
		}
		return attrs;
	}

	public static Attribute[] createAttributes(String[] attrValues) {
		int n = attrValues.length;
		Attribute[] attrs = new Attribute[n];
		for (int i = 0; i < n; i++) {
			String attrName = "a-" + i;
			Attribute a = new Attribute(attrName, attrValues[i]);
			attrs[i] = a;
		}
		return attrs;
	}

	public static Attribute[] createAttributes(String[] names, double[] values) {
		int n = names.length;
		Attribute[] attributes = new Attribute[n];
		for (int i = 0; i < n; i++) {
			attributes[i] = new Attribute(names[i], values[i]);
		}
		return attributes;
	}

	public static Attribute[] createAttributes(String[] names, String[] values) {
		int n = names.length;
		Attribute[] attributes = new Attribute[n];
		for (int i = 0; i < n; i++) {
			attributes[i] = new Attribute(names[i], values[i]);
		}
		return attributes;
	}

	public static String[] getNames(Attribute[] attributes) {
		int n = attributes.length;
		String[] names = new String[n];
		for (int i = 0; i < n; i++) {
			Attribute a = attributes[i];
			names[i] = a.getName();
		}
		return names;
	}

	public static double[] getNumericValues(Attribute[] attributes) {
		int n = attributes.length;
		double[] values = new double[n];
		for (int i = 0; i < n; i++) {
			Attribute a = attributes[i];
			if (a.isNumeric()) {
				values[i] = a.getNumericValue();
			} else {
				throw new RuntimeException(
						"Non-numeric attribute encountered. " + "Attribute: "
								+ a.toString());
			}
		}
		return values;
	}

	public static String[] getTextValues(Attribute[] attributes) {
		int n = attributes.length;
		String[] values = new String[n];
		for (int i = 0; i < n; i++) {
			Attribute a = attributes[i];
			if (a.isText()) {
				values[i] = a.getTextValue();
			} else {
				throw new RuntimeException("Non-text attribute encountered. "
						+ "Attribute: " + a.toString());
			}
		}
		return values;
	}

}
