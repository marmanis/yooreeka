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
package org.yooreeka.util.internet.crawling.transport.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.db.FetchedDocsDB;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.common.TransportException;
import org.yooreeka.util.internet.crawling.util.DocumentIdUtils;
import org.yooreeka.util.internet.crawling.util.FileUtils;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class FileTransport implements Transport {

	private static final String DEFAULT_CONTENT_CHARSET = "UTF-8";
	public static final String FILE_URL_PREFIX = "file:/";
	private static final int FILE_URL_PREFIX_INDEX = 6;

	private boolean verbose = false;
	
	private FetchedDocsDB db;
	
	public FileTransport() {
		//NOTHING YET
	}

	public void clear() {
		// DO NOTHING
	}

	public FetchedDocument fetch(String documentUrl, String groupId, int docSequenceInGroup) throws TransportException {

		FetchedDocument doc = new FetchedDocument();

		String documentId = DocumentIdUtils.getDocumentId(groupId, docSequenceInGroup);

		/*
		 * Maximum document length.
		 */
		int MAX_DOCUMENT_LENGTH = 64 * 1024 * 1024; // 64Mb

		File f=null; 
		// Subtract the file:/ from the targetURL
		if (documentUrl.startsWith(FILE_URL_PREFIX)) {
			
			String docURL = documentUrl.substring(FILE_URL_PREFIX_INDEX);
			f = Paths.get(docURL).toFile();
			
			if (f.isDirectory()) {
				
				if (verbose)
					P.println("Found a directory, descending recursively ...");

				doc.setDocumentId(documentId);
				doc.setContentType(ProcessedDocument.TYPE_DIRECTORY);
				doc.setDocumentURL(docURL);

				List<Path> files;
				try {
					files = FileUtils.listFiles(f.toPath());
				} catch (IOException ioX) {
					throw new FileTransportException(ioX.getMessage());
				}
				
				for (Path p : files) {
					String fileURL;
					try {
						fileURL = p.toUri().toURL().toString();
					} catch (MalformedURLException urlX) {
						throw new FileTransportException(urlX.getMessage());
					}
					
					if (verbose) {
						P.println("Target URL: "+documentUrl);
						P.println("  File URL: "+fileURL);
					}
					
					fetch(fileURL, groupId, docSequenceInGroup++);
				}
			} else {
				
				/* IOException will be thrown for documents that exceed max length */
				byte[] data;
				try {
					data = loadData(f, MAX_DOCUMENT_LENGTH);
				} catch (IOException ioX) {
					throw new FileTransportException(ioX.getMessage());
				}

				//Detect content type
				String DEFAULT_CONTENT_TYPE = ProcessedDocument.TYPE_HTML;
				String contentType = DEFAULT_CONTENT_TYPE;
				if (f.getName().endsWith(ProcessedDocument.MSWORD_ENDS_WITH)) {
					contentType = ProcessedDocument.TYPE_MSWORD;
				} else if (f.getName().endsWith(ProcessedDocument.PDF_ENDS_WITH)) {
					contentType = ProcessedDocument.TYPE_PDF;
				} else if (f.getName().endsWith(ProcessedDocument.TEXT_ENDS_WITH)) {
					contentType = ProcessedDocument.TYPE_TEXT;
				} 
		    
				doc.setDocumentId(documentId);
				doc.setContentType(contentType);
				doc.setDocumentURL(documentUrl);
				//The content character set is detected later on in ProcessedDocument
				doc.setContentCharset(DEFAULT_CONTENT_CHARSET);
				doc.setDocumentContent(data);
				doc.setDocumentMetadata(new HashMap<String, String>());

				if (verbose) {
					doc.print();
					P.println("Calling fetchedDocsDB.saveDocument(doc);");
				}
				
				db.saveDocument(doc);
			}
		} else {
			throw new FileTransportException("There is something wrong with this URL: "+documentUrl);
		}
		return doc;
	}

	public void init() {
		P.println("FileTransport.init() called ...");
	}

	private byte[] loadData(File f, int maxLength) throws IOException {

		if (verbose)
			P.println("Loading data from file: "+f.getAbsolutePath());
		
		byte[] data = new byte[(int) f.length()];

		if (!f.isDirectory()) {
			if (f.length() > maxLength) {
				throw new IOException("The document is too long (doc: "
						+ f.getCanonicalPath() + ", size: " + f.length()
						+ ", max size: " + maxLength);
			}
	
			InputStream in = null;
			try {
				in = new BufferedInputStream(new FileInputStream(f));
			} catch (Exception eX) {
				P.println(eX.getMessage());
				// TODO HttpTransport issue
				// fixDud(in);
			} 
						
			if (in != null) {
				int offset = 0;
				int i = 0;
				while ((offset < data.length) && (i = in.read(data, offset, data.length - offset)) >= 0) {
					offset += i;
				}
				in.close();
			}
		}
		return data;
	}

	public boolean pauseRequired() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.yooreeka.util.internet.crawling.transport.common.Transport#fixDud()
	 */
	@Override
	public void fixDud(InputStream in) {
		String empty = YooreekaConfigurator.getProperty("yooreeka.crawl.dudfile");
		P.println(empty);
		try {
			in = new BufferedInputStream(new FileInputStream(new File(empty)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.yooreeka.util.internet.crawling.transport.common.Transport#setFetchedDocsDB(org.yooreeka.util.internet.crawling.db.FetchedDocsDB)
	 */
	@Override
	public void setFetchedDocsDB(FetchedDocsDB db) {
		this.db = db;
	}
}
