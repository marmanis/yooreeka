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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yooreeka.algos.taxis.networks.neural.core.intf.Layer;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Link;
import org.yooreeka.algos.taxis.networks.neural.core.intf.NeuralNetwork;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Node;

public class BaseNN implements NeuralNetwork, java.io.Serializable {

	private static final long serialVersionUID = -7859066535923217638L;

	private static final double ERROR_THRESHOLD = 0.001d;
	private static final double CONVERGENCE_THRESHOLD = 1E-10;
	private static final double LEARNING_RATE = 0.25;

	private boolean verbose = false;

	/*
	 * Network name
	 */
	private String name;

	/*
	 * Contains nodes that belong to input layer.
	 */
	private Layer inputLayer;

	/*
	 * Contains nodes that belong to output layer.
	 */
	private Layer outputLayer;

	/*
	 * 0..* hidden layers.
	 */
	private List<Layer> hiddenLayers;

	private double learningRate = LEARNING_RATE;

	/*
     * 
     */
	private Map<String, Node> allNodes;

	public BaseNN(String name) {
		this.name = name;
		this.hiddenLayers = new ArrayList<Layer>();
		this.allNodes = new HashMap<String, Node>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#addHiddenLayer(iweb2.ch5.
	 * classification.nn.intf.Layer)
	 */
	public void addHiddenLayer(Layer hiddenLayer) {
		hiddenLayers.add(hiddenLayer);
		for (Node node : hiddenLayer.getNodes()) {
			addNode(node);
		}
	}

