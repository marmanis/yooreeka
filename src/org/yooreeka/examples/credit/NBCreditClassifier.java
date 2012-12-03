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

import org.yooreeka.algos.taxis.bayesian.NaiveBayes;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.credit.data.UserDataset;
import org.yooreeka.examples.credit.data.users.User;
import org.yooreeka.examples.credit.util.UserInstanceBuilder;

public class NBCreditClassifier extends NaiveBayes {

	private static String createDefaultClassifierName() {
		return NBCreditClassifier.class.getSimpleName();
	}

	private static UserInstanceBuilder createDefaultInstanceBuilder() {
		// using Instance Builder configured to produce instances with String
		// attributes
		return new UserInstanceBuilder(false);
	}

	private UserInstanceBuilder instanceBuilder;

	public NBCreditClassifier(String name, TrainingSet ts,
			UserInstanceBuilder instanceBuilder) {

		super(name, ts);

		this.instanceBuilder = instanceBuilder;
	}

	public NBCreditClassifier(String name, UserDataset ds) {
		this(name, ds, createDefaultInstanceBuilder());
	}

	public NBCreditClassifier(String name, UserDataset ds,
			UserInstanceBuilder instanceBuilder) {

		this(name, instanceBuilder.createTrainingSet(ds), instanceBuilder);

	}

	public NBCreditClassifier(TrainingSet ts) {

		super(createDefaultClassifierName(), ts);

		this.instanceBuilder = createDefaultInstanceBuilder();
	}

	public NBCreditClassifier(UserDataset ds) {
		this(createDefaultClassifierName(), ds);
	}

	@Override
	public Concept classify(Instance instance) {
		return super.classify(instance);
	}

	public Concept classify(User user) {
		return classify(instanceBuilder.createInstance(user));
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

	public void useDefaultAttributes() {
		trainOnAttribute(CreditInstance.ATTR_NAME_JOB_CLASS);
		trainOnAttribute(CreditInstance.ATTR_NAME_INCOME_TYPE);
		trainOnAttribute(CreditInstance.ATTR_NAME_AGE);
		trainOnAttribute(CreditInstance.ATTR_NAME_CAR_OWNERSHIP);
		trainOnAttribute(CreditInstance.ATTR_NAME_CREDIT_SCORE);
		trainOnAttribute(CreditInstance.ATTR_NAME_MORTGAGE_DOWN_PAYMENT);
		trainOnAttribute(CreditInstance.ATTR_NAME_MOTOR_BICYCLE_OWNERSHIP);
		trainOnAttribute(CreditInstance.ATTR_NAME_OTHER_PROPERTY_OWNERSHIP);
		trainOnAttribute(CreditInstance.ATTR_NAME_CRIMINAL_RECORD);
		trainOnAttribute(CreditInstance.ATTR_NAME_BANKRUPTCY);
		trainOnAttribute(CreditInstance.ATTR_NAME_RETIREMENT_ACCOUNT);
	}

}
