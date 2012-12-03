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
package org.yooreeka.examples.credit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.tree.DecisionTreeClassifier;
import org.yooreeka.examples.credit.data.UserDataset;
import org.yooreeka.examples.credit.data.users.User;
import org.yooreeka.examples.credit.util.UserInstanceBuilder;

public class DTCreditClassifier extends DecisionTreeClassifier {

	private static final long serialVersionUID = 5491106283513021975L;

	private static String createDefaultClassifierName() {
		return DTCreditClassifier.class.getSimpleName();
	}

	private static UserInstanceBuilder createDefaultInstanceBuilder() {
		// using Instance Builder configured to produce instances with String
		// attributes
		return new UserInstanceBuilder(false);
	}

	public static DTCreditClassifier loadClassifier(String filename) {

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

		return (DTCreditClassifier) o;

	}

	public static void saveClassifier(String filename, DTCreditClassifier o) {

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

	private UserInstanceBuilder instanceBuilder;

	private boolean pruneAfterTraining;

	public DTCreditClassifier(String name, TrainingSet ts,
			UserInstanceBuilder instanceBuilder) {

		super(name, ts);

		this.instanceBuilder = instanceBuilder;
		this.pruneAfterTraining = true;

	}

	public DTCreditClassifier(String name, UserDataset ds) {
		this(name, ds, createDefaultInstanceBuilder());

	}

	public DTCreditClassifier(String name, UserDataset ds,
			UserInstanceBuilder instanceBuilder) {

		this(name, instanceBuilder.createTrainingSet(ds), instanceBuilder);
	}

	public DTCreditClassifier(TrainingSet ts) {
		this(createDefaultClassifierName(), ts, createDefaultInstanceBuilder());
	}

	public DTCreditClassifier(UserDataset ds) {
		this(createDefaultClassifierName(), ds);
	}

	public Concept classify(User u) {
		return classify(instanceBuilder.createInstance(u));
	}

	public Concept classify(User u, boolean print) {
		Concept c = classify(u);
		if (print) {
			System.out.println("Actual ---> " + u.getCategory()
					+ "\nAssigned -> " + c.getName());
		}
		return c;
	}

	public UserInstanceBuilder getInstanceBuilder() {
		return this.instanceBuilder;
	}

	public boolean isPruneAfterTraining() {
		return pruneAfterTraining;
	}

	public void setPruneAfterTraining(boolean pruneAfterTraining) {
		this.pruneAfterTraining = pruneAfterTraining;
	}

	@Override
	public boolean train() {
		boolean result = super.train();
		if (result && pruneAfterTraining) {
			this.pruneTree();
		}
		return result;
	}

	public void useDefaultAttributes() {
		trainOnAttribute(CreditInstance.ATTR_NAME_JOB_CLASS, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_INCOME_TYPE, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_AGE, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_CAR_OWNERSHIP, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_CREDIT_SCORE, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_MORTGAGE_DOWN_PAYMENT, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_MOTOR_BICYCLE_OWNERSHIP, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_OTHER_PROPERTY_OWNERSHIP,
				true);
		trainOnAttribute(CreditInstance.ATTR_NAME_CRIMINAL_RECORD, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_BANKRUPTCY, true);
		trainOnAttribute(CreditInstance.ATTR_NAME_RETIREMENT_ACCOUNT, true);
	}

}
