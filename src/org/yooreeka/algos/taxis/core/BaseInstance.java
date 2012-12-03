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
package org.yooreeka.algos.taxis.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class BaseInstance implements Instance {

	public static BaseInstance createInstance(String conceptName,
			String[] attrNames, String[] attrValues) {
		int n = attrNames.length;
		StringAttribute[] attributes = new StringAttribute[n];
		for (int i = 0; i < n; i++) {
			attributes[i] = new StringAttribute(attrNames[i], attrValues[i]);
		}

		Concept concept = new BaseConcept(conceptName);
		return new BaseInstance(concept, attributes);
	}
	protected Concept concept;

	protected StringAttribute[] attributes;

	public BaseInstance() {
		// DO NOTHING
	}

	/**
	 * @param concept
	 * @param attributes
	 */
	public BaseInstance(Concept concept, StringAttribute[] attributes) {
		this.concept = concept;
		this.attributes = attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		final BaseInstance other = (BaseInstance) obj;

		// Check the basics first
		if (this == obj) {
			return true;
		}

		if ((getClass() != obj.getClass()) || obj == null) {
			return false;
		}

		// Check the concept
		if (concept == null) {
			if (other.concept != null) {
				return false;
			}
		} else {
			if (!concept.equals(other.concept)) {
				return false;
			}
		}

		// Finally check all the attributes
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] == null) {
				if (other.attributes[i] != null) {
					return false;
				}
			} else {
				if (!attributes[i].getName().equals(
						other.attributes[i].getName())) {
					return false;
				} else {
					if (!attributes[i].getValue().equals(
							other.attributes[i].getValue())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public Attribute[] getAtrributes() {
		return attributes;
	}

	public StringAttribute getAttribute(int i) {
		return attributes[i];
	}

	public Attribute getAttributeByName(String attrName) {
		Attribute matchedAttribute = null;

		if (attributes != null) {
			for (Attribute a : attributes) {
				if (attrName.equalsIgnoreCase(a.getName())) {
					matchedAttribute = a;
					break;
				}
			}
		}

		return matchedAttribute;
	}

	public Concept getConcept() {
		return concept;
	}

	public BaseInstance[] load(BufferedReader bR) throws IOException {

		ArrayList<BaseInstance> baseInstances = new ArrayList<BaseInstance>();

		String line;
		boolean hasMoreLines = true;

		while (hasMoreLines) {

			line = bR.readLine();

			if (line == null) {

				hasMoreLines = false;

			} else {

				String[] data = line.split(",");

				int n = data.length;

				StringAttribute[] attributes = new StringAttribute[n - 1];

				for (int i = 0; i < n - 1; i++) {
					attributes[i] = new StringAttribute("a-" + i, data[i]);
				}

				// The last value is assumed to be the class/concept

				baseInstances.add(new BaseInstance(
						new BaseConcept(data[n - 1]), attributes));
			}
		}

		return baseInstances.toArray(new BaseInstance[baseInstances.size()]);
	}

	/**
	 * This method loads the training instances for the user clicks.
	 * 
	 * @param fileName
	 *            the name of the file that contains the user clicks
	 * @throws IOException
	 */
	public BaseInstance[] load(String fileName) throws IOException {

		File file = new File(fileName);
		FileReader fReader = new FileReader(file);
		BufferedReader bR = new BufferedReader(fReader);

		return load(bR);
	}

	/**
	 * Pretty print the information for this Instance
	 */
	public void print() {

		if (attributes != null) {
			for (Attribute a : attributes) {

				if (a == null || a.getName() == null) {
					System.out.print(" -  <NULL ATTRIBUTE> ");
				} else {
					if (a.getValue() == null) {
						System.out.print(" -  <NULL ATTRIBUTE VALUE> ");
					} else {
						System.out.print(" -  " + a.getName() + " = "
								+ a.getValue());
					}
				}
			}
		}

		System.out.println(" -->  " + getConcept().getName());
	}

	/**
	 * @param concept
	 *            the concept to set
	 */
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
}
