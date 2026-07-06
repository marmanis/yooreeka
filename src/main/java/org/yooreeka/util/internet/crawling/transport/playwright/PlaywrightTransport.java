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

package org.yooreeka.util.internet.crawling.transport.playwright;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;

import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.db.FetchedDocsDB;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.common.TransportException;
import org.yooreeka.util.internet.crawling.util.DocumentIdUtils;

public class PlaywrightTransport implements Transport {

	private Playwright playwright;
	private Browser browser;
	private FetchedDocsDB db;

	public PlaywrightTransport() {
		P.println("Initializing PlaywrightTransport...");
	}

	@Override
	public void init() {
		if (playwright == null) {
			playwright = Playwright.create();
			browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
		}
	}

	@Override
	public void clear() {
		if (browser != null) {
			browser.close();
			browser = null;
		}
		if (playwright != null) {
			playwright.close();
			playwright = null;
		}
	}

	@Override
	public FetchedDocument fetch(String url, String groupId, int docSequenceInGroup) throws TransportException {
		init(); // ensure initialized
		FetchedDocument doc = new FetchedDocument();
		String documentId = DocumentIdUtils.getDocumentId(groupId, docSequenceInGroup);
		doc.setDocumentId(documentId);
		doc.setDocumentURL(url);

		try (BrowserContext context = browser.newContext();
			 Page page = context.newPage()) {
			
			Response response = page.navigate(url);
			if (response == null) {
				throw new TransportException("Failed to navigate to " + url + " (response was null)");
			}

			String content = page.content();
			byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

			doc.setDocumentContent(contentBytes);
			doc.setContentCharset("UTF-8");
			
			String contentType = response.headerValue("content-type");
			if (contentType == null) {
				contentType = "text/html";
			}
			doc.setContentType(contentType);
			
			doc.setDocumentMetadata(new HashMap<>(response.headers()));

			return doc;
		} catch (Exception e) {
			throw new TransportException("Failed to fetch document using Playwright: " + url, e);
		}
	}

	@Override
	public boolean pauseRequired() {
		return true;
	}

	@Override
	public void setFetchedDocsDB(FetchedDocsDB db) {
		this.db = db;
	}

	@Override
	public void fixDud(InputStream in) {
		// No-op
	}
}
