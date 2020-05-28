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
package org.yooreeka.examples.spamfilter;

import java.util.Map;

import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.algos.taxis.core.BaseConcept;
import org.yooreeka.algos.taxis.core.BaseInstance;
import org.yooreeka.algos.taxis.core.StringAttribute;
import org.yooreeka.examples.spamfilter.data.Email;

/**
 * Instance for classification.
 */
public class EmailInstance extends BaseInstance {

	private static int DEFAULT_TOP_N_TERMS = 10;

	private String id;

	public EmailInstance(String emailCategory, Email email) {
		this(emailCategory, email, DEFAULT_TOP_N_TERMS);
	}

	public EmailInstance(String emailCategory, Email email, int topNTerms) {
		super();
		this.id = email.getId();
		// email category is our concept/class
		this.setConcept(new BaseConcept(emailCategory));

		/**
		 * TODO: 5.3 -- Considering more attributes as part of the EmailInstance
		 * 
		 * -- Separate "subject" and "body" -- timestamp -- "from" -- "to" --
		 * "to" cardinality
		 */
		// extract top N terms from email content and subject
		String text = email.getSubject() + " " + email.getTextBody();
		Content content = new Content(email.getId(), text, topNTerms);
		Map<String, Integer> tfMap = content.getTFMap();

		attributes = new StringAttribute[1];

		String attrName = "Email_Text_Attribute";
		String attrValue = "";
		for (Map.Entry<String, Integer> tfEntry : tfMap.entrySet()) {
			attrValue = attrValue + " " + tfEntry.getKey();
		}
		attributes[0] = new StringAttribute(attrName, attrValue);
	}

	@Override
	public String toString() {
		return id;
	}

}
