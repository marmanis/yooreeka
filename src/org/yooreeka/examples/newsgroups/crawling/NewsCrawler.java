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
package org.yooreeka.examples.newsgroups.crawling;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.internet.crawling.core.BasicWebCrawler;
import org.yooreeka.util.internet.crawling.core.CrawlData;
import org.yooreeka.util.internet.crawling.core.URLFilter;
import org.yooreeka.util.internet.crawling.core.URLNormalizer;

/**
 * A basic news crawler.
 * 
 * Remember to use <tt>setOffline(false)</tt>, if you want to use the local
 * files
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 */
public class NewsCrawler {

	public static final int DEFAULT_MAX_DEPTH = 1;
	public static final int DEFAULT_MAX_DOCS = 1000;

	private BasicWebCrawler webCrawler;

	private String crawlDataDir;

	private int maxDepth = DEFAULT_MAX_DEPTH;

	private int maxDocs = DEFAULT_MAX_DOCS;

	private List<String> seedUrls;

	/**
	 * This variable determines whether we will crawl the Internet or local
	 * files Remember to use <tt>setOffline(false)</tt>, if you want to use the
	 * local files
	 */
	private boolean isOffline = false;

	/*
	 * Directory that contains "previously unseen" documents.
	 */
	public static final String TEST_FILES_DIR_CH7 = YooreekaConfigurator
			.getHome() + "/data/ch07/test";

	public NewsCrawler(String rootDir, int maxDepth, int maxDocs) {

		this.crawlDataDir = buildUniqueDirectoryName(rootDir);

		this.maxDepth = maxDepth;

		this.maxDocs = maxDocs;

		seedUrls = new ArrayList<String>();

		webCrawler = new BasicWebCrawler(crawlDataDir);

	}

	public void addSeedUrl(String val) {
		URLNormalizer urlNormalizer = new URLNormalizer();
		seedUrls.add(urlNormalizer.normalizeUrl(val));
	}

	private String buildUniqueDirectoryName(String rootDir) {
		return rootDir + System.getProperty("file.separator") + "crawl-"
				+ System.currentTimeMillis();
	}

	public CrawlData getCrawlData() {
		return webCrawler.getCrawlData();
	}

	/**
	 * @return the rootDir
	 */
	public String getCrawlDataDir() {
		return crawlDataDir;
	}

	public List<String> getSeedUrls() {
		return seedUrls;
	}

	/**
	 * @return the isOffline
	 */
	public boolean isOffline() {
		return isOffline;
	}

	private List<String> loadFileUrls(String dir) {

		List<String> fileUrls = new ArrayList<String>();

		File dirFile = new File(dir);

		File[] docs = dirFile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".html");
			}
		});

		try {
			for (File f : docs) {
				URL url = f.toURI().toURL();
				fileUrls.add(url.toExternalForm());
			}
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while converting filename into URL: ", e);
		}

		return fileUrls;
	}

	public void run() {

		webCrawler.addSeedUrls(getSeedUrls());

		URLFilter urlFilter = new URLFilter();

		if (isOffline()) {
			urlFilter.setAllowFileUrls(true);
			urlFilter.setAllowHttpUrls(false);
		} else {
			urlFilter.setAllowFileUrls(false);
			urlFilter.setAllowHttpUrls(true);
		}
		webCrawler.setURLFilter(urlFilter);

		long t0 = System.currentTimeMillis();

		/* run crawl - crawler will fetch and parse the documents */
		webCrawler.fetchAndProcess(maxDepth, maxDocs);

		System.out.println("Timer (s): [Crawler processed data] --> "
				+ (System.currentTimeMillis() - t0) * 0.001);
	}

	public void setAllSeedUrls() {

		seedUrls.clear();

		List<String> fileUrls = loadFileUrls(TEST_FILES_DIR_CH7);

		for (String url : fileUrls) {
			addSeedUrl(url);
		}
	}

	/**
	 * @param isOffline
	 *            the isOffline to set
	 */
	public void setOffline(boolean isOffline) {
		this.isOffline = isOffline;
	}
}
