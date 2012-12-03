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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.model.SimilarItem;
import org.yooreeka.algos.reco.collab.model.SimilarUser;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.algos.reco.collab.similarity.naive.SimilarityMatrix;
import org.yooreeka.algos.reco.collab.similarity.util.SimilarityMatrixRepository;
import org.yooreeka.config.YooreekaConfigurator;

/**
 * Recommender. Has to be initialized with similarity function and data.
 *
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class Delphi implements Recommender {

	private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.50;
	private static final double MAX_RATING = 5;
	private static final Logger LOG = Logger.getLogger(Delphi.class.getName());

	private RecommendationType type;
	private SimilarityMatrix similarityMatrix;
	private Dataset dataSet;
	private boolean verbose = true;
	private double similarityThreshold = DEFAULT_SIMILARITY_THRESHOLD;
	private Map<Integer, Double> maxPredictedRating;

	public Delphi(Dataset dataSet, 
			      RecommendationType type) {

		this(dataSet,type,false);		
	}

	public Delphi(Dataset dataSet, 
			      RecommendationType type,	
			      boolean useSimilarityCache) {
		
		this(dataSet,type,useSimilarityCache,null);
		
		SimilarityMatrixRepository smRepo = new SimilarityMatrixRepository(useSimilarityCache);
		setSimilarityMatrix(smRepo.load(type, dataSet));
	}

	public Delphi(Dataset dataSet, 
				  RecommendationType type,
				  boolean useSimilarityCache,
				  SimilarityMatrix similarityMatrix) {

		LOG.setLevel(YooreekaConfigurator.getLevel(Delphi.class.getName()));
		
		this.type = type;

		this.dataSet = dataSet;
		maxPredictedRating = new HashMap<Integer, Double>(dataSet.getUserCount() / 2);
		
		this.similarityMatrix = similarityMatrix;
	}

	
	// --------------------------------------------------------------------
	// USER BASED SIMILARITY
	// --------------------------------------------------------------------

	private double estimateItemBasedRating(User user, Item item) {

		double estimatedRating;

		if (item != null && user != null) {

			estimatedRating = item.getAverageRating();

		} else {
			if (item == null && user == null) {
				throw new IllegalArgumentException(
						"At least, one of the arguments must not be null!");
			} else {
				return 3.0d;
			}
		}

		int itemId = item.getId();
		int userId = user.getId();
		double similaritySum = 0.0;
		double weightedRatingSum = 0.0;

		// check if the user has already rated the item
		Rating existingRatingByUser = user.getItemRating(item.getId());

		if (existingRatingByUser != null) {

			estimatedRating = existingRatingByUser.getRating();

		} else {

			double similarityBetweenItems = 0;
			double weightedRating = 0;

			for (Item anotherItem : dataSet.getItems()) {

				// only consider items that were rated by the user
				Rating anotherItemRating = anotherItem.getUserRating(userId);

				if (anotherItemRating != null) {

					similarityBetweenItems = similarityMatrix.getValue(itemId,
							anotherItem.getId());

					if (similarityBetweenItems > similarityThreshold) {

						weightedRating = similarityBetweenItems
								* anotherItemRating.getRating();

						weightedRatingSum += weightedRating;
						similaritySum += similarityBetweenItems;
					}
				}
			}

			if (similaritySum > 0.0) {

				estimatedRating = weightedRatingSum / similaritySum;
			}
		}

		return estimatedRating;
	}

	// -----------------------------------------------------------
	// PRIVATE (AUXILIARY) METHODS
	// -----------------------------------------------------------
	private double estimateUserBasedRating(User user, Item item) {

		double estimatedRating = user.getAverageRating();

		int itemId = item.getId();
		int userId = user.getId();

		double similaritySum = 0.0;
		double weightedRatingSum = 0.0;

		// check if user has already rated this item
		Rating existingRatingByUser = user.getItemRating(item.getId());

		if (existingRatingByUser != null) {

			estimatedRating = existingRatingByUser.getRating();

		} else {
			for (User anotherUser : dataSet.getUsers()) {

				Rating itemRating = anotherUser.getItemRating(itemId);

				// only consider users that rated this item
				if (itemRating != null) {

					/**
					 * @todo describe how this generalizes to more accurate
					 *       similarities
					 */
					double similarityBetweenUsers = similarityMatrix.getValue(
							userId, anotherUser.getId());

					double ratingByNeighbor = itemRating.getRating();

					double weightedRating = similarityBetweenUsers
							* ratingByNeighbor;

					weightedRatingSum += weightedRating;
					similaritySum += similarityBetweenUsers;
				}
			}

			if (similaritySum > 0.0) {
				estimatedRating = weightedRatingSum / similaritySum;
			}
		}

		return estimatedRating;
	}

	private List<SimilarUser> findFriendsBasedOnUserSimilarity(User user) {

		List<SimilarUser> similarUsers = new ArrayList<SimilarUser>();

		for (User friend : dataSet.getUsers()) {

			if (user.getId() != friend.getId()) {

				double similarity = similarityMatrix.getValue(user.getId(),
						friend.getId());
				similarUsers.add(new SimilarUser(friend, similarity));
			}
		}

		return similarUsers;
	}

	// --------------------------------------------------------------------
	// ITEM BASED SIMILARITY
	// --------------------------------------------------------------------

	private List<SimilarItem> findItemsBasedOnItemSimilarity(Item item) {

		List<SimilarItem> similarItems = new ArrayList<SimilarItem>();

		int itemId = item.getId();

		for (Item sItem : dataSet.getItems()) {

			if (itemId != sItem.getId()) {

				double similarity = similarityMatrix.getValue(itemId,
						sItem.getId());
				if (similarity > 0.0) {
					similarItems.add(new SimilarItem(sItem, similarity));
				}
			}
		}

		return similarItems;
	}

	public SimilarItem[] findSimilarItems(Item item) {
		return findSimilarItems(item, 5);
	}

	public SimilarItem[] findSimilarItems(Item item, int topN) {

		List<SimilarItem> similarItems = new ArrayList<SimilarItem>();

		if (!isUserBased()) {

			similarItems = findItemsBasedOnItemSimilarity(item);

		} else {

			LOG.warning("Finding similar items based on User similarity is not supported!");
		}

		SimilarItem[] topSimilarItems = SimilarItem.getTopSimilarItems(
				similarItems, topN);

		if (verbose) {
			SimilarItem.printItems(topSimilarItems,
					"Items like item " + item.getName() + ":");
		}

		return topSimilarItems;
	}

	public SimilarUser[] findSimilarUsers(User user) {
		SimilarUser[] topFriends = findSimilarUsers(user, 5);

		if (verbose) {
			SimilarUser.print(topFriends,
					"Top Friends for user " + user.getName() + ":");
		}

		return topFriends;
	}

	public SimilarUser[] findSimilarUsers(User user, int topN) {

		List<SimilarUser> similarUsers = new ArrayList<SimilarUser>();

		if (isUserBased()) {

			similarUsers = findFriendsBasedOnUserSimilarity(user);

		} else {

			/**
			 * TODO: 3.x: Create an algorithm that would allow you to find
			 * similar users based on item similarities. What kind of results do
			 * you get? Is it space efficient? How about execution time?
			 */
			LOG.warning("Finding friends based on Item similarity is not supported!");
		}

		return SimilarUser.getTopNFriends(similarUsers, topN);
	}

	/**
	 * @return recommender's dataset.
	 */
	public Dataset getDataset() {
		return this.dataSet;
	}

	/**
	 * @return the maxPredictedRating of a particular user
	 */
	public double getMaxPredictedRating(Integer uID) {
		Double maxPR = maxPredictedRating.get(uID);

		return (maxPR == null) ? 5.0d : maxPR;
	}

	// --------------------------------------------------------------------
	// RATING PREDICTIONS
	// --------------------------------------------------------------------

	public double getSimilarity(Item i1, Item i2) {

		double sim = similarityMatrix.getValue(i1.getId(), i2.getId());

		if (verbose) {
			System.out.print("Item similarity between");
			System.out.print(" ItemID: " + i1.getId());
			System.out.print(" and");
			System.out.print(" ItemID: " + i2.getId());
			System.out.println(" is equal to " + sim);
		}

		return sim;
	}

	public double getSimilarity(User u1, User u2) {

		double sim = similarityMatrix.getValue(u1.getId(), u2.getId());

		if (verbose) {
			System.out.print("User Similarity between");
			System.out.print(" UserID: " + u1.getId());
			System.out.print(" and");
			System.out.print(" UserID: " + u2.getId());
			System.out.println(" is equal to " + sim);
		}

		return sim;
	}

	// --------------------------------------------------------------------
	// AUXILIARY METHODS
	// --------------------------------------------------------------------

	public SimilarityMatrix getSimilarityMatrix() {
		return similarityMatrix;
	}

	public double getSimilarityThreshold() {
		return similarityThreshold;
	}

	public RecommendationType getType() {
		return type;
	}

	public double getUserItemSimilarity(User user, Item item) {

		if (!isUserItemBased()) {
			throw new IllegalStateException(
					"Not valid for current similarity type:" + type);
		}

		double sim = similarityMatrix.getValue(user.getId(), item.getId());

		if (verbose) {
			System.out.print("User Item Similarity between");
			System.out.print(" UserID: " + user.getId());
			System.out.print(" and");
			System.out.print(" ItemID: " + item.getId());
			System.out.println(" is equal to " + sim);
		}

		return sim;
	}

	private boolean isContentBased() {
		return type.toString().indexOf("CONTENT") >= 0;
	}

	private boolean isUserBased() {
		return type.toString().indexOf("USER") >= 0
				&& type.toString().indexOf("USER_ITEM") < 0;
	}

	private boolean isUserItemBased() {
		return type.toString().indexOf("USER_ITEM") >= 0;
	}

	public boolean isVerbose() {
		return verbose;
	}

	@Override
	public double predictBasedOnItemAverage(Item item) {
		return item.getAverageRating();
	}

	@Override
	public double predictBasedOnUserAverage(User user) {
		return user.getAverageRating();
	}

	public double predictRating(int userId, int itemId) {
		return predictRating(dataSet.getUser(userId), dataSet.getItem(itemId));
	}

	public double predictRating(User user, Item item) {
		switch (type) {
		case USER_BASED:
			return estimateUserBasedRating(user, item);
		case IMPROVED_USER_BASED:
			return estimateUserBasedRating(user, item);
		case ITEM_BASED:
			return estimateItemBasedRating(user, item);
		case ITEM_PENALTY_BASED:
			return estimateItemBasedRating(user, item);
		case USER_CONTENT_BASED:
			throw new IllegalStateException(
					"Not valid for current similarity type:" + type);
		case ITEM_CONTENT_BASED:
			throw new IllegalStateException(
					"Not valid for current similarity type:" + type);
		case USER_ITEM_CONTENT_BASED:
			// Using similarity between User and Item
			return MAX_RATING
					* similarityMatrix.getValue(user.getId(), item.getId());
		}

		throw new RuntimeException("Unknown recommendation type:" + type);
	}

	public List<PredictedItemRating> recommend(Integer userId) {
		return recommend(dataSet.getUser(userId));
	}

	// --------------------------------------------------------------------
	// RECOMMENDATIONS
	// --------------------------------------------------------------------
	public List<PredictedItemRating> recommend(User user) {
		List<PredictedItemRating> recommendedItems = recommend(user, 5);
		return recommendedItems;
	}

	public List<PredictedItemRating> recommend(User user, int topN) {

		List<PredictedItemRating> recommendations = new ArrayList<PredictedItemRating>();

		double maxRating = -1.0d;

		for (Item item : dataSet.getItems()) {

			// only consider items that user hasn't rated yet or doesn't own the
			// content
			if (!skipItem(user, item)) {
				double predictedRating = predictRating(user, item);

				if (maxRating < predictedRating) {
					maxRating = predictedRating;
				}

				if (!Double.isNaN(predictedRating)) {
					recommendations.add(new PredictedItemRating(user.getId(),
							item.getId(), predictedRating));
				}
			} else {
				if (verbose) {
					System.out.println("Skipping item:" + item.getName());
				}
			}
		}

		this.maxPredictedRating.put(user.getId(), maxRating);

		List<PredictedItemRating> topNRecommendations = PredictedItemRating
				.getTopNRecommendations(recommendations, topN);

		if (verbose) {
			PredictedItemRating.printUserRecommendations(user, dataSet,
					topNRecommendations);
		}

		return topNRecommendations;
	}

	public void setSimilarityMatrix(SimilarityMatrix similarityMatrix) {
		this.similarityMatrix = similarityMatrix;
	}

	public void setSimilarityThreshold(double similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	private boolean skipItem(User user, Item item) {
		boolean skipItem = true;
		if (isContentBased()) {
			if (user.getUserContent(item.getItemContent().getId()) == null) {
				skipItem = false;
			}
		} else {
			if (user.getItemRating(item.getId()) == null) {
				skipItem = false;
			}
		}
		return skipItem;
	}

}
