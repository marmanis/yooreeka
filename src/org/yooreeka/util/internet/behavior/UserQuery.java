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

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;

/**
 * This is a class that encapsulates a personalized query
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class UserQuery {

	private String uid;
	private String queryString;
	private String[] queryTerms;
	private Query query;

	public UserQuery(String uid, String q) throws IOException {

		setUid(uid);
		setQueryString(q);

		PhraseQuery query = new PhraseQuery();
		query.add(new Term("content", q));

		Term[] terms = query.getTerms();
		queryTerms = new String[terms.length];

		for (int i = 0; i < terms.length; i++) {

			queryTerms[i] = terms[i].text();
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
		final UserQuery other = (UserQuery) obj;
		if (queryString == null) {
			if (other.queryString != null)
				return false;
		} else if (!queryString.equals(other.queryString))
			return false;
		if (!Arrays.equals(queryTerms, other.queryTerms))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

	public String getName() {
		return UserQuery.class.getCanonicalName();
	}

	public Query getQuery() {
		return query;
	}

	/**
	 * @return the query
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @return the queryTerms
	 */
	public String[] getQueryTerms() {
		return queryTerms;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	public UserQuery getValue() {

		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((queryString == null) ? 0 : queryString.hashCode());
		result = prime * result + Arrays.hashCode(queryTerms);
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQueryString(String query) {
		this.queryString = query;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

}
