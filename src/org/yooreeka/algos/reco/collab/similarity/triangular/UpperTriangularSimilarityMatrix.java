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
package org.yooreeka.algos.reco.collab.similarity.triangular;

import java.util.Hashtable;

import org.yooreeka.algos.reco.collab.similarity.util.RatingCountMatrix;

/**
 * Defines a similarity matrix, which uses a <code>Hashtable</code>. The
 * <code>Hashtable</code> store the upper triangular part of the similarity
 * matrix.
 * 
 * Note: If the similarity matrix is <b>not symmetric</b> then this is not an
 * appropriate representation. For example, in the case of user-oriented methods
 * you might want the similarity matrix to reflect the assymetry between the
 * tastes of various individuals. Person A may like person B and considers
 * himself similar to person B. However, person B may not feel the same way.
 * 
 */
public interface UpperTriangularSimilarityMatrix extends java.io.Serializable {

	/**
	 * Similarity matrix id.
	 * 
	 * @return
	 */
	public abstract String getId();

	public abstract RatingCountMatrix getRatingCountMatrix(Integer idX,
			Integer idY);

	/**
	 * Returns an upper triangular matrix of similarities. For user-oriented
	 * methods it represents similarities between users and for item-oriented
	 * methods the matrix represents similarities between items.
	 * 
	 * @return similarity matrix
	 */
	public abstract Hashtable<Integer, double[]> getSimilarityMatrix();

	/**
	 * Returns similarity value between two objects identified by their IDs.
	 * 
	 * @param idX
	 * @param idY
	 * @return
	 */
	public abstract double getValue(Integer idX, Integer idY);

	public abstract boolean isRatingCountMatrixAvailable();

	public void print();
}