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
package org.yooreeka.algos.taxis.bayesian;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.yooreeka.algos.taxis.core.AttributeValue;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.P;

/**
 * A basic implementation of the Naive Bayes algorithm.
 * The emphasis is on teaching the algorithm, not optimizing its performance.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 */
public class NaiveBayes implements Classifier {

	private static final Logger LOG = Logger.getLogger(NaiveBayes.class.getName());

	/**
	 * You can use the NaiveBayes classifier in many occasions So, let's give it
	 * a name to identify the instance of the Classifier.
	 */
	private String name;

	/**
	 * Every classifier needs a training set. Notice that both the name of the
	 * classifier and its training set are intentionally set during the
	 * Construction phase.
	 * 
	 * Once you created an instance of the NaiveBayes classifier you cannot set
	 * its TrainingSet but you can always get the reference to it and add
	 * instances.
	 */
	protected TrainingSet tSet;

	/**
	 * These are the probabilities for each concept
	 */
	protected Map<Concept, Double> conceptPriors;

	/**
	 * This structure contains the fundamental calculation elements of the Naive
	 * Bayes method, i.e. the conditional probabilities.
	 */
	protected Map<Concept, Map<Attribute, AttributeValue>> p;

	/**
	 * These are the attribute indices that we should consider for training
	 */
	protected ArrayList<String> attributeList;

	/** An auxiliary variable */
	protected boolean verbose = false;

	/**
	 * The only constructor for this classifier takes a name and a training set
	 * as arguments.
	 * 
	 * @param name
	 *            the name of the classifier
	 * @param set
	 *            the training set for this classifier
	 */
	public NaiveBayes(String name, TrainingSet set) {

		LOG.setLevel(YooreekaConfigurator.getLevel(NaiveBayes.class.getName()));

		this.name = name;
		tSet = set;

		conceptPriors = new HashMap<>(tSet.getNumberOfConcepts());
		verbose = false;
	}

	/**
	 * Strictly speaking these are not the prior probabilities but just the
	 * counts. However, we want to reuse these counts and the priors can be
	 * obtained by a simple division.
	 */
	private void calculateConceptPriors() {

		for (Concept c : tSet.getConceptSet()) {

			// Calculate the priors for the concepts
			int totalConceptCount = 0;

			for (Instance i : tSet.getInstances().values()) {

				if (i.getConcept().equals(c)) {
					totalConceptCount++;
				}
			}

			conceptPriors.put(c, (double) totalConceptCount);
		}
	}

	protected void calculateConditionalProbabilities() {

		p = new HashMap<>();

		for (Instance i : tSet.getInstances().values()) {

			for (Attribute a : i.getAtrributes()) {

				if (a != null && attributeList.contains(a.getName())) {

					if (p.get(i.getConcept()) == null) {

						p.put(i.getConcept(),
								new HashMap<Attribute, AttributeValue>());

					}

					Map<Attribute, AttributeValue> aMap = p.get(i.getConcept());
					AttributeValue aV = aMap.get(a);
					if (aV == null) {

						aV = new AttributeValue(a.getValue());
						aMap.put(a, aV);

					} else {
						aV.count();
					}
				}
			}
		}
	}

