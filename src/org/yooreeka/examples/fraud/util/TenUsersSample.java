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
import java.util.Arrays;
import java.util.List;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.fraud.data.Transaction;

/**
 * Example for how to configure and generate file with transactions.
 */
public class TenUsersSample {

	/*
	 * File with descriptions to be used for valid transactions.
	 */
	public static String TXN_DESC_FILENAME = YooreekaConfigurator.getHome()
			+ "/data/ch05/fraud/descriptions.txt";

	/*
	 * File with descriptions to be used for fraud transactions.
	 */
	public static String FRAUD_TXN_DESC_FILENAME = YooreekaConfigurator
			.getHome() + "/data/ch05/fraud/fraud-descriptions.txt";

	/*
	 * Generated transactions will be saved into this file.
	 */
	public static String TRAINING_TXN_FILENAME = YooreekaConfigurator.getHome()
			+ "/data/ch05/fraud/generated-training-txns.txt";

	public static String TEST_TXN_FILENAME = YooreekaConfigurator.getHome()
			+ "/data/ch05/fraud/generated-test-txns.txt";

	public static TransactionSetProfile[] createUsersForTest() {
		List<TransactionSetProfile> allUserParams = new ArrayList<TransactionSetProfile>();

		String[] txnDescriptions = FraudDataUtils
				.loadTxnDescriptions(TXN_DESC_FILENAME);
		String[] fraudTxnDescriptions = FraudDataUtils
				.loadTxnDescriptions(FRAUD_TXN_DESC_FILENAME);

		// Each user will have a set of valid and fraud txns.
		// Using user ids from training set that didn't have any fraud txns.

		for (int userId = 21; userId <= 22; userId++) {
			allUserParams.addAll(createUserType1(userId, 100, 10,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 23; userId <= 24; userId++) {
			allUserParams.addAll(createUserType2(userId, 100, 10,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 25; userId <= 26; userId++) {
			allUserParams.addAll(createUserType3(userId, 100, 10,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 27; userId <= 28; userId++) {
			allUserParams.addAll(createUserType4(userId, 100, 10,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 29; userId <= 30; userId++) {
			allUserParams.addAll(createUserType5(userId, 100, 10,
					txnDescriptions, fraudTxnDescriptions));
		}

		return allUserParams.toArray(new TransactionSetProfile[0]);
	}

	public static TransactionSetProfile[] createUsersForTraining() {
		List<TransactionSetProfile> allUserParams = new ArrayList<TransactionSetProfile>();

		String[] txnDescriptions = FraudDataUtils
				.loadTxnDescriptions(TXN_DESC_FILENAME);
		String[] fraudTxnDescriptions = FraudDataUtils
				.loadTxnDescriptions(FRAUD_TXN_DESC_FILENAME);

		// We have 5 types/profiles of users.

		// First, create 2 users for each profile with fraud txns.

		for (int userId = 1; userId <= 2; userId++) {
			allUserParams.addAll(createUserType1(userId, 300, 25,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 3; userId <= 4; userId++) {
			allUserParams.addAll(createUserType2(userId, 400, 15,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 5; userId <= 6; userId++) {
			allUserParams.addAll(createUserType3(userId, 300, 30,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 7; userId <= 8; userId++) {
			allUserParams.addAll(createUserType4(userId, 300, 10,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 9; userId <= 10; userId++) {
			allUserParams.addAll(createUserType5(userId, 600, 20,
					txnDescriptions, fraudTxnDescriptions));
		}

		// Now, create a couple of users from each profile without fraud txns
		// these users will be used in test dataset as well

		for (int userId = 21; userId <= 22; userId++) {
			allUserParams.addAll(createUserType1(userId, 400, 0,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 23; userId <= 24; userId++) {
			allUserParams.addAll(createUserType2(userId, 400, 0,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 25; userId <= 26; userId++) {
			allUserParams.addAll(createUserType3(userId, 400, 0,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 27; userId <= 28; userId++) {
			allUserParams.addAll(createUserType4(userId, 500, 0,
					txnDescriptions, fraudTxnDescriptions));
		}

		for (int userId = 29; userId <= 30; userId++) {
			allUserParams.addAll(createUserType5(userId, 600, 0,
					txnDescriptions, fraudTxnDescriptions));
		}

		// Users that we will be using for test
		for (int userId = 29; userId <= 30; userId++) {
			allUserParams.addAll(createUserType5(userId, 600, 0,
					txnDescriptions, fraudTxnDescriptions));
		}

		return allUserParams.toArray(new TransactionSetProfile[0]);
	}

	/*
	 * Transaction sequence configuration for Type 1 User.
	 */
	public static List<TransactionSetProfile> createUserType1(int userId,
			int nValidTxns, int nFraudTxns, String[] txnDescriptions,
			String[] fraudTxnDescriptions) {

		TransactionSetProfile[] profiles = new TransactionSetProfile[2];

		profiles[0] = new TransactionSetProfile();
		profiles[1] = new TransactionSetProfile();

		profiles[0].setUserId(userId);
		profiles[0].setNTxns(nValidTxns);
		profiles[0].setTxnDescriptions(txnDescriptions);
		profiles[0].setLocations(700, 700, 1000, 1000);
		profiles[0].setTxnAmtMean(50);
		profiles[0].setTxnAmtStd(20);
		profiles[0].setFraud(false);

		profiles[1].setUserId(userId);
		profiles[1].setNTxns(nFraudTxns);
		profiles[1].setTxnAmtMean(4000);
		profiles[1].setTxnAmtStd(100);
		profiles[1].setLocations(50, 50, 200, 200);
		profiles[1].setTxnDescriptions(fraudTxnDescriptions);
		profiles[1].setFraud(true);

		return Arrays.asList(profiles);

	}

	/*
	 * Transaction sequence configuration for Type 2 User.
	 */
	public static List<TransactionSetProfile> createUserType2(int userId,
			int nValidTxns, int nFraudTxns, String[] txnDescriptions,
			String[] fraudTxnDescriptions) {

		TransactionSetProfile[] profiles = new TransactionSetProfile[2];

		profiles[0] = new TransactionSetProfile();
		profiles[1] = new TransactionSetProfile();

		profiles[0].setUserId(userId);
		profiles[0].setNTxns(nValidTxns);
		profiles[0].setTxnDescriptions(txnDescriptions);
		profiles[0].setLocations(500, 500, 1000, 1000);
		profiles[0].setTxnAmtMean(60);
		profiles[0].setTxnAmtStd(20);
		profiles[0].setFraud(false);

		profiles[1].setUserId(userId);
		profiles[1].setNTxns(nFraudTxns);
		profiles[1].setTxnAmtMean(1000);
		profiles[1].setTxnAmtStd(100);
		profiles[1].setLocations(100, 100, 600, 600);
		profiles[1].setTxnDescriptions(fraudTxnDescriptions);
		profiles[1].setFraud(true);

		return Arrays.asList(profiles);

	}

	/*
	 * Transaction sequence configuration for Type 3 User.
	 */
	public static List<TransactionSetProfile> createUserType3(int userId,
			int nValidTxns, int nFraudTxns, String[] txnDescriptions,
			String[] fraudTxnDescriptions) {

		TransactionSetProfile[] profiles = new TransactionSetProfile[2];

		profiles[0] = new TransactionSetProfile();
		profiles[1] = new TransactionSetProfile();

		profiles[0].setUserId(userId);
		profiles[0].setNTxns(nValidTxns);
		profiles[0].setTxnDescriptions(txnDescriptions);
		profiles[0].setLocations(500, 500, 800, 800);
		profiles[0].setTxnAmtMean(80);
		profiles[0].setTxnAmtStd(20);
		profiles[0].setFraud(false);

		profiles[1].setUserId(userId);
		profiles[1].setNTxns(nFraudTxns);
		profiles[1].setTxnAmtMean(800);
		profiles[1].setTxnAmtStd(50);
		profiles[1].setLocations(100, 100, 400, 400);
		profiles[1].setTxnDescriptions(fraudTxnDescriptions);
		profiles[1].setFraud(true);

		return Arrays.asList(profiles);

	}

	/*
	 * Transaction sequence configuration for Type 4 User.
	 */
	public static List<TransactionSetProfile> createUserType4(int userId,
			int nValidTxns, int nFraudTxns, String[] txnDescriptions,
			String[] fraudTxnDescriptions) {

		TransactionSetProfile[] profiles = new TransactionSetProfile[2];

		profiles[0] = new TransactionSetProfile();
		profiles[1] = new TransactionSetProfile();

		profiles[0].setUserId(userId);
		profiles[0].setNTxns(nValidTxns);
		profiles[0].setTxnDescriptions(txnDescriptions);
		profiles[0].setLocations(100, 100, 400, 400);
		profiles[0].setTxnAmtMean(200);
		profiles[0].setTxnAmtStd(20);
		profiles[0].setFraud(false);

		profiles[1].setUserId(userId);
		profiles[1].setNTxns(nFraudTxns);
		profiles[1].setTxnAmtMean(2000);
		profiles[1].setTxnAmtStd(100);
		profiles[1].setLocations(600, 600, 800, 800);
		profiles[1].setTxnDescriptions(fraudTxnDescriptions);
		profiles[1].setFraud(true);

		return Arrays.asList(profiles);
	}

	/*
	 * Transaction sequence configuration for Type 5 User.
	 */
	public static List<TransactionSetProfile> createUserType5(int userId,
			int nValidTxns, int nFraudTxns, String[] txnDescriptions,
			String[] fraudTxnDescriptions) {

		TransactionSetProfile[] profiles = new TransactionSetProfile[2];
		profiles[0] = new TransactionSetProfile();
		profiles[1] = new TransactionSetProfile();

		profiles[0].setUserId(userId);
		profiles[0].setNTxns(nValidTxns);
		profiles[0].setTxnAmtMean(700);
		profiles[0].setTxnAmtStd(500);
		profiles[0].setTxnDescriptions(txnDescriptions);
		profiles[0].setLocations(100, 100, 400, 400);
		profiles[0].setFraud(false);

		profiles[1].setUserId(userId);
		profiles[1].setNTxns(nFraudTxns);
		profiles[1].setTxnAmtMean(700);
		profiles[1].setTxnAmtStd(100);
		profiles[1].setLocations(500, 500, 700, 700);
		profiles[1].setTxnDescriptions(fraudTxnDescriptions);
		profiles[1].setFraud(true);

		return Arrays.asList(profiles);
	}

	public static void generateTxns(String txnFilename, int startTxnId,
			TransactionSetProfile[] allUsers) {
		DataGenerator dataGenerator = new DataGenerator();
		dataGenerator.setNextTxnId(startTxnId);
		System.out.println("Generating transactions...");
		List<Transaction> allTxns = dataGenerator.generateTxns(allUsers);
		System.out.println("Saving transactions into '" + txnFilename + "'");
		FraudDataUtils.saveTransactions(txnFilename, allTxns);
	}

	public static void main(String[] args) {

		TransactionSetProfile[] userProfiles = createUsersForTraining();
		generateTxns(TRAINING_TXN_FILENAME, 1, userProfiles);
		userProfiles = createUsersForTest();
		generateTxns(TEST_TXN_FILENAME, 500000, userProfiles);
	}

	public static void printTxns(String txnFilename) {
		System.out.println("Loading transactions from '" + txnFilename + "'");
		List<Transaction> allTxns = FraudDataUtils
				.loadTransactions(txnFilename);
		System.out.println("Printing loaded transactions:");
		for (Transaction e : allTxns) {
			System.out.println(e);
		}
	}
}
