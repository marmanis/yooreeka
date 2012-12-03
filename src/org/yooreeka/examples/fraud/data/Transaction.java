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

public class Transaction implements java.io.Serializable {

	private static final long serialVersionUID = -4537757080789309552L;

	private String description;

	private TransactionLocation location;

	private double amount;

	private boolean fraud;

	private int userId;

	private long txnId;

	public Transaction() {
	}

	public double getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public TransactionLocation getLocation() {
		return location;
	}

	public long getTxnId() {
		return txnId;
	}

	public int getUserId() {
		return userId;
	}

	public boolean isFraud() {
		return fraud;
	}

	public void loadFromExternalString(String text) {

		String[] values = text.split(":");

		userId = Integer.parseInt(values[0]);
		txnId = Long.parseLong(values[1]);
		description = values[2];
		amount = Double.parseDouble(values[3]);
		double x = Double.parseDouble(values[4]);
		double y = Double.parseDouble(values[5]);
		location = new TransactionLocation(x, y);
		fraud = Boolean.parseBoolean(values[6]);
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFraud(boolean fraud) {
		this.fraud = fraud;
	}

	public void setLocation(TransactionLocation location) {
		this.location = location;
	}

	public void setTxnId(long txnId) {
		this.txnId = txnId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String toExternalString() {
		return userId + ":" + txnId + ":" + description + ":" + amount + ":"
				+ location.getX() + ":" + location.getY() + ":" + fraud;
	}

	@Override
	public String toString() {
		return toExternalString();
	}

}
