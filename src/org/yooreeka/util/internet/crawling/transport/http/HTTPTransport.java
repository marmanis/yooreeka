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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.common.TransportException;

public class HTTPTransport implements Transport {

	HttpClient httpclient = null;
	CookieStore cookieStore = null;
	HttpContext localContext = null;

	public HTTPTransport() {
	}

	public void clear() {
		httpclient = null;
		// initialState = null;
	}

	private FetchedDocument createDocument(String targetURL, HttpEntity entity)
			throws IOException, HTTPTransportException {
		FetchedDocument doc = new FetchedDocument();

		/*
		 * Maximum document length that transport will attempt to download
		 * without issuing a warning ...
		 */
		int MAX_DOCUMENT_LENGTH = 8 * 1024 * 1024; // 8Mb

		BufferedInputStream bufferedInput = null;
		byte[] buffer = new byte[1024];

		int contentLength = (int) entity.getContentLength();
		if (contentLength > MAX_DOCUMENT_LENGTH)
			System.out.println("WARNING: Retrieved document larger than "
					+ MAX_DOCUMENT_LENGTH + " [bytes]");

		ByteBuffer byteBuffer = ByteBuffer.allocate(contentLength);

		// Construct the BufferedInputStream object
		bufferedInput = new BufferedInputStream(entity.getContent());

		// Keep reading while there is content
		// when the end of the stream has been reached, -1 is returned
		while (bufferedInput.read(buffer) != -1) {

			// Process the chunk of bytes read
			byteBuffer.put(buffer);
		}

		/* IOException will be thrown for documents that exceed max length */
		byte[] data = byteBuffer.array();

		/*
		 * Check if server sent content in compressed form and uncompress the
		 * content if necessary.
		 */
		Header contentEncodingHeader = entity.getContentEncoding();
		if (contentEncodingHeader != null) {
			data = HTTPUtils.decodeContent(contentEncodingHeader.getValue(),
					data);
		}

		/* 'Content-Type' HTTP header value */
		String contentTypeHeaderValue = null;
		Header header = entity.getContentType();
		if (header != null) {
			contentTypeHeaderValue = header.getValue();
		}

		/*
		 * Determine MIME type of the document.
		 * 
		 * It is easy if we have Content-Type http header. In cases when this
		 * header is missing or for protocols that don't pass metadata about the
		 * documents (ftp://, file://) we would have to resort to url and/or
		 * content analysis to determine MIME type.
		 */
		String DEFAULT_CONTENT_TYPE = "text/html";
		String contentType = HTTPUtils.getContentType(contentTypeHeaderValue,
				targetURL, data);
		if (contentType == null) {
			contentType = DEFAULT_CONTENT_TYPE;
		}

		/*
		 * Determine Character encoding used in the document. In some cases it
		 * may be specified in the http header, in html file itself or we have
		 * to perform content analysis to choose the encoding.
		 */
		String DEFAULT_CONTENT_CHARSET = "UTF-8";
		String contentCharset = HTTPUtils.getCharset(contentTypeHeaderValue,
				contentType, data);
		if (contentCharset == null) {
			contentCharset = DEFAULT_CONTENT_CHARSET;
		}

		doc.setContentType(contentType);
		doc.setDocumentURL(targetURL);
		doc.setContentCharset(contentCharset);
		doc.setDocumentContent(data);
		doc.setDocumentMetadata(new HashMap<String, String>());
		return doc;
	}

	public FetchedDocument fetch(String documentUrl) throws TransportException {

		FetchedDocument doc = null;

		HttpGet httpget = new HttpGet(documentUrl);

		System.out.println("executing request " + httpget.getURI());

		// Pass local context as a parameter
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget, localContext);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpEntity entity = response.getEntity();

		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());
		if (entity != null) {
			System.out.println("Response content length: "
					+ entity.getContentLength());
		}
		List<Cookie> cookies = cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			System.out.println("Local cookie: " + cookies.get(i));
		}

		try {
			doc = createDocument(documentUrl, entity);
		} catch (IOException e) {
			throw new TransportException("Failed to fetch url: '" + documentUrl
					+ "': ", e);
		} finally {
			// Consume response content
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("----------------------------------------");

			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}

		return doc;
	}

	public void init() {

		System.out.println("Initializing HTTPTransport ...");

		httpclient = new DefaultHttpClient();

		// Create a local instance of cookie store
		cookieStore = new BasicCookieStore();

		// Create local HTTP context
		localContext = new BasicHttpContext();

		// Bind custom cookie store to the local context
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		// httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		// httpclient.getHttpConnectionManager().getParams().setSoTimeout(30000);
		// httpclient.setState(initialState);
		// httpclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		//
		// //httpclient.getParams().setParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS,
		// Boolean.TRUE);
		//
		// // Set default number of connections per host to 1
		// httpclient.getHttpConnectionManager().
		// getParams().setMaxConnectionsPerHost(
		// HostConfiguration.ANY_HOST_CONFIGURATION, 1);
		// // Set max for total number of connections
		// httpclient.getHttpConnectionManager().getParams().setMaxTotalConnections(10);
	}

	public boolean pauseRequired() {
		return true;
	}
}
