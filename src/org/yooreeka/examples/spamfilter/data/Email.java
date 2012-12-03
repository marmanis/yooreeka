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

/**
 * Represents one email document.
 */
public class Email {

	/*
	 * ID that we will use to identify email.
	 */
	private String id;

	/*
	 * Email subject line
	 */
	private String subject;

	/*
	 * Email Text body
	 */
	private String textBody;

	private String from;

	private String to;

	int ruleFired = 0;

	public Email() {
		// empty
	}

	public String getFrom() {
		return from;
	}

	public String getId() {
		return id;
	}

	public int getRuleFired() {
		return ruleFired;
	}

	public String getSubject() {
		return subject;
	}

	public String getTextBody() {
		return textBody;
	}

	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRuleFired(int ruleNum) {
		System.out.println("Invoked " + this.getClass().getSimpleName()
				+ ".setRuleFired(" + ruleNum + "), current value ruleFired="
				+ this.ruleFired + ", emailId: " + id);
		this.ruleFired = ruleNum;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "id: " + id + "\n" + "from: " + from + "\n" + "to: " + to + "\n"
				+ "subject: " + subject + "\n" + textBody + "\n";
	}
}
