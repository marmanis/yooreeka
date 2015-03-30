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
package org.yooreeka.examples.search;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.yooreeka.algos.search.data.SearchResult;
import org.yooreeka.algos.search.lucene.LuceneIndexBuilder;
import org.yooreeka.algos.search.ranking.Rank;
import org.yooreeka.algos.taxis.bayesian.NaiveBayes;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.util.internet.behavior.UserClick;
import org.yooreeka.util.internet.behavior.UserQuery;

public class MySearcher {

	/**
	 * An arbitrary small value
	 */
	public static final double EPSILON = 0.0001;

	private static final String PRETTY_LINE = "_______________________________________________________________________";

	private File indexFile;
	private NaiveBayes learner = null;

	private boolean verbose = true;

	public MySearcher(String indexDir) {
		indexFile = new File(indexDir);
	}

	public boolean isVerbose() {
		return verbose;
	}

	private void printResults(String header, String query,
			SearchResult[] values, boolean showDocTitle) {

		if (verbose) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);

			boolean printEntrySeparator = false;
			if (showDocTitle) { // multiple lines per entry
				printEntrySeparator = true;
			}

			pw.print("\n");
			pw.println(header);
			if (query != null) {
				pw.println(query);
			}
			pw.print("\n");
			for (int i = 0, n = values.length; i < n; i++) {
				if (values[i] != null) {
					if (showDocTitle) {
						pw.printf("Document Title: %s\n", values[i].getTitle());
					}
					pw.printf(
							"Document URL: %-46s  -->  Relevance Score: %.15f\n",
							values[i].getUrl(), values[i].getScore());
					if (printEntrySeparator) {
						pw.printf(PRETTY_LINE);
						pw.printf("\n");
					}
				} else {
					pw.printf("Document: %s\n",
							"Not available, values[i] is NULL");
				}
			}
			if (!printEntrySeparator) {
				pw.print(PRETTY_LINE);
			}

