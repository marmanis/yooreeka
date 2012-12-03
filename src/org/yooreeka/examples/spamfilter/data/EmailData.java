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
package org.yooreeka.examples.spamfilter.data;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.spamfilter.EmailClassifier;
import org.yooreeka.util.parsing.common.ProcessedDocument;
import org.yooreeka.util.parsing.html.HTMLDocumentParser;

public class EmailData {

	/*
	 * List of html files that we will treat as emails.
	 */
	public static String[][] TRAINING_DATA = new String[][] {
			{ YooreekaConfigurator.getHome() + "/data/ch02/biz-02.html",
					"A@sengerhost", "1@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/biz-03.html",
					"B@sengerhost", "2@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/biz-04.html",
					"C@sengerhost", "3@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/biz-05.html",
					"D@sengerhost", "4@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/biz-06.html",
					"E@sengerhost", "5@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/biz-07.html",
					"F@sengerhost", "6@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/sport-02.html",
					"G@sengerhost", "7@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/sport-03.html",
					"H@sengerhost", "8@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/usa-02.html",
					"I@sengerhost", "9@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/usa-03.html",
					"J@sengerhost", "10@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/usa-04.html",
					"K@sengerhost", "11@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/world-02.html",
					"L@sengerhost", "12@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/world-03.html",
					"M@sengerhost", "13@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/world-04.html",
					"N@sengerhost", "14@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/world-05.html",
					"O@sengerhost", "15@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/spam-biz-02.html",
					"P@sengerhost", "16@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/spam-biz-03.html",
					"Q@sengerhost", "17@host" } };

	public static String[][] TEST_DATA = new String[][] {
			{ YooreekaConfigurator.getHome() + "/data/ch02/biz-01.html",
					"aa@senderhost", "100@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/sport-01.html",
					"bb@senderhost", "101@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/usa-01.html",
					"cc@senderhost", "102@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/world-01.html",
					"dd@senderhost", "103@host" },
			{ YooreekaConfigurator.getHome() + "/data/ch02/spam-biz-01.html",
					"friend@senderhost", "104@host" } };

	public static EmailDataset createTestDataset() {
		List<Email> allEmails = loadEmails(TEST_DATA);
		return new EmailDataset(allEmails);
	}

	public static EmailDataset createTrainingDataset() {
		List<Email> allEmails = loadEmails(TRAINING_DATA);
		return new EmailDataset(allEmails);
	}

	public static Email loadEmailFromHtml(String htmlFile) {

		ProcessedDocument htmlDoc = processHtmlDoc(htmlFile);
		Email email = new Email();
		email.setSubject(htmlDoc.getDocumentTitle());
		email.setTextBody(htmlDoc.getText());

		return email;
	}

	public static List<Email> loadEmails(String[][] allEmails) {

		List<Email> emailList = new ArrayList<Email>();
		for (String[] emailData : allEmails) {
			String filename = emailData[0];
			Email email = loadEmailFromHtml(filename);
			email.setFrom(emailData[1]);
			email.setTo(emailData[2]);
			// use filename as unique id
			String id = filename.substring(filename.lastIndexOf("/") + 1);
			email.setId(id);

			emailList.add(email);
		}

		return emailList;
	}

	public static void main(String[] args) {
		// // Create and train classifier
		// EmailDataset trainEmailDS = EmailData.createTrainingDataset();
		// EmailClassifier emailClassifier = new EmailClassifier(trainEmailDS,
		// 10);
		// emailClassifier.train();
		//
		// // Let's classify some emails from training set. If we can't get them
		// right
		// // then we are in trouble :-)
		// Email email = null;
		// email = trainEmailDS.findEmailById("biz-04.html");
		// emailClassifier.classify(email);
		//
		// email = trainEmailDS.findEmailById("usa-03.html");
		// emailClassifier.classify(email);
		//
		// // Now, let's classify previously unseen emails
		//
		// EmailDataset testEmailDS = EmailData.createTestDataset();
		// email = testEmailDS.findEmailById("biz-01.html");
		// emailClassifier.classify(email);
		//
		// email = testEmailDS.findEmailById("sport-01.html");
		// emailClassifier.classify(email);
		//
		// email = testEmailDS.findEmailById("usa-01.html");
		// emailClassifier.classify(email);
		//
		// email = testEmailDS.findEmailById("world-01.html");
		// emailClassifier.classify(email);
		//
		// email = testEmailDS.findEmailById("spam-biz-01.html");
		// emailClassifier.classify(email);

		// Create and train classifier
		EmailDataset trainEmailDS = EmailData.createTrainingDataset();
		EmailClassifier spamFilter = new EmailClassifier(trainEmailDS, 10);
		spamFilter.train();

		// Let's classify some emails from training set. If we can't get them
		// right
		// then we are in trouble :-)
		Email email = null;
		email = trainEmailDS.findEmailById("biz-04.html");
		spamFilter.classify(email);

		email = trainEmailDS.findEmailById("usa-03.html");
		spamFilter.classify(email);

		// Now, let's classify previously unseen emails

		EmailDataset testEmailDS = EmailData.createTestDataset();
		email = testEmailDS.findEmailById("biz-01.html");
		spamFilter.classify(email);

		email = testEmailDS.findEmailById("sport-01.html");
		spamFilter.classify(email);

		email = testEmailDS.findEmailById("usa-01.html");
		spamFilter.classify(email);

		email = testEmailDS.findEmailById("world-01.html");
		spamFilter.classify(email);

		email = testEmailDS.findEmailById("spam-biz-01.html");
		spamFilter.classify(email);

	}

	private static ProcessedDocument processHtmlDoc(String htmlFile) {

		ProcessedDocument doc = null;
		try {
			HTMLDocumentParser htmlParser = new HTMLDocumentParser();
			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(htmlFile));
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			doc = htmlParser.parse(reader);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse html from file: "
					+ htmlFile, e);
		}

		return doc;
	}
}
