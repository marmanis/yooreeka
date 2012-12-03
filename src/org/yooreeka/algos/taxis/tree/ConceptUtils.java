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
package org.yooreeka.algos.taxis.tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yooreeka.algos.taxis.core.intf.Instance;

public class ConceptUtils {

	public static Map<String, Integer> countConcepts(List<Instance> instances) {

		Map<String, Integer> conceptCounts = new HashMap<String, Integer>();

		for (Instance i : instances) {
			String conceptName = i.getConcept().getName();
			Integer count = conceptCounts.get(conceptName);
			if (count == null) {
				count = 1;
			} else {
				count++;
			}
			conceptCounts.put(conceptName, count);
		}

		return conceptCounts;
	}

	public static String findMostFrequentConcept(List<Instance> instances) {

		Map<String, Integer> conceptCounts = countConcepts(instances);

		String mostFrequentConceptLabel = null;

		int n = 0;
		for (Map.Entry<String, Integer> e : conceptCounts.entrySet()) {
			if (e.getValue() > n) {
				n = e.getValue();
				mostFrequentConceptLabel = e.getKey();
			}
		}

		return mostFrequentConceptLabel;
	}

	public static String[] getUniqueConcepts(List<Instance> instances) {
		Set<String> concepts = new HashSet<String>();
		for (Instance i : instances) {
			concepts.add(i.getConcept().getName());
		}
		return concepts.toArray(new String[concepts.size()]);
	}
}
