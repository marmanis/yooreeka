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

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.core.BasicWebCrawler;
import org.yooreeka.util.internet.crawling.core.CrawlData;
import org.yooreeka.util.internet.crawling.core.URLFilter;
import org.yooreeka.util.internet.crawling.core.URLNormalizer;

public class FetchAndProcessCrawler {

	private final static Logger log = Logger.getLogger(FetchAndProcessCrawler.class.getName());
	
	public static final int DEFAULT_MAX_DEPTH = 3;
	public static final int DEFAULT_MAX_DOCS = 1000;

	// INSTANCE VARIABLES
	// A reference to the crawler
	BasicWebCrawler webCrawler;

	// The location where we will store the fetched data
	String rootDir;

	// Total number of crawlers
	int numberOfCrawlers = 4;

	// total number of iterations
	int maxDepth = DEFAULT_MAX_DEPTH;

	// max number of pages that will be fetched within every crawl/iteration.
	int maxDocs = DEFAULT_MAX_DOCS;

	List<String> seedUrls;

	URLFilter urlFilter;

	public FetchAndProcessCrawler(String dir, int maxDepth, int maxDocs) {

		log.fine("Creating FetchAndProcessCrawler(String dir: "+dir
				+", int maxDepth: "+maxDepth
				+", int maxDocs: "+maxDocs+")");
		
		rootDir = dir;

		// If the root directory is not set or if its length is zero
		if (rootDir == null || rootDir.trim().length() == 0) {

			// Create a default location for storing the data, relative to the
			// IWEB2_HOME location
			rootDir = System.getProperty("yooreeka.home")
					+ System.getProperty("file.separator") + "data";
		}

		rootDir = rootDir + System.getProperty("file.separator") + "crawl-"
				+ System.currentTimeMillis();

		this.maxDepth = maxDepth;

		this.maxDocs = maxDocs;

		this.seedUrls = new ArrayList<String>();

		webCrawler = new BasicWebCrawler(rootDir);
	}

	public void addDocSpam() {

		String iWeb2Home = YooreekaConfigurator.getHome();

		addUrl("file:///" + iWeb2Home + "/data/ch02/spam-biz-01.doc");
		addUrl("file:///" + iWeb2Home + "/data/ch02/spam-biz-02.doc");
		addUrl("file:///" + iWeb2Home + "/data/ch02/spam-biz-03.doc");
	}

	public void addUrl(String val) {
		URLNormalizer urlNormalizer = new URLNormalizer();
		seedUrls.add(urlNormalizer.normalizeUrl(val));
	}

	public CrawlData getCrawlData() {
		return webCrawler.getCrawlData();
	}

	/**
	 * @return the maximum depth of the crawl
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * @return the maxNumberOfDocsPerCrawl
	 */
	public int getMaxNumberOfDocsPerCrawl() {
		return maxDocs;
	}

	/**
	 * @return the rootDir
	 */
	public String getRootDir() {
		return rootDir;
	}

	public List<String> getSeedUrls() {

		return seedUrls;
	}

	public void run() {
		
		webCrawler.addSeedUrls(getSeedUrls());

		webCrawler.setURLFilter(urlFilter);

		long t0 = System.currentTimeMillis();

		/* run crawl */
		webCrawler.fetchAndProcess(maxDepth, maxDocs);

		P.println("Timer (s): [Crawler processed data] --> " + (System.currentTimeMillis() - t0) * 0.001);

	}

	public void setAllBookUrls() {

		setDefaultBookUrls();

		String iWeb2Home = YooreekaConfigurator.getHome();

		// Include the spam pages ... all of them!
		addUrl("file:///" + iWeb2Home + "/data/ch02/spam-01.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/spam-biz-01.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/spam-biz-02.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/spam-biz-03.html");
	}

