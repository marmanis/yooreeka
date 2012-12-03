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
package org.yooreeka.algos.reco.collab.evaluation;

import java.util.Collection;
import java.util.logging.Logger;

import org.yooreeka.algos.reco.collab.data.MovieLensDataset;
import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.algos.reco.collab.recommender.Recommender;
import org.yooreeka.config.YooreekaConfigurator;

/**
 * Calculates Root Mean Squared Error for the recommender.
 */
public class RMSEEstimator {

	private static final Logger LOG = Logger.getLogger(RMSEEstimator.class.getName());

	public RMSEEstimator() {
		LOG.setLevel(YooreekaConfigurator.getLevel(RMSEEstimator.class.getName()));
	}

	/**
	 * Calculates Root Mean Squared Error for the recommender. Uses test rating
	 * values returned by recommender's dataset.
	 * 
	 * @param delphi
	 *            recommender.
	 * @return root mean squared error value.
	 */
	public double calculateRMSE(Recommender delphi) {

		MovieLensDataset ds = (MovieLensDataset) delphi.getDataset();
		Collection<Rating> testRatings = ds.getTestRatings();

		return calculateRMSE(delphi, testRatings);
	}

	/**
	 * Calculates Root Mean Squared Error for the recommender.
	 * 
	 * @param delphi
	 *            recommender to evaluate.
	 * @param testRatings
	 *            ratings that will be used to calculate the error.
	 * @return root mean squared error.
	 */
	public double calculateRMSE(Recommender delphi,
			Collection<Rating> testRatings) {

		double sum = 0.0;

		Dataset ds = delphi.getDataset();

		int totalSamples = testRatings.size();

		LOG.fine("Calculating RMSE ...");
		LOG.fine("Training ratings count: "	+ ds.getRatingsCount());
		LOG.fine("Test ratings count: " + testRatings.size());

		for (Rating r : testRatings) {
			User user = ds.getUser(r.getUserId());
			Item item = ds.getItem(r.getItemId());
			double predictedItemRating = delphi.predictRating(user, item);

			if (predictedItemRating > 5.0) {
				predictedItemRating = 5.0;
				LOG.finest("Predicted item rating: " + predictedItemRating);
			}
			LOG.finest(
			 "user: " + r.getUserId() +
			 ", item: " + r.getItemId() +
			 ", actual rating: " + r.getRating() +
			 ", predicted: " + String.valueOf(predictedItemRating));

			sum += Math.pow((predictedItemRating - r.getRating()), 2);

		}
		double rmse = Math.sqrt(sum / totalSamples);

		LOG.fine("RMSE:" + rmse);
		
		return rmse;
	}

	public void compareRMSEs(Recommender delphi) {

		MovieLensDataset ds = (MovieLensDataset) delphi.getDataset();
		Collection<Rating> testRatings = ds.getTestRatings();

		compareRMSEs(delphi, testRatings);
	}

	public void compareRMSEs(Recommender delphi, Collection<Rating> testRatings) {

		double sum = 0.0;
		double sumAvgItem = 0.0;
		double sumAvgUser = 0.0;

		Dataset ds = delphi.getDataset();

		int totalSamples = testRatings.size();

		LOG.fine("Calculating RMSE ...");
		LOG.fine("Training ratings count: "+ds.getRatingsCount());
		LOG.fine("Test ratings count: " + testRatings.size());

		for (Rating r : testRatings) {
			User user = ds.getUser(r.getUserId());
			Item item = ds.getItem(r.getItemId());
			double predictedItemRating = delphi.predictRating(user, item);
			double predictedAvgItemRating = delphi
					.predictBasedOnItemAverage(item);
			double predictedAvgUserRating = delphi
					.predictBasedOnUserAverage(user);

			if (predictedItemRating > 5.0) {
				predictedItemRating = 5.0;
				LOG.finest("Predicted item rating: " + predictedItemRating);
			}
			 LOG.finest(
			 "user: " + r.getUserId() +
			 ", item: " + r.getItemId() +
			 ", actual rating: " + r.getRating() +
			 ", predicted: " + String.valueOf(predictedItemRating));

			sum += Math.pow((predictedItemRating - r.getRating()), 2);
			sumAvgItem += Math.pow((predictedAvgItemRating - r.getRating()), 2);
			sumAvgUser += Math.pow((predictedAvgUserRating - r.getRating()), 2);

		}

		double rmse = Math.sqrt(sum / totalSamples);
		double rmseAvgItem = Math.sqrt(sumAvgItem / totalSamples);
		double rmseAvgUser = Math.sqrt(sumAvgUser / totalSamples);

		System.out.println("RMSE:" + rmse);
		System.out.println("RMSE (based on avg. Item rating):" + rmseAvgItem);
		System.out.println("RMSE (based on avg. User rating):" + rmseAvgUser);
	}
}
