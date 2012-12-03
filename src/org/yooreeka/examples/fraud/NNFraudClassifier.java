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
package org.yooreeka.examples.fraud;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.yooreeka.algos.taxis.core.DoubleAttribute;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.fraud.data.Transaction;
import org.yooreeka.examples.fraud.data.TransactionDataset;
import org.yooreeka.examples.fraud.data.TransactionInstanceBuilder;

public class NNFraudClassifier implements Classifier, java.io.Serializable {

	private static final long serialVersionUID = -1567098614540042277L;

	private static final String SERIALIZATION_PATH = YooreekaConfigurator
			.getHome() + "\\data\\ch05\\";

	public static NNFraudClassifier load(String filename) {

		Object o = null;
		File f = new File(SERIALIZATION_PATH + filename);
		if (f.exists()) {
			try {
				FileInputStream fInStream = new FileInputStream(f);
				BufferedInputStream bufInStream = new BufferedInputStream(
						fInStream);
				ObjectInputStream objInStream = new ObjectInputStream(
						bufInStream);
				o = objInStream.readObject();
				objInStream.close();
			} catch (Exception e) {
				throw new RuntimeException(
						"Error while loading data from file: '" + filename
								+ "'", e);
			}
		} else {
			throw new IllegalArgumentException("File doesn't exist: '"
					+ filename + "'.");
		}

		System.out.println("loaded classifier from file: " + filename);

		return (NNFraudClassifier) o;

	}

	private boolean verbose = false;

	private String name;

	/*
	 * Neural Network that will be used by this classifier.
	 */
	private TransactionNN nn;

	private int DEFAULT_TRAINING_ITERATIONS = 10;

	/*
	 * Number of times to feed training instances into the network during
	 * training.
	 */
	private int nTrainingIterations = DEFAULT_TRAINING_ITERATIONS;

	private TransactionDataset ds;

	private transient TrainingSet ts;

	private TransactionInstanceBuilder instanceBuilder;

	/*
	 * Attribute names that should be used as Neural Network inputs.
	 */
	private List<String> availableAttributeNames;

	public NNFraudClassifier(String name, TransactionDataset ds) {

		this.name = name;

		this.ds = ds;

		this.ts = ds.createTrainingDataset();

		this.instanceBuilder = ds.getInstanceBuilder();

		this.availableAttributeNames = new ArrayList<String>();

		nn = createNeuralNetwork();

	}

	public NNFraudClassifier(TransactionDataset ds) {
		this(NNFraudClassifier.class.getSimpleName(), ds);
	}

	public Concept classify(Instance instance) {

		double[] x = createNNInputs(instance);

		double[] y = nn.classify(x);

		Concept c = createConceptFromNNOutput(y);

		if (verbose) {
			System.out.println("\nAssessment:\n  >> This is a " + c.getName());
		}
		return c;
	}

	public Concept classify(String transactionId) {
		setVerbose(true);
		Transaction t = ds.findTransactionById(transactionId);
		return classify(t);
	}

	public Concept classify(Transaction t) {
		if (verbose) {
			System.out.println("Transaction:\n  >> " + t.toString());
		}
		return classify(instanceBuilder.createInstance(t));
	}

	private Concept createConceptFromNNOutput(double[] y) {

		double threshold = 0.5;

		Concept c = null;

		if (y[0] >= threshold) {
			c = new TransactionConcept(TransactionConcept.CONCEPT_LABEL_FRAUD);
		} else {
			c = new TransactionConcept(TransactionConcept.CONCEPT_LABEL_VALID);
		}

		return c;
	}

	private TransactionNN createNeuralNetwork() {

		String nnName = TransactionNN.class.getSimpleName();

		return new TransactionNN(nnName);
	}

	public double[] createNNInputs(Instance instance) {

		int nInputNodes = nn.getInputNodeCount();

		double[] x = new double[nInputNodes];

		for (int i = 0; i < nInputNodes; i++) {

			String attrName = this.availableAttributeNames.get(i);
			Attribute a = instance.getAttributeByName(attrName);

			if (a instanceof DoubleAttribute) {
				x[i] = (Double) a.getValue();
			} else {
				if (a == null) {
					throw new RuntimeException(
							"Failed to find attribute with name: '" + attrName
									+ "'. Instance: " + instance.toString());
				} else {
					throw new RuntimeException(
							"Invalid attribute type. Only "
									+ DoubleAttribute.class.getSimpleName()
									+ " attribute"
									+ " types can be used in NN. Actual attribute type: "
									+ a.getClass().getSimpleName());
				}
			}

		}

		return x;
	}

	public double[] createNNOutputs(Instance i) {

		int nOutputNodes = nn.getOutputNodeCount();

		double[] y = new double[nOutputNodes];

		if (TransactionConcept.CONCEPT_LABEL_FRAUD.equals(i.getConcept()
				.getName())) {
			y[0] = 1;
		} else {
			y[0] = 0;
		}
		return y;
	}

	public TransactionInstanceBuilder getInstanceBuilder() {
		return this.instanceBuilder;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	public void save() {

		String filename = SERIALIZATION_PATH + this.getName();
		try {
			File f = new File(filename);
			FileOutputStream foutStream = new FileOutputStream(f);
			BufferedOutputStream boutStream = new BufferedOutputStream(
					foutStream);
			ObjectOutputStream objOutputStream = new ObjectOutputStream(
					boutStream);
			objOutputStream.writeObject(this);
			objOutputStream.flush();
			boutStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Error while saving data into file: '"
					+ filename + "'", e);
		}

		System.out.println("saved classifier in file: " + filename);
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setNTrainingIterations(int trainingIterations) {
		nTrainingIterations = trainingIterations;
	}

	/**
	 * @param verbose
	 *            the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean train() {

		if (ts == null) {
			throw new RuntimeException(
					"Can't train classifier - training dataset is null.");
		}

		if (nn == null) {
			throw new RuntimeException(
					"No Neural Network found. Can't proceed.");
		}

		if (nn.getInputNodeCount() != availableAttributeNames.size()) {
			throw new RuntimeException(
					"Number of attributes doesn't match with the number of input nodes."
							+ "Attributes: " + availableAttributeNames.size()
							+ ", Input nodes: " + nn.getInputNodeCount());
		}

		if (nn.getOutputNodeCount() != 1) {
			throw new RuntimeException("NN has " + nn.getOutputNodeCount()
					+ " output nodes. "
					+ "Classifier expects network with only one output node.");
		}

		// Build and train NN
		trainNeuralNetwork(nTrainingIterations);

		return true;
	}

	private void trainNeuralNetwork(int nIterations) {

		for (int i = 1; i <= nIterations; i++) {
			for (Instance instance : ts.getInstances().values()) {
				double[] nnInput = createNNInputs(instance);
				double[] nnExpectedOutput = createNNOutputs(instance);

				nn.train(nnInput, nnExpectedOutput);
			}

			if (verbose) {
				System.out.println("finished training pass: " + i + " out of "
						+ nIterations);
			}
		}

	}

	public void trainOnAttribute(String name) {
		availableAttributeNames.add(name);
	}

	/**
	 * This methods facilitates the loading of training attributes
	 */
	public void useDefaultAttributes() {
		trainOnAttribute(TransactionInstance.ATTR_NAME_N_TXN_AMT);
		trainOnAttribute(TransactionInstance.ATTR_NAME_N_LOCATION);
		trainOnAttribute(TransactionInstance.ATTR_NAME_N_DESCRIPTION);
	}
}
