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

import java.util.Arrays;

import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.similarity.util.RatingCountMatrix;
import org.yooreeka.util.internet.crawling.util.ValueToIndexMapping;

public abstract class SimilarityMatrixImpl implements SimilarityMatrix {

	private static final long serialVersionUID = -8119322978934551969L;

	protected String id;
	protected double similarityValues[][] = null;
	protected RatingCountMatrix ratingCountMatrix[][] = null;
	protected boolean keepRatingCountMatrix = false;

	protected boolean useObjIdToIndexMapping = true;
	protected ValueToIndexMapping idMapping = new ValueToIndexMapping();

	protected SimilarityMatrixImpl() {
	}

	protected abstract void calculate(Dataset dataSet);

	public String getId() {
		return this.id;
	}

	/**
	 * 
	 * @param objId
	 *            user or item id.
	 * @return index that can be used to access the object in the matrix.
	 */
	protected int getIndexFromObjId(Integer objId) {
		int index = 0;
		if (useObjIdToIndexMapping) {
			index = idMapping.getIndex(String.valueOf(objId));
		} else {
			index = objId - 1;
		}
		return index;
	}

	protected Integer getObjIdFromIndex(int index) {
		Integer objId;
		if (useObjIdToIndexMapping) {
			objId = Integer.parseInt(idMapping.getValue(index));
		} else {
			objId = index + 1;
		}
		return objId;
	}

	public RatingCountMatrix getRatingCountMatrix(Integer idX, Integer idY) {
		int x = getIndexFromObjId(idX);
		int y = getIndexFromObjId(idY);

		return ratingCountMatrix[x][y];
	}

	public double[][] getSimilarityMatrix() {
		return similarityValues;
	}

	public boolean getUseObjIdToIndexMapping() {
		return useObjIdToIndexMapping;
	}

	public double getValue(Integer idX, Integer idY) {
		if (similarityValues == null) {
			throw new IllegalStateException(
					"You have to calculate similarities first.");
		}

		int x = getIndexFromObjId(idX);
		int y = getIndexFromObjId(idY);

		int i, j;
		if (x <= y) {
			i = x;
			j = y;
		} else {
			i = y;
			j = x;
		}
		return similarityValues[i][j];
	}

	public boolean isRatingCountMatrixAvailable() {
		return keepRatingCountMatrix;
	}

	public void print() {
		if (similarityValues != null) {
			for (double[] row : this.similarityValues) {
				System.out.println(Arrays.toString(row));
			}
		}
	}

	public void print(int nRows) {
		int count = 0;
		if (similarityValues != null) {
			for (double[] row : this.similarityValues) {
				if (count < nRows) {
					System.out.println(Arrays.toString(row));
				}
				count++;
			}
		}
	}

	public void setUseObjIdToIndexMapping(boolean value) {
		this.useObjIdToIndexMapping = value;
	}
}