	public void setDefaultBookUrls() {

		String iWeb2Home = YooreekaConfigurator.getHome();

		addUrl("file:///" + iWeb2Home + "/data/ch02/biz-01.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/biz-02.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/biz-03.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/biz-04.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/biz-05.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/biz-06.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/biz-07.html");

		addUrl("file:///" + iWeb2Home + "/data/ch02/sport-01.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/sport-02.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/sport-03.html");

		addUrl("file:///" + iWeb2Home + "/data/ch02/usa-01.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/usa-02.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/usa-03.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/usa-04.html");

		addUrl("file:///" + iWeb2Home + "/data/ch02/world-01.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/world-02.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/world-03.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/world-04.html");
		addUrl("file:///" + iWeb2Home + "/data/ch02/world-05.html");

		setFilesOnlyUrlFilter();
	}

	public void setFilesOnlyUrlFilter() {
		/* configure url filter to accept only file:// urls */
		URLFilter urlFilter = new URLFilter();
		urlFilter.setAllowFileUrls(true);
		urlFilter.setAllowHttpUrls(false);
		setUrlFilter(urlFilter);
	}

	/**
	 * @param maxNumberOfCrawls
	 *            the maxNumberOfCrawls to set
	 */
	public void setMaxNumberOfCrawls(int maxNumberOfCrawls) {
		this.maxDepth = maxNumberOfCrawls;
	}

	/**
	 * @param maxNumberOfDocsPerCrawl
	 *            the maxNumberOfDocsPerCrawl to set
	 */
	public void setMaxNumberOfDocsPerCrawl(int maxNumberOfDocsPerCrawl) {
		this.maxDocs = maxNumberOfDocsPerCrawl;
	}

	/**
	 * @param rootDir
	 *            the rootDir to set
	 */
	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public void setUrlFilter(URLFilter urlFilter) {
		this.urlFilter = urlFilter;
	}

	public void setUrls(String val) {

		String iWeb2Home = YooreekaConfigurator.getHome();

		setFilesOnlyUrlFilter();

		this.seedUrls.clear();

		if (val.equalsIgnoreCase("biz")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-01.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-02.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-03.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-04.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-05.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-06.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-07.html");

		} else if (val.equalsIgnoreCase("sport")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/sport-01.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/sport-02.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/sport-03.html");

		} else if (val.equalsIgnoreCase("usa")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-01.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-02.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-03.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-04.html");

		} else if (val.equalsIgnoreCase("world")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/world-01.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-02.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-03.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-04.html");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-05.html");
		} else if (val.equalsIgnoreCase("biz-docs")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-01.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-02.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-03.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-04.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-05.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-06.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/biz-07.doc");

		} else if (val.equalsIgnoreCase("sport-docs")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/sport-01.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/sport-02.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/sport-03.doc");

		} else if (val.equalsIgnoreCase("usa-docs")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-01.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-02.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-03.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/usa-04.doc");

		} else if (val.equalsIgnoreCase("world-docs")) {

			addUrl("file:///" + iWeb2Home + "/data/ch02/world-01.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-02.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-03.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-04.doc");
			addUrl("file:///" + iWeb2Home + "/data/ch02/world-05.doc");
		} else {
			throw new IllegalArgumentException("Unknown value: '" + val + "'");
		}

	}

	public void setFilesDirectory(String dir) throws IOException, URISyntaxException {
		
		Path path = Paths.get(dir);
		MyFileVisitor visitor = new MyFileVisitor();
		Files.walkFileTree(path, visitor);
	}
	
	public class MyFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

    	if (attr.isSymbolicLink()) {
            
    		P.println("Symbolic link found: %s ", file);
            
        } else if (attr.isRegularFile()) {

        	P.println("Regular file: %s ", file);
        	addUrl("file:///"+file.toString());
        	
        } else {
        	
            P.println("Other: %s ", file);
        }
        //System.out.println("(" + attr.size() + "bytes)");
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        P.println("Directory processed: %s%n", dir);
        return CONTINUE;
    }

    // If there is some error accessing the file, let the user know.
    // If you don't override this method and an error occurs, an IOException is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        exc.printStackTrace();
        return CONTINUE;
    }
}
}
