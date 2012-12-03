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

import java.util.ArrayList;

import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class BaseConcept implements Concept {

	private String name;
	private BaseConcept parent;

	private ArrayList<Instance> instances = new ArrayList<Instance>();

	public BaseConcept(String name) {
		this.name = name;
	}

	public BaseConcept(String name, BaseConcept parent) {
		this.name = name;
		this.parent = parent;
	}

	public synchronized void addInstance(Instance i) {
		instances.add(i);
	}

	@Override
	public boolean equals(Object obj) {

		final BaseConcept other = (BaseConcept) obj;

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BaseConcept)) {
			return false;
		}

		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}

		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}

		return true;
	}

	public Instance[] getInstances() {
		return instances.toArray(new Instance[instances.size()]);
	}

	public String getName() {
		return name;
	}

	public Concept getParent() {
		return parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	public void setParent(BaseConcept parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return name;
	}

}
