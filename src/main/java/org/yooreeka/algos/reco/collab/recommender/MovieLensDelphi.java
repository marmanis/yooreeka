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

import java.util.ArrayList;
import java.util.List;

import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.SimilarItem;
import org.yooreeka.algos.reco.collab.model.SimilarUser;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.algos.reco.collab.similarity.movielens.MovieLensItemSimilarity;

/**
 * Recommender. Has to be initialized with similarity function and dataset.
 */
public class MovieLensDelphi implements Recommender {

	private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.35;

	private Dataset dataSet;
	private boolean verbose = true;
	private double similarityThreshold = DEFAULT_SIMILARITY_THRESHOLD;

	private MovieLensItemSimilarity itemSimilarityMatrix;

	public MovieLensDelphi(Dataset ds) {
		System.out.println("Entering MovieLensDelphi(Dataset) constructor ...");

		this.dataSet = ds;

		// ------------------------------------------------------------------------
		System.out.println("Calculating item based similarities...");
		long start = System.currentTimeMillis();

		itemSimilarityMatrix = new MovieLensItemSimilarity(ds);

		System.out.println("Item based similarities calculated in "
				+ (System.currentTimeMillis() - start) / 1000 + "(sec).");
		System.out.println("Similarities ready.");
		// ------------------------------------------------------------------------

		System.out.println("Leaving MovieLensDelpi(Dataset) constructor ...");
	}

	// --------------------------------------------------------------------
	// USER BASED SIMILARITY
	// --------------------------------------------------------------------

	// public SimilarUser[] findSimilarUsers(User user) {
	// SimilarUser[] topFriends = findSimilarUsers(user, 5);
	//
	// if( verbose ) {
	// SimilarUser.print(topFriends, "Top Friends for user " + user.getName() +
	// ":");
	// }
	//
	// return topFriends;
	// }
	//
	// public SimilarUser[] findSimilarUsers(User user, int topN) {
	//
	// List<SimilarUser> similarUsers = new ArrayList<SimilarUser>();
	//
	// similarUsers = findFriendsBasedOnUserSimilarity(user);
	//
	// System.out.println("Finding friends based on Item similarity is not supported!");
	//
	// return SimilarUser.getTopNFriends(similarUsers, topN);
	// }
	//
	//
	// private List<SimilarUser> findFriendsBasedOnUserSimilarity(User user) {
	//
	// List<SimilarUser> similarUsers = new ArrayList<SimilarUser>();
	//
	// for(User friend : dataSet.getUsers()) {
	//
	// if( user.getId() != friend.getId() ) {
	//
	// double similarity =
	// userSimilarityMatrix.getValue(user.getId(), friend.getId());
	// similarUsers.add(new SimilarUser(friend, similarity));
	// }
	// }
	//
	// return similarUsers;
	// }

	// --------------------------------------------------------------------
	// ITEM BASED SIMILARITY
	// --------------------------------------------------------------------

	// -----------------------------------------------------------
	// PRIVATE (AUXILIARY) METHODS
	// -----------------------------------------------------------
	private double estimateItemBasedRating(User user, Item item) {

		double itemRating = item.getAverageRating();

		int itemId = item.getId();
		int userId = user.getId();

		double itemAvgRating = item.getAverageRating();

		double weightedDeltaSum = 0.0;
		int sumN = 0;

		// check if the user has already rated the item
		Rating existingRatingByUser = user.getItemRating(item.getId());

		if (existingRatingByUser != null) {

			itemRating = existingRatingByUser.getRating();

		} else {

			double similarityBetweenItems = 0;
			double weightedDelta = 0;
			double delta = 0;

			for (Item anotherItem : dataSet.getItems()) {

				// only consider items that were rated by the user
				Rating anotherItemRating = anotherItem.getUserRating(userId);

				if (anotherItemRating != null) {

					delta = itemAvgRating - anotherItemRating.getRating();

					similarityBetweenItems = itemSimilarityMatrix.getValue(
							itemId, anotherItem.getId());

					if (Math.abs(similarityBetweenItems) > similarityThreshold) {

						weightedDelta = similarityBetweenItems * delta;

						weightedDeltaSum += weightedDelta;

						sumN++;
					}
				}
			}

			if (sumN > 0) {
				itemRating = itemAvgRating - (weightedDeltaSum / sumN);
			}
		}

		return itemRating;
	}

