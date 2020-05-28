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
package org.yooreeka.algos.reco.collab.similarity.naive;

import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.util.internet.crawling.util.ValueToIndexMapping;
import org.yooreeka.util.metrics.CosineSimilarityMeasure;

/**
 * Similarity between users based on the content associated with users.
 */
public class UserItemContentBasedSimilarity extends SimilarityMatrixImpl {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = -372816966539384847L;

	private ValueToIndexMapping idMappingForUser = new ValueToIndexMapping();
	private ValueToIndexMapping idMappingForItem = new ValueToIndexMapping();

	public UserItemContentBasedSimilarity(String id, Dataset ds) {
		this.id = id;
		this.useObjIdToIndexMapping = ds.isIdMappingRequired();
		calculate(ds);
	}

	@Override
	protected void calculate(Dataset dataSet) {

		int nUsers = dataSet.getUserCount();
		int nItems = dataSet.getItemCount();

		similarityValues = new double[nUsers][nItems];

		// if we want to use mapping from userId/itemId to matrix index
		// then we need to generate index for every userId and itemId
		if (useObjIdToIndexMapping) {
			for (User u : dataSet.getUsers()) {
				idMappingForUser.getIndex(String.valueOf(u.getId()));
			}

			for (Item i : dataSet.getItems()) {
				idMappingForItem.getIndex(String.valueOf(i.getId()));
			}
		}

		CosineSimilarityMeasure cosineMeasure = new CosineSimilarityMeasure();
		String[] allTerms = dataSet.getAllTerms();

		for (int u = 0; u < nUsers; u++) {
			int userId = getUserIdForIndex(u);
			User user = dataSet.getUser(userId);

			for (int v = 0; v < nItems; v++) {

				int itemId = getItemIdFromIndex(v);
				Item item = dataSet.getItem(itemId);

				double simValue = 0.0;
				double bestCosineSimValue = 0.0;

				for (Content userContent : user.getUserContent()) {

					simValue = cosineMeasure.calculate(userContent
							.getTermVector(allTerms), item.getItemContent()
							.getTermVector(allTerms));
					bestCosineSimValue = Math.max(bestCosineSimValue, simValue);
				}

				similarityValues[u][v] = bestCosineSimValue;
			}
		}
	}

	/*
	 * Utility method to convert itemId into matrix index
	 */
	private int getIndexForItemId(Integer itemId) {
		int index = 0;
		if (useObjIdToIndexMapping) {
			index = idMappingForItem.getIndex(String.valueOf(itemId));
		} else {
			index = itemId - 1;
		}
		return index;
	}

	/*
	 * Utility method to convert userId into matrix index.
	 */
	private int getIndexForUserId(Integer userId) {
		int index = 0;
		if (useObjIdToIndexMapping) {
			index = idMappingForUser.getIndex(String.valueOf(userId));
		} else {
			index = userId - 1;
		}
		return index;
	}

	@Override
	protected int getIndexFromObjId(Integer objId) {
		throw new UnsupportedOperationException(
				"Should not be used. Use user or item specific method istead.");
	}

	/*
	 * Utility method to convert matrix index into itemId.
	 */
	private Integer getItemIdFromIndex(int index) {
		Integer objId;
		if (useObjIdToIndexMapping) {
			objId = Integer.parseInt(idMappingForItem.getValue(index));
		} else {
			objId = index + 1;
		}
		return objId;
	}

	@Override
	protected Integer getObjIdFromIndex(int index) {
		throw new UnsupportedOperationException(
				"Should not be used.  Use user or item specific method istead.");
	}

	/*
	 * Utility method to convert matrix index into userId
	 */
	private Integer getUserIdForIndex(int index) {
		Integer objId;
		if (useObjIdToIndexMapping) {
			objId = Integer.parseInt(idMappingForUser.getValue(index));
		} else {
			objId = index + 1;
		}
		return objId;
	}

	@Override
	public double getValue(Integer userId, Integer itemId) {
		if (similarityValues == null) {
			throw new IllegalStateException(
					"You have to calculate similarities first.");
		}

		int x = getIndexForUserId(userId);
		int y = getIndexForItemId(itemId);

		return similarityValues[x][y];
	}

}
