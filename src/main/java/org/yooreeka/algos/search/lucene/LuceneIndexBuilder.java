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
package org.yooreeka.algos.search.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.yooreeka.util.internet.crawling.core.CrawlData;
import org.yooreeka.util.internet.crawling.core.CrawlDataProcessor;
import org.yooreeka.util.internet.crawling.db.ProcessedDocsDB;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class LuceneIndexBuilder implements CrawlDataProcessor {

	public static final String INDEX_FIELD_DOC_ID = "docid";
	public static final String INDEX_FIELD_DOC_TYPE = "doctype";
	public static final String INDEX_FIELD_CONTENT = "content";
	public static final String INDEX_FIELD_TITLE = "title";
	public static final String INDEX_FIELD_URL = "url";

	private IndexWriter indexWriter;
	private CrawlData crawlData;
	private int RamBufferSizeMB = 128;

	public LuceneIndexBuilder(File indexFile, CrawlData crawlData)
			throws IOException {

		this.crawlData = crawlData;

		try {

			indexWriter = getIndexWriter(indexFile);

		} catch (IOException ioX) {
			throw new RuntimeException("Error while creating lucene index: ",
					ioX);
		}
	}

	/* PRIVATE METHODS */
	private void buildLuceneIndex(String groupId,
			ProcessedDocsDB parsedDocsService) {

		try {

			List<String> docIdList = parsedDocsService.getDocumentIds(groupId);

			for (String docId : docIdList) {
				indexDocument(indexWriter,
						parsedDocsService.loadDocument(docId));
			}
			
		} catch (IOException ioX) {
			throw new RuntimeException("Error while creating lucene index: ",
					ioX);
		}
	}

	private IndexWriter getIndexWriter(File file) throws IOException {
		FSDirectory dir = FSDirectory.open(file);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44,
				new StandardAnalyzer(Version.LUCENE_44));
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		config.setRAMBufferSizeMB(RamBufferSizeMB);
		return new IndexWriter(dir, config);
	}

	private void indexDocument(IndexWriter iw, ProcessedDocument parsedDoc)
			throws IOException {

		org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

		FieldType customType = new FieldType(TextField.TYPE_STORED);
		customType.setStoreTermVectors(true);
		customType.setStoreTermVectorPositions(true);
		customType.setStoreTermVectorOffsets(false);

		doc.add(new Field(INDEX_FIELD_CONTENT, parsedDoc.getText(), customType));

		doc.add(new StringField(INDEX_FIELD_URL, parsedDoc.getDocumentURL(),
				Field.Store.YES));

		doc.add(new StringField(INDEX_FIELD_DOC_ID, parsedDoc.getDocumentId(),
				Field.Store.YES));

		doc.add(new TextField(INDEX_FIELD_TITLE, parsedDoc.getDocumentTitle(),
				Field.Store.YES));

		doc.add(new StringField(INDEX_FIELD_DOC_TYPE, parsedDoc
				.getDocumentType(), Field.Store.YES));

		/**
		 * TODO: 2.2 -- The effect of boosting (Book Section 2.1.2)
		 * 
		 * Uncomment the lines below to demonstrate the effect of boosting
		 */
		// if ( parsedDoc.getDocumentId().equals("g1-d13")) {
		// doc.setBoost(2);
		// }

		iw.addDocument(doc);
	}

	public void run() {
		List<String> allGroups = crawlData.getProcessedDocsDB().getAllGroupIds();
		for (String groupId : allGroups) {
			buildLuceneIndex(groupId, crawlData.getProcessedDocsDB());
		}
		
		try {
			indexWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
