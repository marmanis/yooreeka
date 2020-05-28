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

import java.util.List;

import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.SimilarItem;
import org.yooreeka.algos.reco.collab.model.SimilarUser;
import org.yooreeka.algos.reco.collab.model.User;

/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public interface Recommender {

	public SimilarItem[] findSimilarItems(Item item);

	public SimilarItem[] findSimilarItems(Item item, int topN);

	// Similarities
	public SimilarUser[] findSimilarUsers(User user);

	public SimilarUser[] findSimilarUsers(User user, int topN);

	// Auxiliary
	public Dataset getDataset();

	public double getSimilarityThreshold();

	public double predictBasedOnItemAverage(Item item);

	public double predictBasedOnUserAverage(User user);

	// Predictions
	public double predictRating(User user, Item item);

	/**
	 * Returns recommendations for the user.
	 * 
	 * @param user
	 * @return recommended items with predicted ratings.
	 */
	public List<PredictedItemRating> recommend(User user);

	/**
	 * Returns top N recommendations for the user.
	 * 
	 * @param user
	 * @param topN
	 *            number of top recommendations to return.
	 * @return recommended items with predicted ratings.
	 */
	public List<PredictedItemRating> recommend(User user, int topN);

	public void setSimilarityThreshold(double similarityThreshold);
}
