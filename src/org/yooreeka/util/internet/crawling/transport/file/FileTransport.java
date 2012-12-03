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
package org.yooreeka.util.internet.crawling.transport.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.common.TransportException;

public class FileTransport implements Transport {

	public FileTransport() {
	}

	public void clear() {
		// DO NOTHING
	}

	private FetchedDocument createDocument(String targetURL)
			throws IOException, FileTransportException {
		FetchedDocument doc = new FetchedDocument();

		/*
		 * Maximum document length.
		 */
		int MAX_DOCUMENT_LENGTH = 512 * 1024; // 512K

		URL url = new URL(targetURL);
		File f = null;
		try {
			f = new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new FileTransportException(
					"Error while converting url to file path: ", e);
		}

		/* IOException will be thrown for documents that exceed max length */
		byte[] data = loadData(f, MAX_DOCUMENT_LENGTH);

		String DEFAULT_CONTENT_TYPE = "text/html";
		String contentType = DEFAULT_CONTENT_TYPE;
		if (f.getName().endsWith(".doc")) {
			contentType = "application/msword";
		}

		String DEFAULT_CONTENT_CHARSET = "UTF-8";
		String contentCharset = DEFAULT_CONTENT_CHARSET;

		doc.setContentType(contentType);
		doc.setDocumentURL(targetURL);
		doc.setContentCharset(contentCharset);
		doc.setDocumentContent(data);
		doc.setDocumentMetadata(new HashMap<String, String>());
		return doc;
	}

	public FetchedDocument fetch(String documentUrl) throws TransportException {

		FetchedDocument doc = null;
		try {
			doc = createDocument(documentUrl);
		} catch (Exception eX) {
			System.out.println("ERROR:\n" + eX.getMessage());
			throw new FileTransportException("Failed to fetch url: '"
					+ documentUrl + "': ", eX);
		} finally {
		}

		return doc;
	}

	public void init() {
		// DO NOTHING
	}

	private byte[] loadData(File f, int maxLength) throws IOException {
		if (f.length() > maxLength) {
			throw new IOException("The document is too long (doc: "
					+ f.getCanonicalPath() + ", size: " + f.length()
					+ ", max size: " + maxLength);
		}

		InputStream in = new BufferedInputStream(new FileInputStream(f));
		byte[] data = new byte[(int) f.length()];
		int offset = 0;
		int i = 0;
		while ((offset < data.length)
				&& (i = in.read(data, offset, data.length - offset)) >= 0) {
			offset += i;
		}
		in.close();
		return data;
	}

	public boolean pauseRequired() {
		return false;
	}
}
