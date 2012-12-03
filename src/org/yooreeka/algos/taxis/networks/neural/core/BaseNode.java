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

import org.yooreeka.algos.taxis.networks.neural.core.intf.Link;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Node;

abstract class BaseNode implements Node {

	private static final long serialVersionUID = 9035029651203728480L;

	protected String nodeId;
	protected double x; // input value
	protected double y; // output value
	protected double bias;
	protected double biasDelta;
	protected List<Link> inlinks;
	protected List<Link> outlinks;

	protected double learningRate;

	/*
	 * Used in training mode.
	 */
	private double expectedOutput;

	public BaseNode(String nodeId) {
		this.nodeId = nodeId;
		this.inlinks = new ArrayList<Link>();
		this.outlinks = new ArrayList<Link>();
	}

	public void addInlink(Link inlink) {
		inlinks.add(inlink);
	}

	public void addOutlink(Link outlink) {
		outlinks.add(outlink);
	}

	public void calculate() {
		this.x = calculateActivation();
		this.y = fireNeuron();
	}

	public double calculateActivation() {
		double result = bias;
		for (Link inL : inlinks) {
			result += inL.getWeight() * inL.getValue();
		}
		x = result;
		return x;
	}

	public void calculateWeightAdjustments() {
		double err = getNodeError();

		for (Link link : getInlinks()) {
			double y = link.getValue();
			double dW = learningRate * y * err;
			link.setWeightDelta(link.getWeightDelta() + dW);
		}

		// Bias adjustments
		setBiasDelta(getBiasDelta() + learningRate * 1 * err);
	}

	public abstract double fireNeuron();

	public abstract double fireNeuronDerivative();

	public double getBias() {
		return bias;
	}

	public double getBiasDelta() {
		return biasDelta;
	}

	public List<Link> getInlinks() {
		return inlinks;
	}

	public double getLastInput() {
		return x;
	}

	public double getLastOutput() {
		return y;
	}

	public double getLearningRate() {
		return this.learningRate;
	}

	//
	public double getNodeError() {
		// For output node
		if (outlinks == null || outlinks.size() == 0) {
			double d = expectedOutput;
			/*
			 * Assuming E = 1/2 * ( d - y )^2
			 */
			// return (d - y) * (1 - y) * y;
			return (d - y) * fireNeuronDerivative();

		} else { // for hidden node
			double s = 0.0;

			for (Link outlink : outlinks) {
				Node node = outlink.getToNode();
				s += node.getNodeError() * outlink.getWeight();
			}

			return fireNeuronDerivative() * s;
		}
	}

	public String getNodeId() {
		return nodeId;
	}

	public List<Link> getOutlinks() {
		return outlinks;
	}

	public double getOutput() {
		return y;
	}

	public double getOutputValue() {
		return y;
	}

	// Should it be at the link level?
	public double inputF(List<Link> inputs) {
		if (inputs == null || inputs.size() == 0) {
			return y;
		} else {
			double result = bias;
			for (Link inL : inputs) {
				result += inL.getWeight() * inL.getValue();
			}
			return result;
		}
	}

	public void propagate() {
		for (Link outL : outlinks) {
			outL.setValue(y);
		}
	}

	public void setBias(double b) {
		this.bias = b;
	}

	public void setBiasDelta(double db) {
		this.biasDelta = db;
	}

	public void setExpectedOutput(double d) {
		this.expectedOutput = d;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public void setOutput(double y) {
		this.y = y;
	}

	public void updateWeights() {

		for (Link link : getInlinks()) {
			link.setWeight(link.getWeight() + link.getWeightDelta());
			link.setWeightDelta(0.0);
		}

		// Bias adjustments
		setBias(getBias() + getBiasDelta());
		setBiasDelta(0.0);
	}
}
