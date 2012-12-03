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

import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.ensemble.ClassifierEnsemble;
import org.yooreeka.examples.credit.data.UserDataset;
import org.yooreeka.examples.credit.data.users.User;
import org.yooreeka.examples.credit.util.BootstrapTrainingSetBuilder;
import org.yooreeka.examples.credit.util.UserInstanceBuilder;

public class BaggingCreditClassifier extends ClassifierEnsemble {

	private UserInstanceBuilder instanceBuilder;
	private BootstrapTrainingSetBuilder bootstrapTSetBuilder;

	public BaggingCreditClassifier(UserDataset ds) {

		super(BaggingCreditClassifier.class.getSimpleName());

		/* Creating instance builder for this classifier */
		instanceBuilder = new UserInstanceBuilder(false);

		/*
		 * Creating original training set that will be used to generate
		 * bootstrap sets
		 */
		TrainingSet originalTSet = instanceBuilder.createTrainingSet(ds);

		bootstrapTSetBuilder = new BootstrapTrainingSetBuilder(originalTSet);
	}

	public Concept classify(User user) {

		if (verbose) {
			System.out.println("User:\n  >> " + user.toString());
		}

		return classify(instanceBuilder.createInstance(user));
	}

	public TrainingSet getBootstrapSet() {
		return bootstrapTSetBuilder.buildBootstrapSet();
	}

	public UserInstanceBuilder getInstanceBuilder() {
		return instanceBuilder;
	}

}
