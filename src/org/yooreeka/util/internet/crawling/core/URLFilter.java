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
package org.yooreeka.util.internet.crawling.core;

/**
 * Performs url filtering before url is registered in 'known urls' database.
 */
public class URLFilter {

	private boolean allowFileUrls = true;
	private boolean allowHttpUrls = true;

	public URLFilter() {
		// empty
	}

	/**
	 * Basic implementation of url filter. Only allows urls that start with
	 * 'http:' and 'file:'
	 * 
	 * <p>
	 * Other features that can be added are:
	 * <ul>
	 * <li>extract host from the url and check against robots.txt</li>
	 * <li>check against the list of excluded urls</li>
	 * <li>user defined criteria</li>
	 * </ul>
	 * </p>
	 */
	public boolean accept(String url) {
		boolean acceptUrl = false;
		if (allowFileUrls && url.startsWith("file:")) {
			acceptUrl = true;
		} else if (allowHttpUrls && url.startsWith("http:")) {
			acceptUrl = true;
		} else {
			acceptUrl = false;
			System.out.println("DEBUG: Filtered url: '" + url + "'");
		}

		return acceptUrl;
	}

	public void setAllowFileUrls(boolean flag) {
		this.allowFileUrls = flag;
	}

	public void setAllowHttpUrls(boolean flag) {
		this.allowHttpUrls = flag;
	}
}
