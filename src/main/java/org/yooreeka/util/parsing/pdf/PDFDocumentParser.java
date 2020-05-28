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
package org.yooreeka.util.parsing.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DataEntry;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserException;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class PDFDocumentParser implements DocumentParser {

	// Just set it to something large
	private static final int STRING_BUFFER_SIZE = 16*1024*1024;
	
	ProcessedDocument pdfDoc = new ProcessedDocument();

	@Override
	public DataEntry getDataEntry(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessedDocument parse(AbstractDocument doc) throws DocumentParserException {

		Parser parser = new AutoDetectParser();
		ContentHandler handler = new BodyContentHandler(STRING_BUFFER_SIZE);
	  
		Metadata metadata = new Metadata();

		try (ByteArrayInputStream is = new ByteArrayInputStream(doc.getDocumentContent())) {
			 		  
			parser.parse(is, handler, metadata, new ParseContext());
		  
		 } catch (IOException ioX) {
			 throw new PDFDocumentParserException(ioX.getLocalizedMessage());
		 } catch (TikaException tikaX) {
			 throw new PDFDocumentParserException(tikaX.getLocalizedMessage());
		 } catch (SAXException eX) {
			 throw new PDFDocumentParserException(eX.getLocalizedMessage());
		 } 
		 
		pdfDoc.setDocumentType(ProcessedDocument.TYPE_PDF);
		pdfDoc.setDocumentId(doc.getDocumentId());
		pdfDoc.setDocumentURL(doc.getDocumentURL());
		pdfDoc.setText(handler.toString());
		pdfDoc.setContent(handler.toString());
		pdfDoc.setDocumentTitle(metadata.get("title"));

		return pdfDoc;
	}
}
