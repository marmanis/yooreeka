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
import java.util.List;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class SimilarItem {

	public static SimilarItem[] getTopSimilarItems(
			List<SimilarItem> similarItems, int topN) {

		// sort friends based on itemAgreement
		SimilarItem.sort(similarItems);

		// select top N friends
		List<SimilarItem> topItems = new ArrayList<SimilarItem>();
		for (SimilarItem f : similarItems) {
			if (topItems.size() >= topN) {
				// have enough friends.
				break;
			}
			topItems.add(f);
		}

		return topItems.toArray(new SimilarItem[topItems.size()]);
	}

	public static void printItems(SimilarItem[] items, String header) {
		System.out.println("\n" + header + "\n");
		for (SimilarItem f : items) {
			System.out.printf("name: %-36s, similarity: %f\n", f.getItem()
					.getName(), f.getSimilarity());
		}
	}

	public static void sort(List<SimilarItem> similarItems) {

		Collections.sort(similarItems, new Comparator<SimilarItem>() {

			public int compare(SimilarItem f1, SimilarItem f2) {

				int result = 0;
				if (f1.getSimilarity() < f2.getSimilarity()) {
					result = 1; // reverse order
				} else if (f1.getSimilarity() > f2.getSimilarity()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}
		});
	}

	private Item item;

	/*
	 * Similarity
	 */
	private double similarity = -1;

	public SimilarItem(Item item, double sim) {
		this.item = item;
		similarity = sim;
	}

	// ----------------------------------------------
	// GETTERS / SETTERS
	// ----------------------------------------------

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the similarity
	 */
	public double getSimilarity() {
		return similarity;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

}
