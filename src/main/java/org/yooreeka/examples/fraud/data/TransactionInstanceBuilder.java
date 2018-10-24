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
package org.yooreeka.examples.fraud.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yooreeka.algos.taxis.core.DoubleAttribute;
import org.yooreeka.algos.taxis.core.StringAttribute;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.fraud.TransactionConcept;
import org.yooreeka.examples.fraud.TransactionInstance;
import org.yooreeka.examples.fraud.util.UserStatistics;
import org.yooreeka.util.metrics.JaccardCoefficient;
import org.yooreeka.util.metrics.SimilarityMeasure;

public class TransactionInstanceBuilder implements java.io.Serializable {

	private static final long serialVersionUID = -2334221990318430678L;

	/*
	 * For every user we keep a set of user-specific values to normalize data.
	 */
	private Map<Integer, UserStatistics> userStatisticsMap;

	/*
	 * Similarity measure that will be used to evaluate similarity between
	 * transaction descriptions.
	 */
	private SimilarityMeasure descriptionSim;

	public TransactionInstanceBuilder() {
		userStatisticsMap = new HashMap<Integer, UserStatistics>();
		descriptionSim = new JaccardCoefficient();
	}

	private Double calculateDescriptionSimilarity(String txnDescription,
			UserStatistics u) {

		String[] termsX = tokenizeTxnDescription(txnDescription);
		Set<String> validTxnDescriptions = u.getDescriptions();

		double bestSim = 0.0;
		for (String valueY : validTxnDescriptions) {
			String[] termsY = u.getDescriptionTokens(valueY);
			if (termsY == null) {
				termsY = tokenizeTxnDescription(valueY);
				u.setDescriptionTokens(valueY, termsY);
			}
			double sim = descriptionSim.similarity(termsX, termsY);
			if (sim > bestSim) {
				bestSim = sim;
			}
		}

		return bestSim;
	}

	public TransactionInstance createInstance(Transaction t) {

		int userId = t.getUserId();
		UserStatistics userStats = getUserStatistics(userId);

		if (userStats == null) {
			throw new RuntimeException(
					"Can't create instance. There are no statistics for user: "
							+ userId);
		}

		/*
		 * Calculate distance between user location centroid and instance
		 * location
		 */
		TransactionLocation nLocation = normalizeLocation(t.getLocation(),
				userStats);
		TransactionLocation nCentroid = normalizeLocation(
				userStats.getLocationCentroid(), userStats);
		double nLocationDistance = nCentroid.distance(nLocation);

		double nAmt = normalizeAmount(t.getAmount(), userStats);

		double nDescriptionSim = calculateDescriptionSimilarity(
				t.getDescription(), userStats);

		double nUserId = t.getUserId();

		List<Attribute> attributes = new ArrayList<Attribute>();

		// Attributes that will be used by NN
		attributes.add(new DoubleAttribute(
				TransactionInstance.ATTR_NAME_N_TXN_AMT, nAmt));
		attributes.add(new DoubleAttribute(
				TransactionInstance.ATTR_NAME_N_LOCATION, nLocationDistance));
		attributes.add(new DoubleAttribute(
				TransactionInstance.ATTR_NAME_N_DESCRIPTION, nDescriptionSim));

		// Adding informational attributes
		attributes.add(new StringAttribute(
				TransactionInstance.ATTR_NAME_USERID, String.valueOf(nUserId)));
		attributes.add(new StringAttribute(TransactionInstance.ATTR_NAME_TXNID,
				String.valueOf(t.getTxnId())));

		attributes.add(new DoubleAttribute(
				TransactionInstance.ATTR_NAME_TXN_AMT, t.getAmount()));
		attributes.add(new DoubleAttribute(
				TransactionInstance.ATTR_NAME_LOCATION_X, t.getLocation()
						.getX()));
		attributes.add(new DoubleAttribute(
				TransactionInstance.ATTR_NAME_LOCATION_Y, t.getLocation()
						.getY()));
		attributes.add(new StringAttribute(
				TransactionInstance.ATTR_NAME_DESCRIPTION, t.getDescription()));

		TransactionConcept c = null;
		if (t.isFraud()) {
			c = new TransactionConcept(TransactionConcept.CONCEPT_LABEL_FRAUD);
		} else {
			c = new TransactionConcept(TransactionConcept.CONCEPT_LABEL_VALID);
		}

		return new TransactionInstance(c, attributes.toArray(new Attribute[0]));
	}

	public TrainingSet createTrainingSet(TransactionDataset data) {
		List<Transaction> txns = data.getTransactions();
		int nTxns = txns.size();
		Instance[] instances = new Instance[nTxns];
		for (int i = 0; i < nTxns; i++) {
			Transaction t = txns.get(i);
			instances[i] = createInstance(t);
		}
		return new TrainingSet(instances);
	}

	public Map<Integer, UserStatistics> getUserStatistics() {
		return userStatisticsMap;
	}

	public UserStatistics getUserStatistics(int userId) {
		return userStatisticsMap.get(userId);
	}

	/**
	 * @return the userStatisticsMap
	 */
	public Map<Integer, UserStatistics> getUserStatisticsMap() {
		return userStatisticsMap;
	}

	private Double normalizeAmount(Double amt, UserStatistics u) {
		Double min = u.getTxnAmtMin();
		Double max = u.getTxnAmtMax();
		Double v = (amt - min) / (max - min);
		return v; // Valid values should fall into [0..1] and fraud outside.
	}

	private TransactionLocation normalizeLocation(TransactionLocation location,
			UserStatistics u) {

		double nX = (location.getX() - u.getLocationMinX())
				/ (u.getLocationMaxX() - u.getLocationMinX());

		double nY = (location.getY() - u.getLocationMinY())
				/ (u.getLocationMaxY() - u.getLocationMinY());

		return new TransactionLocation(nX, nY);
	}

	public void printUserStats(int userId) {
		UserStatistics userProps = userStatisticsMap.get(userId);
		System.out.println("Properties for userId: " + userId
				+ " calculated from training data:");
		System.out.println(userProps.toString());
	}

	/**
	 * @param userStatisticsMap
	 *            the userStatisticsMap to set
	 */
	public void setUserStatisticsMap(
			Map<Integer, UserStatistics> userStatisticsMap) {
		this.userStatisticsMap = userStatisticsMap;
	}

	private String[] tokenizeTxnDescription(String description) {
		String[] terms = description.split("\\s");

		return terms;
	}

}
