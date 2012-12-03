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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.model.SimilarItem;
import org.yooreeka.algos.reco.collab.recommender.Delphi;
import org.yooreeka.algos.reco.content.digg.DiggService;
import org.yooreeka.algos.reco.content.digg.DiggStoryItem;
import org.yooreeka.algos.reco.content.digg.DiggUser;

public class DiggData {

	public static List<DiggUser> allUsers = new ArrayList<DiggUser>();
	public static List<DiggStoryItem> allStories = new ArrayList<DiggStoryItem>();

	private static final String[] CSV_ITEM_HEADERS = new String[] { "id",
			"username", "title", "category", "topic", "description", "link",
			"userid" };

	public static BaseDataset createDataset() {

		BaseDataset ds = new BaseDataset();

		Delphi delphiIC = createItemContentDelphi();
		int topN = 10;
		for (DiggUser user : allUsers) {
			List<DiggStoryItem> userItems = findItemsByUsername(user.getName());
			for (DiggStoryItem item : userItems) {

				// similar items across all categories
				SimilarItem[] similarItems = delphiIC.findSimilarItems(item,
						topN);

				// Create a set of biased ratings for user using a subset from
				// similar items
				int lowRating = 0;
				int highRating = 0;
				if (user.getName().toLowerCase().charAt(0) <= 'd') {
					// range of ratings for users whose name starts from A to D
					lowRating = 4;
					highRating = 5;
				} else {
					// range of ratings for users whose name starts from E to Z
					lowRating = 1;
					highRating = 3;
				}

				// select 70% of similar items
				Item[] randomItems = pickRandomItems(similarItems, 0.7);
				RatingBuilder ratingBuider = new RatingBuilder();
				List<Rating> ratings = ratingBuider.createBiasedRatings(
						user.getId(), randomItems, lowRating, highRating);
				for (Rating r : ratings) {
					user.addRating(r);
				}
			}
			ds.add(user);
			System.out.println("Generated " + user.getAllRatings().size()
					+ " ratings for user id: " + user.getId() + ", name: "
					+ user.getName() + ", average rating: "
					+ user.getAverageRating());
		}

		System.out.println("Created Dataset with " + ds.getUserCount()
				+ " users, " + ds.getItemCount() + " items, "
				+ ds.getRatingsCount() + " ratings.");

		return ds;
	}

	private static Delphi createItemContentDelphi() {
		BaseDataset ds = new BaseDataset();
		for (DiggUser user : allUsers) {
			ds.add(user);
		}

		for (DiggStoryItem item : allStories) {
			System.out.println("Description:" + item.getDescription());
			ds.addItem(item);
		}

		return new Delphi(ds, RecommendationType.ITEM_CONTENT_BASED, true);
	}

	private static List<DiggStoryItem> findItemsByUsername(String username) {
		List<DiggStoryItem> items = new ArrayList<DiggStoryItem>();
		for (DiggStoryItem item : allStories) {
			if (item.getUsername().equals(username)) {
				items.add(item);
			}
		}
		return items;
	}

	private static DiggUser findUserByUsername(String username) {
		DiggUser matchedUser = null;
		for (DiggUser u : allUsers) {
			if (u.getName().equals(username)) {
				matchedUser = u;
				break;
			}
		}
		return matchedUser;
	}

