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
package org.yooreeka.algos.taxis.boosting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.algos.taxis.ensemble.ClassifierEnsemble;
import org.yooreeka.algos.taxis.ensemble.ConceptMajorityVoter;

public abstract class BoostingARCX4Classifier extends ClassifierEnsemble {

	private TrainingSet originalTSet;

	private int classifierPopulation = 2;

	public BoostingARCX4Classifier(String name, TrainingSet tSet) {
		super(name);
		this.originalTSet = tSet;
	}

	public TrainingSet buildTSet(TrainingSet tSet, double[] w) {

		WeightBasedRandom wRnd = new WeightBasedRandom(w);

		int n = w.length;

		Instance[] sample = new Instance[n];

		Map<Integer, Instance> instances = tSet.getInstances();

		for (int i = 0; i < n; i++) {
			int instanceIndex = wRnd.nextInt();
			sample[i] = instances.get(instanceIndex);
		}

		return new TrainingSet(sample);
	}

	@Override
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

	public abstract Classifier getClassifierForTraining(TrainingSet set);

	/**
	 * @return the classifierPopulation
	 */
	public int getClassifierPopulation() {
		return classifierPopulation;
	}

	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param classifierPopulation
	 *            the classifierPopulation to set
	 */
	public void setClassifierPopulation(int classifierPopulation) {
		this.classifierPopulation = classifierPopulation;
	}

	@Override
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	@Override
	public boolean train() {

		baseClassifiers = new ArrayList<Classifier>();

		int size = originalTSet.getSize();

		/*
		 * Weights that define sample selection
		 */
		double[] w = new double[size];

		/*
		 * Number of times instance was misclassified by classifiers that are
		 * currently in ensemble.
		 */
		int[] m = new int[size];

		double w0 = 1.0 / size;

		Arrays.fill(w, w0);
		Arrays.fill(m, 0);

		for (int i = 0; i < classifierPopulation; i++) {
			if (verbose) {
				System.out.println("Instance weights: " + Arrays.toString(w));
				System.out.println("Instance misclassifications: "
						+ Arrays.toString(m));
			}

			TrainingSet tSet = buildTSet(originalTSet, w);

			Classifier baseClassifier = getClassifierForTraining(tSet);

			baseClassifier.train();

			updateWeights(originalTSet, w, m, baseClassifier);

			baseClassifiers.add(baseClassifier);
		}

		return true;
	}

	public void updateWeights(TrainingSet tSet, double[] w, int[] m,
			Classifier baseClassifier) {

		int n = w.length;

		// update misclassification counts with results from latest classifier
		for (int i = 0; i < n; i++) {
			Instance instance = tSet.getInstance(i);
			Concept actualConcept = baseClassifier.classify(instance);
			Concept expectedConcept = instance.getConcept();
			if (actualConcept == null
					|| !(actualConcept.getName().equals(expectedConcept
							.getName()))) {
				m[i]++;
			}
		}

		// update weights
		double sum = 0.0;
		for (int i = 0; i < n; i++) {
			sum += (1.0 + Math.pow(m[i], 4));
		}

		for (int i = 0; i < n; i++) {
			w[i] = (1.0 + Math.pow(m[i], 4)) / sum;
		}

	}
}
