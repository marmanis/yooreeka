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
package org.yooreeka.util.internet.crawling.transport.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.db.FetchedDocsDB;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.common.TransportException;

// TODO This class now doesn't do anything. HTTP crawling should be replaced by a much simpler implementation
// since that is not of essence for the Yooreeka library
public class HTTPTransport implements Transport {

	public static final int MINIMUM_BUFFER_SIZE=1024;
	
	private FetchedDocsDB db;
	
	/*
	 * Maximum document length that transport will attempt to download
	 * without issuing a warning ...
	 */
	public static final int MAX_DOCUMENT_LENGTH = 8 * 1024 * 1024; // 2Mb

	CloseableHttpClient httpclient = null;
	
	HttpContext localContext = null;

	public HTTPTransport() {
		
		P.println("Initializing HTTPTransport ...");

	}

	public void clear() {
		httpclient = null;
	}

	private FetchedDocument createDocument(String targetURL, HttpEntity entity, String groupId, int docSequenceInGroup)
			throws IOException, HTTPTransportException {
		
		FetchedDocument doc = new FetchedDocument();
		// IMPLEMENTATION PURGED
		return doc;
	}

	public FetchedDocument fetch(String documentUrl, String groupId, int docSequenceInGroup)
			throws TransportException {

		FetchedDocument doc = null;
		// IMPLEMENTATION PURGED
		return doc;
	}

	public boolean pauseRequired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.yooreeka.util.internet.crawling.transport.common.Transport#fixDud()
	 */
	@Override
	public void fixDud(InputStream in) {
		String empty = YooreekaConfigurator.getProperty("yooreeka.crawl.dudfile");
		
		try {
			in = new BufferedInputStream(new FileInputStream(new File(empty)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.yooreeka.util.internet.crawling.transport.common.Transport#setFetchedDocsDB(org.yooreeka.util.internet.crawling.db.FetchedDocsDB)
	 */
	@Override
	public void setFetchedDocsDB(FetchedDocsDB db) {
		this.db = db;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
