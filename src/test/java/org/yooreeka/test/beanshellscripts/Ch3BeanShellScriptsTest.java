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
package org.yooreeka.test.beanshellscripts;

import org.yooreeka.algos.reco.collab.data.BaseDataset;
import org.yooreeka.algos.reco.collab.data.ContentItem;
import org.yooreeka.algos.reco.collab.data.DiggData;
import org.yooreeka.algos.reco.collab.data.MovieLensData;
import org.yooreeka.algos.reco.collab.data.MovieLensDataset;
import org.yooreeka.algos.reco.collab.data.MusicData;
import org.yooreeka.algos.reco.collab.data.MusicItem;
import org.yooreeka.algos.reco.collab.data.MusicUser;
import org.yooreeka.algos.reco.collab.data.NewsData;
import org.yooreeka.algos.reco.collab.data.NewsUser;
import org.yooreeka.algos.reco.collab.evaluation.RMSEEstimator;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.recommender.Delphi;
import org.yooreeka.algos.reco.collab.recommender.DiggDelphi;
import org.yooreeka.algos.reco.collab.recommender.MovieLensDelphi;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.P;

import junit.framework.TestCase;

public class Ch3BeanShellScriptsTest extends TestCase {

	private long t;
	private String yHome;
	private BaseDataset musicDataset;

	public Ch3BeanShellScriptsTest(String name) {
		super(name);

		t = System.currentTimeMillis();
		yHome = YooreekaConfigurator.getHome();
		DiggData.loadData(yHome + "/data/ch03/digg_stories.csv");

		// The static method createDataset will create random ratings
		// for all the users, pick 75% of all the songs and assign ratings.
		//
		// Users whose name starts from A to D should have ratings between 3 and 5
		// Users whose name starts from E to Z should have ratings between 1 and 3
		//
		musicDataset = MusicData.createDataset();

	}

	public void test_evalCh3Scripts() throws Exception {
		// ScriptEvalUtils.runScripts("ch3");
		test_Ch3_0();
		test_Ch3_1();
		test_Ch3_2();
		test_Ch3_3();
		test_Ch3_4();
		test_Ch3_5();
		test_Ch3_6();
		test_Ch3_7();

		P.timePassedSince(t);
	}

	public void test_Ch3_0() {

		BaseDataset ds = DiggData.createDataset();

		// Pick a user randomly or by username
		org.yooreeka.algos.reco.collab.model.User user = ds.getUser(1);

		// Or pick some specific user by username
		// DiggData.showUsers();
		// org.yooreeka.algos.reco.collab.model.User user = ds.findUserByName("<some
		// name>");

		// Show similar users
		Delphi delphiUC = new Delphi(ds, RecommendationType.USER_CONTENT_BASED);
		delphiUC.setVerbose(true);
		delphiUC.findSimilarUsers(user);

		// Recommend items for user
		Delphi delphiUIC = new Delphi(ds, RecommendationType.USER_ITEM_CONTENT_BASED);
		delphiUIC.setVerbose(true);
		delphiUIC.recommend(user);
	}

	public void test_Ch3_1() {
		//
		// Load some users with predetermined ratings
		//
		MusicUser[] mu = MusicData.loadExample();

		//
		// Compare the first user with the second user (high similarity)
		//
		mu[0].getSimilarity(mu[1], 0);

		//
		// Correction for the ratio of common items
		//
		mu[0].getSimilarity(mu[1], 1);

		//
		// Compare the first user with the third user (low similarity)
		//
		mu[0].getSimilarity(mu[2], 1);

		//
		// Show symmetry property
		//
		mu[1].getSimilarity(mu[2], 0);

		mu[2].getSimilarity(mu[1], 0);

	}

	public void test_Ch3_2() {

		//
		// Serialize the dataset, so that we can load it in the next script
		//

		// This is not needed if we run it as JUnit test
		// musicDataset.save(yHome + "/data/ch03/dataset_script_2.ser");

		//
		// use Delphi with USER_BASED similarity
		//
		Delphi delphi = new Delphi(musicDataset, RecommendationType.USER_BASED);
		delphi.setVerbose(true);

		//
		// Show me users like X (top 5)
		//
		MusicUser mu1 = (MusicUser) musicDataset.pickUser("Bob");
		delphi.findSimilarUsers(mu1);

		MusicUser mu2 = (MusicUser) musicDataset.pickUser("John");
		delphi.findSimilarUsers(mu2);

		//
		// Show me recommendations for user X (top 5)
		//
		delphi.recommend(mu1);

		// ------------------------------------------------------------------------
		// BaseDataset ds = MusicData.createDataset();
		// ds.save(yHome+"/data/ch03/dataset_script_2.ser");
		// Delphi delphi = new Delphi(ds,RecommendationType.USER_BASED);
		// delphi.setVerbose(true);
		// MusicUser mu1 = ds.pickUser("Bob");
		// delphi.findSimilarUsers(mu1);
		// MusicUser mu2 = ds.pickUser("John");
		// delphi.findSimilarUsers(mu2);
		// delphi.recommend(mu1);
		// ------------------------------------------------------------------------

	}

