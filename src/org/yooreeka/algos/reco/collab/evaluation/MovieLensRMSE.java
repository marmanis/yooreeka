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

import java.io.File;
import java.util.List;

import org.yooreeka.algos.reco.collab.data.MovieLensDataset;
import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.recommender.Delphi;
import org.yooreeka.config.YooreekaConfigurator;

/**
 * 
 * @deprecated use <code>RMSEEstimator</code> instead.
 */
public class MovieLensRMSE {

	public static void main(String[] args) {
		MovieLensRMSE rmse = new MovieLensRMSE();
		rmse.calculate();
	}

	public MovieLensRMSE() {
	}

	public double[] calculate() {

		double similarityThreshold = 0.50;

		int N = 5;

		double[] rmse = new double[N];

		RMSEEstimator rmseEstimator = new RMSEEstimator();

		for (int i = 1; i <= N; i++) {

			Dataset ds = createTrainingDataset(i);

			Delphi delphi = new Delphi(ds, RecommendationType.ITEM_BASED);
			delphi.setSimilarityThreshold(similarityThreshold);

			List<Rating> testRatings = createTestRatings(i);

			double rmseValue = rmseEstimator.calculateRMSE(delphi, testRatings);
			System.out.println(i + ": rmse = " + rmseValue);

			rmse[i - 1] = rmseValue;
		}

		return rmse;
	}

	public List<Rating> createTestRatings(int n) {
		String dataDir = YooreekaConfigurator
				.getProperty("yooreeka.movielens.data.dir");

		File ratings = new File(dataDir, "u" + n + ".test");

		return MovieLensDataset.loadRatings(ratings);
	}

	public MovieLensDataset createTrainingDataset(int n) {
		String dataDir = YooreekaConfigurator
				.getProperty("yooreeka.movielens.data.dir");

		File users = new File(dataDir, MovieLensDataset.USERS_FILENAME);
		File items = new File(dataDir, MovieLensDataset.ITEMS_FILENAME);
		File ratings = new File(dataDir, "u" + n + ".base");

		return new MovieLensDataset(users, items, ratings);
	}
}
