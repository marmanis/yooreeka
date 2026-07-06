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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yooreeka.algos.reco.collab.data.BaseDataset;
import org.yooreeka.algos.reco.collab.data.DiggData;
import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.model.SimilarUser;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.config.YooreekaConfigurator;

public class DiggDelphi {

	public static void main(String[] args) {
		BaseDataset ds = DiggData.loadData(YooreekaConfigurator.getHome() + "/data/ch03/digg_stories.csv");
		User user = ds.getUser(1);
		DiggDelphi delphi = new DiggDelphi(ds);
		delphi.recommend(user);
	}

	private Dataset ds;
	private Delphi delphiUC;
	private Delphi delphiUIC;
	private Delphi delphiUR;

	private Delphi delphiIR;

	private boolean verbose = true;

	public DiggDelphi(Dataset ds) {
		this.ds = ds;

		delphiUC = new Delphi(ds, RecommendationType.USER_CONTENT_BASED);

		delphiUIC = new Delphi(ds, RecommendationType.USER_ITEM_CONTENT_BASED);

		delphiUR = new Delphi(ds, RecommendationType.USER_BASED);

		delphiIR = new Delphi(ds, RecommendationType.ITEM_BASED);

		if (verbose) {
			System.out
					.println("Initialized " + this.getClass().getSimpleName());
		}
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

		SimilarUser[] simU = delphiUC.findSimilarUsers(user, topN);
		similarUsers.addAll(Arrays.asList(simU));

		simU = delphiUR.findSimilarUsers(user, topN);
		similarUsers.addAll(Arrays.asList(simU));
		// SimilarUser.print(simU, "Top Friends for user " + user.getName() +
		// ":");

		return SimilarUser.getTopNFriends(similarUsers, topN);
	}

	public List<PredictedItemRating> naiveRecommend(User user, int topN) {
		List<PredictedItemRating> recommendations = new ArrayList<PredictedItemRating>();

		recommendations.addAll(delphiUIC.recommend(user, topN));
		recommendations.addAll(delphiUR.recommend(user, topN));
		recommendations.addAll(delphiIR.recommend(user, topN));

		return PredictedItemRating
				.getTopNRecommendations(recommendations, topN);
	}

	public List<PredictedItemRating> recommend(User user) {
		List<PredictedItemRating> recommendedItems = recommend(user, 5);
		if (verbose) {
			PredictedItemRating.printUserRecommendations(user, ds,
					recommendedItems);
		}
		return recommendedItems;
	}

	public List<PredictedItemRating> recommend(User user, int topN) {
		List<PredictedItemRating> recommendations = new ArrayList<PredictedItemRating>();

		// Establish a relative scaling factor
		double maxR = -1.0d;

		// Get the maximum predicted ratings from each recommender
		double maxRatingDelphiUIC = delphiUIC.getMaxPredictedRating(user
				.getId());
		double maxRatingDelphiUR = delphiUR.getMaxPredictedRating(user.getId());
		double maxRatingDelphiIR = delphiIR.getMaxPredictedRating(user.getId());

		// Find the maximum predicted rating across all recommendations
		double[] sortedMaxR = { maxRatingDelphiUIC, maxRatingDelphiUR,
				maxRatingDelphiIR };

		Arrays.sort(sortedMaxR);

		maxR = sortedMaxR[2]; // This is the maximum predicted rating

		// auxiliary variable
		double scaledRating = 1.0d;

		// Recommender 1 -- User-to-Item content based
		double scaling = maxR / maxRatingDelphiUIC;

		// Set an ad hoc threshold and scale it
		double scaledThreshold = 0.5 * scaling;

		List<PredictedItemRating> uicList = new ArrayList<PredictedItemRating>(
				topN);
		uicList = delphiUIC.recommend(user, topN);

		for (PredictedItemRating pR : uicList) {

			scaledRating = pR.getRating(6) * scaling;

			if (scaledRating < scaledThreshold) {
				uicList.remove(pR);
			} else {
				pR.setRating(scaledRating);
			}
		}

		// Recommender 2 -- User based collaborative filtering
		scaling = maxR / maxRatingDelphiUR;
		scaledThreshold = 0.5 * scaling;

		List<PredictedItemRating> urList = new ArrayList<PredictedItemRating>(
				topN);
		urList = delphiUR.recommend(user, topN);

		for (PredictedItemRating pR : urList) {

			scaledRating = pR.getRating(6) * scaling;

			if (scaledRating < scaledThreshold) {
				urList.remove(pR);
			} else {
				pR.setRating(scaledRating);
			}
		}

		// Recommender 3 -- Item based collaborative filtering
		scaling = maxR / maxRatingDelphiIR;
		scaledThreshold = 0.5 * scaling;

		List<PredictedItemRating> irList = new ArrayList<PredictedItemRating>(
				topN);
		irList = delphiIR.recommend(user, topN);

		for (PredictedItemRating pR : irList) {

			scaledRating = pR.getRating(6) * scaling;

			if (scaledRating < scaledThreshold) {
				irList.remove(pR);
			} else {
				pR.setRating(scaledRating);
			}
		}

		/*
		 * At this point, uicList, urList, and irList contain ratings that are
		 * scaled and exceed the threshold value.
		 */
		double uicRating = 0;
		double urRating = 0;
		double irRating = 0;
		double vote = 0;

		// build a set of items produced by all recommenders
		Set<Integer> allRecommendedItems = new HashSet<Integer>();
		for (PredictedItemRating pir : urList) {
			allRecommendedItems.add(pir.getItemId());
		}
		for (PredictedItemRating pir : irList) {
			allRecommendedItems.add(pir.getItemId());
		}
		for (PredictedItemRating pir : uicList) {
			allRecommendedItems.add(pir.getItemId());
		}

		for (Integer itemId : allRecommendedItems) {
			// Initialize
			uicRating = 0;
			urRating = 0;
			irRating = 0;
			vote = 0;

			for (PredictedItemRating uic : urList) {
				if (itemId == uic.getItemId()) {
					uicRating = uic.getRating(6);
				}
			}

			for (PredictedItemRating ur : urList) {
				if (itemId == ur.getItemId()) {
					urRating = ur.getRating(6);
				}
			}

			for (PredictedItemRating ir : irList) {
				if (itemId == ir.getItemId()) {
					irRating = ir.getRating(6);
				}
			}

			vote = (uicRating + urRating + irRating) / 3.0d;

			recommendations.add(new PredictedItemRating(user.getId(), itemId,
					vote));
		}

		rescale(recommendations, maxR);

		return PredictedItemRating
				.getTopNRecommendations(recommendations, topN);
	}

	private void rescale(List<PredictedItemRating> recommendations,
			double scaleRange) {
		int n = recommendations.size();
		double[] ratings = new double[n];
		int i = 0;
		for (PredictedItemRating pir : recommendations) {
			ratings[i] = pir.getRating(6);
			i++;
		}
		Arrays.sort(ratings);
		for (PredictedItemRating pir : recommendations) {
			pir.setRating(pir.getRating(6) * (scaleRange / ratings[n - 1]));
		}
	}

}
