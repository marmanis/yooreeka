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

class HTTPUtils {

	/**
	 * Decodes content according to content encoding. This is just a place
	 * holder.
	 * 
	 * @param contentEncoding
	 *            content type.
	 * @param encodedContent
	 *            content received from the server
	 * @return decoded content.
	 */
	public static byte[] decodeContent(String contentEncoding,
			byte[] encodedContent) throws HTTPTransportException {
		byte[] decodedContent = null;
		if ("gzip".equalsIgnoreCase(contentEncoding)) {
			throw new HTTPTransportException(
					"Content-Encoding 'gzip' is not supported.");
		} else if ("deflate".equalsIgnoreCase(contentEncoding)) {
			throw new HTTPTransportException(
					"Content-Encoding 'deflate' is not supported.");
		} else if ("compress".equalsIgnoreCase(contentEncoding)) {
			throw new HTTPTransportException(
					"Content-Encoding 'compress' is not supported.");
		} else {
			decodedContent = encodedContent;
		}

		return decodedContent;
	}

	private static String getCharset(String contentTypeHeaderValue) {
		String charset = null;
		String ATTR_NAME = "charset=";
		if (contentTypeHeaderValue != null) {
			int i = contentTypeHeaderValue.toLowerCase().indexOf(ATTR_NAME);
			if (i > -1) {
				charset = contentTypeHeaderValue.substring(
						i + ATTR_NAME.length()).toUpperCase();
			}
		}

		return charset;
	}

	/**
	 * Extracts charset from HTTP header. If HTTP header is missing an attempt
	 * can be made to determine charset based on content type and data.
	 * 
	 * For example, documents with type 'text/html' can define document charset
	 * using 'meta' tag. Such documents should use characters compatible with
	 * ISO-8859-1 charset until the meta tag that defines document charset. For
	 * more details see: http://www.w3.org/TR/html4/charset.html#h-5.2.2
	 * 
	 * @param contentTypeHeaderValue
	 * @param contentType
	 *            type of data. Can be used to interpret the data.
	 * @param data
	 * @return charset or null.
	 */
	public static String getCharset(String contentTypeHeaderValue,
			String contentType, byte[] data) {
		String charset = getCharset(contentTypeHeaderValue);
		if (charset == null || charset.trim().length() == 0) {
			/*
			 * here we can implement charset detection based on content
			 * analysis.
			 */
		}

		return charset;
	}

	/**
	 * Extracts MIME type. Ideally the value should be extracted from HTTP
	 * header. But if it is missing an attempt can be made to determine content
	 * type based on URL and/or data.
	 * 
	 * @param contentTypeHeaderValue
	 * @param url
	 *            document URL.
	 * @param data
	 *            document content
	 * 
	 * @return MIME type for document content or null if couldn't determine the
	 *         type.
	 */
	public static String getContentType(String contentTypeHeaderValue,
			String url, byte[] data) {
		String type = null;
		if (contentTypeHeaderValue != null
				&& contentTypeHeaderValue.trim().length() > 0) {
			int i = contentTypeHeaderValue.indexOf(";");
			if (i > -1) {
				type = contentTypeHeaderValue.substring(0, i);
			} else {
				type = contentTypeHeaderValue.substring(0);
			}
		}

		if (type == null) {
			/*
			 * here url and content itself can be used to determine content
			 * type.
			 */
		}

		return type;
	}
}
