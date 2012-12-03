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

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;

public class TrainingSet implements Serializable {

	/**
	 * A unique ID, just in case that we want to serialize our training
	 * instanceSet.
	 */
	private static final long serialVersionUID = 4754213130190809633L;

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	private boolean verbose = false;
	/**
	 * TODO: 5.x -- Training set management (Book Section 2.4.1 and 5.7)
	 * 
	 * For large training sets, it may be beneficial to serialize them and store
	 * them because loading a large training instanceSet is computationally
	 * expensive.
	 * 
	 * How would you go about merging two training sets? What problems do you
	 * foresee?
	 */
	private HashMap<Integer, Instance> instanceSet;
	private HashSet<Concept> conceptSet;

	private HashSet<String> attributeNameSet;

	public TrainingSet() {

		instanceSet = new HashMap<Integer, Instance>();
	}

	public TrainingSet(Instance[] instances) {

		int instanceId = 0;

		instanceSet = new HashMap<Integer, Instance>();
		conceptSet = new HashSet<Concept>();
		attributeNameSet = new HashSet<String>();

		Concept c;
		for (Instance i : instances) {

			// System.out.println("Instance Added: ");
			// i.print();

			instanceSet.put(instanceId, i);

			c = i.getConcept();
			if (!conceptSet.contains(c)) {

				conceptSet.add(c);
			}

			for (Attribute a : i.getAtrributes()) {
				if (a != null) {
					attributeNameSet.add(a.getName());
				}
			}

			instanceId++;
		}

		if (verbose) {
			System.out
					.println("-------------------------------------------------------------");
			System.out.print("Loaded " + getSize()
					+ " instances that belong into ");
			System.out.println(this.getNumberOfConcepts() + " concepts");
			System.out
					.println("-------------------------------------------------------------");
		}
	}

	public HashSet<String> getAttributeNameSet() {
		return attributeNameSet;
	}

	/**
	 * @return the conceptSet
	 */
	public HashSet<Concept> getConceptSet() {
		return conceptSet;
	}

	public Instance getInstance(int index) {
		return instanceSet.get(index);
	}

	/**
	 * @return the instanceSet
	 */
	public HashMap<Integer, Instance> getInstances() {
		return instanceSet;
	}

	public int getNumberOfConcepts() {
		return conceptSet.size();
	}

	/**
	 * @return the size of the instanceSet
	 */
	public int getSize() {
		return instanceSet.size();
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	public void print() {

		for (Instance i : instanceSet.values()) {
			i.print();
		}
	}

	/**
	 * @param verbose
	 *            the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
