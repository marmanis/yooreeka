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
import java.util.List;

import org.yooreeka.algos.taxis.core.DoubleAttribute;
import org.yooreeka.algos.taxis.core.StringAttribute;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Attribute;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.credit.CreditConcept;
import org.yooreeka.examples.credit.CreditInstance;
import org.yooreeka.examples.credit.data.UserDataset;
import org.yooreeka.examples.credit.data.users.User;

public class UserInstanceBuilder {

	private boolean useDoubleAttributes;

	public UserInstanceBuilder() {
		this(false);
	}

	/**
	 * 
	 * @param useDoubleAttributes
	 *            determines whether instance builder should produce instances
	 *            with string attributes or double attributes.
	 */
	public UserInstanceBuilder(boolean useDoubleAttributes) {

		this.useDoubleAttributes = useDoubleAttributes;
	}

	private CreditInstance convertToDoubleAttributes(Instance instance) {

		CreditInstance creditInstance = (CreditInstance) instance;

		List<Attribute> attributes = new ArrayList<Attribute>();

		for (Attribute a : creditInstance.getAtrributes()) {
			DoubleAttribute da = null;
			if (a instanceof StringAttribute) {
				String name = a.getName();
				double value = Double.valueOf((String) a.getValue());
				// double normalizedValue = value;
				double normalizedValue = AttributeUtils.getNormalizedValue(
						name, value);
				da = new DoubleAttribute(name, normalizedValue);
			} else if (a instanceof DoubleAttribute) {
				da = (DoubleAttribute) a;
			} else {
				throw new RuntimeException("Unexpected attribute type: "
						+ a.getClass().getSimpleName() + ", attribute name: "
						+ a.getName() + ", attribute value: " + a.getValue());
			}

			attributes.add(da);
		}

		return new CreditInstance(creditInstance.getConcept(), attributes);
	}

	public Instance createInstance(Instance i) {
		if (useDoubleAttributes) {
			return convertToDoubleAttributes(i);
		} else {
			return i;
		}
	}

	public Instance createInstance(User u) {
		List<Attribute> attributes = new ArrayList<Attribute>();

		attributes.add(new StringAttribute(CreditInstance.ATTR_NAME_JOB_CLASS,
				String.valueOf(u.getJobClass())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_INCOME_TYPE, String.valueOf(u
						.getIncome())));

		attributes.add(new StringAttribute(CreditInstance.ATTR_NAME_AGE, String
				.valueOf(u.getAge())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_CAR_OWNERSHIP, String.valueOf(u
						.getCarOwnership())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_CREDIT_SCORE, String.valueOf(u
						.getCreditScore())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_MORTGAGE_DOWN_PAYMENT, String
						.valueOf(u.getDownPayment())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_MOTOR_BICYCLE_OWNERSHIP, String
						.valueOf(u.getBicycleOwnership())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_OTHER_PROPERTY_OWNERSHIP, String
						.valueOf(u.getPropertyOwnership())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_CRIMINAL_RECORD, String.valueOf(u
						.getCriminalRecord())));

		attributes.add(new StringAttribute(CreditInstance.ATTR_NAME_BANKRUPTCY,
				String.valueOf(u.getBankruptcy())));

		attributes.add(new StringAttribute(
				CreditInstance.ATTR_NAME_RETIREMENT_ACCOUNT, String.valueOf(u
						.getRetirementAccount())));

		CreditConcept c = new CreditConcept(u.getCategory());

		CreditInstance instance = new CreditInstance(c, attributes);

		return createInstance(instance);
	}

	public TrainingSet createTrainingSet(UserDataset ds) {
		List<User> users = ds.getUsers();
		int nUsers = users.size();
		Instance[] instances = new Instance[nUsers];
		for (int i = 0; i < nUsers; i++) {
			User u = users.get(i);
			instances[i] = createInstance(u);
		}

		TrainingSet tS = new TrainingSet(instances);

		return tS;
	}

}
