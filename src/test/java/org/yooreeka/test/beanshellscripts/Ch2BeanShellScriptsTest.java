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


import org.yooreeka.algos.taxis.bayesian.NaiveBayes;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.search.DocRank;
import org.yooreeka.examples.search.LuceneIndexer;
import org.yooreeka.examples.search.MySearcher;
import org.yooreeka.examples.search.PageRank;
import org.yooreeka.util.P;
import org.yooreeka.util.internet.behavior.UserClick;
import org.yooreeka.util.internet.behavior.UserQuery;
import org.yooreeka.util.internet.crawling.FetchAndProcessCrawler;

import junit.framework.TestCase;

public class Ch2BeanShellScriptsTest extends TestCase {

	// Measure execution time
	private long t;
	
	private String yHome;
    
    public Ch2BeanShellScriptsTest(String name) {
        super(name);
		
        t = System.currentTimeMillis();
        yHome = YooreekaConfigurator.getHome();
        
    }
    
    public void test_EvalCh2Scripts() throws Exception {
    	// ScriptEvalUtils.runScripts("ch2");
    	P.println("Entering: test_Ch2_1()");
    	test_Ch2_1();
    	P.hline();
    	
    	P.println("Entering: test_Ch2_2()");
    	test_Ch2_2();
    	P.hline();
    	
    	P.println("Entering: test_Ch2_3()");
    	test_Ch2_3();
    	P.hline();
    	    	    	
    	P.timePassedSince(t);
    }

	public void test_Ch2_1() {
		
		FetchAndProcessCrawler crawler = new FetchAndProcessCrawler(yHome+"/data/ch02",5,200);
		// -- Data (default URL list)
		//
		crawler.setDefaultBookUrls(); 
		crawler.run(); 

		//
		// -- Lucene
		//
		LuceneIndexer luceneIndexer = new LuceneIndexer(crawler.getRootDir());
		luceneIndexer.run(); 

		MySearcher oracle = new MySearcher(luceneIndexer.getLuceneDir());
		oracle.setVerbose(false);

		oracle.search("armstrong",5); 		
	}
	
	public void test_Ch2_2() throws Exception {
		
		FetchAndProcessCrawler crawler = new FetchAndProcessCrawler(yHome+"/data/ch02",5,200);
		//
		// -- Data (Business news URLs and 3 SPAM pages)
		crawler.setUrls("biz"); 
		crawler.addDocSpam();
		crawler.run(); 
		
		// or use ALL the pages for this test by removing the comments for the two lines below
		// crawler.setAllUrls(); 
		// crawler.run(); 

		//
		// -- Lucene
		//
		LuceneIndexer luceneIndexer = new LuceneIndexer(crawler.getRootDir());
		luceneIndexer.run(); 
		
		MySearcher oracle = new MySearcher(luceneIndexer.getLuceneDir());
		oracle.setVerbose(false);
		
		//
		// -- Lucene Index ranking only 
		//
		oracle.search("nvidia",5); 
		oracle.search("economy news",5); 

		//
		// -- PageRank
		//
		PageRank pageRank = new PageRank(crawler.getCrawlData());
		pageRank.setAlpha(0.99);
		pageRank.setEpsilon(0.00000001);
		pageRank.build();

		//
		// -- Combined ranking
		//
		oracle.search("nvidia",5,pageRank); 
		oracle.search("economy news",5,pageRank); 
	
		//
		// -- Load the user clicks
		//
		UserClick aux = new UserClick();
		UserClick[] clicks = (UserClick[]) aux.load(yHome+"/data/ch02/user-clicks.csv");
		TrainingSet tSet = new TrainingSet(clicks);

		//
		// -- Create the classifier and train it
		//
		NaiveBayes naiveBayes = new NaiveBayes("Naive Bayes", tSet);
		naiveBayes.trainOnAttribute("a-0");
		naiveBayes.trainOnAttribute("a-1");
		naiveBayes.trainOnAttribute("a-2");
		naiveBayes.train();

		// naiveBayes.printConcepts();
		
		oracle.setUserLearner(naiveBayes);

		//
		// -- Combined ranking
		//
		UserQuery babisQuery = new UserQuery("babis","google ads");
		oracle.search(babisQuery,5,pageRank); 

		UserQuery dmitryQuery = new UserQuery("dmitry","google ads");
		oracle.search(dmitryQuery,5,pageRank); 
	}

	public void test_Ch2_3() throws Exception {
		// -- Data (Business news in Word document format and 3 spam documents)
        FetchAndProcessCrawler crawler = new FetchAndProcessCrawler(yHome+"/data/ch02",5,200);

		crawler.setUrls("biz-docs"); 
		crawler.addDocSpam();
		crawler.run(); 

		//
		// -- Lucene
		//
		LuceneIndexer lidx = new LuceneIndexer(crawler.getRootDir());
		lidx.run(); 
		MySearcher oracle = new MySearcher(lidx.getLuceneDir());
		oracle.setVerbose(false);
		
		//
		// -- Lucene Index ranking only 
		//
		oracle.search("nvidia",5); 

		//
		// -- PageRank
		//
		DocRank dr = new DocRank(lidx.getLuceneDir(), 7);
		dr.setAlpha(0.9);
		dr.setEpsilon(0.00000001);
		dr.build();

		//
		// -- Combined ranking
		//
		oracle.search("nvidia",5,dr); 		
	}


}
