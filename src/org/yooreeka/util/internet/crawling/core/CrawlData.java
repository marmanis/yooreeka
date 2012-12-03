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
package org.yooreeka.util.internet.crawling.core;

import java.io.File;

import org.yooreeka.util.internet.crawling.db.FetchedDocsDB;
import org.yooreeka.util.internet.crawling.db.KnownUrlDB;
import org.yooreeka.util.internet.crawling.db.PageLinkDB;
import org.yooreeka.util.internet.crawling.db.ProcessedDocsDB;

public class CrawlData {

	private File crawlRootDir;

	private FetchedDocsDB fetchedDocsDB;
	private ProcessedDocsDB processedDocsDB;
	private KnownUrlDB knownUrlsDB;
	private PageLinkDB pageLinkDB;

	public CrawlData(String rootDir) {
		this.crawlRootDir = new File(rootDir);
		crawlRootDir.mkdirs();

		File fetchedDocsDBRoot = new File(crawlRootDir, "fetched");
		this.fetchedDocsDB = new FetchedDocsDB(fetchedDocsDBRoot);

		File processedDocsDBRoot = new File(crawlRootDir, "processed");
		this.processedDocsDB = new ProcessedDocsDB(processedDocsDBRoot);

		File knownUrlsDBRoot = new File(crawlRootDir, "knownurls");
		this.knownUrlsDB = new KnownUrlDB(knownUrlsDBRoot);

		File pageLinkDBRoot = new File(crawlRootDir, "pagelinks");
		this.pageLinkDB = new PageLinkDB(pageLinkDBRoot);
	}

	public void delete() {
		this.fetchedDocsDB.delete();
		this.processedDocsDB.delete();
		this.knownUrlsDB.delete();
		this.pageLinkDB.delete();
	}

	public File getCrawlRootDir() {
		return crawlRootDir;
	}

	public FetchedDocsDB getFetchedDocsDB() {
		return fetchedDocsDB;
	}

	public KnownUrlDB getKnownUrlsDB() {
		return knownUrlsDB;
	}

	public PageLinkDB getPageLinkDB() {
		return pageLinkDB;
	}

	public ProcessedDocsDB getProcessedDocsDB() {
		return processedDocsDB;
	}

	public void init() {
		this.fetchedDocsDB.init();
		this.processedDocsDB.init();
		this.knownUrlsDB.init();
		this.pageLinkDB.init();
	}
}
