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

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.yooreeka.util.parsing.common.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MSWordDocumentParser implements DocumentParser {

	ProcessedDocument wordDoc = new ProcessedDocument();

	XWPFDocument document;

	@Override
	public DataEntry getDataEntry(int i) {
		// TODO: Just satisfying the interface, it is not used; refactor later ...
		return null;
	}

	public ProcessedDocument parse(AbstractDocument doc)
			throws DocumentParserException {

		poiReadDocument(stripProtocolFromURL(doc.getDocumentURL()));
		
		wordDoc.setDocumentType(ProcessedDocument.TYPE_MSWORD);
		wordDoc.setDocumentId(doc.getDocumentId());
		wordDoc.setDocumentURL(doc.getDocumentURL());
		
		return wordDoc;
	}
	    
	public void poiReadDocument(String fileName){
		
        try {
			Path msWordPath = Paths.get(fileName);
			try {
				document = new XWPFDocument(Files.newInputStream(msWordPath));
				XWPFParagraph title = document.getParagraphs().getFirst();

				// Read the content
				wordDoc.setDocumentTitle(title.getText());
				wordDoc.setText(document.getDocument().getBody().toString());
				wordDoc.setContent(document.getDocument().getBody().toString());
				
				document.close();
			} catch (org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException oleException) {
				// Fallback to old DOC format (.doc)
				try (org.apache.poi.hwpf.extractor.WordExtractor extractor = new org.apache.poi.hwpf.extractor.WordExtractor(Files.newInputStream(msWordPath))) {
					wordDoc.setDocumentTitle(msWordPath.getFileName().toString());
					wordDoc.setText(extractor.getText());
					wordDoc.setContent(extractor.getText());
				}
			}

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String stripProtocolFromURL(String url) {
		String docURL= null;
		if (url.startsWith("file:")) {
			docURL = url.substring(6);
		}
		return docURL;
    }

	public XWPFDocument getDocument() {
		return document;
	}
}
