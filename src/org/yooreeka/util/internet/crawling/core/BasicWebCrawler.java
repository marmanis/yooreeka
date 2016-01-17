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

import java.util.List;
import java.util.logging.Logger;

import org.yooreeka.util.internet.crawling.db.FetchedDocsDB;
import org.yooreeka.util.internet.crawling.db.KnownUrlDB;
import org.yooreeka.util.internet.crawling.db.ProcessedDocsDB;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.model.KnownUrlEntry;
import org.yooreeka.util.internet.crawling.model.Outlink;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.common.TransportException;
import org.yooreeka.util.internet.crawling.transport.file.FileTransport;
import org.yooreeka.util.internet.crawling.transport.http.HTTPTransport;
import org.yooreeka.util.internet.crawling.util.DocumentIdUtils;
import org.yooreeka.util.internet.crawling.util.UrlGroup;
import org.yooreeka.util.internet.crawling.util.UrlUtils;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserException;
import org.yooreeka.util.parsing.common.DocumentParserFactory;
import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * This class and all the crawling related code was written for supporting
 * the book "Algorithms of the Intelligent Web". However, there are many good crawlers
 * in the Open Source community today that it is not worth maintaining this code in
 * future versions.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 * 
 */
public class BasicWebCrawler {

	private final static Logger log = Logger.getLogger(BasicWebCrawler.class.getName());
	
	private CrawlData crawlData;

	private URLFilter urlFilter;

	private static final int DEFAULT_MAX_BATCH_SIZE = 50;

	private long DEFAULT_PAUSE_IN_MILLIS = 500;
	private long pauseBetweenFetchesInMillis = DEFAULT_PAUSE_IN_MILLIS;

	/*
	 * Number of URLs to fetch and parse at a time.
	 */
	private int maxBatchSize = DEFAULT_MAX_BATCH_SIZE;

	/*
	 * Number of fetched and parsed URLs so far.
	 */
	private int processedUrlCount = 0;

	public BasicWebCrawler(String rootDir) {
		crawlData = new CrawlData(rootDir);
		crawlData.init();
	}

	public void addSeedUrls(List<String> seedUrls) {
		int seedUrlDepth = 0;
		KnownUrlDB knownUrlsDB = crawlData.getKnownUrlsDB();
		for (String url : seedUrls) {
			knownUrlsDB.addNewUrl(url, seedUrlDepth);
		}
	}

	public void fetchAndProcess(int maxDepth, int maxDocs) {

		log.fine("fetchAndProcess(int maxDepth:"+maxDepth+", int maxDocs"+maxDocs+")");
		
		boolean maxUrlsLimitReached = false;
		int documentGroup = 1;

		if (maxBatchSize <= 0) {
			throw new RuntimeException("Invalid value for maxBatchSize = "
					+ maxBatchSize);
		}

		for (int depth = 0; depth < maxDepth; depth++) {

			int urlsProcessedAtThisDepth = 0;

			boolean noMoreUrlsAtThisDepth = false;

			while (maxUrlsLimitReached == false
					&& noMoreUrlsAtThisDepth == false) {

//				StringBuilder msg = new StringBuilder("Starting url group: ");
//				msg.append(documentGroup).append(", current depth: ").append(depth);
//				msg.append(", total known urls: ").append(crawlData.getKnownUrlsDB().getTotalUrlCount());
//				msg.append(", maxDepth: ").append(maxDepth);
//				msg.append(", maxDocs: ").append(maxDocs);
//				msg.append(", maxDocs per group: ").append(maxBatchSize);
//				msg.append(", pause between docs: ").append(pauseBetweenFetchesInMillis).append("(ms)");
//				
//				P.println(msg.toString());

				List<String> urlsToProcess = selectNextBatchOfUrlsToCrawl(maxBatchSize, depth);

				/* for batch of urls create a separate document group */
				String currentGroupId = String.valueOf(documentGroup);
				fetchPages(urlsToProcess, crawlData.getFetchedDocsDB(),	currentGroupId);

				// process downloaded data
				processPages(currentGroupId, crawlData.getProcessedDocsDB(), crawlData.getFetchedDocsDB());

				// get processed doc, get links, add links to all-known-urls.dat
				processLinks(currentGroupId, depth + 1,
						crawlData.getProcessedDocsDB());

				int lastProcessedBatchSize = urlsToProcess.size();
				processedUrlCount += lastProcessedBatchSize;
				urlsProcessedAtThisDepth += lastProcessedBatchSize;

//				StringBuilder msg2 = new StringBuilder("Finished url group: "); 
//				msg2.append(documentGroup).append(", urls processed in this group: ");
//				msg2.append(lastProcessedBatchSize).append(", current depth: ").append(depth);
//				msg2.append(", total urls processed: ").append(processedUrlCount);
//				P.println(msg2.toString());
				
				documentGroup += 1;

				if (processedUrlCount >= maxDocs) {
					maxUrlsLimitReached = true;
				}

				if (lastProcessedBatchSize == 0) {
					noMoreUrlsAtThisDepth = true;
				}
				
				if (urlFilter.hasOnlyFileUrls()) {
					noMoreUrlsAtThisDepth = true;
				}
			}

			if (urlsProcessedAtThisDepth == 0) {
				break;
			}

			if (maxUrlsLimitReached) {
				break;
			}			
		}
	}

