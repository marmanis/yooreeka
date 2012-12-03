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
package org.yooreeka.examples.credit;

import org.yooreeka.algos.taxis.core.BaseConcept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.credit.data.users.UserType;

public class CreditConcept extends BaseConcept {

	public static final String CONCEPT_LABEL_EX = UserType.EXCELLENT;
	public static final String CONCEPT_LABEL_VG = UserType.VERY_GOOD;
	public static final String CONCEPT_LABEL_GD = UserType.GOOD;
	public static final String CONCEPT_LABEL_BD = UserType.BAD;
	public static final String CONCEPT_LABEL_DN = UserType.DANGEROUS;

	public static int getIndex(String val) {
		int index = -1;
		if (val.equals(CONCEPT_LABEL_EX)) {
			index = 0;
		} else if (val.equals(CONCEPT_LABEL_VG)) {
			index = 1;
		} else if (val.equals(CONCEPT_LABEL_GD)) {
			index = 2;
		} else if (val.equals(CONCEPT_LABEL_BD)) {
			index = 3;
		} else if (val.equals(CONCEPT_LABEL_DN)) {
			index = 4;
		} else {
			throw new IllegalArgumentException("Unknown CreditConcept name!");
		}
		return index;
	}

	public static String getLabel(int val) {

		String label = null;

		if (val == 0) {
			label = CONCEPT_LABEL_EX;
		} else if (val == 1) {
			label = CONCEPT_LABEL_VG;
		} else if (val == 2) {
			label = CONCEPT_LABEL_GD;
		} else if (val == 3) {
			label = CONCEPT_LABEL_BD;
		} else if (val == 4) {
			label = CONCEPT_LABEL_DN;
		} else {
			throw new IllegalArgumentException(
					"Unknown CreditConcept index for label!");
		}
		return label;
	}

	public CreditConcept(String name) {
		super(name);
	}

	@Override
	public Instance[] getInstances() {
		throw new UnsupportedOperationException("not implemented.");
	}
}
