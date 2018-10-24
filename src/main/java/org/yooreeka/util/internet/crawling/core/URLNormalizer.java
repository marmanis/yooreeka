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

import java.net.URL;

/**
 * Performs url normalization.
 */
public class URLNormalizer {
	
	public URLNormalizer() {
		// empty
	}

	/*
	 * If we are adding a URL that refers to a file 
	 * it will be delegated to the following method
	 */
	private String normalizeFileUrl(String fileUrl) {
		try {
			URL url = new URL(fileUrl);
			return url.toExternalForm();
		} catch (Exception e) {
			throw new RuntimeException("URL Normalization error: ", e);
		}
	}

	/**
	 * Implementation that does nothing.
	 * 
	 * <p>
	 * Other features that can be added are:
	 * <ul>
	 * <li>convert IP address into DNS name</li>
	 * <li>lower-case DNS name</li>
	 * <li>extract session id from the URL</li>
	 * <li>process escape sequences</li>
	 * <li>remove default port</li>
	 * <li>remove fragment portion from the url</li>
	 * <li>sort variables</li>
	 * <li>...and a lot more</li>
	 * </ul>
	 * </p>
	 * 
	 */
	public String normalizeUrl(String url) {
		String normalizedUrl = url;
		if (url.startsWith("file://")) {
			normalizedUrl = normalizeFileUrl(url);
		}
		return normalizedUrl;
	}
}
