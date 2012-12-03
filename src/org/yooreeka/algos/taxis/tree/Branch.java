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
import java.util.List;
import java.util.Map;

import org.yooreeka.algos.taxis.core.intf.Instance;

public class Branch {
	public static void addInstance(Map<String, Branch> branches,
			String branchName, Instance i) {

		Branch branch = branches.get(branchName);
		if (branch == null) {
			branch = new Branch(branchName);
			branches.put(branchName, branch);
		}

		branch.add(i);
	}
	private String branchName;

	private List<Instance> data;

	public Branch() {
		init(null);
	}

	public Branch(String name) {
		init(name);
	}

	public void add(Instance instance) {
		this.data.add(instance);
	}

	public void add(List<Instance> multipleInstances) {
		this.data.addAll(multipleInstances);
	}

	public List<Instance> getData() {
		return data;
	}

	public String getName() {
		return branchName;
	}

	private void init(String name) {
		branchName = name;
		data = new ArrayList<Instance>();
	}

	public void setData(List<Instance> data) {
		this.data = data;
	}

	public void setName(String name) {
		this.branchName = name;
	}

}
