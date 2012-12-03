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

import java.util.ArrayList;
import java.util.List;

import org.yooreeka.algos.taxis.networks.neural.core.intf.Layer;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Link;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Node;

public class BaseLayer implements Layer {

	private static final long serialVersionUID = -1482668413756729940L;

	private int layerId;
	private List<Node> nodes;

	public BaseLayer(int layerId) {
		this.layerId = layerId;
		this.nodes = new ArrayList<Node>();
	}

	public void addNode(Node n) {
		nodes.add(n);
	}

	public void calculate() {
		for (Node node : nodes) {
			node.calculate();
		}
	}

	public void calculateWeightAdjustments() {
		for (Node node : nodes) {
			node.calculateWeightAdjustments();
		}
	}

	public int getId() {
		return layerId;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public String getType() {
		return "";
	}

	public double[] getValues() {

		double[] y = new double[nodes.size()];

		for (int i = 0, n = y.length; i < n; i++) {
			y[i] = nodes.get(i).getOutput();
		}

		return y;
	}

	public void printWeights() {
		for (Node n : nodes) {
			for (Link link : n.getInlinks()) {
				System.out.println(link.getFromNode().getNodeId() + "->"
						+ n.getNodeId() + ":" + link.getWeight());
			}
		}
	}

	public void propagate() {
		for (Node node : nodes) {
			node.propagate();
		}
	}

	public void setExpectedOutputValues(double[] d) {
		if (nodes.size() != d.length) {
			throw new RuntimeException("Invalid layer configuration. "
					+ "Layer id: " + layerId + ", Expected number of nodes: "
					+ d.length + ", Actual number of nodes: " + nodes.size());
		}

		for (int i = 0, n = d.length; i < n; i++) {
			Node node = nodes.get(i);
			node.setExpectedOutput(d[i]);
		}
	}

	public void setInputValues(double[] x) {
		if (nodes.size() != x.length) {
			throw new RuntimeException("Invalid layer configuration. "
					+ "Layer id: " + layerId + ", Expected number of nodes: "
					+ x.length + ", Actual number of nodes: " + nodes.size());
		}

		for (int i = 0, n = x.length; i < n; i++) {
			Node node = nodes.get(i);
			Link inlink = node.getInlinks().get(0);
			inlink.setValue(x[i]);
		}
	}

	public void updateWeights() {
		for (Node node : nodes) {
			node.updateWeights();
		}
	}

}
