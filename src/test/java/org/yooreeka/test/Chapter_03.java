package org.yooreeka.test;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.algos.reco.collab.data.MusicData;
import org.yooreeka.algos.reco.collab.data.MusicUser;
import org.yooreeka.algos.reco.collab.data.MusicItem;
import org.yooreeka.algos.reco.collab.data.BaseDataset;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.recommender.Delphi;
import org.yooreeka.algos.reco.collab.recommender.DiggDelphi;
import org.yooreeka.algos.reco.collab.data.NewsData;
import org.yooreeka.algos.reco.collab.data.NewsUser;
import org.yooreeka.algos.reco.collab.data.ContentItem;
import org.yooreeka.algos.reco.collab.data.DiggData;
import org.yooreeka.algos.reco.collab.data.MovieLensData;
import org.yooreeka.algos.reco.collab.data.MovieLensDataset;
import org.yooreeka.algos.reco.collab.recommender.MovieLensDelphi;
import org.yooreeka.algos.reco.collab.evaluation.RMSEEstimator;
import org.yooreeka.util.P;

public class Chapter_03 {

	private long t = 0;

	public Chapter_03() {
	}

	public void run() throws Exception {
		t = System.currentTimeMillis();

		// Basic Similarity
		script_03_01();

		// User Based Similarity
		script_03_02();

		// Item Based Similarity
		script_03_03();

		// Content Based Similarity
		script_03_04();

		// Digg
		script_03_Digg();

		// Digg Content And Ratings
		script_03_05();

		// MovieLens
		script_03_06();

		// MovieLens RMSE
		script_03_07();

		P.timePassedSince(t);
	}

	public void script_03_01() {
		MusicUser[] mu = MusicData.loadExample();
		mu[0].getSimilarity(mu[1], 0);
		mu[0].getSimilarity(mu[1], 1);
		mu[0].getSimilarity(mu[2], 1);
		mu[1].getSimilarity(mu[2], 0);
		mu[2].getSimilarity(mu[1], 0);
	}

	public void script_03_02() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		BaseDataset ds = MusicData.createDataset();
		ds.save(yHome + "/data/ch03/dataset_script_2.ser");

		Delphi delphi = new Delphi(ds, RecommendationType.USER_BASED);
		delphi.setVerbose(true);

		MusicUser mu1 = (MusicUser) ds.pickUser("Bob");
		delphi.findSimilarUsers(mu1);

		MusicUser mu2 = (MusicUser) ds.pickUser("John");
		delphi.findSimilarUsers(mu2);

		delphi.recommend(mu1);
	}

	public void script_03_03() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		BaseDataset ds = BaseDataset.load(yHome + "/data/ch03/dataset_script_2.ser");

		Delphi delphi = new Delphi(ds, RecommendationType.ITEM_BASED);
		delphi.setVerbose(true);

		MusicUser mu1 = (MusicUser) ds.pickUser("Bob");
		delphi.recommend(mu1);

		MusicItem mi = (MusicItem) ds.pickItem("La Bamba");
		delphi.findSimilarItems(mi);
	}

	public void script_03_04() {
		BaseDataset ds = NewsData.createDataset();

		Delphi delphiUC = new Delphi(ds, RecommendationType.USER_CONTENT_BASED);
		delphiUC.setVerbose(true);

		NewsUser nu1 = (NewsUser) ds.pickUser("Bob");
		delphiUC.findSimilarUsers(nu1);

		NewsUser nu2 = (NewsUser) ds.pickUser("John");
		delphiUC.findSimilarUsers(nu2);

		Delphi delphiIC = new Delphi(ds, RecommendationType.ITEM_CONTENT_BASED);
		delphiIC.setVerbose(true);

		ContentItem biz1 = ds.pickContentItem("biz-01.html");
		delphiIC.findSimilarItems(biz1);

		ContentItem usa1 = ds.pickContentItem("usa-01.html");
		delphiIC.findSimilarItems(usa1);

		ContentItem sport1 = ds.pickContentItem("sport-01.html");
		delphiIC.findSimilarItems(sport1);

		Delphi delphiUIC = new Delphi(ds, RecommendationType.USER_ITEM_CONTENT_BASED);
		delphiUIC.setVerbose(true);

		delphiUIC.recommend(nu1);
	}

	public void script_03_05() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		BaseDataset ds = DiggData.loadData(yHome + "/data/ch03/digg_stories.csv");

		org.yooreeka.algos.reco.collab.model.User user = ds.getUser(1);

		DiggDelphi delphi = new DiggDelphi(ds);
		delphi.findSimilarUsers(user);
		delphi.recommend(user);
	}

	public void script_03_06() {
		MovieLensDataset ds = MovieLensData.createDataset();
		MovieLensDelphi delphi = new MovieLensDelphi(ds);

		org.yooreeka.algos.reco.collab.model.User u1 = ds.getUser(1);
		delphi.recommend(u1);

		org.yooreeka.algos.reco.collab.model.User u155 = ds.getUser(155);
		delphi.recommend(u155);

		org.yooreeka.algos.reco.collab.model.User u876 = ds.getUser(876);
		delphi.recommend(u876);
	}

	public void script_03_07() {
		MovieLensDataset ds = MovieLensData.createDataset(10000);
		MovieLensDelphi delphi = new MovieLensDelphi(ds);

		RMSEEstimator rmseEstimator = new RMSEEstimator();
		rmseEstimator.calculateRMSE(delphi);
		rmseEstimator.compareRMSEs(delphi);
	}

	public void script_03_Digg() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		DiggData.loadData(yHome + "/data/ch03/digg_stories.csv");
		BaseDataset ds = DiggData.createDataset();

		org.yooreeka.algos.reco.collab.model.User user = ds.getUser(1);

		Delphi delphiUC = new Delphi(ds, RecommendationType.USER_CONTENT_BASED);
		delphiUC.setVerbose(true);
		delphiUC.findSimilarUsers(user);

		Delphi delphiUIC = new Delphi(ds, RecommendationType.USER_ITEM_CONTENT_BASED);
		delphiUIC.setVerbose(true);
		delphiUIC.recommend(user);
	}
}
