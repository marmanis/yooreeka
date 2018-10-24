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
package org.yooreeka.algos.search.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class TermFreqMapUtils {

	public static Map<String, Integer> buildTermFreqMap(String[] keys,
			int[] values) {
		int n = keys.length;
		Map<String, Integer> map = new HashMap<String, Integer>(n);

		for (int i = 0; i < n; i++) {
			map.put(keys[i], values[i]);
		}

		return map;
	}

	public static Map<String, Integer> getTopNTermFreqMap(String[] terms,
			int[] frequencies, int topNTerms) {

		Map<String, Integer> tfMap = TermFreqMapUtils.buildTermFreqMap(terms,
				frequencies);
		boolean descending = true;
		String[] sortedTerms = TermFreqMapUtils.sortTermsByFrequencies(tfMap,
				descending);
		int n = Math.min(sortedTerms.length, topNTerms);
		Map<String, Integer> topNTermFreqMap = new HashMap<String, Integer>();
		for (int i = 0; i < n; i++) {
			String key = sortedTerms[i];
			Integer value = tfMap.get(sortedTerms[i]);
			topNTermFreqMap.put(key, value);
		}

		return topNTermFreqMap;

	}

	public static String[] sortTermsByFrequencies(
			final Map<String, Integer> tfMap, final boolean descending) {

		String[] sortedTerms = tfMap.keySet().toArray(new String[tfMap.size()]);

		Arrays.sort(sortedTerms, new Comparator<String>() {

			public int compare(String key1, String key2) {
				int v1 = tfMap.get(key1);
				int v2 = tfMap.get(key2);
				if (descending) {
					return v2 - v1;
				} else {
					return v1 - v2;
				}
			}

			@Override
			public Comparator<String> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<String> thenComparing(
					Comparator<? super String> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<String> thenComparing(
					Function<? super String, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<String> thenComparing(
					Function<? super String, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<String> thenComparingInt(
					ToIntFunction<? super String> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<String> thenComparingLong(
					ToLongFunction<? super String> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<String> thenComparingDouble(
					ToDoubleFunction<? super String> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

		});

		return sortedTerms;
	}
}
