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

public class VeryGoodUserType extends UserType {

	{
		setAge(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 });
		setBancruptcy(new int[] { 0 });
		setCarOwnership(new int[] { 1 });
		setCreditScore(new int[] { 5, 6, 7 });
		setCriminalRecord(new int[] { 0 });
		setDownPayment(new int[] { 3, 4 });
		setIncome(new int[] { 4, 5, 6, 7 });
		setJobClass(new int[] { 2, 3, 4, 5 });
		setMotorcycleOwnership(new int[] { 0, 1 });
		setPropertyOwnership(new int[] { 1 });
		setRetirementAccounts(new int[] { 3, 4, 5 });
	}

	@Override
	public String getUserType() {
		return UserType.VERY_GOOD;
	}
}