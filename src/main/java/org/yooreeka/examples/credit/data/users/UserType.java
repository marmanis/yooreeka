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

import java.util.HashMap;
import java.util.Random;

public abstract class UserType {

	public static final String EXCELLENT = "EX";
	public static final String VERY_GOOD = "VG";
	public static final String GOOD = "GD";
	public static final String BAD = "BD";
	public static final String DANGEROUS = "DN";

	private static volatile HashMap<String, Double[]> noiseLevels;

	/**
	 * This method allows the insertion of custom noise levels by credit type.
	 * 
	 * @param type
	 * @param levels
	 */
	public static void addNoiseLevel(String type, Double[] levels) {

		if (noiseLevels.containsKey(type)) {
			System.out.println("WARN: Replacing noise levels for credit type: "
					+ type);
		}
		UserType.noiseLevels.put(type, levels);
	}

	/**
	 * This method returns the noise levels by credit type
	 * 
	 * @return the noiseLevels
	 */
	public static HashMap<String, Double[]> getNoiseLevels() {
		return UserType.noiseLevels;
	}

	/**
	 * This method allows the insertion of custom noise levels in bulk
	 * 
	 * @param noiseLevels
	 *            the noiseLevels to set
	 */
	public static void setNoiseLevels(HashMap<String, Double[]> noiseLevels) {
		UserType.noiseLevels = noiseLevels;
	}
	private Random rnd = new Random();
	private int nUsers;
	private int[] jobClass;
	private int[] carOwnership;
	private int[] motorcycleOwnership;
	private int[] propertyOwnership;
	private int[] retirementAccounts;
	private int[] creditScore;
	private int[] age;
	private int[] downPayment;

	private int[] bancruptcy;

	private int[] criminalRecord;

	private int[] income;

	static {
		// Set the default noise levels
		noiseLevels = new HashMap<String, Double[]>();

		Double[] exLevels = new Double[] { 1.0d, 3.0d, 7.5d, 10.0d };
		Double[] vgLevels = new Double[] { 1.0d, 3.0d, 6.0d, 10.0d };
		Double[] gdLevels = new Double[] { 1.0d, 3.0d, 4.0d, 8.0d };
		Double[] bdLevels = new Double[] { 1.0d, 3.0d, 7.5d, 10.0d };
		Double[] dnLevels = new Double[] { 1.0d, 4.5d, 9.0d, 13.5d };

		noiseLevels.put(EXCELLENT, exLevels);
		noiseLevels.put(VERY_GOOD, vgLevels);
		noiseLevels.put(GOOD, gdLevels);
		noiseLevels.put(BAD, bdLevels);
		noiseLevels.put(DANGEROUS, dnLevels);
	}

