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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.yooreeka.examples.fraud.data.TransactionLocation;

/**
 * Holds user-specific statistics that are calculated from training data.
 */
public class UserStatistics implements java.io.Serializable {

	private static final long serialVersionUID = -7537387975282866317L;

	private int userId;
	private double txnAmtMin;
	private double txnAmtMax;
	private Map<String, String[]> descriptionTokensMap;
	private TransactionLocation locationCentroid;
	private double locationMinX;
	private double locationMaxX;
	private double locationMinY;
	private double locationMaxY;

	public UserStatistics() {
		descriptionTokensMap = new HashMap<String, String[]>();
	}

	public Set<String> getDescriptions() {
		return descriptionTokensMap.keySet();
	}

	public String[] getDescriptionTokens(String d) {
		return this.descriptionTokensMap.get(d);
	}

	public TransactionLocation getLocationCentroid() {
		return locationCentroid;
	}

	public double getLocationMaxX() {
		return locationMaxX;
	}

	public double getLocationMaxY() {
		return locationMaxY;
	}

	public double getLocationMinX() {
		return locationMinX;
	}

	public double getLocationMinY() {
		return locationMinY;
	}

	public Double getTxnAmtMax() {
		return txnAmtMax;
	}

	public Double getTxnAmtMin() {
		return txnAmtMin;
	}

	public int getUserId() {
		return userId;
	}

	public void setDescriptions(Set<String> descriptions) {
		descriptionTokensMap.clear();
		for (String d : descriptions) {
			this.descriptionTokensMap.put(d, null);
		}
	}

	public void setDescriptionTokens(String d, String[] tokens) {
		this.descriptionTokensMap.put(d, tokens);
	}

	public void setLocationCentroid(TransactionLocation locationCentroid) {
		this.locationCentroid = locationCentroid;
	}

	public void setLocationMaxX(double locationMaxX) {
		this.locationMaxX = locationMaxX;
	}

	public void setLocationMaxY(double locationMaxY) {
		this.locationMaxY = locationMaxY;
	}

	public void setLocationMinX(double locationMinX) {
		this.locationMinX = locationMinX;
	}

	public void setLocationMinY(double locationMinY) {
		this.locationMinY = locationMinY;
	}

	public void setTxnAmtMax(Double txnAmountMax) {
		this.txnAmtMax = txnAmountMax;
	}

	public void setTxnAmtMin(Double txnAmountMin) {
		this.txnAmtMin = txnAmountMin;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "[userId=" + userId + ", txnAmtMin=" + txnAmtMin
				+ ", txnAmtMax=" + txnAmtMax + ", locationMinX=" + locationMinX
				+ ", locationMaxX=" + locationMaxX + ", locationMinY="
				+ locationMinY + ", locationMaxY=" + locationMaxY
				+ ", descriptions=" + descriptionTokensMap.keySet().toString()
				+ ", locationCentroid=" + locationCentroid.toString() + "]";
	}

}