	private void fetchPages(List<String> urls, FetchedDocsDB fetchedDocsDB, String groupId) {
		
		int docSequenceInGroup = 1;
		
		List<UrlGroup> urlGroups = UrlUtils.groupByProtocolAndHost(urls);
		
		for (UrlGroup urlGroup : urlGroups) {
			
			Transport t = getTransport(urlGroup.getProtocol());
			t.setFetchedDocsDB(fetchedDocsDB);
			
			try {
			
				t.init();
				
				for (String url : urlGroup.getUrls()) {
					
					log.fine("fetchPages with URL: "+url);
					
					try {
						
						FetchedDocument doc = t.fetch(url,groupId,docSequenceInGroup);
					
						if (doc.getContentType().endsWith(ProcessedDocument.TYPE_DIRECTORY)) {
						
							log.warning("Not saving information about directory: "+doc.getDocumentURL());

						} else {

							String documentId = DocumentIdUtils.getDocumentId(groupId, docSequenceInGroup);
                            doc.setDocumentId(documentId);
                            fetchedDocsDB.saveDocument(doc);
						}
						
						if (t.pauseRequired()) {
							pause();
						}
					} catch (TransportException tX) {
						//We failed to retrieve the document, log the fact and just skip that file
						log.warning(tX.getMessage());
						
					} catch (Exception e) {
						e.printStackTrace();
						log.warning("Failed to fetch document from url: '"
										+ url + "'.\n" + e.getMessage());
						crawlData.getKnownUrlsDB().updateUrlStatus(url,
								KnownUrlEntry.STATUS_PROCESSED_ERROR);
					}
					docSequenceInGroup++;
				}
			} finally {
				t.clear();
			}
		}
	}

	public CrawlData getCrawlData() {
		return crawlData;
	}

	public long getPauseBetweenFetchesInMillis() {
		return pauseBetweenFetchesInMillis;
	}

	private Transport getTransport(String protocol) {
		if ("http".equalsIgnoreCase(protocol)) {
			return new HTTPTransport();
		} else if ("file".equalsIgnoreCase(protocol)) {
			return new FileTransport();
		} else {
			throw new RuntimeException("Unsupported protocol: '" + protocol
					+ "'.");
		}
	}

	public URLFilter getURLFilter() {
		return urlFilter;
	}

	public void pause() {
		try {
			Thread.sleep(pauseBetweenFetchesInMillis);
		} catch (InterruptedException e) {
			// do nothing
		}
	}

