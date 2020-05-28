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
package org.yooreeka.algos.taxis.networks.neural.core;

import org.yooreeka.algos.taxis.networks.neural.core.intf.Link;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Node;

public class BaseLink implements Link {

	private static final long serialVersionUID = 6462508677299269035L;

	private Node fromNode;
	private Node toNode;
	private double value;
	private double weight;
	private double weightDelta;

	public Node getFromNode() {
		return fromNode;
	}

	public Node getToNode() {
		return toNode;
	}

	public double getValue() {
		return value;
	}

	public double getWeight() {
		return weight;
	}

	public double getWeightDelta() {
		return weightDelta;
	}

	public void setFromNode(Node unit) {
		this.fromNode = unit;
	}

	public void setToNode(Node unit) {
		this.toNode = unit;
	}

	public void setValue(double x) {
		this.value = x;
	}

	public void setWeight(double w) {
		this.weight = w;
	}

	public void setWeightDelta(double dw) {
		weightDelta = dw;
	}
}
