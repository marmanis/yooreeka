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

import org.yooreeka.algos.reco.collab.data.MovieLensData;
import org.yooreeka.algos.reco.collab.data.MovieLensDataset;
import org.yooreeka.algos.reco.collab.evaluation.RMSEEstimator;
import org.yooreeka.algos.reco.collab.recommender.MovieLensDelphi;

public class MovieLensRMSESample {

	public static void main(String[] args) throws Exception {

		int testSize = Integer.parseInt(args[0]);

		MovieLensDataset ds = MovieLensData.createDataset(testSize);

		// Create an instance of our recommender
		MovieLensDelphi delphi = new MovieLensDelphi(ds);

		// Create an instance of the RMSE estimator
		RMSEEstimator rmseEstimator = new RMSEEstimator();

		// Calculate the RMSE
		// rmseEstimator.calculateRMSE(delphi);

		// Compare RMSEs
		for (int i = 0; i < 25; i++) {
			delphi.setSimilarityThreshold(0.05d + i * 0.01d);
			rmseEstimator.compareRMSEs(delphi);
		}
	}
}
