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
package org.yooreeka.examples.credit.data.users;

public class User {

	private String username;
	private int jobClass;
	private int carOwnership;
	private int bicycleOwnership;
	private int propertyOwnership;
	private int retirementAccount;
	private int creditScore;
	private int age;
	private int downPayment;
	private int bankruptcy;
	private int criminalRecord;
	private int income;

	public User() {
		// empty
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final User other = (User) obj;
		if (age != other.age)
			return false;
		if (carOwnership != other.carOwnership)
			return false;
		if (creditScore != other.creditScore)
			return false;
		if (income != other.income)
			return false;
		if (jobClass != other.jobClass)
			return false;
		if (downPayment != other.downPayment)
			return false;
		if (bicycleOwnership != other.bicycleOwnership)
			return false;
		if (propertyOwnership != other.propertyOwnership)
			return false;
		if (criminalRecord != other.criminalRecord)
			return false;
		if (bankruptcy != other.bankruptcy)
			return false;
		if (retirementAccount != other.retirementAccount)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return the bankruptcy
	 */
	public int getBankruptcy() {
		return bankruptcy;
	}

	/**
	 * @return the bicycleOwnership
	 */
	public int getBicycleOwnership() {
		return bicycleOwnership;
	}

	/**
	 * @return the carOwnership
	 */
	public int getCarOwnership() {
		return carOwnership;
	}

	public String getCategory() {
		return username.substring(0, 2);
	}

	/**
	 * @return the creditScore
	 */
	public int getCreditScore() {
		return creditScore;
	}

	/**
	 * @return the criminalRecord
	 */
	public int getCriminalRecord() {
		return criminalRecord;
	}

	/**
	 * @return the downPayment
	 */
	public int getDownPayment() {
		return downPayment;
	}

	/**
	 * @return the income
	 */
	public int getIncome() {
		return income;
	}

	/**
	 * @return the jobClass
	 */
	public int getJobClass() {
		return jobClass;
	}

	/**
	 * @return the propertyOwnership
	 */
	public int getPropertyOwnership() {
		return propertyOwnership;
	}

	/**
	 * @return the retirementAccount
	 */
	public int getRetirementAccount() {
		return retirementAccount;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + carOwnership;
		result = prime * result + creditScore;
		result = prime * result + income;
		result = prime * result + jobClass;
		result = prime * result + downPayment;
		result = prime * result + bicycleOwnership;
		result = prime * result + propertyOwnership;
		result = prime * result + criminalRecord;
		result = prime * result + bankruptcy;
		result = prime * result + retirementAccount;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	public void loadFromExternalString(String text) {

		String[] values = text.split(":");

		username = values[0];
		jobClass = Integer.parseInt(values[1]);
		carOwnership = Integer.parseInt(values[2]);
		bicycleOwnership = Integer.parseInt(values[3]);
		propertyOwnership = Integer.parseInt(values[4]);
		retirementAccount = Integer.parseInt(values[5]);
		creditScore = Integer.parseInt(values[6]);
		age = Integer.parseInt(values[7]);
		downPayment = Integer.parseInt(values[8]);
		bankruptcy = Integer.parseInt(values[9]);
		criminalRecord = Integer.parseInt(values[10]);
		income = Integer.parseInt(values[11]);
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @param bankruptcy
	 *            the bankruptcy to set
	 */
	public void setBankruptcy(int bankruptcy) {
		this.bankruptcy = bankruptcy;
	}

	/**
	 * @param bicycleOwnership
	 *            the bicycleOwnership to set
	 */
	public void setBicycleOwnership(int bicycleOwnership) {
		this.bicycleOwnership = bicycleOwnership;
	}

	/**
	 * @param carOwnership
	 *            the carOwnership to set
	 */
	public void setCarOwnership(int carOwnership) {
		this.carOwnership = carOwnership;
	}

	/**
	 * @param creditScore
	 *            the creditScore to set
	 */
	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

	/**
	 * @param criminalRecord
	 *            the criminalRecord to set
	 */
	public void setCriminalRecord(int criminalRecord) {
		this.criminalRecord = criminalRecord;
	}

	/**
	 * @param downPayment
	 *            the downPayment to set
	 */
	public void setDownPayment(int downPayment) {
		this.downPayment = downPayment;
	}

	/**
	 * @param income
	 *            the income to set
	 */
	public void setIncome(int incomeType) {
		this.income = incomeType;
	}

	/**
	 * @param jobClass
	 *            the jobClass to set
	 */
	public void setJobClass(int jobClass) {
		this.jobClass = jobClass;
	}

	/**
	 * @param propertyOwnership
	 *            the propertyOwnership to set
	 */
	public void setPropertyOwnership(int propertyOwnership) {
		this.propertyOwnership = propertyOwnership;
	}

	/**
	 * @param retirementAccount
	 *            the retirementAccount to set
	 */
	public void setRetirementAccount(int retirementAccount) {
		this.retirementAccount = retirementAccount;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toExternalString() {
		return username + ":" + jobClass + ":" + carOwnership + ":"
				+ bicycleOwnership + ":" + propertyOwnership + ":"
				+ retirementAccount + ":" + creditScore + ":" + age + ":"
				+ downPayment + ":" + bankruptcy + ":" + criminalRecord + ":"
				+ income;
	}

	@Override
	public String toString() {
		return toExternalString();
	}

}
