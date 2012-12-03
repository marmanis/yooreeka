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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Instance;

public class BranchGroup {
	/**
	 * Value that is used to identify data subset when the split is done on
	 * continuous value.
	 */
	public static class BinaryBranchNames {
		public static final String TRUE_BRANCH = "true";
		public static final String FALSE_BRANCH = "false";

		private BinaryBranchNames() {
		}
	}
	public static BranchGroup createBranchesFromContiniuousAttr(
			List<Instance> data, String attrName, Double splitPoint) {

		BranchGroup branches = new BranchGroup(attrName);

		for (Instance i : data) {
			Attribute a = i.getAttributeByName(attrName);
			Double value = AttributeUtils.toDouble(a.getValue());
			String branchName = SplittingCriterion.getBranchName(value,
					splitPoint);

			branches.add(branchName, i);
		}

		return branches;
	}

	public static BranchGroup createBranchesFromDiscreteAttr(
			List<Instance> data, String attrName) {

		// Separate branch for each attribute value
		BranchGroup branches = new BranchGroup(attrName);

		for (Instance i : data) {
			Attribute a = i.getAttributeByName(attrName);
			String attrValue = AttributeUtils.toString(a.getValue());
			String branchName = SplittingCriterion.getBranchName(attrValue);

			branches.add(branchName, i);
		}

		return branches;
	}

	private String name;

	private Map<String, Branch> branches;

	public BranchGroup(String name) {
		this.name = name;
		branches = new HashMap<String, Branch>();
	}

	public void add(String branchName, Instance i) {

		Branch branch = branches.get(branchName);
		if (branch == null) {
			branch = new Branch(branchName);
			branches.put(branchName, branch);
		}

		branch.add(i);
	}

	public Branch getBranch(String branchName) {
		return branches.get(branchName);
	}

	public List<Branch> getBranches() {
		return new ArrayList<Branch>(branches.values());
	};

	public List<List<Instance>> getData() {
		List<List<Instance>> allData = new ArrayList<List<Instance>>();

		for (Branch b : branches.values()) {
			List<Instance> branchData = b.getData();
			allData.add(branchData);
		}

		return allData;
	}

	public String getName() {
		return name;
	}

}
