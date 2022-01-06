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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.yooreeka.examples.fraud.data.Transaction;
import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.DataField;
import org.yooreeka.util.parsing.common.DataType;
import org.yooreeka.util.parsing.csv.CSVFile;
import org.yooreeka.util.parsing.csv.CSVSchema;

/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class VCFFile {

	private boolean isVerbose=false;
	
	private File file;

	private VCFDocument doc;
		
	public VCFFile(String fileName) {

		file = new File(fileName);
		
		//Initialize the document that corresponds to the file
		doc = new VCFDocument();
	}

	public VCFDocument read() throws IOException {

		FileReader fReader = new FileReader(file);
		BufferedReader bReader = new BufferedReader(fReader);

		VCFParser vcfParser = new VCFParser(this);
		vcfParser.parse(bReader);
		
		bReader.close();
		
		return doc;
	}

	/**
	 * @return the doc
	 */
	public VCFDocument getDoc() {
		return doc;
	}

	
	/**
	 * A method to store the Contacts as CSV
	 * 
	 * @throws IOException
	 */
	private void saveAsCsv(VCFDocument d, String filename) {
		
		try {
			FileWriter fout = new FileWriter(filename);
			BufferedWriter writer = new BufferedWriter(fout);
			
			writer.write("ID, Name, FullName, Tel_Cell, Tel_Home, Tel_Work, Tel_Work_Pref, Voice_Mail, Email, Samsung_Address, Address_Work, Organization, Title, Note_Encoded, Fax, Photo, Address_Home, URL, Email_Home, Address, Organization_Encoded, Note, Tel_Pager, Tel_Unspecified");
			writer.write("\n");

			int nullCount=0;

			for (VCFEntry e: doc.getVcfData()) {
				StringBuilder b = new StringBuilder();
				b.append(e.getId()).append(", \"");
				
				int nullValueCount=0;
				for(int i=1; i<24; i++) {
					if (e.getData()[i] != null) {
						// b.append("  > ");
						// b.append(d.getVCFEntryLabel(i)).append(": ");
						
						String content = null;
						String rawText = e.getData()[i].trim();

						if (i == 9 || i == 10 || i==13 || i==20) {
							// b.append("\n");
							try {
								
								content = d.decode(rawText, "UTF-8", "quoted-printable", "UTF-8").replaceAll("\"", " ").replaceAll(";+", " ");
								
							} catch (Exception e1) {
								P.println(rawText);
								e1.printStackTrace();
								System.exit(0);
								
							}
						} else {
							content = rawText.replaceAll("\"", " ").replaceAll(";+", " ").trim();
						}
						
						b.append(content);					
					} else {
						b.append("NULL");
						nullValueCount++;
					}
					
					if (i <23)
						b.append("\", \"");
					else 
						b.append("\"");
				}
				
				if (nullValueCount == 23) {
					
					//P.println("EMPTY Record");
					nullCount++;
				
				} else {
				
					
					writer.write(b.toString());
					writer.write("\n");
				}
				
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to load descriptions from file: '" + filename
							+ "' ", e);
		}

	}
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		VCFFile f = new VCFFile(args[0]);

		VCFDocument d = f.read();
		// f.printContacts(d);
		
		f.saveAsCsv(d, args[0]+".csv");
		
	}

	private void printContacts(VCFDocument d) {
		int nullCount=0;
		
		for (VCFEntry e: d.getVcfData()) {
			// P.hline();
			StringBuilder b = new StringBuilder("Contact [");
			b.append(e.getId()).append("]: \n");
			
			int nullValueCount=0;
			for(int i=1; i<24; i++) {
				if (e.getData()[i] != null) {
					b.append("  > ");
					b.append(d.getVCFEntryLabel(i)).append(": ");
					
					String content = null;
					String rawText = e.getData()[i].trim();

					if (i == 9 || i == 10 || i==13 || i==20) {
						b.append("\n");
						try {
							
							content = d.decode(rawText, "UTF-8", "quoted-printable", "UTF-8").replaceAll(";+", " ");
							
						} catch (Exception e1) {
							P.println(rawText);
							e1.printStackTrace();
							System.exit(0);
							
						}
					} else {
						content = rawText;
					}
					
					b.append(content).append("\n");					
				} else {
					nullValueCount++;
				}
			}
			
			if (nullValueCount == 23) {
				
				//P.println("EMPTY Record");
				nullCount++;
			
			} else {
			
				P.println(b.toString());
				P.hline();
			}
		}
		P.println("Found "+nullCount+" null entries!");		
	}
	
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
	public void isVerbose(boolean val) {
		isVerbose=val;
	}
	
	public boolean isVerbose() {
		return isVerbose;
	}
}
