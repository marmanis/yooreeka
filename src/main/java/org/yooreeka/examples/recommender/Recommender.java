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
import java.util.List;

import org.yooreeka.algos.reco.collab.data.MovieLensDataset;
import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.recommender.Delphi;
import org.yooreeka.algos.reco.collab.recommender.PredictedItemRating;

/**
 * @deprecated not used at the moment.
 */
@Deprecated
class Recommender {

	// private static final Logger logger = Logger.getLogger(Recommender.class);

	public static void main(String[] args) throws Exception {
		Recommender m = new Recommender(args[0]);
		boolean useSimilarityCacheWhenAvailable = true;
		m.recommendOnMovieLens(useSimilarityCacheWhenAvailable);
	}

	private Dataset dataset;

	private Recommender(String dataDir) {
		// Load MovieLens dataset
		File users = new File(dataDir, MovieLensDataset.USERS_FILENAME);
		File items = new File(dataDir, MovieLensDataset.ITEMS_FILENAME);
		File ratings = new File(dataDir, MovieLensDataset.RATINGS_FILENAME);
		this.dataset = new MovieLensDataset("MovieLensDataset", users, items,
				ratings);
	}

	private void printFirstN(List<PredictedItemRating> sortedRecommendations,
			int printNum) {
		for (int i = 0, n = sortedRecommendations.size(); i < n && i < printNum; i++) {
			System.out.println(sortedRecommendations.get(i));
		}
	}

	private void printMinMax(List<PredictedItemRating> c) {
		int minId = 0;
		double minIdRating = 6.0;
		int maxId = 0;
		double maxIdRating = 0.0;
		for (PredictedItemRating r : c) {
			if (r.getRating() < minIdRating) {
				minId = r.getItemId();
				minIdRating = r.getRating();
			}
			if (r.getRating() > maxIdRating) {
				maxId = r.getItemId();
				maxIdRating = r.getRating();
			}
		}
		System.out.println("minId=" + minId + ",minIdRating=" + maxIdRating
				+ ",maxId=" + maxId + ",maxIdRating=" + maxIdRating);
	}

	private void recommendOnMovieLens(boolean useSimilarityCache)
			throws Exception {

		long start = System.currentTimeMillis();
		Delphi delphi = new Delphi(dataset,
				RecommendationType.ITEM_PENALTY_BASED, useSimilarityCache);
		System.out.println("Time:" + (System.currentTimeMillis() - start)
				/ 1000 + "(sec)");
		List<PredictedItemRating> r = delphi.recommend(4);
		System.out.println("4: size: " + r.size());
		printMinMax(r);
		printFirstN(r, 3);
		r = delphi.recommend(3);
		System.out.println("3: size: " + r.size());
		printMinMax(r);
		printFirstN(r, 3);
		r = delphi.recommend(100);
		System.out.println("100: size: " + r.size());
		printMinMax(r);
		printFirstN(r, 3);
		r = delphi.recommend(50);
		System.out.println("50: size: " + r.size());
		printMinMax(r);
		printFirstN(r, 3);
	}

}