	public Concept classify(Instance instance) {

		if (instance == null) {
			throw new IllegalArgumentException("Instance to classify cannot be null.");
		}

		Concept bestConcept = null;
		double bestP = 0.0;

		if (tSet == null || tSet.getConceptSet().isEmpty()) {
			throw new IllegalStateException("You have to train classifier first.");
		}

		LOG.finest("\n*** Classifying instance: " + instance.toString() + "\n");

		for (Concept c : tSet.getConceptSet()) {
			
			double p = getProbability(c, instance);
			
			LOG.finest(MessageFormat.format("P(%s|%s) = %.15f\n", c.getName(), instance.toString(), p));
			
			if (p >= bestP) {
				bestConcept = c;
				bestP = p;
			}
		}
		return bestConcept;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	private boolean useLaplacianSmoothing = false;
	private boolean useLogSum = false;

	public boolean isUseLaplacianSmoothing() {
		return useLaplacianSmoothing;
	}

	public void setUseLaplacianSmoothing(boolean useLaplacianSmoothing) {
		this.useLaplacianSmoothing = useLaplacianSmoothing;
	}

	public boolean isUseLogSum() {
		return useLogSum;
	}

	public void setUseLogSum(boolean useLogSum) {
		this.useLogSum = useLogSum;
	}

	private int getVocabularySize(String attributeName) {
		java.util.Set<Object> uniqueValues = new java.util.HashSet<>();
		for (Instance inst : tSet.getInstances().values()) {
			for (Attribute attr : inst.getAtrributes()) {
				if (attr != null && attributeName.equals(attr.getName())) {
					uniqueValues.add(attr.getValue());
				}
			}
		}
		return Math.max(1, uniqueValues.size());
	}

	public double getProbability(Concept c) {
		Double trInstanceCount = conceptPriors.get(c);
		if (trInstanceCount == null) {
			trInstanceCount = 0.0;
		}
		return trInstanceCount / tSet.getSize();
	}

	/**
	 * This method calculates the <I>posterior probability</I> that we deal with
	 * concept <CODE>c</CODE> provided that we observed instance <CODE>i</CODE>.
	 * This is the application of Bayes theorem.
	 * 
	 * @param c
	 *            is a probable concept for instance <CODE>i</CODE>
	 * @param i
	 *            is the observed instance
	 * @return posterior probability of <CODE>c</CODE> given instance
	 *         <CODE>i</CODE>
	 */
	public double getProbability(Concept c, Instance i) {

		double cP;

		if (tSet.getConceptSet().contains(c)) {
			if (useLogSum) {
				double prior = Math.max(getProbability(c), 1e-15);
				cP = getProbability(i, c) + Math.log(prior);
			} else {
				cP = (getProbability(i, c) * getProbability(c)) / getProbability(i);
			}
		} else {
			// We have never seen this concept before
			// assign to it a "reasonable" value
			if (useLogSum) {
				cP = Math.log(1.0 / (tSet.getNumberOfConcepts() + 1.0));
			} else {
				cP = 1.0 / (tSet.getNumberOfConcepts() + 1.0);
			}
		}

		return cP;
	}

	/**
	 * This method calculates the denumerator of Bayes theorem
	 * 
	 * @param i the instance whose probability of occurrence we seek
	 * @return the probability of observing <tt>Instance</tt> i
	 */
	public double getProbability(Instance i) {

		double cP = 0;

		for (Concept c : getTset().getConceptSet()) {

			cP += getProbability(i, c) * getProbability(c);
		}
		return (cP == 0) ? (double) 1 / tSet.getSize() : cP;
	}

	public double getProbability(Instance i, Concept c) {

		if (useLogSum) {
			double logSum = 0.0;
			for (Attribute a : i.getAtrributes()) {
				if (a != null && attributeList.contains(a.getName())) {
					Map<Attribute, AttributeValue> aMap = p.get(c);
					AttributeValue aV = (aMap != null) ? aMap.get(a) : null;
					double count = (aV != null) ? aV.getCount() : 0.0;
					double prob;
					if (useLaplacianSmoothing) {
						int vocabularySize = getVocabularySize(a.getName());
						prob = (count + 1.0) / (conceptPriors.get(c) + vocabularySize);
					} else {
						if (aV == null) {
							prob = ((double) 1 / (tSet.getSize() + 1));
						} else {
							prob = (count / conceptPriors.get(c));
						}
					}
					logSum += Math.log(Math.max(prob, 1e-15));
				}
			}
			return logSum;
		}

		double cP = 1;

		for (Attribute a : i.getAtrributes()) {

			if (a != null && attributeList.contains(a.getName())) {

				Map<Attribute, AttributeValue> aMap = p.get(c);
				AttributeValue aV = (aMap != null) ? aMap.get(a) : null;
				double count = (aV != null) ? aV.getCount() : 0.0;
				double prob;
				if (useLaplacianSmoothing) {
					int vocabularySize = getVocabularySize(a.getName());
					prob = (count + 1.0) / (conceptPriors.get(c) + vocabularySize);
				} else {
					if (aV == null) {
						prob = ((double) 1 / (tSet.getSize() + 1));
					} else {
						prob = (count / conceptPriors.get(c));
					}
				}
				cP *= prob;
			}
		}

		return (cP == 1) ? (double) 1 / tSet.getNumberOfConcepts() : cP;
	}

	/**
	 * @return the tSet
	 */
	public TrainingSet getTset() {
		return tSet;
	}

	/**
	 * Training simply sets the probability for each concept
	 * 
	 */
	public boolean train() {

		long t0 = System.currentTimeMillis();

		boolean hasTrained = false;

		if (attributeList == null || attributeList.isEmpty()) {

			String msg = "Can't train the classifier without specifying the attributes"+
						 " for training!\n"+
					     "Use the method --> trainOnAttribute(Attribute a)";
			throw new IllegalStateException(msg);

		} else {

			calculateConceptPriors();

			calculateConditionalProbabilities();

			hasTrained = true;
		}

		LOG.fine("       Naive Bayes training completed in ");
		LOG.fine((System.currentTimeMillis() - t0) + " (ms)");
		
		return hasTrained;
	}

	public void trainOnAttribute(String aName) {

		if (attributeList == null) {
			attributeList = new ArrayList<>();
		}

		attributeList.add(aName);
	}

	public void printConcepts() {
		
		for (Concept c: conceptPriors.keySet()) {
			P.println(c.getName()+": "+getProbability(c));
		}
	}
}
