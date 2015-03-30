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
package org.yooreeka.algos.reco.collab.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.logging.Logger;

import org.yooreeka.algos.search.lucene.analyzer.TextDocumentTerms;
import org.yooreeka.config.YooreekaConfigurator;

public class Content implements java.io.Serializable {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 1098727290087922462L;
	private static final Logger LOG = Logger.getLogger(Content.class.getName());

	private String id;
	private String text;
	private String[] terms;
	private int[] termFrequencies;
	private Map<String, Integer> tfMap;

	public Content(String id, String text) {
		this(id, text, 10);
	}

	public Content(String id, String text, int topNTerms) {
		
		LOG.setLevel(YooreekaConfigurator.getLevel(Content.class.getName()));

		this.id = id;
		this.text = text;

		Map<String, Integer> allTermFrequencyMap = (new TextDocumentTerms(text))
				.getTf();
		tfMap = getTopNTermFrequencies(allTermFrequencyMap, topNTerms);

		terms = new String[tfMap.size()];
		termFrequencies = new int[tfMap.size()];

		int i = 0;
		for (Map.Entry<String, Integer> e : tfMap.entrySet()) {
			terms[i] = e.getKey();
			termFrequencies[i] = e.getValue();
			i++;
		}
	}

	public String getId() {
		return id;
	}

	public int[] getTermFrequencies() {
		return termFrequencies;
	}

	public String[] getTerms() {
		return terms;
	}

	public double[] getTermVector(String[] terms) {
		double[] termVector = new double[terms.length];
		for (int i = 0, n = terms.length; i < n; i++) {
			if (tfMap.containsKey(terms[i])) {
				termVector[i] = 1;
			} else {
				termVector[i] = 0;
			}
		}
		return termVector;
	}

	public String getText() {
		return text;
	}

	public Map<String, Integer> getTFMap() {
		return this.tfMap;
	}

	// private Map<String, Integer> buildTermFrequencyMap(String text) {
	//
	// CustomAnalyzer analyzer = new CustomAnalyzer(Version.LUCENE_40);
	// TokenStream tokenStream = analyzer.tokenStream("content", new
	// StringReader(text));
	//
	// Map<String, Integer> termFrequencyMap = new HashMap<String, Integer>();
	//
	// boolean hasTokens = true;
	// try {
	// while (hasTokens) {
	// Token t = null;//tokenStream.next();
	// if (t == null) {
	// hasTokens = false;
	// } else {
	// String term = new String(t.termBuffer(), 0, t.termLength());
	// Integer frequency = termFrequencyMap.get(term);
	// if( frequency == null ) {
	// termFrequencyMap.put(term, 1);
	// }
	// else {
	// termFrequencyMap.put(term, frequency + 1);
	// }
	// }
	// }
	// }
	// catch(IOException e) {
	// throw new RuntimeException(e);
	// }
	//
	// return termFrequencyMap;
	// }

	private Map<String, Integer> getTopNTermFrequencies(
			Map<String, Integer> termFrequencyMap, int topN) {

		List<Map.Entry<String, Integer>> terms = new ArrayList<Map.Entry<String, Integer>>(
				termFrequencyMap.entrySet());

		// Different terms can have the same frequency.
		Collections.sort(terms, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> e1,
					Map.Entry<String, Integer> e2) {
				int result = 0;
				if (e1.getValue() < e2.getValue()) {
					result = 1; // reverse order
				} else if (e1.getValue() > e2.getValue()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}

			@Override
			public Comparator<Entry<String, Integer>> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<String, Integer>> thenComparing(
					Comparator<? super Entry<String, Integer>> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<Entry<String, Integer>> thenComparing(
					Function<? super Entry<String, Integer>, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<Entry<String, Integer>> thenComparing(
					Function<? super Entry<String, Integer>, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<String, Integer>> thenComparingInt(
					ToIntFunction<? super Entry<String, Integer>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<String, Integer>> thenComparingLong(
					ToLongFunction<? super Entry<String, Integer>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<String, Integer>> thenComparingDouble(
					ToDoubleFunction<? super Entry<String, Integer>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

		});

		Map<String, Integer> topNTermsFrequencyMap = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> term : terms) {
			topNTermsFrequencyMap.put(term.getKey(), term.getValue());
			if (topNTermsFrequencyMap.size() >= topN) {
				break;
			}
		}

		return topNTermsFrequencyMap;
	}

}
