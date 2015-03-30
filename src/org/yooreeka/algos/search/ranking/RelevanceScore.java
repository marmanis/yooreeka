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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Utility class that acts as a holder for double value and id of the object
 * that this value corresponds.
 */
public class RelevanceScore {
	/**
	 * Sorts list in descending order of score value.
	 */
	public static void sort(List<RelevanceScore> values) {
		Collections.sort(values, new Comparator<RelevanceScore>() {
			public int compare(RelevanceScore r1, RelevanceScore r2) {
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
			public Comparator<RelevanceScore> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<RelevanceScore> thenComparing(
					Comparator<? super RelevanceScore> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<RelevanceScore> thenComparing(
					Function<? super RelevanceScore, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<RelevanceScore> thenComparing(
					Function<? super RelevanceScore, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<RelevanceScore> thenComparingInt(
					ToIntFunction<? super RelevanceScore> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<RelevanceScore> thenComparingLong(
					ToLongFunction<? super RelevanceScore> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<RelevanceScore> thenComparingDouble(
					ToDoubleFunction<? super RelevanceScore> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}
	private String id;

	private double score;

	public RelevanceScore(String id, double rank) {
		this.id = id;
		this.score = rank;
	}

	public String getId() {
		return id;
	}

	public double getScore() {
		return score;
	}

}
