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
package org.yooreeka.algos.reco.collab.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;

/**
 * Utility class to generate random ratings.
 */
class RatingBuilder {

	private Random rand = null;

	public RatingBuilder() {
		rand = new java.util.Random();
	}

	/**
	 * Creates biased ratings for all items.
	 * 
	 * @param userId
	 *            rating user.
	 * @param items
	 *            to create ratings for.
	 * @param lowerBias
	 *            low range for rating value
	 * @param upperBias
	 *            high range for rating value
	 * @return
	 */
	public List<Rating> createBiasedRatings(int userId, Item[] items,
			int lowerBias, int upperBias) {
		List<Rating> ratings = new ArrayList<Rating>();
		for (Item item : items) {
			int biasedRandomRating = getRandomRating(lowerBias, upperBias);
			Rating rating = new Rating(userId, item.getId(), biasedRandomRating);
			rating.setItem(item);
			ratings.add(rating);
		}
		return ratings;
	}

	public int getRandomRating() {
		// No bias
		return getRandomRating(5);
	}

	public int getRandomRating(int upperBias) {

		// Lower bias is 1
		return getRandomRating(1, upperBias);
	}

	public int getRandomRating(int lowerBias, int upperBias) {

		// We add 1 at the end because the nextInt(n) call excludes n
		int n = (upperBias - lowerBias) + 1;
		return (lowerBias + rand.nextInt(n));
	}

}