			System.out.println(sw.toString());
		}
	}

	public SearchResult[] search(String query, int numberOfMatches) {

		SearchResult[] docResults = null;

		IndexSearcher is = null;

		Directory dir = null;
		try {
			dir = FSDirectory.open(indexFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DirectoryReader dirReader = null;
		try {

			dirReader = DirectoryReader.open(dir);
			is = new IndexSearcher(dirReader);

		} catch (IOException ioX) {
			System.out.println("ERROR: " + ioX.getMessage());
		}

		StandardQueryParser queryParserHelper = new StandardQueryParser();
		Query q = null;

		try {

			q = queryParserHelper.parse(query,
					LuceneIndexBuilder.INDEX_FIELD_CONTENT);

		} catch (QueryNodeException e) {
			e.printStackTrace();
		}

		TopDocs hits = null;
		try {
			hits = is.search(q, numberOfMatches);

			docResults = new SearchResult[hits.scoreDocs.length];

			for (int i = 0; i < hits.scoreDocs.length; i++) {

				Document hitDoc = is.doc(hits.scoreDocs[i].doc);

				docResults[i] = new SearchResult(hitDoc.get("docid"),
						hitDoc.get("doctype"), hitDoc.get("title"),
						hitDoc.get("url"), hits.scoreDocs[i].score);
			}

			dirReader.close();
			dir.close();

		} catch (IOException ioX) {
			System.out.println("ERROR: " + ioX.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String header = "Search results using Lucene index scores:";
		boolean showTitle = true;
		printResults(header, "Query: " + query, docResults, showTitle);

		return docResults;
	}

	/**
	 * A method that combines the score of an index based search and the score
	 * of the PageRank algorithm to achieve better relevance results.
	 */
	public SearchResult[] search(String query, int numberOfMatches, Rank pR) {

		SearchResult[] docResults = search(query, numberOfMatches);

		String url;

		int n = pR.getH().getSize();

		/**
		 * TODO: 2.3 -- The PageRank scaling factor m (Book Section 2.3)
		 * 
		 * When the number of pages in your graph are few, the PageRank values
		 * need some boosting. As the number of pages increases m approaches the
		 * value 1 quickly because 1/n goes to zero.
		 */
		double m = 1 - (double) 1 / n;

		// actualNumberOfMatches <= numberOfMatches
		int i = 0;

		while (i < docResults.length && docResults[i] != null) {

			url = docResults[i].getUrl();
			
			double hScore = docResults[i].getScore() * Math.pow(pR.getPageRank(url), m);

			// Update the score of the results
			docResults[i].setScore(hScore);

			i++;
		}

		// sort results by score
		SearchResult.sortByScore(docResults);

		String header = "Search results using combined Lucene scores and page rank scores:";
		boolean showTitle = false;
		printResults(header, "Query: " + query, docResults, showTitle);

		return docResults;
	}

	/**
	 * A method that combines the score of an index based search and the score
	 * of the PageRank algorithm to achieve better relevance results, while
	 * personalizing the result set based on past user clicks on the same or
	 * similar queries.
	 * 
	 * NOTE: You would typically refactor all these search methods in order to
	 * consider it production quality code. Here, we repeat the code of the
	 * previous method, so that it is easier to read.
	 * 
	 * @param userID
	 *            identifies the person who issues the query
	 * @param query
	 *            is the whole query
	 * @param numberOfMatches
	 *            defines the maximim number of desired matches
	 * @param pR
	 *            the PageRank vector
	 * @return the result set
	 */
	public SearchResult[] search(UserQuery uQuery, int numberOfMatches, Rank pR) {

		SearchResult[] docResults = search(uQuery.getQueryString(),
				numberOfMatches);

		String url;

		int docN = docResults.length;

		if (docN > 0) {

			int loop = (docN < numberOfMatches) ? docN : numberOfMatches;

			for (int i = 0; i < loop; i++) {

				url = docResults[i].getUrl();

				UserClick uClick = new UserClick(uQuery, url);

				/**
				 * TODO: 2.6 -- Weighing the scores to meet your needs (Book
				 * Section 2.4.2)
				 * 
				 * At this point, we have three scores of relevance. The
				 * relevance score that is based on the index search, the
				 * PageRank score, and the score that is based on the user's
				 * prior selections. There is no golden formula for everybody.
				 * Below we are selecting a formula that we think would make
				 * sense for most people.
				 * 
				 * Feel free to change the formula, experiment with different
				 * weighting factors, to find out the choices that are most
				 * appropriate for your own site.
				 * 
				 */
				double indexScore = docResults[i].getScore();

				double pageRankScore = pR.getPageRank(url);

				double userClickScore = 0.0;

				for (Concept bC : learner.getTset().getConceptSet()) {
					if (bC.getName().equalsIgnoreCase(url)) {
						userClickScore = learner.getProbability(bC, uClick);
					}
				}

				// Create the final score
				double hScore;

				if (userClickScore == 0) {

					hScore = indexScore * pageRankScore * EPSILON;

				} else {

					hScore = indexScore * pageRankScore * userClickScore;
				}

				// Update the score of the results
				docResults[i].setScore(hScore);

				/*
				 * Uncomment this block to show the various scores in the
				 * BeanShell
				 */ 
				
//				 StringBuilder b = new StringBuilder();
//				  
//				 b.append("Document      : ").append(docResults[i].getUrl()).append("\n");
//				 b.append("UserClick URL :").append(uClick.getUrl()).append("\n"); b.append("\n");
//				 b.append("Index score: ").append(indexScore).append(", ");
//				 b.append("PageRank score: ").append(pageRankScore).append(", ");
//				 b.append("User click score: ").append(userClickScore);
//				 
//				 P.hline();
//				 P.println(b.toString());
//				 P.hline();
			}
		}

		// Sort array of results
		SearchResult.sortByScore(docResults);

		String header = "Search results using combined Lucene scores, "
				+ "page rank scores and user clicks:";
		String query = "Query: user=" + uQuery.getUid() + ", query text="
				+ uQuery.getQueryString();
		boolean showTitle = false;
		printResults(header, query, docResults, showTitle);

		return docResults;
	}

	public void setUserLearner(NaiveBayes nb) {
		learner = nb;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
