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
package org.yooreeka.algos.search.ranking;

import java.util.List;
import java.util.Set;

import org.yooreeka.util.internet.crawling.core.CrawlData;
import org.yooreeka.util.internet.crawling.core.CrawlDataProcessor;
import org.yooreeka.util.internet.crawling.db.KnownUrlDB;
import org.yooreeka.util.internet.crawling.db.PageLinkDB;
import org.yooreeka.util.internet.crawling.model.KnownUrlEntry;

public class PageRankMatrixBuilder implements CrawlDataProcessor {

	// private static final Logger logger =
	// Logger.getLogger(PageRankMatrixBuilder.class);

	private PageRankMatrixH matrixH;
	private CrawlData crawlData;

	public PageRankMatrixBuilder(CrawlData crawlData) {
		this.crawlData = crawlData;
	}

	private PageRankMatrixH buildMatrixH(KnownUrlDB knownUrlDB,
			PageLinkDB pageLinkDB) {

		// logger.info("starting calculation of matrix H...");

		// only consider URLs that with fetched and parsed content
		List<String> allProcessedUrls = knownUrlDB
				.findProcessedUrls(KnownUrlEntry.STATUS_PROCESSED_SUCCESS);

		PageRankMatrixH pageMatrix = new PageRankMatrixH(
				allProcessedUrls.size());

		for (String url : allProcessedUrls) {

			// register url here in case it has no outlinks.
			pageMatrix.addLink(url);

			Set<String> pageOutlinks = pageLinkDB.getOutlinks(url);

			for (String outlink : pageOutlinks) {

				// only consider URLs with parsed content
				if (knownUrlDB.isSuccessfullyProcessed(outlink)) {
					pageMatrix.addLink(url, outlink);
				}
			}
		}

		pageMatrix.calculate();

		// logger.info("matrix H is ready. Matrix size: " +
		// pageMatrix.getMatrix().length);

		return pageMatrix;
	}

	public PageRankMatrixH getH() {
		return matrixH;
	}

	public void run() {
		this.matrixH = buildMatrixH(crawlData.getKnownUrlsDB(),
				crawlData.getPageLinkDB());
	}
}
