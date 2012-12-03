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
package org.yooreeka.algos.clustering.test;

import java.util.List;

import org.yooreeka.algos.clustering.dbscan.DBSCANAlgorithm;
import org.yooreeka.algos.clustering.hierarchical.Dendrogram;
import org.yooreeka.algos.clustering.model.Attribute;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.rock.ROCKAlgorithm;
import org.yooreeka.algos.clustering.utils.Attributes;
import org.yooreeka.algos.reco.collab.data.DiggData;
import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.algos.reco.content.digg.DiggStoryItem;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.metrics.CosineDistance;

public class MyDiggSpaceData {

	private static DataPoint createDataPoint(DiggStoryItem story, int topNTerms) {
		String storyLabel = String.valueOf(story.getId() + ":"
				+ story.getTitle());
		String storyText = story.getTitle() + " " + story.getDescription();
		Content content = new Content(storyLabel, storyText, topNTerms);
		String[] terms = content.getTerms();
		// using term as attribute name and value.
		Attribute[] attributes = Attributes.createAttributes(terms, terms);
		return new DataPoint(storyLabel, attributes);
	}

	public static MyDiggSpaceDataset createDataset() {
		return createDataset(10);
	}

	public static MyDiggSpaceDataset createDataset(int topNTerms) {
		DiggData.loadData(YooreekaConfigurator.getHome()
				+ "/data/ch04/ch4_digg_stories.csv");

		List<DiggStoryItem> allStories = DiggData.allStories;

		DataPoint[] allDataPoints = new DataPoint[allStories.size()];

		for (int i = 0, n = allDataPoints.length; i < n; i++) {
			DiggStoryItem story = allStories.get(i);
			DataPoint di = createDataPoint(story, topNTerms);
			allDataPoints[i] = di;
		}
		return new MyDiggSpaceDataset(allDataPoints);
	}

	public static MyDiggSpaceDataset createDataset(int topNTerms,
			List<DiggStoryItem> allStories) {

		DataPoint[] allDataPoints = new DataPoint[allStories.size()];

		for (int i = 0, n = allDataPoints.length; i < n; i++) {

			DiggStoryItem story = allStories.get(i);
			story.print();

			DataPoint di = createDataPoint(story, topNTerms);
			allDataPoints[i] = di;
		}
		return new MyDiggSpaceDataset(allDataPoints);
	}

	public static void main(String[] args) {
		// testRockOnDigg();
		testDBSCAN();
	}

	private static void testDBSCAN() {
		MyDiggSpaceDataset ds = MyDiggSpaceData.createDataset(3);
		double eps = 0.8;
		int minPts = 2;
		boolean useTermFreq = true;
		DBSCANAlgorithm dbscan = new DBSCANAlgorithm(ds.getData(),
				new CosineDistance(), eps, minPts, useTermFreq);

		dbscan.cluster();
		// dbscan.printDistances();
	}

	public static void testRockOnDigg() {
		MyDiggSpaceDataset ds = MyDiggSpaceData.createDataset(10);
		ROCKAlgorithm rock = new ROCKAlgorithm(ds.getData(), 4, 0.1);
		// rock.getLinkMatrix().printSimilarityMatrix();
		// rock.getLinkMatrix().printPointNeighborMatrix();
		// rock.getLinkMatrix().printPointLinkMatrix();
		Dendrogram dnd = rock.cluster();
		dnd.print(130); // if you get NPE here it means that level doesn't
						// exist.

		// ROCK stops clustering if there are no links between clusters.
	}
}
