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
package org.yooreeka.algos.clustering.rock;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class SimilarCluster {
	/**
	 * Sorts list by goodness value in descending order. Higher goodness values
	 * will be in the head of the list.
	 * 
	 * @param values
	 *            list to sort.
	 */
	public static void sortByGoodness(List<SimilarCluster> values) {
		Collections.sort(values, new Comparator<SimilarCluster>() {

			public int compare(SimilarCluster f1, SimilarCluster f2) {

				int result = 0;
				if (f1.getGoodness() < f2.getGoodness()) {
					result = 1; // order in the decreasing order of goodness
								// value
				} else if (f1.getGoodness() > f2.getGoodness()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}

			@Override
			public Comparator<SimilarCluster> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarCluster> thenComparing(
					Comparator<? super SimilarCluster> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<SimilarCluster> thenComparing(
					Function<? super SimilarCluster, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<SimilarCluster> thenComparing(
					Function<? super SimilarCluster, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarCluster> thenComparingInt(
					ToIntFunction<? super SimilarCluster> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarCluster> thenComparingLong(
					ToLongFunction<? super SimilarCluster> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarCluster> thenComparingDouble(
					ToDoubleFunction<? super SimilarCluster> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}
	private Integer clusterKey;

	private Double goodness;

	public SimilarCluster(Integer clusterKey, Double goodness) {
		this.clusterKey = clusterKey;
		this.goodness = goodness;
	}

	public Integer getClusterKey() {
		return clusterKey;
	}

	public Double getGoodness() {
		return goodness;
	}

	@Override
	public String toString() {
		return "[clusterKey=" + this.clusterKey + ",goodness=" + this.goodness
				+ "]";
	}
}
