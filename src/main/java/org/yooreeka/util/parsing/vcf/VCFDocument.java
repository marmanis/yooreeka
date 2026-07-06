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
package org.yooreeka.util.parsing.vcf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.ProcessedDocument;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeUtility;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class VCFDocument extends ProcessedDocument {

	private ArrayList<VCFEntry> vcfData;

	
	/**
	 * @param vcfData
	 */
	public VCFDocument() {
		super();
		this.vcfData = new ArrayList<VCFEntry>();
	}

	/**
	 * @param vcfData
	 */
	public VCFDocument(ArrayList<VCFEntry> vcfData) {
		super();
		this.vcfData = vcfData;
	}

	public String getVCFEntryLabel(int i) {
		String label =null;
		switch(i) {
		case 0: label = "Version";
				break;
		case 1: label = "Name";
				break;
		case 2: label = "Full Name";
				break;
		case 3: label = "Telephone (Cellular)";
				break;
		case 4: label = "Telephone (Home)";
				break;
		case 5: label = "Telephone (Work)";
				break;
		case 6: label = "Telephone (Work - Preferred)";
				break;
		case 7: label = "Voice Mail";
				break;
		case 8: label = "Email";
				break;
		case 9: label = "Samsung Address";
				break;
		case 10: label = "Address (Work)";
				 break;
		case 11: label = "Organization";
				 break;
		case 12: label = "Title";
				 break;
		case 13: label = "Note (Encoded)";
		         break;
		case 14: label = "Fax";
		         break;
		case 15: label = "Photo";
                 break;
		case 16: label = "Address (Home)";
                 break;
		case 17: label = "URL";
                 break;
        case 18: label = "Email Home";
		         break;
		case 19: label = "Address (Unspecified)";
		         break;
		case 20: label = "Organization (Encoded)";
				 break;
		case 21: label = "Note";
				 break;
		case 22: label = "Telephone Pager";
				 break;
		case 23: label = "Telephone (Unspecified)";
				 break;
		case 24: label = "Birthday";
				 break;
		default: 
			throw new IllegalArgumentException("Unknown semantics for index: "+i);
		}
		
		return label;
	}
	
	public ArrayList<VCFEntry> getVcfData() {
		return vcfData;
	}

	public void print(String printSeparator) {
		P.hline();

		ListIterator<VCFEntry> elements = vcfData.listIterator();
		while (elements.hasNext()) {
			VCFEntry e = elements.next();
			P.println(e.toString(printSeparator));
		}
		P.hline();
	}
	
	public String decode(String text, String textEncoding, String encoding, String charset) throws MessagingException, IOException {
	    if (text.length() == 0) {
	        return text;
	    }

	    byte[] asciiBytes = text.getBytes(textEncoding);
	    InputStream decodedStream = MimeUtility.decode(new ByteArrayInputStream(asciiBytes), encoding);
	    byte[] tmp = new byte[asciiBytes.length];
	    int n = decodedStream.read(tmp);
	    byte[] res = new byte[n];
	    System.arraycopy(tmp, 0, res, 0, n);
	    
	    return new String(res, charset);
	}

}
