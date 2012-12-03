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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.util.gui.XyGui;

/**
 * User for music dataset.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 */
public class MusicUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4866915806848833932L;

	public MusicUser(int userId, String name) {
		super(userId, name);
	}

	public MusicUser(int userId, String name, List<Rating> ratings) {
		super(userId, name, ratings);
	}

	public double getSimilarity(MusicUser u, int simType) {

		double sim = 0.0d;
		int commonItems = 0;

		/**
		 * TODO: 3.1 -- Types of similarity (Book section 3.1.2)
		 * 
		 * In the following switch, we include two types of similarity You can
		 * extend the functionality of this method by adding more types. For
		 * example, the Jaccard similarity could be defined as the ratio of the
		 * intersection over the union of the items between two users. In other
		 * words, Number of songs in common Jaccard Similarity =
		 * ------------------------------------------- Number of all songs
		 * listened by either user
		 * 
		 * Are more complicated similarity metrics more accurate?
		 */

		switch (simType) {

		case 0:
			for (Rating r : this.ratingsByItemId.values()) {
				for (Rating r2 : u.ratingsByItemId.values()) {

					// Find the same item
					if (r.getItemId() == r2.getItemId()) {
						commonItems++;
						sim += Math.pow((r.getRating() - r2.getRating()), 2);
					}
				}
			}

			// If there are not common items, we cannot tell whether
			// the users are similar or not. So, we let it return 0.
			if (commonItems > 0) {

				// This is the RMSE, which is more like the distance
				sim = Math.sqrt(sim / commonItems);

				// Similarity should be between 0 and 1
				// For the value 0, the two users are as dissimilar as they come
				// For the value 1, their preferences (based on the available
				// data) are identical.
				//
				// Here is a function that accomplishes exactly that
				sim = 1.0d - Math.tanh(sim);
			}

			break;

		// ---------------------------------------------------------
		case 1:
			for (Rating r : this.ratingsByItemId.values()) {
				for (Rating r2 : u.ratingsByItemId.values()) {

					// Find the same item
					if (r.getItemId() == r2.getItemId()) {
						commonItems++;
						sim += Math.pow((r.getRating() - r2.getRating()), 2);
					}
				}
			}

			// If there are not common items, we cannot tell whether
			// the users are similar or not. So, we let it return 0.
			if (commonItems > 0) {
				// Same as before (case 0)
				sim = Math.sqrt(sim / commonItems);

				// Similarity should be between 0 and 1
				// For the value 0, the two users are as disimilar as they come
				// For the value 1, their preferences (based on the available
				// data) are identical.
				//
				// Here is a function that accomplishes exactly that
				sim = 1.0d - Math.tanh(sim);

				// However, the above calculation takes into account only the
				// common items
				// It does not account for the number of items that could have
				// in common
				// So, let us consider the following

				// This is the maximum number of items that the two users can
				// have in common
				int maxCommonItems = Math.min(this.ratingsByItemId.size(),
						u.ratingsByItemId.size());

				// Adjust the similarity to account for the importance of the
				// common terms
				// through the ratio of the common items over the number of all
				// possible common items

				sim = sim * ((double) commonItems / (double) maxCommonItems);
			}

			break;
		}

		// Let us know what it is
		System.out.print("\n"); // Just for pretty printing in the Shell
		System.out.print(" User Similarity between");
		System.out.print(" " + this.getName());
		System.out.print(" and " + u.getName());
		System.out.println(" is equal to " + sim);
		System.out.print("\n"); // Just for pretty printing in the Shell

		return sim;
	}

	public void plot() {

		int n = this.ratingsByItemId.size();

		double[] x = new double[n];
		double[] y = new double[n];

		double xCount = 0;
		int i;
		for (Integer itemId : this.ratingsByItemId.keySet()) {
			i = (int) xCount;
			x[i] = xCount;
			y[i] = this.getItemRating(itemId).getRating();
		}

		XyGui gui = new XyGui("", x, y);
		gui.plot();
	}

	public void plot(MusicUser anotherUser) {
		// ratings for items rated by both users
		List<Rating[]> sharedRatings = new ArrayList<Rating[]>();

		// iterate through user ratings and check if another user rated the same
		// items
		for (Rating r : ratingsByItemId.values()) {
			Rating anotherUserRating = anotherUser.getItemRating(r.getItemId());
			if (anotherUserRating != null) {
				// item was rated by both users. Add both ratings to the list
				Rating[] itemRatings = new Rating[2];
				itemRatings[0] = r;
				itemRatings[1] = anotherUserRating;
				sharedRatings.add(itemRatings);
			}
		}

		// sort shared ratings based on the difference of opinions
		Collections.sort(sharedRatings, new Comparator<Rating[]>() {
			public int compare(Rating[] x, Rating[] y) {
				int result = 0;

				double xDiff = Math.abs(x[0].getRating() - x[1].getRating());
				double yDiff = Math.abs(y[0].getRating() - y[1].getRating());

				if (xDiff < yDiff) {
					result = -1;
				} else if (xDiff > yDiff) {
					result = 1;
				} else {
					result = 0;
				}

				return result;
			}
		});

		double[] data1 = new double[sharedRatings.size()];
		double[] data2 = new double[sharedRatings.size()];
		String[] itemNames = new String[sharedRatings.size()];
		for (int i = 0, n = itemNames.length; i < n; i++) {
			Rating[] itemRatings = sharedRatings.get(i);
			// Right now there is no way to get to Item from User or Rating.
			// Only itemId is available from User or Rating instance.
			// I'll change loading to include Item in Rating if we need to show
			// song name on the chart.
			itemNames[i] = String.valueOf(itemRatings[0].getItemId());
			data1[i] = itemRatings[0].getRating();
			data2[i] = itemRatings[1].getRating();
		}

		XyGui gui = new XyGui("User Similarity", this.getName(),
				anotherUser.getName(), itemNames, data1, data2);

		gui.plot();
	}

}
