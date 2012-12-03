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

/**
 * Represents information about the split.
 */
public class SplittingCriterion {

	/**
	 * Returns branch name for continuous attributes.
	 * 
	 * @param attrValue
	 *            attribute value that should be evaluated.
	 * @param splitValue
	 *            split point for continuous attributes.
	 * 
	 * @return name of the branch.
	 */
	public static String getBranchName(Double attrValue, Double splitValue) {
		String branchName = null;

		if (attrValue <= splitValue) {
			branchName = BranchGroup.BinaryBranchNames.TRUE_BRANCH;
		} else {
			branchName = BranchGroup.BinaryBranchNames.FALSE_BRANCH;
		}

		return branchName;
	}

	/**
	 * Returns branch name for discrete attributes. Currently we always create a
	 * separate branch for every discrete attribute.
	 * 
	 * @param attrValue
	 *            attribute value that should be evaluated.
	 * 
	 * @return name of the branch.
	 */
	public static String getBranchName(String attrValue) {
		// Using attribute value as a branch name.
		return attrValue;
	}

	/*
	 * Attribute name to split on
	 */
	private String splitAttributeName;

	/*
	 * Only relevant for continuous attributes. Indicates value that will be
	 * used to decide true/false branch.
	 */
	private Double splitPoint;

	/*
	 * Data by branch. Each branch will have a subset of instances from the
	 * initial set that reached the node. We return it to avoid calculating this
	 * data for every branch again.
	 */
	private BranchGroup splitData;

	public String getSplitAttributeName() {
		return splitAttributeName;
	}

	public BranchGroup getSplitData() {
		return splitData;
	}

	public Double getSplitPoint() {
		return splitPoint;
	}

	public boolean isContinuousValueSplit() {
		return splitPoint != null;
	}

	public boolean isDiscreteValueSplit() {
		return splitPoint == null;
	}

	public void setSplitAttributeName(String splitAttributeName) {
		this.splitAttributeName = splitAttributeName;
	}

	public void setSplitData(BranchGroup splitData) {
		this.splitData = splitData;
	}

	public void setSplitPoint(Double splitPoint) {
		this.splitPoint = splitPoint;
	}

}
