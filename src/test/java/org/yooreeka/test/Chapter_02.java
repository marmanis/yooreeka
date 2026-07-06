package org.yooreeka.test;

import java.io.IOException;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.internet.crawling.FetchAndProcessCrawler;
import org.yooreeka.examples.search.LuceneIndexer;
import org.yooreeka.examples.search.MySearcher;
import org.yooreeka.examples.search.PageRank;
import org.yooreeka.examples.search.DocRank;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.bayesian.NaiveBayes;
import org.yooreeka.util.internet.behavior.UserClick;
import org.yooreeka.util.internet.behavior.UserQuery;
import org.yooreeka.util.P;

public class Chapter_02 {

	private long t = 0;

	public Chapter_02() {
	}

	public void run() throws Exception {
		
		t = System.currentTimeMillis();

		// Lucene Search
		script_02_01();

		// PageRank
		script_02_03_PageRank();

		// Lucene And PageRank
		script_02_03_LuceneAndPageRank();

		// User Clicks
		script_02_04();

		// Lucene And DocRank
		script_02_05();

		P.timePassedSince(t);
	}

	public void script_02_01() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		FetchAndProcessCrawler crawler = new FetchAndProcessCrawler(yHome + "/data/ch02", 5, 200);
		crawler.setDefaultBookUrls();
		crawler.run();

		LuceneIndexer luceneIndexer = new LuceneIndexer(crawler.getRootDir());
		luceneIndexer.run();

		MySearcher oracle = new MySearcher(luceneIndexer.getLuceneDir());
		oracle.search("armstrong", 5);
	}

	public void script_02_03_PageRank() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		FetchAndProcessCrawler crawler = new FetchAndProcessCrawler(yHome + "/data/ch02", 5, 200);
		crawler.setUrls("biz");
		crawler.run();

		PageRank pageRank = new PageRank(crawler.getCrawlData());
		pageRank.setAlpha(0.85);
		pageRank.setEpsilon(0.0001);
		pageRank.build();
	}

	public void script_02_03_LuceneAndPageRank() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		FetchAndProcessCrawler crawler = new FetchAndProcessCrawler(yHome + "/data/ch02", 5, 200);
		crawler.setUrls("biz");
		crawler.addDocSpam();
		crawler.run();

		LuceneIndexer luceneIndexer = new LuceneIndexer(crawler.getRootDir());
		luceneIndexer.run();
		MySearcher oracle = new MySearcher(luceneIndexer.getLuceneDir());

		// oracle.search("nvidia", 5);
		// oracle.search("economy news", 5);

		PageRank pageRank = new PageRank(crawler.getCrawlData());
		pageRank.setAlpha(0.99);
		pageRank.setEpsilon(0.00000001);
		pageRank.build();

		oracle.search("nvidia", 5, pageRank);
		//oracle.search("economy news", 5, pageRank);
	}

	public void script_02_04() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		FetchAndProcessCrawler crawler = new FetchAndProcessCrawler(yHome + "/data/ch02", 5, 200);

		crawler.setUrls("biz");
		crawler.addDocSpam();

		crawler.run();

		LuceneIndexer luceneIndexer = new LuceneIndexer(crawler.getRootDir());
		luceneIndexer.run();
		MySearcher oracle = new MySearcher(luceneIndexer.getLuceneDir());

		PageRank pageRank = new PageRank(crawler.getCrawlData());
		pageRank.setAlpha(0.9);
		pageRank.setEpsilon(0.00000001);
		pageRank.build();

		UserClick aux = new UserClick();
		UserClick[] clicks = (UserClick[]) aux.load(yHome + "/data/ch02/user-clicks.csv");
		TrainingSet tSet = new TrainingSet(clicks);

		NaiveBayes naiveBayes = new NaiveBayes("Naive Bayes", tSet);
		naiveBayes.trainOnAttribute("a-0");
		naiveBayes.trainOnAttribute("a-1");
		naiveBayes.trainOnAttribute("a-2");
		naiveBayes.train();

		oracle.setUserLearner(naiveBayes);

		UserQuery babisQuery = new UserQuery("babis", "google ads");
		oracle.search(babisQuery, 5, pageRank);

		UserQuery dmitryQuery = new UserQuery("dmitry", "google ads");
		oracle.search(dmitryQuery, 5, pageRank);
	}

	public void script_02_05() throws Exception {
		String yHome = YooreekaConfigurator.getHome();
		FetchAndProcessCrawler c = new FetchAndProcessCrawler(yHome + "/data/ch02", 5, 200);
		c.setUrls("biz-docs");
		c.addDocSpam();
		c.run();

		LuceneIndexer lidx = new LuceneIndexer(c.getRootDir());
		lidx.run();
		MySearcher oracle = new MySearcher(lidx.getLuceneDir());

		oracle.search("nvidia", 5);

		DocRank dr = new DocRank(lidx.getLuceneDir(), 7);
		dr.setAlpha(0.9);
		dr.setEpsilon(0.00000001);
		dr.build();

		oracle.search("nvidia", 5, dr);
	}
}
