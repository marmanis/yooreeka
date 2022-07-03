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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DataEntry;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserException;
import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * 
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class VCFParser implements DocumentParser {

	private VCFFile vcfFile;
	private boolean verbose=false;
	private long contactsParsed = 0;
	private long linesRead = 0;
	private int lines2Skip=0;

	/**
	 * 
	 */
	public VCFParser(VCFFile f) {
		this.vcfFile = f;
	}

	@Override
	public ProcessedDocument parse(AbstractDocument abstractDocument)
			throws DocumentParserException {
		ProcessedDocument processedDocument = null;
		String content = new String(abstractDocument.getDocumentContent(),
				Charset.forName(abstractDocument.getContentCharset()));
		BufferedReader reader = new BufferedReader(new StringReader(content));
		try {
			abstractDocument = parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return processedDocument;
	}

	/**
	 * 
	 * @param bR
	 * @return
	 * @throws IOException
	 */
	public VCFDocument parse(BufferedReader bR) throws IOException {

		long t0 = System.currentTimeMillis();
		
		StringBuilder msg = new StringBuilder("\nProcessed ");
		
		contactsParsed = 0;

		boolean hasMoreLines = true;
		String line;

		if (isVerbose())
			P.print("VCF file parsing begins");

		while (hasMoreLines) {

			line = bR.readLine();

			if (line == null || line.trim().length() == 0) {

				hasMoreLines = false;

			} else {

				boolean vcardRead = false;
				VCFEntry vcf = new VCFEntry(contactsParsed);
				
				if (line.startsWith("BEGIN:VCARD")) {
					
					boolean hasTokenAlready=false;
					VCFToken primeToken = new VCFToken();
					
					while(!vcardRead) {
						
						if(!hasTokenAlready) {
							line = bR.readLine();
//							if (verbose) 
//								P.println("New line: "+line);
						
						} else {
							line = primeToken.getEntry();
							//P.println("Line read from existing token: "+line);
						}
						
						if (line.startsWith("END:VCARD")) {
							vcardRead=true;
							contactsParsed++;
							//P.hline();
							
						} else {
							
							if(!hasTokenAlready)
								primeToken = vcf.parse(line);

							if (line.contains("ENCODING=QUOTED-PRINTABLE:") || line.contains("ENCODING=BASE64;JPEG:")) {
							
								//P.println("Started reading an encoded entry:");
								
								boolean isEncoded=true;

								StringBuffer buff = new StringBuffer(primeToken.getVal());
								buff.append("\n");
								
								while (isEncoded) { 
									
									String nextLine = bR.readLine();
									VCFToken vcfToken = vcf.parse(nextLine);
									
									if (!vcfToken.isToken) {
										buff.append(vcfToken.getVal()).append("\n");
										
									} else {		
										primeToken.setVal(buff.toString());
										vcf.store(primeToken);
										isEncoded=false;
										
										//Reset
										hasTokenAlready=true;
										
										primeToken.setEntry(vcfToken.getEntry());
										primeToken.setIndex(vcfToken.getIndex());
										primeToken.setToken(vcfToken.isToken());
										primeToken.setVal(vcfToken.getVal());
										//vcf.store(vcfToken);
									}
								}
								//P.println("Completed reading an encoded entry.");
								
							} else {
								vcf.store(primeToken);
								hasTokenAlready=false;
							}
						}
				    }
				
				} else {
					//This shouldn't happen
					P.print("ERROR: Unrecognized start for Contacts: "+line);
					throw new IllegalArgumentException("ERROR: Unrecognized start for Contacts: "+line);
				}
																
				vcfFile.getDoc().getVcfData().add(vcf);				
			}
		}
		
		if (verbose) {
			msg.append(contactsParsed).append(" contacts from file: ");
			msg.append(vcfFile.getFile().getAbsolutePath());
			msg.append("\n in "+(System.currentTimeMillis()-t0)+"ms\n");
			P.println(msg.toString());
		}
		
		return vcfFile.getDoc();
	}

	@Override
	public DataEntry getDataEntry(int i) {
		return vcfFile.getDoc().getVcfData().get(i);
	}

	/**
	 * @return the number of lines parsed
	 */
	public long getLinesParsed() {
		return contactsParsed;
	}

	/**
	 * @param val sets the number of rows that must be skipped
	 */
	public void skipRows(int val) {
		lines2Skip = val;
	}
	
	/** 
	 * @return the number of lines read
	 */
	public long getLinesRead() {
		return linesRead;
	}
	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * @return the vcfFile
	 */
	public VCFFile getCsvFile() {
		return vcfFile;
	}

	/**
	 * @return the lines2Skip
	 */
	public int getLines2Skip() {
		return lines2Skip;
	}
}
