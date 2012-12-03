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

import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.db.FetchedDocsDB;
import org.yooreeka.util.internet.crawling.db.KnownUrlDB;
import org.yooreeka.util.internet.crawling.db.ProcessedDocsDB;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.model.KnownUrlEntry;
import org.yooreeka.util.internet.crawling.model.Outlink;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.file.FileTransport;
import org.yooreeka.util.internet.crawling.transport.http.HTTPTransport;
import org.yooreeka.util.internet.crawling.util.DocumentIdUtils;
import org.yooreeka.util.internet.crawling.util.UrlGroup;
import org.yooreeka.util.internet.crawling.util.UrlUtils;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserFactory;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class BasicWebCrawler {

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
	}

	public void addSeedUrls(List<String> seedUrls) {
		int seedUrlDepth = 0;
		KnownUrlDB knownUrlsDB = crawlData.getKnownUrlsDB();
		for (String url : seedUrls) {
			knownUrlsDB.addNewUrl(url, seedUrlDepth);
		}
	}

	public void fetchAndProcess(int maxDepth, int maxDocs) {

		boolean maxUrlsLimitReached = false;
		int documentGroup = 1;

		crawlData.init();

		if (maxBatchSize <= 0) {
			throw new RuntimeException("Invalid value for maxBatchSize = "
					+ maxBatchSize);
		}

		for (int depth = 0; depth < maxDepth; depth++) {

			int urlsProcessedAtThisDepth = 0;

			boolean noMoreUrlsAtThisDepth = false;

			while (maxUrlsLimitReached == false
					&& noMoreUrlsAtThisDepth == false) {

				System.out.println("Starting url group: " + documentGroup
						+ ", current depth: " + depth + ", total known urls: "
						+ crawlData.getKnownUrlsDB().getTotalUrlCount()
						+ ", maxDepth: " + maxDepth + ", maxDocs: " + maxDocs
						+ ", maxDocs per group: " + maxBatchSize
						+ ", pause between docs: "
						+ pauseBetweenFetchesInMillis + "(ms)");

				List<String> urlsToProcess = selectNextBatchOfUrlsToCrawl(
						maxBatchSize, depth);

				/* for batch of urls create a separate document group */
				String currentGroupId = String.valueOf(documentGroup);
				fetchPages(urlsToProcess, crawlData.getFetchedDocsDB(),
						currentGroupId);

				// process downloaded data
				processPages(currentGroupId, crawlData.getProcessedDocsDB(),
						crawlData.getFetchedDocsDB());

				// get processed doc, get links, add links to all-known-urls.dat
				processLinks(currentGroupId, depth + 1,
						crawlData.getProcessedDocsDB());

				int lastProcessedBatchSize = urlsToProcess.size();
				processedUrlCount += lastProcessedBatchSize;
				urlsProcessedAtThisDepth += lastProcessedBatchSize;

				System.out.println("Finished url group: " + documentGroup
						+ ", urls processed in this group: "
						+ lastProcessedBatchSize + ", current depth: " + depth
						+ ", total urls processed: " + processedUrlCount);

				documentGroup += 1;

				if (processedUrlCount >= maxDocs) {
					maxUrlsLimitReached = true;
				}

				if (lastProcessedBatchSize == 0) {
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

	private void fetchPages(List<String> urls, FetchedDocsDB fetchedDocsDB,
			String groupId) {
		DocumentIdUtils docIdUtils = new DocumentIdUtils();
		int docSequenceInGroup = 1;
		List<UrlGroup> urlGroups = UrlUtils.groupByProtocolAndHost(urls);
		for (UrlGroup urlGroup : urlGroups) {
			Transport t = getTransport(urlGroup.getProtocol());
			try {
				t.init();
				for (String url : urlGroup.getUrls()) {
					try {
						FetchedDocument doc = t.fetch(url);
						String documentId = docIdUtils.getDocumentId(groupId,
								docSequenceInGroup);
						doc.setDocumentId(documentId);
						fetchedDocsDB.saveDocument(doc);
						if (t.pauseRequired()) {
							pause();
						}
					} catch (Exception e) {
						System.out
								.println("Failed to fetch document from url: '"
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

	private void processLinks(String groupId, int currentDepth,
			ProcessedDocsDB parsedDocs) {
		URLNormalizer urlNormalizer = new URLNormalizer();
		if (urlFilter == null) {
			urlFilter = new URLFilter();
			urlFilter.setAllowFileUrls(true);
			urlFilter.setAllowHttpUrls(false);
			System.out
					.println("Using default URLFilter configuration that only accepts 'file://' urls");
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
			try {
				doc = fetchedDocsDB.getDocument(id);
				String url = doc.getDocumentURL();

				String contentType = doc.getContentType();

				DocumentParser docParser = DocumentParserFactory.getInstance()
						.getDocumentParser(contentType);

				// DEBBUG
				P.println(docParser.toString());
				P.println(doc.toString());

				ProcessedDocument parsedDoc = docParser.parse(doc);

				parsedDocsService.saveDocument(parsedDoc);

				crawlData.getKnownUrlsDB().updateUrlStatus(url,
						KnownUrlEntry.STATUS_PROCESSED_SUCCESS);

			} catch (Exception e) {

				if (doc != null) {

					System.out.println("ERROR:\n");
					System.out
							.println("Unexpected exception while processing: '"
									+ id + "', ");
					System.out.println("   URL='" + doc.getDocumentURL()
							+ "'\n");
					System.out.println("Exception message: " + e.getMessage());

				} else {
					System.out.println("ERROR:\n");
					System.out
							.println("Unexpected exception while processing: '"
									+ id + "', ");
					System.out.println("Exception message: " + e.getMessage());
				}
			}
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
