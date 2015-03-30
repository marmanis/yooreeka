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
package org.yooreeka.util.internet.crawling.model;

import java.util.Map;

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.AbstractDocument;

/**
 * Collection of raw (unprocessed) data about crawled/fetched document.
 */
public class FetchedDocument implements AbstractDocument {

	/*
	 * Document id that was assigned by the FetcherModule.
	 */
	private String documentId;

	/*
	 * Document URL. URL that was used to fetch the document.
	 */
	private String url;

	/*
	 * MIME content type that was derived from transport protocol (HTTP
	 * headers), document content or document URL.
	 */
	private String contentType;

	/*
	 * Character encoding that was derived from transport protocol (HTTP
	 * headers), document content.
	 */
	private String contentCharset;

	/*
	 * Raw document content.
	 */
	private byte[] documentContent;

	/*
	 * Various optional meta data about the document that was extracted from the
	 * protocol.
	 */
	private Map<String, String> documentMetadata;

	public FetchedDocument() {
	}

	public String getContentCharset() {
		return contentCharset;
	}

	public long getContentLength() {
		return documentContent.length;
	}

	public String getContentType() {
		return contentType;
	}

	public byte[] getDocumentContent() {
		return documentContent;
	}

	public String getDocumentId() {
		return documentId;
	}

	public Map<String, String> getDocumentMetadata() {
		return documentMetadata;
	}

	public String getDocumentURL() {
		return url;
	}

	public void print() {
		P.hline();
		P.println("Document ID    : " + this.documentId);
		P.println("Content URL    : " + this.url);
		P.println("Content Type   : " + this.contentType);
		P.println("Content Charset: " + this.contentCharset);
		P.hline();
//		P.println("CONTENT\n"
//				+ new String(this.getDocumentContent(), Charset
//						.forName(contentCharset)));
//		P.hline();
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setDocumentContent(byte[] data) {
		this.documentContent = data;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public void setDocumentMetadata(Map<String, String> metadata) {
		this.documentMetadata = metadata;
	}

	public void setDocumentURL(String url) {
		this.url = url;
	}
}