	private void processLinks(String groupId, int currentDepth,	ProcessedDocsDB parsedDocs) {
		URLNormalizer urlNormalizer = new URLNormalizer();
		if (urlFilter == null) {
			urlFilter = new URLFilter();
			log.warning("Using default URLFilter configuration that only accepts 'file://' urls");
		}

		List<String> docIds = parsedDocs.getDocumentIds(groupId);
		for (String documentId : docIds) {
			ProcessedDocument doc = parsedDocs.loadDocument(documentId);
			// register url without any outlinks first
			crawlData.getPageLinkDB().addLink(doc.getDocumentURL());
			List<Outlink> outlinks = doc.getOutlinks();
			for (Outlink outlink : outlinks) {
				String url = outlink.getLinkUrl();
				String normalizedUrl = urlNormalizer.normalizeUrl(url);
				if (urlFilter.accept(normalizedUrl)) {
					crawlData.getKnownUrlsDB().addNewUrl(url, currentDepth);
					crawlData.getPageLinkDB()
							.addLink(doc.getDocumentURL(), url);
				}
			}
		}
		crawlData.getKnownUrlsDB().save();
		crawlData.getPageLinkDB().save();
	}

	private void processPages(String groupId,
			ProcessedDocsDB parsedDocsService, FetchedDocsDB fetchedDocsDB) {

		List<String> docIds = fetchedDocsDB.getDocumentIds(groupId);

		for (String id : docIds) {
			
			AbstractDocument doc = null;
			
//			try {
				doc = fetchedDocsDB.getDocument(id);
				String url = doc.getDocumentURL();

				String contentType = doc.getContentType();

				/*
				 StringBuilder msg = new StringBuilder("Now processing:\n");
				 msg.append(doc.getDocumentURL()).append("\n");
				 P.hline();
				 P.println(msg.toString());
				 P.hline();
				 */
				
				DocumentParser docParser = null;
				try {
					docParser = DocumentParserFactory.getInstance().getDocumentParser(contentType);
				} catch (DocumentParserException e) {
					
					e.printStackTrace();
				}

				ProcessedDocument parsedDoc = null;
				try {
					
					parsedDoc = docParser.parse(doc);
					
				} catch (DocumentParserException e) {

					e.printStackTrace();
				}

				//DEBUG
//				P.hline();
//				P.println(parsedDoc.getContent().substring(0, 10000));
//				P.hline();
				
				parsedDocsService.saveDocument(parsedDoc);

//				if (doc.getContentType().equals(ProcessedDocument.TYPE_MSWORD)  ||
//					doc.getContentType().equals(ProcessedDocument.TYPE_PDF)     ||
//					doc.getContentType().equals(ProcessedDocument.TYPE_TEXT))    {
//				
//					String fixedUrl = FileTransport.FILE_URL_PREFIX+url;
//					crawlData.getKnownUrlsDB().updateUrlStatus(fixedUrl, KnownUrlEntry.STATUS_PROCESSED_SUCCESS);
//				} else {
					crawlData.getKnownUrlsDB().updateUrlStatus(url,	KnownUrlEntry.STATUS_PROCESSED_SUCCESS);
//				}
		}
	}

	private List<String> selectNextBatchOfUrlsToCrawl(int maxBatchSize,
			int depth) {
		return crawlData.getKnownUrlsDB().findUnprocessedUrls(maxBatchSize,
				depth);
	}

	/**
	 * @deprecated use method that uses depth
	 * 
	 * @param maxDocs
	 * @return
	 */
	@Deprecated
	public List<String> selectURLsForNextCrawl(int maxDocs) {
		return crawlData.getKnownUrlsDB().findUnprocessedUrls(maxDocs);
	}

	public void setPauseBetweenFetchesInMillis(long pauseBetweenFetchesInMillis) {
		this.pauseBetweenFetchesInMillis = pauseBetweenFetchesInMillis;
	}

	public void setURLFilter(URLFilter urlFilter) {
		this.urlFilter = urlFilter;
	}
}
