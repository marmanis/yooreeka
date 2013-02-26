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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.algos.reco.collab.model.Dataset;
import org.yooreeka.algos.reco.collab.model.Item;
import org.yooreeka.algos.reco.collab.model.Rating;
import org.yooreeka.algos.reco.collab.model.User;

/**
 * Dataset implementation that we will use to work with sample data.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 */
public class BaseDataset implements Serializable, Dataset {

	// private static final Logger logger = Logger.getLogger(BaseDataset.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8414181723065929475L;

	public static BaseDataset load(String file) {
		Object o = null;
		File f = new File(file);
		if (f.exists()) {
			try {
				FileInputStream fInStream = new FileInputStream(f);
				BufferedInputStream bufInStream = new BufferedInputStream(
						fInStream);
				ObjectInputStream objInStream = new ObjectInputStream(
						bufInStream);
				o = objInStream.readObject();
				objInStream.close();
			} catch (Exception e) {
				throw new RuntimeException(
						"Error while loading data from file: '" + file + "'", e);
			}
		} else {
			throw new IllegalArgumentException("File doesn't exist: '" + file
					+ "'.");
		}
		System.out.println("loaded dataset from file");
		return (BaseDataset) o;
	}

	public static void save(String file, BaseDataset o) {
		try {
			File f = new File(file);
			FileOutputStream foutStream = new FileOutputStream(f);
			BufferedOutputStream boutStream = new BufferedOutputStream(
					foutStream);
			ObjectOutputStream objOutputStream = new ObjectOutputStream(
					boutStream);
			objOutputStream.writeObject(o);
			objOutputStream.flush();
			boutStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Error while saving data into file: '"
					+ file + "'", e);
		}
	}

	/*
	 * Dataset name
	 */
	private String name = getClass().getSimpleName()
			+ System.currentTimeMillis();

	/*
	 * All item ratings.
	 */
	private List<Rating> allRatings = new ArrayList<Rating>();

	/*
	 * Map of all users.
	 */
	private Map<Integer, User> allUsers = new HashMap<Integer, User>();

	/*
	 * Map of all items.
	 */
	private Map<Integer, Item> allItems = new HashMap<Integer, Item>();

	/*
	 * Map of item ratings by user id.
	 */
	Map<Integer, List<Rating>> ratingsByUserId = new HashMap<Integer, List<Rating>>();

	Set<String> allTermsSet = new HashSet<String>();

	/**
	 * Auxiliary method for loading users one by one. This is for demonstration
	 * purposes. Use other kind of loaders for loading data en mass.
	 * 
	 * @param u
	 *            denotes a User who has rated certain items and we want to add
	 *            his ratings in this dataset
	 * @return true if no errors occurred and all data have been added.
	 *         Otherwise, return false but do add whatever we can.
	 */
	public boolean add(User u) {

		boolean addedUser = true;

		// Auxiliary
		Item item;

		// Add the ratings
		Collection<Rating> urc = u.getAllRatings();
		Rating[] uRatings = urc.toArray(new Rating[urc.size()]);

		// Add the user
		if (!allUsers.containsKey(u.getId())) {
			this.allUsers.put(u.getId(), u);

			for (Content content : u.getUserContent()) {
				updateTerms(content.getTerms());
			}
		}

		for (Rating r : uRatings) {
			if (!this.allRatings.add(r)) {
				System.out.println("________________________________");
				System.out.println("ERROR >> Could not add rating! ");
				System.out.println("      >> User ID: " + r.getUserId());
				System.out.println("      >> Item ID: " + r.getItemId());
				System.out.println("      >> Rating : " + r.getRating());
				System.out.println("________________________________");

				addedUser = false;
			}

			item = r.getItem();

			/*
			 * Reuse existing item if it is available. Existing item contains
			 * ratings of previously added users and we don't want to overwrite
			 * them in case new item is a different instance with the same id.
			 */
			if (!allItems.containsKey(item.getId())) {
				this.allItems.put(item.getId(), item);
			}

			// Populate item ratings if item doesn't have them
			// Note that here we rely on all users/ratings sharing the same
			// instance of an item.
			if (item.getUserRating(u.getId()) == null) {
				item.addUserRating(r);
			}
		}

		return addedUser;
	}

	/*
	 * Auxiliary method for loading items one by one. This is for demonstration
	 * purposes. Can be used when we want to link users and items using item
	 * content instead of rating. In such cases ratings won't be available and
	 * as a result <code>add(User)</code> won't be able to derive any Items
	 * through user ratings.
	 */
	public boolean addItem(Item item) {
		boolean addedItem = false;
		if (!allItems.containsKey(item.getId())) {
			this.allItems.put(item.getId(), item);
			addedItem = true;

			Content content = item.getItemContent();
			updateTerms(content.getTerms());
		}
		return addedItem;
	}

	public Item findItemByName(String name) {
		Item matchedItem = null;
		for (Item item : this.allItems.values()) {
			if (name.equalsIgnoreCase(item.getName())) {
				matchedItem = item;
				break;
			}
		}
		return matchedItem;

	}

	public User findUserByName(String name) {
		User matchedUser = null;
		for (User user : this.allUsers.values()) {
			if (name.equalsIgnoreCase(user.getName())) {
				matchedUser = user;
				break;
			}
		}
		return matchedUser;
	}

	public String[] getAllTerms() {
		return allTermsSet.toArray(new String[allTermsSet.size()]);
	}

	public double getAverageItemRating(int itemId) {
		return getItem(itemId).getAverageRating();
	}

	public double getAverageUserRating(int userId) {
		return getUser(userId).getAverageRating();
	}

	public Item getItem(Integer itemId) {
		return allItems.get(itemId);
	}

	public int getItemCount() {
		return allItems.size();
	}

	public Collection<Item> getItems() {
		return allItems.values();
	}

	public String getName() {
		return name;
	}

	public List<Item> getRatedItems(Integer userId) {
		List<Item> ratedItems = new ArrayList<Item>();
		User user = getUser(userId);
		Collection<Rating> userRatings = user.getAllRatings();
		for (Rating r : userRatings) {
			Item ratedItem = getItem(r.getItemId());
			ratedItems.add(ratedItem);
		}
		return ratedItems;
	}

	public Collection<Rating> getRatings() {
		return this.allRatings;
	}

	public int getRatingsCount() {
		return allRatings.size();
	}

	public User getUser(Integer userId) {
		return allUsers.get(userId);
	}

	public int getUserCount() {
		return allUsers.size();
	}

	public Collection<User> getUsers() {
		return allUsers.values();
	}

	public boolean isIdMappingRequired() {
		return true;
	}

	public ContentItem pickContentItem(String name) {
		ContentItem contentItem = null;

		for (Map.Entry<Integer, Item> entry : allItems.entrySet()) {
			Item anItem = entry.getValue();
			if (name.equals(anItem.getName())) {
				contentItem = new ContentItem(entry.getValue());
				break;
			}
		}
		return contentItem;
	}

	public Item pickItem(String name) {
		Item item = null;
		for (Map.Entry<Integer, Item> entry : allItems.entrySet()) {
			Item anItem = entry.getValue();
			if (name.equals(anItem.getName())) {
				item = entry.getValue();
				break;
			}
		}
		return item;
	}

	public User pickUser(String name) {
		User user = null;
		for (Map.Entry<Integer, User> entry : allUsers.entrySet()) {
			User aUser = entry.getValue();
			if (name.equals(aUser.getName())) {
				user = entry.getValue();
				break;
			}
		}
		return user;
	}

	/**
	 * Prints all ratings by item.
	 */
	public void printItemRatings() {
		System.out.println("\nItem ratings:\n");
		for (Item item : allItems.values()) {
			System.out.println("Item: " + item.getName());
			for (Rating r : item.getAllRatings()) {
				User user = this.allUsers.get(r.getUserId());
				System.out.println(" Rated by " + user.getName() + " as "
						+ r.getRating());
			}
		}
	}

	/**
	 * Prints all ratings by item.
	 */
	public void printUserRatings() {
		System.out.println("\nUser ratings:\n");
		for (User user : allUsers.values()) {
			System.out.println("User: " + user.getName());
			for (Rating r : user.getAllRatings()) {
				Item item = allItems.get(r.getItemId());
				System.out.println(" Rated " + item.getName() + " as "
						+ r.getRating());
			}
		}
	}

	public void save(String file) {
		BaseDataset.save(file, this);
		System.out.println("saved dataset into file");
	}

	private void updateTerms(String[] terms) {
		for (String term : terms) {
			allTermsSet.add(term);
		}
	}

}
