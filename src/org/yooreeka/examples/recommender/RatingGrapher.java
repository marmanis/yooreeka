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
package org.yooreeka.examples.recommender;

import java.io.File;
import java.util.Collection;

import org.yooreeka.algos.reco.collab.data.MovieLensDataset;
import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.gui.XyGui;

public class RatingGrapher {

	private static Dataset getMovieLensData() {
		String dataDir = YooreekaConfigurator
				.getProperty(YooreekaConfigurator.MOVIELENS_DATA_DIR);
		File users = new File(dataDir, MovieLensDataset.USERS_FILENAME);
		File items = new File(dataDir, MovieLensDataset.ITEMS_FILENAME);
		File ratings = new File(dataDir, MovieLensDataset.RATINGS_FILENAME);
		Dataset ds = new MovieLensDataset("MovieLensDataset", users, items,
				ratings);
		return ds;
	}

	public static void main(String[] args) {
		// RatingGrapher.plotAverageItemRating();
		// RatingGrapher.plotAverageUserRating();
		RatingGrapher.plotRatingsDistribution();

	}

	/**
	 * Plots average item rating for MovieLens dataset.
	 */
	public static void plotAverageItemRating() {
		Dataset ds = getMovieLensData();
		Collection<Item> items = ds.getItems();
		double[] x = new double[items.size()];
		double[] y = new double[items.size()];
		int i = 0;
		for (Item item : items) {
			x[i] = item.getId();
			y[i] = item.getAverageRating();
			i++;
		}

		XyGui gui = new XyGui(ds.getName(), x, y);
		gui.plot();
	}

	/**
	 * Plots average user rating for MovieLens dataset.
	 */
	public static void plotAverageUserRating() {
		Dataset ds = getMovieLensData();
		Collection<User> users = ds.getUsers();
		double[] x = new double[users.size()];
		double[] y = new double[users.size()];
		int i = 0;
		for (User user : users) {
			x[i] = user.getId();
			y[i] = user.getAverageRating();
			i++;
		}

		XyGui gui = new XyGui(ds.getName(), x, y);
		gui.plot();
	}

	public static void plotNumberOfRatingsPerItem() {
		Dataset ds = getMovieLensData();
		Collection<Item> items = ds.getItems();
		double[] x = new double[items.size()];
		double[] y = new double[items.size()];
		int i = 0;
		for (Item item : items) {
			x[i] = item.getId();
			y[i] = item.getAllRatings().size();
			i++;
		}

		XyGui gui = new XyGui(ds.getName(), x, y);
		gui.plot();
	}

	public static void plotNumberOfRatingsPerUser() {
		Dataset ds = getMovieLensData();
		Collection<User> users = ds.getUsers();
		double[] x = new double[users.size()];
		double[] y = new double[users.size()];
		int i = 0;
		for (User user : users) {
			x[i] = user.getId();
			y[i] = user.getAllRatings().size();
			i++;
		}

		XyGui gui = new XyGui(ds.getName(), x, y);
		gui.plot();
	}

	public static void plotRatingsDistribution() {
		Dataset ds = getMovieLensData();
		plotRatingsDistribution(
				"Ratings for all items by all users, n=" + ds.getRatingsCount(),
				ds.getRatings());
	}

	private static void plotRatingsDistribution(String plotName,
			Collection<Rating> ratings) {
		double[] x = { 1, 2, 3, 4, 5 };
		double[] y = { 0.0, 0.0, 0.0, 0.0, 0.0 };

		if (ratings != null && ratings.size() > 0) {
			for (Rating r : ratings) {
				y[r.getRating() - 1]++;
			}

			int nRatings = ratings.size();
			for (int i = 0, n = x.length; i < n; i++) {
				y[i] = y[i] / nRatings;
			}
		}
		XyGui gui = new XyGui(plotName, x, y);
		gui.plot();
	}

	public static void plotRatingsDistributionForItem(int itemId) {
		Dataset ds = getMovieLensData();
		Collection<Rating> ratings = ds.getItem(itemId).getAllRatings();
		plotRatingsDistribution("Ratings distribution for item: " + itemId
				+ ", n=" + ratings.size(), ratings);
	}

	public static void plotRatingsDistributionForUser(int userId) {
		Dataset ds = getMovieLensData();
		Collection<Rating> ratings = ds.getUser(userId).getAllRatings();
		plotRatingsDistribution("Ratings distribution for user: " + userId
				+ ", n=" + ratings.size(), ratings);
	}

}
