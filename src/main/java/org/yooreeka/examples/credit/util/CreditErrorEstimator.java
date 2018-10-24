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
package org.yooreeka.examples.credit.util;

import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.credit.BaggingCreditClassifier;
import org.yooreeka.examples.credit.BoostingCreditClassifier;
import org.yooreeka.examples.credit.CreditConcept;
import org.yooreeka.examples.credit.DTCreditClassifier;
import org.yooreeka.examples.credit.NBCreditClassifier;
import org.yooreeka.examples.credit.NNCreditClassifier;
import org.yooreeka.examples.credit.data.UserDataset;
import org.yooreeka.examples.credit.data.users.User;

public class CreditErrorEstimator {

	private Classifier classifier;
	private UserInstanceBuilder instanceBuilder;
	private UserDataset testDS;
	private ClassifierResults classifierResults;

	int[][] confusionMatrix = new int[5][5];

	private int correctCount = 0;
	private int misclassifiedInstanceCount = 0;
	private boolean verbose = true;

	public CreditErrorEstimator(UserDataset testDS,
			BaggingCreditClassifier classifier) {

		this.testDS = testDS;
		this.classifier = classifier;
		this.instanceBuilder = classifier.getInstanceBuilder();
		this.classifierResults = new ClassifierResults(classifier.getName(),
				testDS.getSize());
	}

	public CreditErrorEstimator(UserDataset testDS,
			BoostingCreditClassifier classifier) {

		this.testDS = testDS;
		this.classifier = classifier;
		this.instanceBuilder = classifier.getInstanceBuilder();
		this.classifierResults = new ClassifierResults(classifier.getName(),
				testDS.getSize());
	}

	public CreditErrorEstimator(UserDataset testDS,
			DTCreditClassifier classifier) {

		this.testDS = testDS;
		this.classifier = classifier;
		this.instanceBuilder = classifier.getInstanceBuilder();
		this.classifierResults = new ClassifierResults(classifier.getName(),
				testDS.getSize());
	}

	public CreditErrorEstimator(UserDataset testDS,
			NBCreditClassifier classifier) {

		this.testDS = testDS;
		this.classifier = classifier;
		this.instanceBuilder = classifier.getInstanceBuilder();
		this.classifierResults = new ClassifierResults(classifier.getName(),
				testDS.getSize());
	}

	public CreditErrorEstimator(UserDataset testDS,
			NNCreditClassifier classifier) {

		this.testDS = testDS;
		this.classifier = classifier;
		this.instanceBuilder = classifier.getInstanceBuilder();
		this.classifierResults = new ClassifierResults(classifier.getName(),
				testDS.getSize());
	}

	public double getAccuracy() {
		return (double) correctCount / (double) testDS.getSize();
	}

	/**
	 * @return the confusionMatrix
	 */
	public int[][] getConfusionMatrix() {
		return confusionMatrix;
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public int getMisclassifiedInstanceCount() {
		return this.misclassifiedInstanceCount;
	}

	public ClassifierResults getResults() {
		return classifierResults;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void run() {

		correctCount = 0;
		misclassifiedInstanceCount = 0;

		int idx = 0;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				confusionMatrix[i][j] = 0;
			}
		}

		long tStart = System.currentTimeMillis();

		for (User user : testDS.getUsers()) {

			Instance instance = instanceBuilder.createInstance(user);
			Concept concept = classifier.classify(instance);
			Concept expectedConcept = new CreditConcept(user.getCategory());

			String actualCreditLabel = expectedConcept.getName();
			String predictedCreditLabel = concept.getName();

			// Build the confusion matrix
			int i = CreditConcept.getIndex(actualCreditLabel);
			int j = CreditConcept.getIndex(predictedCreditLabel);

			confusionMatrix[i][j]++;

			if (actualCreditLabel.equals(predictedCreditLabel)) {

				correctCount++;

				classifierResults.setResult(idx, true);

			} else {
				// Uncomment the following lines to see the details of the
				// misclassifications
				// System.out.print("Classified as: " + concept.getName() +
				// " ");
				// instance.print();

				misclassifiedInstanceCount++;

				classifierResults.setResult(idx, false);
			}

			idx++;
		}

		if (verbose) {

			long tEnd = System.currentTimeMillis();

			// SUMMARY
			System.out.println(" Classification completed in " + 0.001
					* (tEnd - tStart) + " seconds.\n");
			int totalCount = testDS.getSize();

			System.out.println(" Total test dataset txns: " + totalCount);

			System.out.println("    Classified correctly: " + getCorrectCount()
					+ ", Misclassified: " + getMisclassifiedInstanceCount());

			System.out.println("                Accuracy: " + getAccuracy());
			System.out
					.println("___________________________________________________________\n");
			// DETAILS
			System.out.println("                CONFUSION MATRIX");
			System.out
					.println("___________________________________________________________\n");

			System.out.printf("%4s", "");
			for (int i = 0; i < 5; i++) {
				System.out.printf("%7s", CreditConcept.getLabel(i));
			}
			System.out.println();

			for (int i = 0; i < 5; i++) {
				System.out.printf("%4s", CreditConcept.getLabel(i));
				for (int j = 0; j < 5; j++) {
					System.out.printf("%7s", confusionMatrix[i][j]);
				}
				System.out.println();
			}
			System.out
					.println("___________________________________________________________\n");

		}
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
