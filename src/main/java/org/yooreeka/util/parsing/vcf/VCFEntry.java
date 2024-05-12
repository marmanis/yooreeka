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

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.DataEntry;

/**
 * A <tt>VCFEntry</tt> is simply an array of <tt>String</tt>s. 
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class VCFEntry extends DataEntry {
	
	/**
	 * The semantics for the data array will be as follows
	 * 
	 *    0 --> Version         (VERSION:)
	 *    1 --> Name            (N:)
	 *    2 --> Full Name       (FN:)
	 *    3 --> Telephone Cell  (TEL;CELL:)
	 *    4 --> Telephone Home  (TEL;HOME:)
	 *    5 --> Telephone Work  (TEL;WORK:)
	 *    6 --> Telephone Work  (TEL;WORK;PREF:)
	 *    7 --> Voice Mail      (TEL;VOICE:)
	 *    8 --> Email           (EMAIL:)
	 *    9 --> SAMSUNGADR      (X-SAMSUNGADR;ENCODING=QUOTED-PRINTABLE:)
	 *   10 --> Address Work    (ADR;WORK;ENCODING=QUOTED-PRINTABLE:)
	 *   11 --> Organization    (ORG:)
	 *   12 --> Title           (TITLE:)
	 *   13 --> Note Encoded    (NOTE;ENCODING=QUOTED-PRINTABLE:)
	 *   14 --> Fax             (TEL;WORK;FAX:)
	 *   15 --> Photo           (PHOTO;ENCODING=BASE64;JPEG:)
	 *   16 --> Address	Home    (ADR;HOME:)
	 *   17 --> URL             (URL:)
	 *   18 --> Email Home      (EMAIL;HOME:)
	 *   19 --> Address         (ADR:)
	 *   20 --> Organization    ("ORG;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:)
	 *   21 --> Note            (NOTE:)
	 *   22 --> Telephone Pager (TEL;PAGER:)
	 *   23 --> Telephone       (TEL;:)
	 *   24 --> Birthday       (BDAY:)
	 *   
	 *   Clearly not the best way to go about this but it works for now
	 *   TODO: Create a cleaner implementation
	 */
	private String[] data = new String[25];
	
	private long id;

	public static String UNRECOGNIZED_TOKEN = "|>> UNRECOGNIZED VCF TOKEN <<|";
	
	
	/**
	 * @param data
	 */
	public VCFEntry(long id) {
		this.id = id;
	}

	public void store(VCFToken v) {
		
		P.println("Storing "+v.toString().trim());
		data[v.getIndex()] = v.getVal();		
	}
	
	public VCFToken parse(String s) {
		
		VCFToken vcfToken = new VCFToken();
		
		if (s.startsWith("VERSION:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(0);
			vcfToken.setVal(s.substring("VERSION:".length()).trim());
			
		} else if (s.startsWith("N:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(1);
			vcfToken.setVal(s.substring("N:".length()).trim());
			
		} else if (s.startsWith("N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(1);
			vcfToken.setVal(s.substring("N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("FN:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(2);
			vcfToken.setVal(s.substring("FN:".length()).trim());
			
		} else if (s.startsWith("FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(2);
			vcfToken.setVal(s.substring("FN;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("TEL;CELL:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(3);
			vcfToken.setVal(s.substring("TEL;CELL:".length()).trim());
			
		} else if (s.startsWith("TEL;HOME:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(4);
			vcfToken.setVal(s.substring("TEL;HOME:".length()).trim());
			
		} else if (s.startsWith("TEL;WORK:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(5);
			vcfToken.setVal(s.substring("TEL;WORK:".length()).trim());
			
		} else if (s.startsWith("TEL;WORK;PREF:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(6);
			vcfToken.setVal(s.substring("TEL;WORK;PREF:".length()).trim());
			
		} else if (s.startsWith("TEL;VOICE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(7);
			vcfToken.setVal(s.substring("TEL;VOICE:".length()).trim());
			
		} else if (s.startsWith("EMAIL:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(8);
			vcfToken.setVal(s.substring("EMAIL:".length()).trim());
			
		} else if (s.startsWith("X-SAMSUNGADR;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(9);
			vcfToken.setVal(s.substring("X-SAMSUNGADR;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("X-SAMSUNGADR;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(9);
			vcfToken.setVal(s.substring("X-SAMSUNGADR;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("X-SAMSUNGADR:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(9);
			vcfToken.setVal(s.substring("X-SAMSUNGADR:".length()).trim());
			
		} else if (s.startsWith("ADR;WORK;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(10);
			vcfToken.setVal(s.substring("ADR;WORK;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("ADR;WORK;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(10);
			vcfToken.setVal(s.substring("ADR;WORK;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("ADR;WORK:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(10);
			vcfToken.setVal(s.substring("ADR;WORK:".length()).trim());
			
		} else if (s.startsWith("ORG:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(11);
			vcfToken.setVal(s.substring("ORG:".length()).trim());
			
		} else if (s.startsWith("TITLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(12);
			vcfToken.setVal(s.substring("TITLE:".length()).trim());
			
		} else if (s.startsWith("TITLE;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(12);
			vcfToken.setVal(s.substring("TITLE;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("NOTE;ENCODING=QUOTED-PRINTABLE:")) { 
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(13);
			vcfToken.setVal(s.substring("NOTE;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("NOTE;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) { 
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(13);
			vcfToken.setVal(s.substring("NOTE;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("TEL;WORK;FAX:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(14);
			vcfToken.setVal(s.substring("TEL;WORK;FAX:".length()).trim());
			
		} else if (s.startsWith("PHOTO;ENCODING=BASE64;JPEG:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(15);
			vcfToken.setVal(s.substring("PHOTO;ENCODING=BASE64;JPEG:".length()).trim());
			
		} else if (s.startsWith("ADR;HOME:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(16);
			vcfToken.setVal(s.substring("ADR;HOME:".length()).trim());
			
		} else if (s.startsWith("URL:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(17);
			vcfToken.setVal(s.substring("URL:".length()).trim());
			
		} else if (s.startsWith("EMAIL;HOME:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(18);
			vcfToken.setVal(s.substring("EMAIL;HOME:".length()).trim());
			
		} else if (s.startsWith("ADR:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(19);
			vcfToken.setVal(s.substring("ADR:".length()).trim());
			
		} else if (s.startsWith("ORG;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(20);
			vcfToken.setVal(s.substring("ORG;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:".length()).trim());
			
		} else if (s.startsWith("NOTE:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(21);
			vcfToken.setVal(s.substring("NOTE:".length()).trim());
			
		} else if (s.startsWith("TEL;PAGER:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(22);
			vcfToken.setVal(s.substring("TEL;PAGER:".length()).trim());
			
		} else if (s.startsWith("TEL;:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(23);
			vcfToken.setVal(s.substring("TEL;:".length()).trim());

		} else if (s.startsWith("BDAY:")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(24);
			vcfToken.setVal(s.substring("BDAY:".length()).trim());


		} else if (s.startsWith("END:VCARD")) {
			vcfToken.setEntry(s);
			vcfToken.setToken(true);
			vcfToken.setIndex(-1);
			
		} else {
			vcfToken.setEntry(s);
			vcfToken.setToken(false);
			vcfToken.setVal(s);
			//P.println("WARNING: VCFEntry.parse: Unrecognizable line entry: "+s);
		}
		return vcfToken;
	}
	
	public String getEntryAt(int i) {
		if (i <0 || i>data.length)
			throw new IllegalArgumentException("i must be within the range of the data index [0,11]");
		return data[i];
	}
	
	public String[] getData() {
		return data;
	}

	@Override
	public DataEntry getDataEntry() {

		return this;
	}

	@Override
	public String toString() {

		return toString(data[2]);
	}

	public String toString(String printSeparator) {
		StringBuilder sb = new StringBuilder();
		int i=1;

		for (String s : data) {
			if (i<data.length) {
				sb.append(s).append(printSeparator);
			} else {
				sb.append(s);
			}
			i++;
		}
		return sb.toString();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
}
