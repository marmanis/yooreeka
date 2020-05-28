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
import java.util.List;

import org.yooreeka.examples.fraud.data.Transaction;
import org.yooreeka.examples.fraud.data.TransactionLocation;

public class DataGenerator {

	private long nextTxnId = 0;

	public DataGenerator() {
		// default value
		this.setNextTxnId(1);
	}

	private double generateAmt(TransactionSetProfile user) {
		return FraudDataUtils.nextTxnAmount(user.getTxnAmtMean(),
				user.getTxnAmtStd());
	}

	private String generateDescription(TransactionSetProfile userParams) {
		int txnDescriptionId;
		String[] txnDescriptions;
		txnDescriptions = userParams.getTxnDescriptions();
		txnDescriptionId = FraudDataUtils.randomInt(txnDescriptions.length);
		return txnDescriptions[txnDescriptionId];
	}

	private TransactionLocation generateLocation(
			TransactionSetProfile userParams) {

		int minX = userParams.getLocationMinX();
		int maxX = userParams.getLocationMaxX();
		int minY = userParams.getLocationMinY();
		int maxY = userParams.getLocationMaxY();

		int x = FraudDataUtils.randomInt(minX, maxX);
		int y = FraudDataUtils.randomInt(minY, maxY);

		return new TransactionLocation(x, y);
	}

	private long generateNextUniqueTxnId() {
		return nextTxnId++;
	}

	private Transaction generateTxn(TransactionSetProfile userParams) {
		Transaction e = new Transaction();

		e.setUserId(userParams.getUserId());
		e.setTxnId(generateNextUniqueTxnId());

		// Txn Amount
		double amt = generateAmt(userParams);
		e.setAmount(amt);

		// Txn Description
		String txnDescription = generateDescription(userParams);
		e.setDescription(txnDescription);

		// Txn Location
		TransactionLocation location = generateLocation(userParams);
		e.setLocation(location);

		// Txn fraud flag
		e.setFraud(userParams.isFraud());

		return e;
	}

	public List<Transaction> generateTxns(TransactionSetProfile[] allUsers) {
		List<Transaction> allTransactions = new ArrayList<Transaction>();
		for (int i = 0, n = allUsers.length; i < n; i++) {
			TransactionSetProfile user = allUsers[i];

			for (int j = 0; j < user.getNTxns(); j++) {
				allTransactions.add(generateTxn(user));
			}

		}
		return allTransactions;
	}

	public void setNextTxnId(long nextTxnId) {
		this.nextTxnId = nextTxnId;
	}
}
