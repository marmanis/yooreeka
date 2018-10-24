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

import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.tree.DecisionTreeClassifier;
import org.yooreeka.examples.fraud.data.Transaction;
import org.yooreeka.examples.fraud.data.TransactionDataset;
import org.yooreeka.examples.fraud.data.TransactionInstanceBuilder;

public class DTFraudClassifier extends DecisionTreeClassifier {

	private static final long serialVersionUID = 5491106283513021975L;

	public static DTFraudClassifier loadClassifier(String filename) {

		Object o = null;
		File f = new File(filename);
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

		return (DTFraudClassifier) o;

	}

	public static void saveClassifier(String filename, DTFraudClassifier o) {

		try {
			File f = new File(filename);
			FileOutputStream foutStream = new FileOutputStream(f);
			BufferedOutputStream boutStream = new BufferedOutputStream(
					foutStream);
			ObjectOutputStream objOutputStream = new ObjectOutputStream(
					boutStream);
			objOutputStream.writeObject(o);
			objOutputStream.flush();
			boutStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Error while saving data into file: '"
					+ filename + "'", e);
		}

		System.out.println("saved classifier in file: " + filename);
	}

	private TransactionInstanceBuilder instanceBuilder;

	public DTFraudClassifier(String name, TransactionDataset ds) {

		super(name, ds.createTrainingDataset());
		this.instanceBuilder = ds.getInstanceBuilder();
	}

	public DTFraudClassifier(TransactionDataset ds) {
		this(DTFraudClassifier.class.getSimpleName(), ds);
	}

	public Concept classify(Transaction t) {
		return classify(instanceBuilder.createInstance(t));
	}

	@Override
	protected Concept createConcept(String category) {
		return new TransactionConcept(category);
	}

	public TransactionInstanceBuilder getInstanceBuilder() {
		return instanceBuilder;
	}

	public void setInstanceBuilder(TransactionInstanceBuilder instanceBuilder) {
		this.instanceBuilder = instanceBuilder;
	}

	public void useDefaultAttributes() {
		trainOnAttribute(TransactionInstance.ATTR_NAME_N_DESCRIPTION, false);
		trainOnAttribute(TransactionInstance.ATTR_NAME_N_LOCATION, false);
		trainOnAttribute(TransactionInstance.ATTR_NAME_N_TXN_AMT, false);

	}
}
