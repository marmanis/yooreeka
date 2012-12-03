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
package org.yooreeka.util.internet.behavior;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.yooreeka.algos.taxis.core.BaseConcept;
import org.yooreeka.algos.taxis.core.BaseInstance;
import org.yooreeka.algos.taxis.core.StringAttribute;

/**
 * Auxiliary class that captures a user click.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class UserClick extends BaseInstance {

	UserQuery userQuery;
	String url;

	public UserClick() {
		super();
	}

	public UserClick(UserQuery uQ, String url) {

		super();

		userQuery = uQ;
		this.setConcept(new BaseConcept(url));

		attributes = new StringAttribute[userQuery.getQueryTerms().length + 1];

		attributes[0] = new StringAttribute("UserName", userQuery.getUid());

		int j = 1;
		for (String s : uQ.getQueryTerms()) {
			attributes[j] = new StringAttribute("QueryTerm_" + j, s);
			j++;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UserClick other = (UserClick) obj;
		if (getUrl() == null) {
			if (other.getUrl() != null)
				return false;
		} else if (!getUrl().equals(other.getUrl()))
			return false;
		if (userQuery == null) {
			if (other.userQuery != null)
				return false;
		} else if (!userQuery.equals(other.userQuery))
			return false;
		return true;
	}

	/**
	 * The concept of a user click is its URL
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return getConcept().getName();
	}

	/**
	 * @return the userQuery
	 */
	public UserQuery getUserQuery() {
		return userQuery;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getUrl() == null) ? 0 : getUrl().hashCode());
		result = prime * result
				+ ((userQuery == null) ? 0 : userQuery.hashCode());
		return result;
	}

	@Override
	public UserClick[] load(BufferedReader bR) throws IOException {

		ArrayList<UserClick> userClicks = new ArrayList<UserClick>();

		String line;
		boolean hasMoreLines = true;

		while (hasMoreLines) {

			line = bR.readLine();

			if (line == null) {

				hasMoreLines = false;

			} else {

				String[] data = line.split(",");

				UserQuery uQ = new UserQuery(data[0], data[1]);

				UserClick userClick = new UserClick(uQ, data[2].substring(1,
						data[2].length() - 1));

				userClick.print();

				userClicks.add(userClick);
			}
		}

		return userClicks.toArray(new UserClick[userClicks.size()]);
	}

}
