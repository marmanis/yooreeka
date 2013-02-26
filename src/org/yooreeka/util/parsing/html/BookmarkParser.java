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
 *   Copyright (c) 2007-2009    Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-2012 Marmanis Group LLC and individual contributors as indicated by the @author tags.  
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
package org.yooreeka.util.parsing.html;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * TODO: Implement a parser for bookmarks 
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class BookmarkParser extends HTMLDocumentParser {

	/**
	 * 
	 */
	public BookmarkParser() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reader
	 * @throws HTMLDocumentParserException
	 */
	public BookmarkParser(Reader reader) throws HTMLDocumentParserException {
		super(reader);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String filename = args[0];
		BookmarkParser bookParser = null;
		ProcessedDocument doc = null;
		try {
			bookParser = new BookmarkParser();
			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(filename));
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			doc = bookParser.parse(reader);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse html from file: "
					+ filename, e);
		}

		//P.println(doc.getText());
		
	}

}
