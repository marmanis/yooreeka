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
package org.yooreeka.algos.reco.collab.similarity.util;

import java.io.File;

import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.similarity.naive.ImprovedUserBasedSimilarity;
import org.yooreeka.algos.reco.collab.similarity.naive.ItemBasedSimilarity;
import org.yooreeka.algos.reco.collab.similarity.naive.ItemContentBasedSimilarity;
import org.yooreeka.algos.reco.collab.similarity.naive.ItemPenaltyBasedSimilarity;
import org.yooreeka.algos.reco.collab.similarity.naive.SimilarityMatrix;
import org.yooreeka.algos.reco.collab.similarity.naive.UserBasedSimilarity;
import org.yooreeka.algos.reco.collab.similarity.naive.UserContentBasedSimilarity;
import org.yooreeka.algos.reco.collab.similarity.naive.UserItemContentBasedSimilarity;
import org.yooreeka.config.YooreekaConfigurator;

public class SimilarityMatrixRepository {

	/**
	 * Generates id for similarity matrix based on type and dataset name.
	 * 
	 * @param type
	 * @param datasetName
	 * @return
	 */
	public static String getId(RecommendationType type, String datasetName) {
		String classname = null;
		switch (type) {
		case ITEM_BASED:
			classname = ItemBasedSimilarity.class.getSimpleName();
			break;
		case ITEM_PENALTY_BASED:
			classname = ItemPenaltyBasedSimilarity.class.getSimpleName();
			break;
		case USER_BASED:
			classname = UserBasedSimilarity.class.getSimpleName();
			break;
		case IMPROVED_USER_BASED:
			classname = ImprovedUserBasedSimilarity.class.getSimpleName();
			break;
		case USER_CONTENT_BASED:
			classname = UserContentBasedSimilarity.class.getSimpleName();
			break;
		case ITEM_CONTENT_BASED:
			classname = ItemContentBasedSimilarity.class.getSimpleName();
			break;
		case USER_ITEM_CONTENT_BASED:
			classname = UserItemContentBasedSimilarity.class.getSimpleName();
			break;
		default:
			throw new IllegalArgumentException("Unknown type: " + type);
		}
		return classname + "-" + datasetName;
	}

	SimilarityMatrixCache cache;

	public SimilarityMatrixRepository(boolean useCache) {
		if (useCache) {
			String appTempDir = YooreekaConfigurator
					.getProperty(YooreekaConfigurator.TEMP_DIR);
			File cacheDir = new File(appTempDir,
					"ch3/collaborative/SimilarityCache");
			cache = new SimilarityMatrixCache(cacheDir);
		} else {
			cache = null;
		}
	}

	public SimilarityMatrixRepository(SimilarityMatrixCache cache) {
		this.cache = cache;
	}

	public SimilarityMatrix load(RecommendationType type, Dataset data) {
		boolean keepRatingCountMatrix = true;
		return load(type, data, keepRatingCountMatrix);
	}

	public SimilarityMatrix load(RecommendationType type, Dataset data,
			boolean keepRatingCountMatrix) {
		SimilarityMatrix m = null;

		String id = getId(type, data.getName());
		// if cache is available then try to load from cache first
		if (cache != null) {
			m = cache.get(id);
			if (m == null) {
				System.out
						.println("similarity matrix instance doesn't exist in cache: "
								+ "id: "
								+ id
								+ ", cache: '"
								+ cache.getLocation() + "'.");
			} else {
				System.out
						.println("similarity matrix instance was loaded from cache: "
								+ "id: "
								+ id
								+ ", cache: '"
								+ cache.getLocation() + "'.");
			}
		}

		// create a new instance
		if (m == null) {
			switch (type) {
			case ITEM_BASED:
				m = new ItemBasedSimilarity(id, data, keepRatingCountMatrix);
				break;
			case ITEM_PENALTY_BASED:
				m = new ItemPenaltyBasedSimilarity(id, data,
						keepRatingCountMatrix);
				break;
			case USER_BASED:
				m = new UserBasedSimilarity(id, data, keepRatingCountMatrix);
				break;
			case IMPROVED_USER_BASED:
				m = new ImprovedUserBasedSimilarity(id, data,
						keepRatingCountMatrix);
				break;
			case USER_CONTENT_BASED:
				m = new UserContentBasedSimilarity(id, data);
				break;
			case ITEM_CONTENT_BASED:
				m = new ItemContentBasedSimilarity(id, data);
				break;
			case USER_ITEM_CONTENT_BASED:
				m = new UserItemContentBasedSimilarity(id, data);
				break;
			default:
				throw new IllegalArgumentException(
						"Unsupported recommendation type: " + type.toString());
			}
			// store new instance in cache
			if (cache != null) {
				cache.put(id, m);
			}
		}

		return m;
	}

}
