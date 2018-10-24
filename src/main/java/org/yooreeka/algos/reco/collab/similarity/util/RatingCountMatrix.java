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
package org.yooreeka.algos.reco.collab.similarity.util;

import java.io.Serializable;

import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.User;

public class RatingCountMatrix implements Serializable {

	/**
	 * Unique identifier for serialization
	 */
	private static final long serialVersionUID = -8216800040843757769L;

	private int matrix[][] = null;

	public RatingCountMatrix(Item itemA, Item itemB, int nRatingValues) {
		init(nRatingValues);
		calculate(itemA, itemB);
	}

	public RatingCountMatrix(User userA, User userB, int nRatingValues) {
		init(nRatingValues);
		calculate(userA, userB);
	}

	/*
	 * Populates matrix using user ratings for provided items. We only consider
	 * users that rated both items.
	 */
	private void calculate(Item itemA, Item itemB) {
		for (Rating ratingForA : itemA.getAllRatings()) {
			// check if the same user rated itemB
			Rating ratingForB = itemB.getUserRating(ratingForA.getUserId());
			if (ratingForB != null) {
				// element in the matrix is determined by the rating values.
				int i = ratingForA.getRating() - 1;
				int j = ratingForB.getRating() - 1;
				matrix[i][j]++;
			}
		}
	}

	/*
	 * Populates matrix using ratings for items that the two users share.
	 */
	private void calculate(User userA, User userB) {

		for (Rating ratingByA : userA.getAllRatings()) {

			Rating ratingByB = userB.getItemRating(ratingByA.getItemId());

			if (ratingByB != null) {

				int i = ratingByA.getRating() - 1;
				int j = ratingByB.getRating() - 1;
				matrix[i][j]++;
			}
		}
	}

	public int getAgreementCount() {
		int ratingCount = 0;
		for (int i = 0, n = matrix.length; i < n; i++) {
			ratingCount += matrix[i][i];
		}
		return ratingCount;
	}

	public int getBandCount(int bandId) {
		int bandCount = 0;
		for (int i = 0, n = matrix.length; (i + bandId) < n; i++) {
			bandCount += matrix[i][i + bandId];
			bandCount += matrix[i + bandId][i];
		}
		return bandCount;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public int getTotalCount() {

		int ratingCount = 0;
		int n = matrix.length;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				ratingCount += matrix[i][j];
			}
		}
		return ratingCount;
	}

	private void init(int nSize) {
		// starting point - all elements are zero
		matrix = new int[nSize][nSize];
	}
}
