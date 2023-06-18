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


import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.fraud.NNFraudClassifier;
import org.yooreeka.examples.fraud.data.TransactionDataset;
import org.yooreeka.examples.fraud.data.TransactionLoader;
import org.yooreeka.examples.fraud.util.FraudErrorEstimator;
import org.yooreeka.examples.spamfilter.EmailClassifier;
import org.yooreeka.examples.spamfilter.EmailRuleClassifier;
import org.yooreeka.examples.spamfilter.data.Email;
import org.yooreeka.examples.spamfilter.data.EmailData;
import org.yooreeka.examples.spamfilter.data.EmailDataset;
import org.yooreeka.util.P;

import junit.framework.TestCase;

public class Ch5BeanShellScriptsTest extends TestCase {

	private String yHome;
	private EmailDataset emailTrainingDataset;
	private EmailDataset emailTestingDataset;
	private TransactionDataset transactionDataset;
	private long t;
	
    public Ch5BeanShellScriptsTest(String name) {
        super(name);
        
        t = System.currentTimeMillis();
		yHome = YooreekaConfigurator.getHome();
		
		emailTrainingDataset = EmailData.createTrainingDataset();
		emailTestingDataset = EmailData.createTestDataset();

		transactionDataset = TransactionLoader.loadTrainingDataset();
    }
    
    public void test_evalCh5Scripts() throws Exception {
        // ScriptEvalUtils.runScripts("ch5");
    	
    	test_Ch5_1();
    	test_Ch5_2();
    	test_Ch5_3();
    	
    	P.timePassedSince(t);
    }

	public void test_Ch5_1() {
		// Create and train classifier
		EmailClassifier emailFilter = new EmailClassifier(emailTrainingDataset, 10);
		emailFilter.train();

		// Let's classify some emails from training set. If we can't get them right
		// then we are in trouble :-)
		Email email = null;
		email = emailTrainingDataset.findEmailById("biz-04.html");
		emailFilter.classify(email);

		email = emailTrainingDataset.findEmailById("usa-03.html");
		emailFilter.classify(email);

		// Now, let's classify previously unseen emails
		email = emailTestingDataset.findEmailById("biz-01.html");
		emailFilter.classify(email);

		email = emailTestingDataset.findEmailById("sport-01.html");
		emailFilter.classify(email);

		email = emailTestingDataset.findEmailById("usa-01.html");
		emailFilter.classify(email);

		email = emailTestingDataset.findEmailById("world-01.html");
		emailFilter.classify(email);

		email = emailTestingDataset.findEmailById("spam-biz-01.html");
		emailFilter.classify(email);
	}
	
	public void test_Ch5_2() {

		// Create classifier based on rules        

		// Expecting one spam email
		EmailRuleClassifier classifier = new EmailRuleClassifier(yHome+"/data/ch05/spamRules.drl");
		classifier.train();

		classifier.run(this.emailTestingDataset,"Expecting one spam email.  :-(");

		// There should be no spam emails.
		// Rule that checks for known email address should win over rules that detect spam content.
		//
		classifier = new EmailRuleClassifier(yHome+"/data/ch05/spamRulesWithConflict.drl");

		classifier.train();

		classifier.run(this.emailTestingDataset,"No spam emails here. Hurray!\n");
	}
	
	public void test_Ch5_3() {

		transactionDataset.calculateUserStats();

		//
		//CREATE the classifier
		//
		NNFraudClassifier nnFraudClassifier = new NNFraudClassifier(transactionDataset);

		// Give it a name. 
		// It will be used later when we serialize the classifier
		nnFraudClassifier.setName("MyNeuralClassifier");

		//
		//TRAIN the classifier
		//

		// Configure classifier with attributes that will be used as inputs into NN
		nnFraudClassifier.useDefaultAttributes();

		// Set the number of training iterations
		nnFraudClassifier.setNTrainingIterations(10);

		// Start the training ...
		nnFraudClassifier.train();

		//
		// STORE the classifier
		//
		nnFraudClassifier.save();


		// You can load a previously saved classifier
		NNFraudClassifier nnClone = NNFraudClassifier.load(nnFraudClassifier.getName());

		// Classify a couple of samples from Training set

		// This should be a legitimate transaction
		nnClone.classify("1");

		// This should be a fraudulent transaction
		nnClone.classify("305");

		// Now, calculate error rate for test set
		TransactionDataset testDS = TransactionLoader.loadTestDataset();

		FraudErrorEstimator auditor = new FraudErrorEstimator(testDS, nnClone);

		auditor.run();

	}
}
