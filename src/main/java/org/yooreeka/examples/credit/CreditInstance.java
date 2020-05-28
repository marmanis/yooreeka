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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Instance;

public class CreditInstance implements Instance {

	public static final String ATTR_NAME_USERID = "userid";
	public static final String ATTR_NAME_JOB_CLASS = "jobClass";
	public static final String ATTR_NAME_INCOME_TYPE = "incomeType";
	public static final String ATTR_NAME_CAR_OWNERSHIP = "carOwnership";
	public static final String ATTR_NAME_MOTOR_BICYCLE_OWNERSHIP = "motorBicycleOwnership";
	public static final String ATTR_NAME_OTHER_PROPERTY_OWNERSHIP = "otherPropertyOwnership";
	public static final String ATTR_NAME_RETIREMENT_ACCOUNT = "retirementAccount";
	public static final String ATTR_NAME_CREDIT_SCORE = "creditScore";
	public static final String ATTR_NAME_AGE = "age";
	public static final String ATTR_NAME_MORTGAGE_DOWN_PAYMENT = "mortgageDownPayment";
	public static final String ATTR_NAME_BANKRUPTCY = "priorDeclaredBankruptcy";
	public static final String ATTR_NAME_CRIMINAL_RECORD = "priorCriminalRecord";

	protected CreditConcept concept;
	protected Attribute[] attributes;

	public CreditInstance(CreditConcept c, Attribute[] attrs) {
		this.concept = c;
		this.attributes = attrs;
	}

	public CreditInstance(CreditConcept c, List<Attribute> attrs) {
		this(c, attrs.toArray(new Attribute[attrs.size()]));
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

	public CreditConcept getConcept() {
		return concept;
	}

	public void print() {
		print(new PrintWriter(System.out));
	}

	public void print(PrintWriter writer) {
		if (attributes != null) {
			for (Attribute a : attributes) {

				if (a == null || a.getName() == null) {
					writer.print(" -  <NULL ATTRIBUTE> ");
				} else {
					if (a.getValue() == null) {
						writer.print(" -  <NULL ATTRIBUTE VALUE> ");
					} else {
						writer.print(" -  " + a.getName() + " = "
								+ a.getValue());
					}
				}
			}
		}

		writer.println(" -->  " + getConcept().getName());
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		print(new PrintWriter(sw));
		return sw.toString();
	}

}
