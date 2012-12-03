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

import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Instance;

public class AttributeSelector implements java.io.Serializable {

	private static final long serialVersionUID = 1722498208605607524L;

	public AttributeSelector() {

	}

	/**
	 * Evaluates all candidate attributes and chooses one that provides the best
	 * split of the data.
	 * 
	 * @param data
	 *            data that will be used to evaluate split quality.
	 * @param candidateAttributes
	 *            attributes to chose from.
	 * 
	 * @return information about selected attribute along with the data for
	 *         every branch produced by this split.
	 */
	public SplittingCriterion apply(List<Instance> data,
			List<AttributeDefinition> candidateAttributes) {

		int n = candidateAttributes.size();

		double bestGainRatio = Double.MIN_VALUE;

		SplittingCriterion splitCriterion = new SplittingCriterion();

		/* Calculate Gain Ratio for every available attribute. */
		for (int i = 0; i < n; i++) {
			AttributeDefinition attrDef = candidateAttributes.get(i);
			String attrName = attrDef.getName();
			Double splitPoint = null;

			BranchGroup branches = null;

			if (attrDef.isDiscrete()) {
				/*
				 * For discrete attribute we split all data into subsets based
				 * on attribute values.
				 */
				branches = BranchGroup.createBranchesFromDiscreteAttr(data,
						attrName);
			} else {
				/*
				 * For continuous attribute we pick a value that is in the
				 * middle of min and max attribute values that are present in
				 * the data.
				 */
				splitPoint = pickSplitPoint(data, attrName);

				/*
				 * All data will be split into two groups: group with values x
				 * <= splitPoint and group with values x > splitPoint
				 */
				branches = BranchGroup.createBranchesFromContiniuousAttr(data,
						attrName, splitPoint);
			}

			// Only consider attributes that split the data into more than one
			// branch
			if (branches.getBranches().size() > 1) {
				Double gainRatio = calculateGainRatio(data, branches);

				if (gainRatio > bestGainRatio) {
					bestGainRatio = gainRatio;
					splitCriterion.setSplitAttributeName(attrName);
					splitCriterion.setSplitPoint(splitPoint);
					splitCriterion.setSplitData(branches);
				}
			}
		}

		return splitCriterion;
	}

	private Double calculateGainRatio(List<Instance> allData,
			BranchGroup branches) {

		List<List<Instance>> dataByBranch = branches.getData();

		InfoGain infoGain = new InfoGain();

		return infoGain.gainRatio(allData, dataByBranch);
	}

	/*
	 * Calculates a value to split on for continuous valued attributes.
	 */
	private Double pickSplitPoint(List<Instance> data, String attrName) {
		Double minValue = Double.MAX_VALUE;
		Double maxValue = Double.MIN_VALUE;

		for (Instance i : data) {
			Attribute a = i.getAttributeByName(attrName);
			Double value = AttributeUtils.toDouble(a.getValue());
			if (value != null && value < minValue) {
				minValue = value;
			}
			if (value != null && value > maxValue) {
				maxValue = value;
			}
		}

		return (maxValue - minValue) / 2.0;
	}
}
