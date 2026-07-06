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
package org.yooreeka.algos.search.lucene.analyzer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopwordAnalyzerBase;

public class CustomAnalyzer extends StopwordAnalyzerBase {

	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	public static final CharArraySet STOP_WORDS_SET = EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;

	private static final String[] ADDITIONAL_STOP_WORDS = { "should", "would",
			"from", "up", "i", "s", "it", "his", "has", "he", "she", "her",
			"said", "been", "being", "final", "now", "hour", "minute",
			"second", "stop", "start", "first", "third", "fast", "slow",
			"large", "small" };

	private static CharArraySet MERGED_STOP_WORDS;

	static {
		MERGED_STOP_WORDS = new CharArraySet(
				STOP_WORDS_SET.size() + ADDITIONAL_STOP_WORDS.length, true);
		MERGED_STOP_WORDS.addAll(STOP_WORDS_SET);
		for (String word : ADDITIONAL_STOP_WORDS) {
			MERGED_STOP_WORDS.add(word);
		}
	}

	public CustomAnalyzer() {
		this(MERGED_STOP_WORDS);
	}

	public CustomAnalyzer(CharArraySet stopWords) {
		super(stopWords);
	}

	@Override
	protected TokenStreamComponents createComponents(final String fieldName) {
		final StandardTokenizer src = new StandardTokenizer();
		src.setMaxTokenLength(maxTokenLength);
		TokenStream tok = new LowerCaseFilter(src);
		tok = new StopFilter(tok, stopwords);
		return new TokenStreamComponents(src, tok);
	}
}