	private List<SimilarItem> findItemsBasedOnItemSimilarity(Item item) {

		List<SimilarItem> similarItems = new ArrayList<SimilarItem>();

		int itemId = item.getId();

		for (Item sItem : dataSet.getItems()) {

			if (itemId != sItem.getId()) {

				double similarity = itemSimilarityMatrix.getValue(itemId,
						sItem.getId());
				if (similarity > 0.5) {
					similarItems.add(new SimilarItem(sItem, similarity));
				}
			}
		}

		return similarItems;
	}

	public SimilarItem[] findSimilarItems(Item item) {
		SimilarItem[] topFriends = findSimilarItems(item, 5);

		if (verbose) {
			SimilarItem.printItems(topFriends,
					"Items like item " + item.getName() + ":");
		}
		return topFriends;
	}

	public SimilarItem[] findSimilarItems(Item item, int topN) {

		List<SimilarItem> similarItems = new ArrayList<SimilarItem>();

		similarItems = findItemsBasedOnItemSimilarity(item);

		return SimilarItem.getTopSimilarItems(similarItems, topN);
	}

	public SimilarUser[] findSimilarUsers(User user) {
		throw new UnsupportedOperationException("Not supported.");
	}

	public SimilarUser[] findSimilarUsers(User user, int topN) {
		throw new UnsupportedOperationException("Not supported.");
	}

	public Dataset getDataset() {
		return dataSet;
	}

	// --------------------------------------------------------------------
	// AUXILIARY METHODS
	// --------------------------------------------------------------------
	public double getSimilarityThreshold() {
		return similarityThreshold;
	}

	public List<PredictedItemRating> getTopNRecommendations(
			List<PredictedItemRating> recommendations, int topN) {

		PredictedItemRating.sort(recommendations);

		double maxR = recommendations.get(0).getRating();
		double scaledR;

		List<PredictedItemRating> topRecommendations = new ArrayList<PredictedItemRating>();
		for (PredictedItemRating r : recommendations) {
			if (topRecommendations.size() >= topN) {
				// have enough recommendations.
				break;
			}
			if (maxR > 5) {
				scaledR = r.getRating() * (5 / maxR);
				r.setRating(scaledR);
			}

			topRecommendations.add(r);
		}

		return topRecommendations;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public double predictBasedOnItemAverage(Item item) {
		return item.getAverageRating();
	}

	public double predictBasedOnUserAverage(User user) {
		return user.getAverageRating();
	}

	public double predictRating(User user, Item item) {
		return estimateItemBasedRating(user, item);
	}

	// --------------------------------------------------------------------
	// RECOMMENDATIONS
	// --------------------------------------------------------------------
	public List<PredictedItemRating> recommend(User user) {
		List<PredictedItemRating> recommendedItems = recommend(user, 5);
		if (verbose) {
			PredictedItemRating.printUserRecommendations(user, dataSet,
					recommendedItems);
		}
		return recommendedItems;
	}

	public List<PredictedItemRating> recommend(User user, int topN) {

		List<PredictedItemRating> recommendations = new ArrayList<PredictedItemRating>();

		for (Item item : dataSet.getItems()) {

			// only consider items that user hasn't rated yet
			if (user.getItemRating(item.getId()) == null) {

				double predictedRating = estimateItemBasedRating(user, item);

				if (!Double.isNaN(predictedRating)) {
					recommendations.add(new PredictedItemRating(user.getId(),
							item.getId(), predictedRating));
				}
			}
		}

		return getTopNRecommendations(recommendations, topN);
	}

	public void setSimilarityThreshold(double similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
