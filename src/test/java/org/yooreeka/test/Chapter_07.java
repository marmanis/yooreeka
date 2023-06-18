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
 *   Copyright (c) 2007-2009    Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-2013 Marmanis Group LLC and individual contributors as indicated by the @author tags.  
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
package org.yooreeka.test;

import java.io.IOException;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsProcessor;
import org.yooreeka.examples.newsgroups.crawling.FileListNewsDataset;
import org.yooreeka.examples.newsgroups.crawling.NewsCrawler;
import org.yooreeka.examples.newsgroups.reco.StoryRecommender;
import org.yooreeka.examples.newsgroups.ui.NewsUI;
import org.yooreeka.util.P;

/**
 * Run all the tests of chapter 7
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class Chapter_07 {

	private long t;

	private String yHome;
	private String rootDir;

	/**
	 * 
	 */
	public Chapter_07() {
		
		t = System.currentTimeMillis();
		
		yHome = YooreekaConfigurator.getHome();
		rootDir = YooreekaConfigurator.getProperty(YooreekaConfigurator.CRAWL_DATA_DIR);
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public void run() throws IOException {

		// Crawling for News
		script_07_01("https://marmanis.com/");
		
		//  Run indexing and search on the default pages(no crawling) 
		script_07_02();
		
		// Indexing and Searching
		script_07_03();
		
		// Classification
		script_07_04();

		P.timePassedSince(t);
	}
	
	private void script_07_01(String seedUrl) {

		NewsCrawler crawler = new NewsCrawler(rootDir, 2, 100);

		crawler.addSeedUrl(seedUrl);

		crawler.run();		
	}
	
	/**
	 * Build the dataset, show it and return it
	 * 
	 * @return dataset that was created
	 */
	public NewsDataset script_07_02() {
		NewsDataset dataset = getNewsDataset("DefaultDS","/data/ch07/all",15);
		dataset.init();
		
		NewsUI ui = new NewsUI(dataset);

		NewsUI.createAndShowUI(ui);
		
		return dataset;
	}
	
	public void script_07_03() throws IOException {
		
		NewsDataset dataset = script_07_02();
		
		NewsProcessor newsProcessor = new NewsProcessor(dataset);

		newsProcessor.buildIndexDir();
		newsProcessor.runIndexing();

		newsProcessor.search("cell",5);
		newsProcessor.search("football",5);
	}
	
	/**
	 * Classification test
	 */
	public void script_07_04() {

		NewsDataset trainingDS = getNewsDataset("TrainingDS", "/data/ch07/training");
		trainingDS.init();

		NewsDataset ds1 = getNewsDataset("Cluster-Classify-DS", "/data/ch07/all");
		ds1.init();

		NewsDataset ds2 = getNewsDataset("Classify-Cluster-DS", "/data/ch07/all");
		ds2.init();

		NewsProcessor newsProcessor = new NewsProcessor(trainingDS);
		newsProcessor.trainClassifier();

		newsProcessor.createClusters(ds1);
		newsProcessor.classifyClusters(ds1);

		NewsUI ui1 = new NewsUI(ds1);
		NewsUI.createAndShowUI(ui1);

		newsProcessor.classifyStories(ds2);
		newsProcessor.createClustersWithinTopics(ds2);

		NewsUI ui2 = new NewsUI(ds2);
		NewsUI.createAndShowUI(ui2);

		// -----------------------------------------------------------------------------------
		NewsDataset ds = getNewsDataset("Cluster-DS", "/data/ch07/all");
		ds.init();

		NewsProcessor anotherNewsProcessor = new NewsProcessor();
		anotherNewsProcessor.createClusters(ds);

		NewsUI ui = new NewsUI(ds);
		ui.showClustersOnly(true);
		
		NewsUI.createAndShowUI(ui);
	}
	
	public void script_07_05() {

		NewsDataset trainingDS = getNewsDataset("TrainingDS", "/data/ch07/training");
		trainingDS.init();

		NewsDataset ds = getNewsDataset("Classify-Cluster-DS", "/data/ch07/all");
		ds.init();

		NewsProcessor newsProcessor = new NewsProcessor(trainingDS);

		newsProcessor.trainClassifier();

		newsProcessor.classifyStories(ds);

		newsProcessor.createClustersWithinTopics(ds);

		NewsUI ui = new NewsUI(ds);

		ui.showClustersOnly(true);

		NewsUI.createAndShowUI(ui);

	}

	public void script_07_06() {

		NewsDataset ds = getNewsDataset("NewsDataset", "/data/ch07/all", 25);
		
		ds.setUserAndRatingsFilename(yHome+"/data/ch07/ratings/ratings.txt");
		ds.init();

		StoryRecommender delphi = new StoryRecommender(ds);
		delphi.calculateRecommendations();

		delphi.recommendStories("1");	
	}
	
	// ======= A U X I L I A R Y ===============================================================
	
	private NewsDataset getNewsDataset(String dsName, String documentDirectory) {
		
		NewsDataset dataset = new FileListNewsDataset(dsName);

		dataset.setDocumentDir(yHome+documentDirectory);
		return dataset;
	}

	private NewsDataset getNewsDataset(String dsName, String documentDirectory, int topTerms) {
		
		NewsDataset dataset = getNewsDataset(dsName, documentDirectory);

		dataset.setTopTerms(topTerms);

		return dataset;
	}
}
