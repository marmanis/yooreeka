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
package org.yooreeka.examples.fraud.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yooreeka.examples.fraud.data.Transaction;
import org.yooreeka.examples.fraud.data.TransactionDataset;
import org.yooreeka.examples.fraud.data.TransactionLocation;

public class UserStatisticsCalculator {

	public Map<Integer, UserStatistics> calculateStatistics(
			TransactionDataset trainingData) {

		Map<Integer, UserStatistics> statsByUserMap = new HashMap<Integer, UserStatistics>();

		List<Integer> users = trainingData.getUsers();

		for (Integer userId : users) {
			List<Transaction> userTxns = trainingData.findUserTxns(userId);

			UserStatistics userProps = calculateUserProperties(userId, userTxns);

			statsByUserMap.put(userId, userProps);
		}

		return statsByUserMap;

	}

	private UserStatistics calculateUserProperties(Integer userId,
			List<Transaction> userTxns) {

		UserStatistics props = new UserStatistics();

		props.setUserId(userId);

		/*
		 * Unique descriptions of non-fraud transactions from training set for
		 * this user.
		 */
		Set<String> descriptions = new HashSet<String>();

		/*
		 * Total number of non-fraud transactions from training set for this
		 * user.
		 */
		int nonFraudTxnCount = 0;

		/*
		 * All locations of non-fraud transaction from training set for this
		 * user.
		 */
		List<TransactionLocation> locations = new ArrayList<TransactionLocation>();

		Double minAmount = null;
		Double maxAmount = null;

		Double locationMinX = null;
		Double locationMaxX = null;
		Double locationMinY = null;
		Double locationMaxY = null;

		for (Transaction t : userTxns) {
			if (t.isFraud()) {
				// do not use fraud transactions to calculate user statistics
			} else {
				nonFraudTxnCount++;

				descriptions.add(t.getDescription());

				locations.add(t.getLocation());

				double x = t.getLocation().getX();
				double y = t.getLocation().getY();

				// update min/max values for location
				if (locationMinX == null || x < locationMinX) {
					locationMinX = x;
				}
				if (locationMinY == null || y < locationMinY) {
					locationMinY = y;
				}
				if (locationMaxX == null || x > locationMaxX) {
					locationMaxX = x;
				}
				if (locationMaxY == null || y > locationMaxY) {
					locationMaxY = y;
				}

				double amt = t.getAmount();

				if (minAmount == null || amt < minAmount) {
					minAmount = amt;
				}

				if (maxAmount == null || amt > maxAmount) {
					maxAmount = amt;
				}

			}
		}

		TransactionLocation locationCentroid = centroid(locations);
		props.setDescriptions(descriptions);
		props.setLocationCentroid(locationCentroid);
		props.setTxnAmtMin(minAmount);
		props.setTxnAmtMax(maxAmount);
		props.setLocationMaxX(locationMaxX);
		props.setLocationMinX(locationMinX);
		props.setLocationMaxY(locationMaxY);
		props.setLocationMinY(locationMinY);

		return props;
	}

	private TransactionLocation centroid(List<TransactionLocation> locations) {
		double x = 0.0;
		double y = 0.0;
		double n = locations.size();

		for (TransactionLocation location : locations) {
			x += location.getX();
			y += location.getY();
		}

		return new TransactionLocation(x / n, y / n);
	}

}
