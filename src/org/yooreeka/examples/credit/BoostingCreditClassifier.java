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

import org.yooreeka.algos.taxis.boosting.BoostingARCX4Classifier;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.examples.credit.data.UserDataset;
import org.yooreeka.examples.credit.data.users.User;
import org.yooreeka.examples.credit.util.UserInstanceBuilder;

public class BoostingCreditClassifier extends BoostingARCX4Classifier {

	private UserInstanceBuilder instanceBuilder;

	private ClassifierMemberType classifierType;

	public BoostingCreditClassifier(String name, UserDataset ds,
			UserInstanceBuilder instanceBuilder) {
		this(name, instanceBuilder, instanceBuilder.createTrainingSet(ds));
	}

	public BoostingCreditClassifier(String name,
			UserInstanceBuilder instanceBuilder, TrainingSet tSet) {

		super(name, tSet);

		this.instanceBuilder = instanceBuilder;
	}

	public BoostingCreditClassifier(UserDataset ds) {

		this(BoostingCreditClassifier.class.getSimpleName(), ds,
				new UserInstanceBuilder(false));

	}

	public Concept classify(User user) {

		if (verbose) {
			System.out.println("User:\n  >> " + user.toString());
		}

		return classify(instanceBuilder.createInstance(user));
	}

	@Override
	public Classifier getClassifierForTraining(TrainingSet set) {

		Classifier baseClassifier = null;

		switch (classifierType) {
		case NEURAL_NETWORK:
			NNCreditClassifier nnClassifier = new NNCreditClassifier(set);
			nnClassifier.setLearningRate(0.01);
			nnClassifier.useDefaultAttributes();
			baseClassifier = nnClassifier;
			break;
		case DECISION_TREE:
			DTCreditClassifier dtClassifier = new DTCreditClassifier(set);
			dtClassifier.useDefaultAttributes();
			dtClassifier.setPruneAfterTraining(true);
			baseClassifier = dtClassifier;
			break;
		case NAIVE_BAYES:
			NBCreditClassifier nbClassifier = new NBCreditClassifier(set);
			nbClassifier.useDefaultAttributes();
			baseClassifier = nbClassifier;
			break;
		default:
			throw new RuntimeException("Invalid classifier member type!");
		}

		return baseClassifier;
	}

	/**
	 * @return the classifierType
	 */
	public ClassifierMemberType getClassifierType() {
		return classifierType;
	}

	public UserInstanceBuilder getInstanceBuilder() {
		return instanceBuilder;
	}

	/**
	 * @param classifierType
	 *            the classifierType to set
	 */
	public void setClassifierType(String type) {

		if (type.equalsIgnoreCase("decision tree")) {
			this.classifierType = ClassifierMemberType.DECISION_TREE;
		} else if (type.equalsIgnoreCase("neural network")) {
			this.classifierType = ClassifierMemberType.NEURAL_NETWORK;
		} else if (type.equalsIgnoreCase("naive bayes")) {
			this.classifierType = ClassifierMemberType.NAIVE_BAYES;
		}
	}
}