	public UserType() {
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
		final UserType other = (UserType) obj;
		if (getUserType() == null) {
			if (other.getUserType() != null)
				return false;
		} else if (!getUserType().equals(other.getUserType()))
			return false;
		return true;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the age
	 */
	public int[] getAge() {
		return age;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the bancruptcy
	 */
	public int[] getBancruptcy() {
		return bancruptcy;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the carOwnership
	 */
	public int[] getCarOwnership() {
		return carOwnership;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the creditScore
	 */
	public int[] getCreditScore() {
		return creditScore;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the criminalRecord
	 */
	public int[] getCriminalRecord() {
		return criminalRecord;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the downPayment
	 */
	public int[] getDownPayment() {
		return downPayment;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the income
	 */
	public int[] getIncome() {
		return income;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the jobClass
	 */
	public int[] getJobClass() {
		return jobClass;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the motorcycleOwnership
	 */
	public int[] getMotorcycleOwnership() {
		return motorcycleOwnership;
	}

	public String getNoisyType() {

		double gaussian = rnd.nextGaussian();

		String noisyType = null;

		String userType = getUserType();

		Double[] nLevels = noiseLevels.get(userType);

		if (getUserType().equals(EXCELLENT)) {

			if (gaussian <= nLevels[0]) {

				noisyType = EXCELLENT;

			} else if (gaussian > nLevels[0] && gaussian <= nLevels[1]) {

				noisyType = VERY_GOOD;

			} else if (gaussian > nLevels[1] && gaussian <= nLevels[2]) {

				noisyType = GOOD;

			} else if (gaussian > nLevels[2] && gaussian <= nLevels[3]) {

				noisyType = BAD;

			} else {

				noisyType = DANGEROUS;
			}

		} else if (getUserType().equals(VERY_GOOD)) {

			if (gaussian <= nLevels[0]) {

				noisyType = VERY_GOOD;

			} else if (gaussian > nLevels[0] && gaussian <= nLevels[1]) {

				noisyType = GOOD;

			} else if (gaussian > nLevels[1] && gaussian <= nLevels[2]) {

				noisyType = EXCELLENT;

			} else if (gaussian > nLevels[2] && gaussian <= nLevels[3]) {

				noisyType = BAD;

			} else {

				noisyType = DANGEROUS;
			}

		} else if (getUserType().equals(GOOD)) {

			if (gaussian <= nLevels[0]) {

				noisyType = GOOD;

			} else if (gaussian > nLevels[0] && gaussian <= nLevels[1]) {

				noisyType = VERY_GOOD;

			} else if (gaussian > nLevels[1] && gaussian <= nLevels[2]) {

				noisyType = EXCELLENT;

			} else if (gaussian > nLevels[2] && gaussian <= nLevels[3]) {

				noisyType = BAD;

			} else {

				noisyType = DANGEROUS;
			}

		} else if (getUserType().equals(BAD)) {

			if (gaussian <= nLevels[0]) {

				noisyType = BAD;

			} else if (gaussian > nLevels[0] && gaussian <= nLevels[1]) {

				noisyType = GOOD;

			} else if (gaussian > nLevels[1] && gaussian <= nLevels[2]) {

				noisyType = DANGEROUS;

			} else if (gaussian > nLevels[2] && gaussian <= nLevels[3]) {

				noisyType = VERY_GOOD;

			} else {

				noisyType = EXCELLENT;
			}

		} else if (getUserType().equals(DANGEROUS)) {

			if (gaussian <= nLevels[0]) {

				noisyType = DANGEROUS;

			} else if (gaussian > nLevels[0] && gaussian <= nLevels[1]) {

				noisyType = BAD;

			} else if (gaussian > nLevels[1] && gaussian <= nLevels[2]) {

				noisyType = GOOD;

			} else if (gaussian > nLevels[2] && gaussian <= nLevels[3]) {

				noisyType = VERY_GOOD;

			} else {

				noisyType = EXCELLENT;
			}
		}

		return noisyType;
	}

	public int getNUsers() {
		return nUsers;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the propertyOwnership
	 */
	public int[] getPropertyOwnership() {
		return propertyOwnership;
	}

	// -----------------------------------------------------------------
	/**
	 * @return the retirementAccounts
	 */
	public int[] getRetirementAccounts() {
		return retirementAccounts;
	}

	public abstract String getUserType();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getUserType() == null) ? 0 : getUserType().hashCode());
		return result;
	}

	public int pickAge() {
		return age[rnd.nextInt(age.length)];
	}

	public int pickBancruptcy() {
		return bancruptcy[rnd.nextInt(bancruptcy.length)];
	}

	public int pickCarOwnership() {
		return carOwnership[rnd.nextInt(carOwnership.length)];
	}

	public int pickCreditScore() {
		return creditScore[rnd.nextInt(creditScore.length)];
	}

	public int pickCriminalRecord() {
		return criminalRecord[rnd.nextInt(criminalRecord.length)];
	}

	public int pickDownPayment() {
		return downPayment[rnd.nextInt(downPayment.length)];
	}

	public int pickIncome() {
		return income[rnd.nextInt(income.length)];
	}

	/**
	 * This method, and the other "pickX()" methods in this class, select a
	 * random value from the set of eligible values for a particular
	 * <code>UserType</code>. Hence, clearly, the returned values will be
	 * different for the different <code>UserType</code>s.
	 * 
	 * @return a random selection from the set of eligible job classes.
	 */
	public int pickJobClass() {
		return jobClass[rnd.nextInt(jobClass.length)];
	}

	public int pickMotorcycleOwnership() {
		return motorcycleOwnership[rnd.nextInt(motorcycleOwnership.length)];
	}

	public int pickPropertyOwnership() {
		return propertyOwnership[rnd.nextInt(propertyOwnership.length)];
	}

	public int pickRetirementAccounts() {
		return retirementAccounts[rnd.nextInt(retirementAccounts.length)];
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(int[] age) {
		this.age = age;
	}

	/**
	 * @param bancruptcy
	 *            the bancruptcy to set
	 */
	public void setBancruptcy(int[] bancruptcy) {
		this.bancruptcy = bancruptcy;
	}

	/**
	 * @param carOwnership
	 *            the carOwnership to set
	 */
	public void setCarOwnership(int[] carOwnership) {
		this.carOwnership = carOwnership;
	}

	/**
	 * @param creditScore
	 *            the creditScore to set
	 */
	public void setCreditScore(int[] creditScore) {
		this.creditScore = creditScore;
	}

	/**
	 * @param criminalRecord
	 *            the criminalRecord to set
	 */
	public void setCriminalRecord(int[] criminalRecord) {
		this.criminalRecord = criminalRecord;
	}

	/**
	 * @param downPayment
	 *            the downPayment to set
	 */
	public void setDownPayment(int[] downPayment) {
		this.downPayment = downPayment;
	}

	// -----------------------------------------------------------------

	/**
	 * @param income
	 *            the income to set
	 */
	public void setIncome(int[] income) {
		this.income = income;
	}

	/**
	 * @param jobClass
	 *            the jobClass to set
	 */
	public void setJobClass(int[] jobClass) {
		this.jobClass = jobClass;
	}

	/**
	 * @param motorcycleOwnership
	 *            the motorcycleOwnership to set
	 */
	public void setMotorcycleOwnership(int[] bicycleOwnership) {
		this.motorcycleOwnership = bicycleOwnership;
	}

	public void setNUsers(int nUsers) {
		this.nUsers = nUsers;
	}

	/**
	 * @param propertyOwnership
	 *            the propertyOwnership to set
	 */
	public void setPropertyOwnership(int[] propertyOwnership) {
		this.propertyOwnership = propertyOwnership;
	}

	/**
	 * @param retirementAccounts
	 *            the retirementAccounts to set
	 */
	public void setRetirementAccounts(int[] retirementAccounts) {
		this.retirementAccounts = retirementAccounts;
	}

}
