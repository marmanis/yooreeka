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
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.similarity.naive.SimilarityMatrixImpl;
import org.yooreeka.algos.reco.collab.similarity.util.PearsonCorrelation;

public class MovieLensItemSimilarity extends SimilarityMatrixImpl {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 2571216412528879244L;

	public MovieLensItemSimilarity(Dataset ds) {
		this(MovieLensItemSimilarity.class.getSimpleName(), ds);
	}

	public MovieLensItemSimilarity(String id, Dataset ds) {
		this.id = id;
		this.useObjIdToIndexMapping = ds.isIdMappingRequired();
		calculate(ds);
	}

	@Override
	protected void calculate(Dataset dataSet) {

		int nItems = dataSet.getItemCount();

		similarityValues = new double[nItems][nItems];

		// if we want to use mapping from itemId to index then generate
		// index for every itemId
		if (useObjIdToIndexMapping) {
			for (Item item : dataSet.getItems()) {
				idMapping.getIndex(String.valueOf(item.getId()));
			}
		}

		PearsonCorrelation pC = null;

		for (int u = 0; u < nItems; u++) {

			int itemAId = getObjIdFromIndex(u);
			Item itemA = dataSet.getItem(itemAId);

			// we only need to calculate elements above the main diagonal.
			for (int v = u + 1; v < nItems; v++) {

				int itemBId = getObjIdFromIndex(v);
				Item itemB = dataSet.getItem(itemBId);

				pC = new PearsonCorrelation(dataSet, itemA, itemB);

				similarityValues[u][v] = pC.calculate();
			}

			// for u == v assign 1
			similarityValues[u][u] = 1.0;
		}
	}
}
