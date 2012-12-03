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
package org.yooreeka.examples.credit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.yooreeka.examples.credit.data.users.User;
import org.yooreeka.examples.credit.data.users.UserType;

public class DataGenerator {

	private long nextUserId = 1;

	private boolean isNoiseOn = false;
	private HashMap<UserType, Integer> userTypeDistributions;

	public DataGenerator() {
		userTypeDistributions = new HashMap<UserType, Integer>();
	}

	private long generateNextUniqueUserId() {
		return nextUserId++;
	}

	public User generateUser(UserType userType) {

		User user = new User();

		long userId = generateNextUniqueUserId();

		String username;

		if (isNoiseOn) {
			username = userType.getNoisyType();
		} else {
			username = userType.getUserType();
		}

		username = username + String.valueOf(userId);

		user.setUsername(username);

		user.setAge(userType.pickAge());
		user.setCarOwnership(userType.pickCarOwnership());
		user.setCreditScore(userType.pickCreditScore());
		user.setIncome(userType.pickIncome());
		user.setJobClass(userType.pickJobClass());
		user.setDownPayment(userType.pickDownPayment());
		user.setBicycleOwnership(userType.pickMotorcycleOwnership());
		user.setPropertyOwnership(userType.pickPropertyOwnership());
		user.setCriminalRecord(userType.pickCriminalRecord());
		user.setBankruptcy(userType.pickBancruptcy());
		user.setRetirementAccount(userType.pickRetirementAccounts());

		return user;
	}

	public List<User> generateUsers(List<UserType> userTypes) {
		List<User> allUsers = new ArrayList<User>();

		for (UserType userType : userTypes) {
			allUsers.addAll(generateUsers(userType, userType.getNUsers()));
		}

		return allUsers;
	}

	public List<User> generateUsers(UserType userType, int n) {

		List<User> users = new ArrayList<User>();

		userTypeDistributions.put(userType, n);

		for (int i = 0; i < n; i++) {
			User u = generateUser(userType);
			users.add(u);
		}

		return users;
	}

	/**
	 * @return the isNoiseOn
	 */
	public boolean isNoiseOn() {
		return isNoiseOn;
	}

	public void setNextUserId(long nextUserId) {
		this.nextUserId = nextUserId;
	}

	/**
	 * @param isNoiseOn
	 *            the isNoiseOn to set
	 */
	public void setNoiseOn(boolean isNoiseOn) {
		this.isNoiseOn = isNoiseOn;
	}
}
