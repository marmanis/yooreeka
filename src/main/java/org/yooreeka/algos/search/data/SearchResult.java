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
package org.yooreeka.algos.search.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Custom wrapper for the search results.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class SearchResult {

	/**
	 * Sorts list in descending order of score value.
	 */
	public static void sortByScore(List<SearchResult> values) {
		Collections.sort(values, new Comparator<SearchResult>() {
			public int compare(SearchResult r1, SearchResult r2) {
				int result = 0;
				// sort based on score value
				if (r1.getScore() < r2.getScore()) {
					result = 1; // sorting in descending order
				} else if (r1.getScore() > r2.getScore()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}

			@Override
			public Comparator<SearchResult> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparing(
					Comparator<? super SearchResult> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<SearchResult> thenComparing(
					Function<? super SearchResult, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<SearchResult> thenComparing(
					Function<? super SearchResult, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparingInt(
					ToIntFunction<? super SearchResult> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparingLong(
					ToLongFunction<? super SearchResult> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparingDouble(
					ToDoubleFunction<? super SearchResult> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}
	/**
	 * Sorts array in descending order of score value.
	 */
	public static void sortByScore(SearchResult[] values) {
		Arrays.sort(values, new Comparator<SearchResult>() {
			public int compare(SearchResult r1, SearchResult r2) {
				int result = 0;
				// sort based on score value
				if (r1.getScore() < r2.getScore()) {
					result = 1; // sorting in descending order
				} else if (r1.getScore() > r2.getScore()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}

			@Override
			public Comparator<SearchResult> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparing(
					Comparator<? super SearchResult> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<SearchResult> thenComparing(
					Function<? super SearchResult, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<SearchResult> thenComparing(
					Function<? super SearchResult, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparingInt(
					ToIntFunction<? super SearchResult> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparingLong(
					ToLongFunction<? super SearchResult> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SearchResult> thenComparingDouble(
					ToDoubleFunction<? super SearchResult> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}
	private String docId;
	private String docType;

	private String title;

	private String url;

	private double score;

	public SearchResult(String docId, String docType, String title, String url,
			double score) {

		this.docId = docId;
		this.docType = docType;
		this.title = title;
		this.url = url;
		this.score = score;
	}

	/**
	 * @return the docId
	 */
	public String getDocId() {
		return docId;
	}

	public String getDocType() {
		return docType;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @return document title if available
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	public String print() {
		StringBuilder strB = new StringBuilder();
		// strB.append("Document ID    : ").append(docId).append("\n");
		strB.append("Document Type: ").append(docType).append("\n");
		strB.append("Document Title : ").append(title).append("\n");
		strB.append("Document URL: ").append(url).append("  -->  ");
		strB.append("Relevance Score: ").append(score).append("\n");
		return strB.toString();
	}

	/**
	 * @param docId
	 *            the docId to set
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @param title
	 *            document title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