	private void addNode(Node node) {
		String nodeId = node.getNodeId();
		if (allNodes.containsKey(nodeId)) {
			throw new RuntimeException("Duplicate nodeId: " + nodeId);
		}
		allNodes.put(nodeId, node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#classify(double[])
	 */
	public double[] classify(double[] x) {

		inputLayer.setInputValues(x);
		inputLayer.calculate();
		inputLayer.propagate();

		for (Layer hLayer : hiddenLayers) {
			hLayer.calculate();
			hLayer.propagate();
		}

		outputLayer.calculate();
		double[] y = outputLayer.getValues();
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#createHiddenLayer(int,
	 * int, double[])
	 */
	public Layer createHiddenLayer(int layerId, int nNodes, double[] bias) {
		if (bias.length != nNodes) {
			throw new RuntimeException("Each node should have bias defined.");
		}
		BaseLayer baseLayer = new BaseLayer(layerId);
		for (int i = 0; i < nNodes; i++) {
			Node node = createHiddenNode(layerId + ":" + i);
			node.setBias(bias[i]);
			baseLayer.addNode(node);
		}
		return baseLayer;
	}

	protected Node createHiddenNode(String nodeId) {
		Node node = new SigmoidNode(nodeId);
		node.setLearningRate(learningRate);
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#createInputLayer(int, int)
	 */
	public Layer createInputLayer(int layerId, int nNodes) {

		BaseLayer baseLayer = new BaseLayer(layerId);
		for (int i = 0; i < nNodes; i++) {
			Node node = createInputNode(layerId + ":" + i);
			Link inlink = new BaseLink();
			inlink.setFromNode(node);
			inlink.setWeight(1.0);
			node.addInlink(inlink);
			baseLayer.addNode(node);
		}

		return baseLayer;
	}

	protected Node createInputNode(String nodeId) {
		Node node = new LinearNode(nodeId);
		node.setLearningRate(learningRate);
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#createOutputLayer(int,
	 * int, double[])
	 */
	public Layer createOutputLayer(int layerId, int nNodes, double[] bias) {
		if (bias.length != nNodes) {
			throw new RuntimeException("Each node should have bias defined.");
		}

		BaseLayer baseLayer = new BaseLayer(layerId);
		for (int i = 0; i < nNodes; i++) {
			Node node = createOutputNode(layerId + ":" + i);
			node.setBias(bias[i]);
			baseLayer.addNode(node);
		}
		return baseLayer;
	}

	protected Node createOutputNode(String nodeId) {
		Node node = new LinearNode(nodeId);
		node.setLearningRate(learningRate);
		return node;
	}

	private double error(double[] expectedY, double[] actualY) {

		double sum = 0.0;

		for (int i = 0, n = expectedY.length; i < n; i++) {
			sum += Math.pow(actualY[i] - expectedY[i], 2.0);
		}

		return sum / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#getInputNodeCount()
	 */
	public int getInputNodeCount() {
		return getNodeCount(this.inputLayer);
	}

	public double getLearningRate() {
		return learningRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#getName()
	 */
	public String getName() {
		return name;
	}

	private int getNodeCount(Layer layer) {
		int nodeCount = 0;

		if (layer != null) {
			nodeCount = layer.getNodes().size();
		}

		return nodeCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#getOutputNodeCount()
	 */
	public int getOutputNodeCount() {
		return getNodeCount(this.outputLayer);
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#printWeights()
	 */
	public void printWeights() {
		for (Layer layer : hiddenLayers) {
			System.out.println(String.valueOf(layer.getId()) + ":");
			layer.printWeights();
		}
		System.out.println(String.valueOf(outputLayer.getId()) + ":");
		outputLayer.printWeights();
	}

	public void removeAllNodesAndLayers() {
		this.allNodes.clear();
		this.hiddenLayers.clear();
		this.inputLayer = null;
		this.outputLayer = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#setInputLayer(iweb2.ch5.
	 * classification.nn.intf.Layer)
	 */
	public void setInputLayer(Layer inputLayer) {
		this.inputLayer = inputLayer;
		for (Node node : this.inputLayer.getNodes()) {
			addNode(node);
		}
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public void setLink(String fromNodeId, String toNodeId, double w) {
		Link link = new BaseLink();
		Node fromNode = allNodes.get(fromNodeId);
		if (fromNode == null) {
			throw new RuntimeException("Unknown node id: " + fromNodeId);
		}
		Node toNode = allNodes.get(toNodeId);
		if (toNode == null) {
			throw new RuntimeException("Unknown node id: " + toNodeId);
		}

		link.setFromNode(fromNode);
		link.setToNode(toNode);
		link.setWeight(w);

		fromNode.addOutlink(link);
		toNode.addInlink(link);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setOutputLayer(Layer outputLayer) {
		this.outputLayer = outputLayer;
		for (Node node : this.outputLayer.getNodes()) {
			addNode(node);
		}
	}

	/**
	 * @param verbose
	 *            the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	// trains NN with one training sample at a time
	/*
	 * (non-Javadoc)
	 * 
	 * @see iweb2.ch5.classification.nn.NeuralNetwork#train(double[], double[])
	 */
	public void train(double[] tX, double[] tY) {

		double lastError = 0.0;
		int i = 0;
		while (true) {
			i++;
			// Evaluate sample
			double[] y = classify(tX);

			double err = error(tY, y);

			if (Double.isInfinite(err) || Double.isNaN(err)) {
				// Couldn't even evaluate the error. Stop.
				throw new RuntimeException(
						"Training failed. Couldn't evaluate the error: "
								+ err
								+ ". Try some other NN configuration, parameters.");
			}

			double convergence = Math.abs(err - lastError);

			if (err <= ERROR_THRESHOLD) {
				// Good enough. No need to adjust weights for this sample.
				lastError = err;
				if (verbose) {
					System.out.print("Error Threshold: " + ERROR_THRESHOLD);
					System.out.print(" |  Error Achieved: " + err);
					System.out.print(" |  Number of Iterations: " + i);
					System.out.println(" |  Absolute convergence: "
							+ convergence);
				}
				break;
			}

			if (convergence <= CONVERGENCE_THRESHOLD) { // If we made almost no
														// progress stop.
				// No change. Stop.
				if (verbose) {
					System.out.print("Error Threshold: " + ERROR_THRESHOLD);
					System.out.print(" |  Error Achieved: " + err);
					System.out.print(" |  Number of Iterations: " + i);
					System.out.println(" |  Absolute convergence: "
							+ convergence);
				}
				break;
			}

			lastError = err;

			// Set expected values so that we can determine the error
			outputLayer.setExpectedOutputValues(tY);

			/*
			 * Calculate weight adjustments in the whole network
			 */

			outputLayer.calculateWeightAdjustments();

			for (Layer hLayer : hiddenLayers) {
				// layer order doesn't matter because we will update weights
				// later
				hLayer.calculateWeightAdjustments(); // WeightIncrements
			}

			/*
			 * Update Weights
			 */

			outputLayer.updateWeights();

			for (Layer hLayer : hiddenLayers) {
				// layer order doesn't matter.
				hLayer.updateWeights();
			}
		}
		// System.out.println("i = " + i + ", err = " + lastError);
	}

}