	public void test_Ch3_3() {
		//
		// Load the dataset that we created before
		//

		// This is not needed if we run it as JUnit test
		// BaseDataset musicDataset = BaseDataset.load(yHome +
		// "/data/ch03/dataset_script_2.ser");

		//
		// use Delphi with ITEM_BASED similarity
		//
		Delphi delphi = new Delphi(musicDataset, RecommendationType.ITEM_BASED);
		delphi.setVerbose(true);

		//
		// Show me recommendations for user X (top 5)
		//
		MusicUser mu1 = (MusicUser) musicDataset.pickUser("Bob");
		delphi.recommend(mu1);

		//
		// Show me items like X (top 5)
		//
		MusicItem mi = (MusicItem) musicDataset.pickItem("La Bamba");
		delphi.findSimilarItems(mi);
	}

	public void test_Ch3_4() {
		BaseDataset newsDataset = NewsData.createDataset();

		//
		// use Delphi with USER_CONTENT_BASED similarity
		//
		Delphi delphiUC = new Delphi(newsDataset, RecommendationType.USER_CONTENT_BASED);
		delphiUC.setVerbose(true);

		//
		// Show me users like X (top 5)
		//
		org.yooreeka.algos.reco.collab.data.NewsUser nu1 = (NewsUser) newsDataset.pickUser("Bob");
		delphiUC.findSimilarUsers(nu1);

		org.yooreeka.algos.reco.collab.data.NewsUser nu2 = (NewsUser) newsDataset.pickUser("John");
		delphiUC.findSimilarUsers(nu2);

		// ---------------------------------------------------------
		//
		// use Delphi with ITEM_CONTENT_BASED similarity
		//
		Delphi delphiIC = new Delphi(newsDataset, RecommendationType.ITEM_CONTENT_BASED);
		delphiIC.setVerbose(true);

		//
		// Show me items like X (top 5)
		//
		ContentItem biz1 = newsDataset.pickContentItem("biz-01.html");
		delphiIC.findSimilarItems(biz1);

		ContentItem usa1 = newsDataset.pickContentItem("usa-01.html");
		delphiIC.findSimilarItems(usa1);

		ContentItem sport1 = newsDataset.pickContentItem("sport-01.html");
		delphiIC.findSimilarItems(sport1);

		// ---------------------------------------------------------
		//
		// use Delphi with USER_ITEM_CONTENT_BASED similarity
		//
		Delphi delphiUIC = new Delphi(newsDataset, RecommendationType.USER_ITEM_CONTENT_BASED);
		delphiUIC.setVerbose(true);

		//
		// Show me recommendations for user X (top 5)
		//
		delphiUIC.recommend(nu1);
	}

	public void test_Ch3_5() {
		// Load data from Digg and save them in a file
		// BaseDataset ds =
		// DiggData.loadDataFromDigg(yHome+"/data/ch03/digg_stories.csv");

		// Load previously saved data
		BaseDataset ds = DiggData.loadData(yHome + "/data/ch03/digg_stories.csv");

		// 2. Pick a user randomly or by username
		org.yooreeka.algos.reco.collab.model.User user = ds.getUser(1);

		// Or pick some specific user by username
		// DiggData.showUsers();
		// User user = ds.findUserByName("<some name>");

		// 3. Show similar users
		DiggDelphi delphi = new DiggDelphi(ds);

		delphi.findSimilarUsers(user);
		delphi.recommend(user);

		// org.yooreeka.algos.reco.collab.model.User u2 = ds.findUserByName("adrian67");
		// delphi.findSimilarUsers(u2);
		// delphi.recommend(u2);

		// org.yooreeka.algos.reco.collab.model.User u3 = ds.findUserByName("amipress");
		// delphi.findSimilarUsers(u3);
		// delphi.recommend(u3);
	}

	public void test_Ch3_6() {
		// Create the dataset
		MovieLensDataset ds = MovieLensData.createDataset();

		// Create the recommender
		MovieLensDelphi delphi = new MovieLensDelphi(ds);

		// Pick users and create recommendations
		org.yooreeka.algos.reco.collab.model.User u1 = ds.getUser(1);
		delphi.recommend(u1);

		org.yooreeka.algos.reco.collab.model.User u155 = ds.getUser(155);
		delphi.recommend(u155);

		org.yooreeka.algos.reco.collab.model.User u876 = ds.getUser(876);
		delphi.recommend(u876);
	}

	public void test_Ch3_7() {
		//
		// Create the dataset but reserve 100000 ratings for testing
		// Change that to 10000, if you are using the small dataset
		//
		MovieLensDataset ds = MovieLensData.createDataset(10000);

		// Create an instance of our recommender
		MovieLensDelphi delphi = new MovieLensDelphi(ds);

		// Create an instance of the RMSE estimator
		RMSEEstimator rmseEstimator = new RMSEEstimator();

		// Calculate the RMSE
		rmseEstimator.calculateRMSE(delphi);

		// Compare RMSEs
		rmseEstimator.compareRMSEs(delphi);
	}

}
