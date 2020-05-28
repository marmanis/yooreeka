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

import java.util.List;
import java.util.Map;

import org.yooreeka.algos.taxis.core.intf.Instance;

public class InfoGain {

	public InfoGain() {
	}

	/**
	 * Entropy of the dataset.
	 * 
	 * @param data
	 * @return
	 */
	public Double entropy(List<Instance> data) {

		/*
		 * How many times each class (category) occurs in the data.
		 */
		Map<String, Integer> instanceCountByClassMap = ConceptUtils
				.countConcepts(data);

		int n = data.size();

		double sum = 0.0;

		for (Integer count : instanceCountByClassMap.values()) {

			double p = (double) count / (double) n;

			sum += p * log2(p);

		}

		return -sum;

	}

	public Double expectedInformation(List<Instance> allData,
			List<List<Instance>> allDataSubsets) {

		double sum = 0.0;

		int n = allData.size();

		for (List<Instance> dataSubset : allDataSubsets) {

			sum += (double) dataSubset.size() / (double) n
					* entropy(dataSubset);

		}

		return sum;

	}

	/**
	 * Information gain for a given split.
	 * 
	 * @param allData
	 *            initial set of instances.
	 * @param allDataSubsets
	 *            initial set split into subsets.
	 * 
	 * @return information gain.
	 */
	public Double gain(List<Instance> allData,
			List<List<Instance>> allDataSubsets) {

		return entropy(allData) - expectedInformation(allData, allDataSubsets);

	}

	/**
	 * Gain ratio.
	 * 
	 * @param allData
	 *            initial set of instances.
	 * @param allDataSubsets
	 *            initial set split into subsets.
	 * 
	 * @return gain ratio.
	 */
	public Double gainRatio(List<Instance> allData,
			List<List<Instance>> allDataSubsets) {

		return gain(allData, allDataSubsets)
				/ splitInfo(allData, allDataSubsets);

	}

	private double log2(double d) {

		return Math.log(d) / Math.log(2.0);

	}

	public Double splitInfo(List<Instance> allData,
			List<List<Instance>> allDataSubsets) {

		double sum = 0.0;

		int n = allData.size();

		for (List<Instance> dataSubset : allDataSubsets) {

			double ratio = (double) dataSubset.size() / (double) n;

			sum += ratio * log2(ratio);

		}

		return -sum;

	}

}