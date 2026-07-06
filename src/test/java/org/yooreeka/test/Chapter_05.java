package org.yooreeka.test;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.spamfilter.data.EmailData;
import org.yooreeka.examples.spamfilter.data.EmailDataset;
import org.yooreeka.examples.spamfilter.EmailClassifier;
import org.yooreeka.examples.spamfilter.data.Email;
import org.yooreeka.examples.spamfilter.EmailRuleClassifier;
import org.yooreeka.examples.fraud.data.TransactionDataset;
import org.yooreeka.examples.fraud.data.TransactionLoader;
import org.yooreeka.examples.fraud.NNFraudClassifier;
import org.yooreeka.examples.fraud.util.FraudErrorEstimator;
import org.yooreeka.util.P;

public class Chapter_05 {

	private long t = 0;

	public Chapter_05() {
	}

	public void run() throws Exception {
		t = System.currentTimeMillis();

		// Email Classification
		script_05_01();

		// Email Classification with Rules
		script_05_02();

		// Fraud Detection (Neural Network)
		script_05_03();

		P.timePassedSince(t);
	}

	public void script_05_01() {
		EmailDataset trainEmailDS = EmailData.createTrainingDataset();
		EmailClassifier emailFilter = new EmailClassifier(trainEmailDS, 10);
		emailFilter.train();

		Email email = null;
		email = trainEmailDS.findEmailById("biz-04.html");
		emailFilter.classify(email);

		email = trainEmailDS.findEmailById("usa-03.html");
		emailFilter.classify(email);

		EmailDataset testEmailDS = EmailData.createTestDataset();
		email = testEmailDS.findEmailById("biz-01.html");
		emailFilter.classify(email);

		email = testEmailDS.findEmailById("sport-01.html");
		emailFilter.classify(email);

		email = testEmailDS.findEmailById("usa-01.html");
		emailFilter.classify(email);

		email = testEmailDS.findEmailById("world-01.html");
		emailFilter.classify(email);

		email = testEmailDS.findEmailById("spam-biz-01.html");
		emailFilter.classify(email);
	}

	public void script_05_02() {
		String yHome = YooreekaConfigurator.getHome();
		EmailDataset ds = EmailData.createTestDataset();

		EmailRuleClassifier classifier = new EmailRuleClassifier(yHome + "/data/ch05/spamRules.drl");
		classifier.train();
		classifier.run(ds, "Expecting one spam email.  :-(");

		classifier = new EmailRuleClassifier(yHome + "/data/ch05/spamRulesWithConflict.drl");
		classifier.train();
		classifier.run(ds, "No spam emails here. Hurray!\n");
	}

	public void script_05_03() throws Exception {
		TransactionDataset ds = TransactionLoader.loadTrainingDataset();
		ds.calculateUserStats();

		NNFraudClassifier nnFraudClassifier = new NNFraudClassifier(ds);
		nnFraudClassifier.setName("MyNeuralClassifier");
		nnFraudClassifier.useDefaultAttributes();
		nnFraudClassifier.setNTrainingIterations(10);
		nnFraudClassifier.train();
		nnFraudClassifier.save();

		NNFraudClassifier nnClone = NNFraudClassifier.load(nnFraudClassifier.getName());
		nnClone.classify("1");
		nnClone.classify("305");

		TransactionDataset testDS = TransactionLoader.loadTestDataset();
		FraudErrorEstimator auditor = new FraudErrorEstimator(testDS, nnClone);
		auditor.run();
	}
}
