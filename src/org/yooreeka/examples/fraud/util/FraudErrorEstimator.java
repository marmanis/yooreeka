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
package org.yooreeka.examples.fraud.util;

import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.fraud.DTFraudClassifier;
import org.yooreeka.examples.fraud.NNFraudClassifier;
import org.yooreeka.examples.fraud.TransactionConcept;
import org.yooreeka.examples.fraud.data.Transaction;
import org.yooreeka.examples.fraud.data.TransactionDataset;
import org.yooreeka.examples.fraud.data.TransactionInstanceBuilder;

public class FraudErrorEstimator {

	private Classifier classifier;
	private TransactionInstanceBuilder instanceBuilder;
	private TransactionDataset testDS;

	private int correctCount = 0;
	private int incorrectValidCount = 0;
	private int incorrectFraudCount = 0;
	private int totalFraudTxnsCount = 0;

	public FraudErrorEstimator(TransactionDataset testDS,
			DTFraudClassifier classifier) {

		this.testDS = testDS;
		this.classifier = classifier;
		this.instanceBuilder = classifier.getInstanceBuilder();
	}

	public FraudErrorEstimator(TransactionDataset testDS,
			NNFraudClassifier classifier) {

		this.testDS = testDS;

		if (classifier.isVerbose()) {
			classifier.setVerbose(false);
		}

		this.classifier = classifier;
		this.instanceBuilder = classifier.getInstanceBuilder();
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public int getIncorrectFraudCount() {
		return incorrectFraudCount;
	}

	public int getIncorrectValidCount() {
		return incorrectValidCount;
	}

	public int getTotalFraudTxnsCount() {
		return totalFraudTxnsCount;
	}

	public void run() {

		for (Transaction txn : testDS.getTransactions()) {
			Instance i = instanceBuilder.createInstance(txn);
			Concept concept = classifier.classify(i);
			Concept expectedConcept = new TransactionConcept(txn.isFraud());

			if (txn.isFraud()) {
				totalFraudTxnsCount++;
			}

			if (concept.getName().equals(expectedConcept.getName())) {
				correctCount++;
			} else {
				// Print classified instance
				i.print();
				if (!txn.isFraud()) {
					incorrectValidCount++;
				} else {
					incorrectFraudCount++;
				}
			}
		}

		System.out.println("Total test dataset txns: " + testDS.getSize()
				+ ", Number of fraud txns:" + getTotalFraudTxnsCount());

		System.out.println("Classified correctly: " + getCorrectCount()
				+ ", Misclassified valid txns: " + getIncorrectValidCount()
				+ ", Misclassified fraud txns: " + getIncorrectFraudCount());
	}

}
