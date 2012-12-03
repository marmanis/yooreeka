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
package org.yooreeka.examples.credit.data;

import java.util.ArrayList;
import java.util.List;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.credit.data.users.BadUserType;
import org.yooreeka.examples.credit.data.users.DangerousUserType;
import org.yooreeka.examples.credit.data.users.ExcellentUserType;
import org.yooreeka.examples.credit.data.users.GoodUserType;
import org.yooreeka.examples.credit.data.users.User;
import org.yooreeka.examples.credit.data.users.UserType;
import org.yooreeka.examples.credit.data.users.VeryGoodUserType;
import org.yooreeka.examples.credit.util.CreditDataUtils;
import org.yooreeka.examples.credit.util.DataGenerator;

/**
 * Example for how to configure and generate file with transactions.
 */
public class UseCaseData {

	/*
	 * Generated transactions will be saved into this file.
	 */
	public static String TRAINING_USERS_FILENAME = YooreekaConfigurator
			.getHome() + "/data/ch06/generated-training-users.txt";

	public static String TEST_USERS_FILENAME = YooreekaConfigurator.getHome()
			+ "/data/ch06/generated-test-users.txt";

	public static void main(String[] args) {

		UseCaseData useCaseData = new UseCaseData(100000, 50000);
		// UseCaseData useCaseData = new UseCaseData(10000,5000);
		useCaseData.create();
	}

	DataGenerator dataGenerator = new DataGenerator();
	// INSTANCE VARIABLES
	int nTrainingUsers;

	int nTestUsers;

	public UseCaseData(int nTrainingUsers, int nTestUsers) {
		this.nTrainingUsers = nTrainingUsers;
		this.nTestUsers = nTestUsers;
	}

	public void create() {

		System.out
				.println("Creating data for the credit worthiness (score) use case:");
		System.out.println("   Number of users in the training set: "
				+ nTrainingUsers);
		System.out.println("    Number of users in the testing set: "
				+ nTestUsers);
		System.out
				.println("___________________________________________________________");

		List<UserType> trainingUserTypes = createUserTypes(nTrainingUsers);
		int userIdSequenceStart = 1;
		generateUsers(TRAINING_USERS_FILENAME, userIdSequenceStart,
				trainingUserTypes);

		dataGenerator.setNoiseOn(true);

		List<UserType> testUserTypes = createUserTypes(nTestUsers);
		userIdSequenceStart = 500000;
		// generateUsers(TEST_USERS_FILENAME, 2*nTrainingUsers, testUserTypes);
		generateUsers(TEST_USERS_FILENAME, userIdSequenceStart, testUserTypes);

		System.out.println("Done!");
	}

	public void create(boolean overwrite) {
		if (overwrite) {
			TRAINING_USERS_FILENAME = YooreekaConfigurator.getHome()
					+ "/data/ch06/training-users.txt";
			TEST_USERS_FILENAME = YooreekaConfigurator.getHome()
					+ "/data/ch06/test-users.txt";
		}
		create();
	}

	public List<UserType> createUserTypes(int nUsers) {
		List<UserType> allUserTypes = new ArrayList<UserType>();

		// Excellent credit users
		// 5% of the total number of users
		UserType userType = new ExcellentUserType();
		userType.setNUsers((int) (nUsers * 0.05));

		allUserTypes.add(userType);

		// Very good credit users
		// 15% of the total number of users
		userType = new VeryGoodUserType();
		userType.setNUsers((int) (nUsers * 0.15));

		allUserTypes.add(userType);

		// Good credit users
		// 50% of the total number of users
		userType = new GoodUserType();
		userType.setNUsers((int) (nUsers * 0.50));

		allUserTypes.add(userType);

		// Bad credit users
		// 25% of the total number of users
		userType = new BadUserType();
		userType.setNUsers((int) (nUsers * 0.25));

		allUserTypes.add(userType);

		// Dangerous credit users
		// 5% of the total number of users
		userType = new DangerousUserType();
		userType.setNUsers((int) (nUsers * 0.05));
		allUserTypes.add(userType);

		return allUserTypes;
	}

	public void generateUsers(String filename, int nextUserId,
			List<UserType> userTypes) {

		dataGenerator.setNextUserId(nextUserId);
		System.out.println("Generating users...");
		List<User> allUsers = dataGenerator.generateUsers(userTypes);
		System.out.println("Saving users into '" + filename + "'");
		CreditDataUtils.saveUsers(filename, allUsers);
	}

	/**
	 * @return the nTestUsers
	 */
	public int getTestUsers() {
		return nTestUsers;
	}

	/**
	 * @return the nTrainingUsers
	 */
	public int getTrainingUsers() {
		return nTrainingUsers;
	}

	/**
	 * @param testUsers
	 *            the nTestUsers to set
	 */
	public void setTestUsers(int n) {
		nTestUsers = n;
	}

	/**
	 * @param trainingUsers
	 *            the nTrainingUsers to set
	 */
	public void setTrainingUsers(int n) {
		nTrainingUsers = n;
	}
}
