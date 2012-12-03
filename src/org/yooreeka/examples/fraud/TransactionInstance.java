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
package org.yooreeka.examples.fraud;

import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Instance;

public class TransactionInstance implements Instance {

	public static final String ATTR_NAME_N_TXN_AMT = "n_txnamt";
	public static final String ATTR_NAME_N_LOCATION = "n_location";
	public static final String ATTR_NAME_N_DESCRIPTION = "n_description";
	public static final String ATTR_NAME_USERID = "userid";
	public static final String ATTR_NAME_TXNID = "txnid";
	public static final String ATTR_NAME_TXN_AMT = "txnamt";
	public static final String ATTR_NAME_LOCATION_X = "location_x";
	public static final String ATTR_NAME_LOCATION_Y = "location_y";
	public static final String ATTR_NAME_DESCRIPTION = "description";

	protected TransactionConcept concept;
	protected Attribute[] attributes;

	public TransactionInstance(TransactionConcept c, Attribute[] attrs) {
		this.concept = c;
		this.attributes = attrs;
	}

	public Attribute[] getAtrributes() {
		return attributes;
	}

	public Attribute getAttributeByName(String attrName) {
		Attribute matchedAttribute = null;

		if (attributes != null) {
			for (Attribute a : attributes) {
				if (attrName.equalsIgnoreCase(a.getName())) {
					matchedAttribute = a;
					break;
				}
			}
		}

		return matchedAttribute;
	}

	public TransactionConcept getConcept() {
		return concept;
	}

	public void print() {
		if (attributes != null) {
			for (Attribute a : attributes) {

				if (a == null || a.getName() == null) {
					System.out.print(" -  <NULL ATTRIBUTE> ");
				} else {
					if (a.getValue() == null) {
						System.out.print(" -  <NULL ATTRIBUTE VALUE> ");
					} else {
						System.out.print(" -  " + a.getName() + " = "
								+ a.getValue());
					}
				}
			}
		}

		System.out.println(" -->  " + getConcept().getName());
	}

}
