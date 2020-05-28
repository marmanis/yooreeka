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
package org.yooreeka.algos.reco.collab.similarity.movielens;

import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.algos.reco.collab.similarity.naive.SimilarityMatrixImpl;
import org.yooreeka.algos.reco.collab.similarity.util.PearsonCorrelation;

public class MovieLensUserSimilarity extends SimilarityMatrixImpl {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 8510536889333771002L;

	public MovieLensUserSimilarity(Dataset ds) {
		this(MovieLensUserSimilarity.class.getSimpleName(), ds);
	}

	public MovieLensUserSimilarity(String id, Dataset ds) {
		this.id = id;
		this.useObjIdToIndexMapping = ds.isIdMappingRequired();
		calculate(ds);
	}

	@Override
	protected void calculate(Dataset dataSet) {

		int nUsers = dataSet.getUserCount();

		similarityValues = new double[nUsers][nUsers];

		// if we want to use mapping from userId to index then generate
		// index for every userId
		if (useObjIdToIndexMapping) {
			for (User u : dataSet.getUsers()) {
				idMapping.getIndex(String.valueOf(u.getId()));
			}
		}

		for (int u = 0; u < nUsers; u++) {

			int userAId = getObjIdFromIndex(u);
			User userA = dataSet.getUser(userAId);

			for (int v = u; v < nUsers; v++) {
				int userBId = getObjIdFromIndex(v);
				User userB = dataSet.getUser(userBId);

				/* Collect shared ratings */
				Integer[] sharedItemIds = User.getSharedItems(userA, userB);

				if (sharedItemIds.length > 0) {
					double[] ratingsA = userA
							.getRatingsForItemList(sharedItemIds);
					double[] ratingsB = userB
							.getRatingsForItemList(sharedItemIds);

					/* Center ratings by subtracting average */
					double avgA = userA.getAverageRating();
					double avgB = userB.getAverageRating();
					for (int i = 0; i < sharedItemIds.length; i++) {
						ratingsA[i] = ratingsA[i] - avgA;
						ratingsB[i] = ratingsB[i] - avgB;
					}

					/* Calculate similarity - Pearson Correlation */
					PearsonCorrelation pr = new PearsonCorrelation(ratingsA,
							ratingsB);

					similarityValues[u][v] = pr.calculate();
				} else {
					similarityValues[u][v] = 0.0;
				}
			}
		}
	}
}
