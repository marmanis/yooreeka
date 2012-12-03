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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.config.YooreekaConfigurator;

/**
 * Utility class that we use as the source for Music data.
 */
public class NewsData {

	public static final String[] USERS = { "Albert", "Alexandra", "Athena",
			"Aurora", "Babis", "Bill", "Bob", "Carl", "Catherine", "Charlie",
			"Constantine", "Dmitry", "Elena", "Eric", "Frank", "George",
			"Jack", "John", "Maria", "Lukas", "Nick", "Terry", "Todd" };

	public static final String[] DOC_SAMPLES = { "biz-01.html", "biz-02.html",
			"biz-03.html", "biz-04.html", "biz-05.html", "biz-06.html",
			"biz-07.html", "sport-01.html", "sport-02.html", "sport-03.html",
			"usa-01.html", "usa-02.html", "usa-03.html", "usa-04.html",
			"world-01.html", "world-02.html", "world-03.html", "world-04.html",
			"world-05.html" };

	/**
	 * Builds data set with all the users where each user is assigned 80% of all
	 * the eligible content, as defined below:
	 * <ul>
	 * <li>Users whose name starts from A to D will have 'business' and 'sport'
	 * content.</li>
	 * <li>Users whose name starts from E to Z will have 'usa' and 'world'
	 * content.</li>
	 * </ul>
	 */
	public static BaseDataset createDataset() {
		BaseDataset ds = new BaseDataset();

		/* Create items first */
		ContentItem[] allItems = loadAllNewsItems();

		for (ContentItem item : allItems) {
			ds.addItem(item);
		}

		for (int i = 0, n = USERS.length; i < n; i++) {
			int userId = i;
			String userName = USERS[i];
			ContentItem[] eligibleDocs = null;
			if (userName.toLowerCase().charAt(0) <= 'd') {
				eligibleDocs = selectEligibleDocs(allItems, new String[] {
						"biz", "sport" });
			} else {
				eligibleDocs = selectEligibleDocs(allItems, new String[] {
						"usa", "world" });
			}

			/*
			 * Percent of document items that will be selected from provided
			 * group of items.
			 */
			double percentOfDocs = 0.80;

			ContentItem[] docs = pickRandomDocs(eligibleDocs, percentOfDocs);

			NewsUser u = new NewsUser(userId, userName);
			for (ContentItem doc : docs) {
				u.addUserContent(doc.getItemContent());
			}

			ds.add(u);
		}

		return ds;
	}

	// private static Item createItem(String docName) {
	// int id = -1;
	// for(int i = 0, n = DOC_SAMPLES.length; i < n; i++) {
	// if( DOC_SAMPLES[i].equals(docName)) {
	// id = i;
	// break;
	// }
	// }
	//
	// if( id < 0 ) {
	// throw new IllegalArgumentException("Invalid document name: '" + docName +
	// "'. This document is not on the list of predefined documents.");
	// }
	//
	// return createDocItem(id, docName);
	// }

	private static ContentItem createNewsItem(int docId, String docName) {
		Content content = loadContent(docName);
		ContentItem docItem = new ContentItem(docId, docName, content);
		// docItem.setItemContent(content);
		return docItem;
	}

	/**
	 * Returns array of new ContentItem instances for every document listed in
	 * <code>DOC_SAMPLES</code> array.
	 */
	private static ContentItem[] loadAllNewsItems() {
		ContentItem[] allItems = new ContentItem[NewsData.DOC_SAMPLES.length];
		for (int i = 0, n = allItems.length; i < n; i++) {
			int id = i;
			String name = NewsData.DOC_SAMPLES[i];
			ContentItem item = createNewsItem(id, name);
			allItems[i] = item;
		}
		return allItems;
	}

	private static Content loadContent(String docName) {
		return new HTMLContent(docName, YooreekaConfigurator.getHome()
				+ "/data/ch02/" + docName);
	}

	/**
	 * Returns a random selection of documents.
	 * 
	 * @param newsItems
	 *            list of documents to pick from
	 * @param percentOfDocs
	 *            determines size of returned selection.
	 * 
	 * @return array of songs.
	 */
	private static ContentItem[] pickRandomDocs(ContentItem[] newsItems,
			double percentOfDocs) {

		if (percentOfDocs < 0.0 || percentOfDocs > 1.0) {
			throw new IllegalArgumentException(
					"Value for 'percentOfDocs' argument should be "
							+ "between 0 and 1.");
		}

		Random rand = new Random();
		int sampleSize = (int) Math.round(percentOfDocs * newsItems.length);
		Map<Integer, Item> pickedItems = new HashMap<Integer, Item>();
		while (pickedItems.size() < sampleSize) {
			int itemId = rand.nextInt(newsItems.length);
			Item item = newsItems[itemId];
			if (!pickedItems.containsKey(item.getId())) {
				pickedItems.put(item.getId(), item);
			}
		}

		return pickedItems.values()
				.toArray(new ContentItem[pickedItems.size()]);
	}

	private static ContentItem[] selectEligibleDocs(ContentItem[] docs,
			String[] prefixes) {
		List<ContentItem> eligibleDocs = new ArrayList<ContentItem>();
		for (ContentItem doc : docs) {
			for (String prefix : prefixes) {
				if (doc.getName().startsWith(prefix)) {
					eligibleDocs.add(doc);
					break;
				}
			}
		}
		return eligibleDocs.toArray(new ContentItem[eligibleDocs.size()]);
	}
}
