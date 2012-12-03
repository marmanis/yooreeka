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
package org.yooreeka.algos.reco.collab.recommender;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.User;

/**
 * Represents predicted user rating of an item. Used to return recommendations
 * for the user.
 */
public class PredictedItemRating {
	/**
	 * Sorts list of recommendations in descending order and return topN
	 * elements.
	 * 
	 * @param recommendations
	 * @param topN
	 * @return
	 */
	public static List<PredictedItemRating> getTopNRecommendations(
			List<PredictedItemRating> recommendations, int topN) {

		PredictedItemRating.sort(recommendations);

		List<PredictedItemRating> topRecommendations = new ArrayList<PredictedItemRating>();
		for (PredictedItemRating r : recommendations) {
			if (topRecommendations.size() >= topN) {
				// have enough recommendations.
				break;
			}
			topRecommendations.add(r);
		}

		return topRecommendations;
	}
	public static void printUserRecommendations(User user, Dataset ds,
			List<PredictedItemRating> recommendedItems) {
		System.out.println("\nRecommendations for user " + user.getName()
				+ ":\n");
		for (PredictedItemRating r : recommendedItems) {
			System.out.printf("Item: %-36s, predicted rating: %f\n", ds
					.getItem(r.getItemId()).getName(), r.getRating(4));
		}
	}
	/**
	 * Sorts list by rating value in descending order. Items with higher ratings
	 * will be in the head of the list.
	 * 
	 * @param values
	 *            list to sort.
	 */
	public static void sort(List<PredictedItemRating> values) {
		Collections.sort(values, new Comparator<PredictedItemRating>() {

			public int compare(PredictedItemRating f1, PredictedItemRating f2) {

				int result = 0;
				if (f1.getRating() < f2.getRating()) {
					result = 1; // reverse order
				} else if (f1.getRating() > f2.getRating()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}
		});
	}

	private int userId;

	private int itemId;

	private double rating;

	public PredictedItemRating(int userId, int itemId, double rating) {
		this.userId = userId;
		this.itemId = itemId;
		this.rating = rating;
	}

	public int getItemId() {
		return itemId;
	}

	public double getRating() {
		return rating;
	}

	/**
	 * Returns rounded rating value with number of digits after decimal point
	 * specified by <code>scale</code> parameter.
	 * 
	 * @param scale
	 *            number of digits to keep after decimal point.
	 * @return rounded value.
	 */
	public double getRating(int scale) {
		BigDecimal bd = new BigDecimal(rating);
		return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	public int getUserId() {
		return userId;
	}

	public void setRating(double val) {
		this.rating = val;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[userId: " + userId
				+ ", itemId: " + itemId + ", rating: " + rating + "]";
	}
}
