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
package org.yooreeka.util.internet.crawling;

import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * A general crawler based on the Crawler4J library.
 * 
 * {@link http://code.google.com/p/crawler4j/}
 * 
 */
public class YCrawler extends WebCrawler {

	// PUBLIC STATIC CONSTANTS
	public final static int CONNECTION_TIMEOUT = 5000;

	// PRIVATE STATIC CONSTANTS
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	// INSTANCE VARIABLES

	public static void main(String[] args) throws Exception {

		YCrawler crawler = new YCrawler();

		// To change the root dir you can invoke setRootDir() here
		// before the setup()
		CrawlController controller = crawler.setup();

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(YCrawler.class, crawler.getNumberOfCrawlers());
	}

	/**
	 * The location where we will store the fetched data. Note that this is a
	 * location for all the crawls of this class. If you would like to change it
	 * use the <tt>setRootDir()</tt> method.
	 */
	private String rootDir;

	private int numberOfCrawlers = 5;

	private int connectionTimeout = CONNECTION_TIMEOUT;

	private int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	private String getRootDir() {

		// If the root directory is not set or if its length is zero
		if (rootDir == null || rootDir.trim().length() == 0) {

			// Create a default location for storing the data, relative to the
			// IWEB2_HOME location
			rootDir = System.getProperty("yooreeka.home")
					+ System.getProperty("file.separator") + "data";
		}

		rootDir = rootDir + System.getProperty("file.separator") + "crawl-"
				+ System.currentTimeMillis();

		return rootDir;
	}

	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	private CrawlController setup() {

		CrawlConfig crawlConfiguration = new CrawlConfig();
		crawlConfiguration.setConnectionTimeout(connectionTimeout);
		crawlConfiguration.setCrawlStorageFolder(getRootDir());
		crawlConfiguration.setFollowRedirects(true);
		crawlConfiguration.setIncludeBinaryContentInCrawling(true);
		crawlConfiguration.setIncludeHttpsPages(true);

		// The default value is 100
		crawlConfiguration.setMaxConnectionsPerHost(32);

		// Try 32 Mb; the default is 1 Mb
		crawlConfiguration.setMaxDownloadSize(32 * 1024 * 1024);

		// LIMIT THE MAX NUMBER OF PAGES!!!
		// Unless you know what you are doing, technically and business wise ...
		crawlConfiguration.setMaxPagesToFetch(64);

		PageFetcher pageFetcher = new PageFetcher(crawlConfiguration);

		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();

		RobotstxtServer robotsTextServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);

		CrawlController controller = null;
		try {
			controller = new CrawlController(crawlConfiguration, pageFetcher,
					robotsTextServer);
		} catch (Exception e) {
			// TODO proper logging ...
			e.printStackTrace();
		}

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		controller.addSeed("http://arxiv.org/");

		return controller;
	}

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith("http://www.ics.uci.edu/");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
		}
	}
}