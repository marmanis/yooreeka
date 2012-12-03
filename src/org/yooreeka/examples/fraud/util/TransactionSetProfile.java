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

/**
 * Configuration properties to control generation of user transactions.
 */
public class TransactionSetProfile {

	/*
	 * Identifies Credit Card User.
	 */
	private int userId;

	private int nTxns;

	/*
	 * Mean value for transaction amount.
	 */
	private double txnAmtMean;

	/*
	 * Standard deviation for transaction amount.
	 */
	private double txnAmtStd;

	/*
	 * Location coordinates.
	 */
	private int locationMinX;
	private int locationMaxX;
	private int locationMinY;
	private int locationMaxY;

	/*
	 * Descriptions that will be used for valid transactions.
	 */
	private String[] txnDescriptions;

	private boolean isFraud;

	public TransactionSetProfile() {
		// empty
	}

	public int getLocationMaxX() {
		return locationMaxX;
	}

	public int getLocationMaxY() {
		return locationMaxY;
	}

	public int getLocationMinX() {
		return locationMinX;
	}

	public int getLocationMinY() {
		return locationMinY;
	}

	public int getNTxns() {
		return nTxns;
	}

	public double getTxnAmtMean() {
		return txnAmtMean;
	}

	public double getTxnAmtStd() {
		return txnAmtStd;
	}

	public String[] getTxnDescriptions() {
		return txnDescriptions;
	}

	public int getUserId() {
		return userId;
	}

	public boolean isFraud() {
		return isFraud;
	}

	public void setFraud(boolean isFraud) {
		this.isFraud = isFraud;
	}

	public void setLocations(int minX, int minY, int maxX, int maxY) {
		this.locationMinX = minX;
		this.locationMinY = minY;
		this.locationMaxX = maxX;
		this.locationMaxY = maxY;
	}

	public void setNTxns(int txns) {
		nTxns = txns;
	}

	public void setTxnAmtMean(double txnAmtMean) {
		this.txnAmtMean = txnAmtMean;
	}

	public void setTxnAmtStd(double txnAmtStd) {
		this.txnAmtStd = txnAmtStd;
	}

	public void setTxnDescriptions(String[] txnDescriptions) {
		this.txnDescriptions = txnDescriptions;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
