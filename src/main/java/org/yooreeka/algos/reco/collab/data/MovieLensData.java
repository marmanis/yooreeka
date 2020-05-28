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
package org.yooreeka.algos.reco.collab.data;

import java.io.File;

import org.yooreeka.config.YooreekaConfigurator;

/**
 * Utility class to create MovieLens dataset.
 */
public class MovieLensData {

	/**
	 * Loads MovieLens dataset from default directory.
	 */
	public static MovieLensDataset createDataset() {
		int numOfTestRatings = 0;
		return createDataset(numOfTestRatings);
	}

	public static MovieLensDataset createDataset(int numOfTestRatings) {
		return createDataset(
				YooreekaConfigurator.getProperty("yooreeka.movielens.data.dir"),
				numOfTestRatings);
	}

	/**
	 * Loads MovieLens dataset from specified directory.
	 * 
	 * @param dataDir
	 *            directory that contains MovieLens files.
	 * @return
	 */
	public static MovieLensDataset createDataset(String dataDir,
			int numOfTestRatings) {
		File users = new File(dataDir, MovieLensDataset.USERS_FILENAME);
		File items = new File(dataDir, MovieLensDataset.ITEMS_FILENAME);
		File ratings = new File(dataDir, MovieLensDataset.RATINGS_FILENAME);

		System.out.println("*** Loading MovieLens dataset...");
		System.out.println("make sure that you are using at least: -Xmx1024m");

		MovieLensDataset dataSet = new MovieLensDataset(users, items, ratings,
				numOfTestRatings);

		System.out.println("\n*** Loaded MovieLens dataset.");
		System.out.println("users: " + dataSet.getUserCount());
		System.out.println("movies: " + dataSet.getItemCount());
		System.out.println("ratings: " + dataSet.getRatingsCount());
		System.out.println("test ratings: " + dataSet.getTestRatings().size());

		return dataSet;
	}
}
