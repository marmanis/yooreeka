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
package org.yooreeka.examples.spamfilter;

import java.util.HashMap;
import java.util.Map;

import org.yooreeka.algos.taxis.bayesian.NaiveBayes;
import org.yooreeka.algos.taxis.core.AttributeValue;
import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.spamfilter.data.Email;
import org.yooreeka.examples.spamfilter.data.EmailData;
import org.yooreeka.examples.spamfilter.data.EmailDataset;
import org.yooreeka.util.metrics.JaccardCoefficient;

public class EmailClassifier extends NaiveBayes {

	private EmailDataset emailDataset;
	private int topNTerms;
	private boolean verbose = true;
	private double jaccardThreshold = 0.25;

	public EmailClassifier(EmailDataset emailDataset, int topNTerms) {
		super("EmailClassifier", emailDataset.getTrainingSet(topNTerms));
		this.emailDataset = emailDataset;
		this.topNTerms = topNTerms;
	}

	@Override
	protected void calculateConditionalProbabilities() {

		p = new HashMap<Concept, Map<Attribute, AttributeValue>>();

		for (Instance i : tSet.getInstances().values()) {

			// In this specific implementation we have exactly one attribute
			// In general, you need a loop over the attributes
			Attribute a = i.getAtrributes()[0];

			Map<Attribute, AttributeValue> aMap = p.get(i.getConcept());

			if (aMap == null) {
				aMap = new HashMap<Attribute, AttributeValue>();
				p.put(i.getConcept(), aMap);
			}

			/**
			 * TODO: 5.3
			 */
			AttributeValue bestAttributeValue = findBestAttributeValue(aMap, a);

			if (bestAttributeValue != null) {

				bestAttributeValue.count();

			} else {
				AttributeValue aV = new AttributeValue(a.getValue());
				// register attribute as representative attribute
				aMap.put(a, aV);
			}
		}
	}

	public String classify(Email email) {
		EmailInstance i = emailDataset.toEmailInstance(email, topNTerms);
		Concept c = classify(i);
		if (verbose) {
			System.out.println("Classified " + email.getId() + " as "
					+ c.getName());
		}
		return c.getName();
	}

	@Override
	public Concept classify(Instance instance) {
		return super.classify(instance);
	}

	/*
	 * Finds best match for attribute value among existing attribute value
	 * representatives.
	 * 
	 * @param aMap map of all attribute representatives.
	 * 
	 * @param a new attribute to compare against
	 * 
	 * @return representative attribute that is the best match for a new
	 * attribute or null if no satisfactory match was found.
	 */
	private AttributeValue findBestAttributeValue(
			Map<Attribute, AttributeValue> aMap, Attribute a) {

		JaccardCoefficient jaccardCoeff = new JaccardCoefficient();

		String aValue = (String) a.getValue();
		String[] aTerms = aValue.split(" ");
		Attribute bestMatch = null;
		double bestSim = 0.0;

		/*
		 * Here we only check representative attribute values. Other attribute
		 * values associated with representative attribute values will be
		 * ignored by this implementation.
		 */
		for (Attribute attr : aMap.keySet()) {
			String attrValue = (String) attr.getValue();
			String[] attrTerms = attrValue.split(" ");
			double sim = jaccardCoeff.similarity(aTerms, attrTerms);
			if (sim > jaccardThreshold && sim > bestSim) {
				bestSim = sim;
				bestMatch = attr;
			}
		}

		return aMap.get(bestMatch);
	}

	/**
	 * @return the jaccardThreshold
	 */
	public double getJaccardThreshold() {
		return jaccardThreshold;
	}

	@Override
	public double getProbability(Instance i, Concept c) {

		double cP = 1;

		for (Attribute a : i.getAtrributes()) {

			if (a != null && attributeList.contains(a.getName())) {

				Map<Attribute, AttributeValue> aMap = p.get(c);

				AttributeValue bestAttributeValue = findBestAttributeValue(
						aMap, a);

				if (bestAttributeValue == null) {

					// the specific attribute value is not present for the
					// current concept.
					// Can you justify the following estimate?
					// Can you think of a better choice?
					cP *= ((double) 1 / (tSet.getSize() + 1));

				} else {

					cP *= (bestAttributeValue.getCount() / conceptPriors.get(c));
				}
			}
		}
		return (cP == 1) ? (double) 1 / tSet.getNumberOfConcepts() : cP;
	}

	public void sample() {

		Email email;
		// TRAINING SET
		System.out.println("________________________________________________");
		System.out.println("Validating with emails from the training dataset");
		System.out.println("________________________________________________");
		email = emailDataset.findEmailById("biz-04.html");
		classify(email);

		email = emailDataset.findEmailById("usa-03.html");
		classify(email);

		// TEST SET
		System.out.println("_______________________________________________");
		System.out.println("Testing with unseen emails");
		System.out.println("_______________________________________________");

		EmailDataset testEmailDS = EmailData.createTestDataset();
		email = testEmailDS.findEmailById("biz-01.html");
		classify(email);

		email = testEmailDS.findEmailById("sport-01.html");
		classify(email);

		email = testEmailDS.findEmailById("usa-01.html");
		classify(email);

		email = testEmailDS.findEmailById("world-01.html");
		classify(email);

		email = testEmailDS.findEmailById("spam-biz-01.html");
		classify(email);
	}

	/**
	 * @param jaccardThreshold
	 *            the jaccardThreshold to set
	 */
	public void setJaccardThreshold(double jaccardThreshold) {
		this.jaccardThreshold = jaccardThreshold;
	}

	@Override
	public boolean train() {

		if (emailDataset.getSize() == 0) {
			System.out
					.println("Can't train classifier - training dataset is empty.");
			return false;
		}

		for (String attrName : getTset().getAttributeNameSet()) {
			trainOnAttribute(attrName);
		}

		super.train();

		return true;
	}

}
