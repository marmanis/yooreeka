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
package org.yooreeka.util.parsing.msword;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.textmining.extraction.TextExtractor;
import org.textmining.extraction.word.WordTextExtractorFactory;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DataEntry;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserException;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class MSWordDocumentParser implements DocumentParser {

	ProcessedDocument wordDoc = new ProcessedDocument();

	@Override
	public DataEntry getDataEntry(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Finds the first non-empty line in the document.
	 */
	private String getTitle(String text) throws IOException {
		if (text == null) {
			return null;
		}
		String title = "";

		StringReader sr = new StringReader(text);
		BufferedReader r = new BufferedReader(sr);
		String line = null;
		while ((line = r.readLine()) != null) {
			if (line.trim().length() > 0) {
				title = line.trim();
				break;
			}
		}

		return title;
	}

	public ProcessedDocument parse(AbstractDocument doc)
			throws DocumentParserException {

		wordDoc.setDocumentType(ProcessedDocument.TYPE_MSWORD);
		wordDoc.setDocumentId(doc.getDocumentId());
		wordDoc.setDocumentURL(doc.getDocumentURL());

		InputStream contentData = new ByteArrayInputStream(
				doc.getDocumentContent());
		WordTextExtractorFactory wteFactory = new WordTextExtractorFactory();

		try {
			TextExtractor txtExtractor = wteFactory.textExtractor(contentData);
			String text = txtExtractor.getText();
			wordDoc.setText(text);
			// using the same value as text
			wordDoc.setContent(text);
			wordDoc.setDocumentTitle(getTitle(text));
		} catch (Exception e) {
			throw new MSWordDocumentParserException(
					"MSWord Document parsing error: ", e);
		}
		return wordDoc;
	}
}
