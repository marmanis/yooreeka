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

import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.examples.fraud.util.UserStatisticsCalculator;

public class TransactionDataset implements java.io.Serializable {

	private static final long serialVersionUID = 3061645520644719411L;

	private Map<Integer, List<Transaction>> txnsByUserIdMap;
	private Map<String, Transaction> txnsByTxnIdMap;
	private Integer maxUserId;
	private TransactionInstanceBuilder instanceBuilder;

	public TransactionDataset(List<Transaction> txnsList) {
		this.txnsByUserIdMap = new HashMap<Integer, List<Transaction>>();
		this.txnsByTxnIdMap = new HashMap<String, Transaction>(txnsList.size());

		for (Transaction e : txnsList) {

			txnsByTxnIdMap.put(String.valueOf(e.getTxnId()), e);

			Integer userId = e.getUserId();
			List<Transaction> userTxns = txnsByUserIdMap.get(userId);
			if (userTxns == null) {
				userTxns = new ArrayList<Transaction>();
				txnsByUserIdMap.put(userId, userTxns);
			}

			if (maxUserId == null || e.getUserId() > maxUserId) {
				maxUserId = e.getUserId();
			}

			userTxns.add(e);
		}

		instanceBuilder = new TransactionInstanceBuilder();

	}

	public void calculateUserStats() {
		UserStatisticsCalculator userStatsCalculator = new UserStatisticsCalculator();

		instanceBuilder.setUserStatisticsMap(userStatsCalculator
				.calculateStatistics(this));
	}

	public TrainingSet createTrainingDataset() {
		return instanceBuilder.createTrainingSet(this);
	}

	public Transaction findTransactionById(String id) {
		return txnsByTxnIdMap.get(id);
	}

	public List<Transaction> findUserTxns(Integer userId) {
		return new ArrayList<Transaction>(txnsByUserIdMap.get(userId));
	}

	/**
	 * @return the instanceBuilder
	 */
	public TransactionInstanceBuilder getInstanceBuilder() {
		return instanceBuilder;
	}

	public Integer getMaxUserId() {
		return maxUserId;
	}

	public int getSize() {
		return txnsByTxnIdMap.size();
	}

	public List<Transaction> getTransactions() {
		return new ArrayList<Transaction>(txnsByTxnIdMap.values());
	}

	public List<Integer> getUsers() {
		return new ArrayList<Integer>(txnsByUserIdMap.keySet());
	}

	public void printAll() {
		for (Map.Entry<String, Transaction> e : txnsByTxnIdMap.entrySet()) {
			Transaction t = e.getValue();
			System.out.println(t);
		}
	}

	public void printTransaction(String id) {
		Transaction e = findTransactionById(id);
		if (e != null) {
			System.out.println(e.toString());
		} else {
			System.out.println("Transaction not found (txn id: '" + id + "')");
		}
	}

}
