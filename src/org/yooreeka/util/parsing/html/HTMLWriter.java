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
package org.yooreeka.util.parsing.html;

import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.cyberneko.html.filters.Writer;

/**
 * Extending NekoHTML Writer filter to override its behavior (most probably a
 * bug).
 */
public class HTMLWriter extends Writer {

	public HTMLWriter(java.io.Writer writer, String encoding) {
		super(writer, encoding);
	}

	/**
	 * This code was copied from
	 * org.cyberneko.html.filters.Writer.printStartElement It overrides original
	 * version with minor adjustment for bug fix.
	 * 
	 * Original version would wipe out value of 'content' attribute from in meta
	 * elements. In our case we are interested in:
	 * 
	 * <meta name="robots" content="...."/>
	 */
	@Override
	protected void printStartElement(QName element, XMLAttributes attributes) {
		// modify META[@http-equiv='content-type']/@content value
		int contentIndex = -1;
		String originalContent = null;
		if (element.rawname.toLowerCase().equals("meta")) {
			String httpEquiv = null;
			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				String aname = attributes.getQName(i).toLowerCase();
				if (aname.equals("http-equiv")) {
					httpEquiv = attributes.getValue(i);
				} else if (aname.equals("content")) {
					contentIndex = i;
				}
			}
			if (httpEquiv != null
					&& httpEquiv.toLowerCase().equals("content-type")) {
				fSeenHttpEquiv = true;
				String content = null;
				if (contentIndex != -1) {
					originalContent = attributes.getValue(contentIndex);
					content = originalContent.toLowerCase();
				}
				if (content != null) {
					int charsetIndex = content.indexOf("charset=");
					if (charsetIndex != -1) {
						content = content.substring(0, charsetIndex + 8);
					} else {
						content += ";charset=";
					}
					content += fEncoding;
					attributes.setValue(contentIndex, content);
				}
			} else {
				// this is the difference from original code
				contentIndex = -1;
			}
		}

		// print element
		fPrinter.print('<');
		fPrinter.print(element.rawname);
		int attrCount = attributes != null ? attributes.getLength() : 0;
		for (int i = 0; i < attrCount; i++) {
			String aname = attributes.getQName(i);
			String avalue = attributes.getValue(i);
			fPrinter.print(' ');
			fPrinter.print(aname);
			fPrinter.print("=\"");
			printAttributeValue(avalue);
			fPrinter.print('"');
		}
		fPrinter.print('>');
		fPrinter.flush();

		// return original META[@http-equiv]/@content value
		if (contentIndex != -1) {
			attributes.setValue(contentIndex, originalContent);
		}

	} // printStartElement(QName,XMLAttributes)
}
