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
package org.yooreeka.examples.spamfilter.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.examples.spamfilter.EmailInstance;

public class EmailDataset {

	private Map<String, Email> emails;

	// By default we set up an email dataset for binary classification
	private boolean isBinary = true;

	public EmailDataset(List<Email> emailList) {
		this.emails = new HashMap<String, Email>(emailList.size());
		for (Email e : emailList) {
			emails.put(e.getId(), e);
		}
	}

	private List<EmailInstance> createEmailInstances(int topNTerms) {
		List<EmailInstance> allInstances = new ArrayList<EmailInstance>();
		for (Email email : getEmails()) {
			EmailInstance i = toEmailInstance(email, topNTerms);
			allInstances.add(i);
		}
		return allInstances;
	}

	public Email findEmailById(String id) {
		return emails.get(id);
	}

	private String getEmailCategory(Email email) {

		if (isBinary()) {
			if (email.getId().startsWith("spam-")) {
				return "SPAM";
			} else {
				return "NOT SPAM";
			}
		} else {
			// relying id to have pattern: "biz-???", "world-???", ...
			String[] parts = email.getId().split("-");
			if (parts.length < 2) {
				throw new RuntimeException(
						"Unsupported id format. Expected id format: '<catgory>-???'");
			}
			return parts[0].toUpperCase();
		}
	}

	public List<Email> getEmails() {
		return new ArrayList<Email>(emails.values());
	}

	public int getSize() {
		return emails.size();
	}

	public TrainingSet getTrainingSet(int topNTerms) {
		List<EmailInstance> allInstances = createEmailInstances(topNTerms);
		EmailInstance[] instances = allInstances
				.toArray(new EmailInstance[allInstances.size()]);
		return new TrainingSet(instances);
	}

	/**
	 * @return the isBinary
	 */
	public boolean isBinary() {
		return isBinary;
	}

	public void printAll() {
		for (Map.Entry<String, Email> e : emails.entrySet()) {
			Email email = e.getValue();
			System.out.println(email);
		}
	}

	public void printEmail(String id) {
		Email e = findEmailById(id);
		if (e != null) {
			System.out.println(e.toString());
		} else {
			System.out.println("Email not found (email id: '" + id + "')");
		}
	}

	/**
	 * @param isBinary
	 *            the isBinary to set
	 */
	public void setBinary(boolean isBinary) {
		this.isBinary = isBinary;
	}

	public EmailInstance toEmailInstance(Email email, int topNTerms) {
		String emailCategory = getEmailCategory(email);
		return new EmailInstance(emailCategory, email, topNTerms);
	}
}