	/**
	 * Load data from csv file.
	 * 
	 * @param filename
	 */
	public static BaseDataset loadData(String filename) {

		allStories = new ArrayList<DiggStoryItem>();
		allUsers = new ArrayList<DiggUser>();

		CsvListReader csvReader = null;
		try {
			csvReader = new CsvListReader(new BufferedReader(new FileReader(
					filename)), CsvPreference.EXCEL_PREFERENCE);

			csvReader.getCSVHeader(true);

			List<String> line = null;
			while ((line = csvReader.read()) != null) {
				try {
					int id = Integer.valueOf(line.get(0));
					String username = line.get(1);
					String title = line.get(2);
					String category = line.get(3);
					String topic = line.get(4);
					String description = line.get(5);
					String link = line.get(6);
					int userid = Integer.valueOf(line.get(7));

					DiggUser user = findUserByUsername(username);
					if (user == null) {
						user = new DiggUser(userid, username);
						allUsers.add(user);
					}

					DiggStoryItem item = new DiggStoryItem(id, title,
							description);
					item.setUsername(username);
					item.setCategory(category);
					item.setTopic(topic);
					item.setLink(link);
					allStories.add(item);

					// adding item content to the user
					user.addUserContent(item.getItemContent());
				} catch (Exception e) {
					throw new RuntimeException("Error while reading item: ", e);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading digg items from csv file.", e);
		} finally {
			try {
				if (csvReader != null) {
					csvReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("From file: " + filename);
		System.out.println("Loaded " + allUsers.size() + " users.");
		System.out.println("Loaded " + allStories.size() + " stories (items).");

		return DiggData.createDataset();
	}

	/*
	 * Loading data from Digg.
	 * 
	 * @param filename file that will be used to save the data.
	 */
	public static BaseDataset loadDataFromDigg(String filename) {

		allUsers.clear();
		allStories.clear();

		Set<String> allKnownUsers = new HashSet<String>();
		Set<Integer> allKnownStories = new HashSet<Integer>();

		DiggService news = new DiggService();
		news.setItemCountPerCategory(5);
		// Top stories across a set of categories (Technology, Sports, ...)
		List<DiggStoryItem> topStories = news.getAllStories();

		// used to assign unique id to each user
		int nextUserId = 1;

		// iterate through top stories and collect a set of users
		for (DiggStoryItem item : topStories) {
			String username = item.getUsername();
			if (!allKnownUsers.contains(username)) {
				allKnownUsers.add(username);
				int userId = nextUserId++;
				DiggUser diggUser = new DiggUser(userId, username);
				allUsers.add(diggUser);
			}
		}

		// for every user retrieve up to 5 stories
		int maxStories = 5;
		for (DiggUser user : allUsers) {
			List<DiggStoryItem> userItems = news.getUserStories(user.getName(),
					maxStories);

			for (DiggStoryItem i : userItems) {
				if (!allKnownStories.contains(i.getId())) {
					allStories.add(i);
					allKnownStories.add(i.getId());
				} else {
					System.out.println("Duplicate story: id=" + i.getId()
							+ ", name=" + i.getName());
				}
				// adding item content to the user
				user.addUserContent(i.getItemContent());
			}
		}
		System.out.println("From Digg:");
		System.out.println("Loaded " + allUsers.size() + " users.");
		System.out.println("Loaded " + allStories.size() + " stories (items).");

		DiggData.saveData(filename);
		return DiggData.createDataset();
	}

	private static Item[] pickRandomItems(SimilarItem[] items,
			double percentOfAllItems) {

		if (percentOfAllItems < 0.0 || percentOfAllItems > 1.0) {
			throw new IllegalArgumentException(
					"Value for 'percentOfAllItems' argument should be between 0 and 1.");
		}
		Random rand = new Random();
		int sampleSize = (int) Math.round(percentOfAllItems * items.length);
		Map<Integer, Item> pickedItems = new HashMap<Integer, Item>();
		while (pickedItems.size() < sampleSize) {
			int itemId = rand.nextInt(items.length);
			Item item = items[itemId].getItem();
			if (!pickedItems.containsKey(item.getId())) {
				pickedItems.put(item.getId(), item);
			}
		}

		return pickedItems.values().toArray(new Item[pickedItems.size()]);
	}

	/**
	 * Save data into csv file.
	 * 
	 * @param filename
	 */
	public static void saveData(String filename) {
		String[] data = new String[CSV_ITEM_HEADERS.length];

		CsvListWriter csvWriter = null;
		try {
			csvWriter = new CsvListWriter(new BufferedWriter(new FileWriter(
					filename)), CsvPreference.EXCEL_PREFERENCE);

			csvWriter.writeHeader(CSV_ITEM_HEADERS);

			for (DiggStoryItem item : allStories) {
				try {
					data[0] = String.valueOf(item.getId());
					data[1] = item.getUsername();
					data[2] = item.getTitle();
					data[3] = item.getCategory();
					data[4] = item.getTopic();
					data[5] = item.getDescription();
					data[6] = item.getLink();
					DiggUser user = findUserByUsername(item.getUsername());
					data[7] = String.valueOf(user.getId());
					csvWriter.write(data);
				} catch (Exception e) {
					throw new RuntimeException("Error while writing item "
							+ item.getName() + ": ", e);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while writing digg items into csv file.", e);
		} finally {
			try {
				if (csvWriter != null) {
					csvWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Saved data into file: " + filename);
		System.out.println("saved " + allUsers.size() + " users.");
		System.out.println("saved " + allStories.size() + " stories (items).");

	}

	public static void showUsers() {
		System.out.println("All Users:");
		for (DiggUser user : allUsers) {
			System.out.println("User id:" + user.getId() + ", name: "
					+ user.getName());
		}

	}

}
