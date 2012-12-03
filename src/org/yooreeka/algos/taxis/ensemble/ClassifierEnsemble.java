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
package org.yooreeka.algos.taxis.ensemble;

import java.util.ArrayList;
import java.util.List;

import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;

/**
 * Base implementation for bagging classifier.
 */
public abstract class ClassifierEnsemble implements Classifier {

	public enum ClassifierMemberType {
		NEURAL_NETWORK, DECISION_TREE, NAIVE_BAYES
	}

	protected String name;

	protected boolean verbose = false;

	protected List<Classifier> baseClassifiers = new ArrayList<Classifier>();

	public ClassifierEnsemble(String name) {
		this.name = name;
	}

	public void addMember(Classifier baseClassifier) {
		baseClassifiers.add(baseClassifier);
	}

	public Concept classify(Instance instance) {

		ConceptMajorityVoter voter = new ConceptMajorityVoter(instance);

		for (Classifier baseClassifier : baseClassifiers) {

			Concept c = baseClassifier.classify(instance);

			voter.addVote(c);
		}

		if (verbose) {
			voter.print();
		}

		return voter.getWinner();
	}

	public int getEnsemblePopulation() {
		return baseClassifiers.size();
	}

	public String getName() {
		return name;
	}

	public void removeMember(Classifier c) {
		baseClassifiers.remove(c);
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean train() {

		for (Classifier c : baseClassifiers) {
			// training base classifier
			c.train();
		}

		return true;
	}
}
