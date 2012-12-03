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
package org.yooreeka.algos.reco.collab.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic representation of product or service that users can rate.
 */
public class Item implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6119040388138010186L;

	public static Integer[] getSharedUserIds(Item x, Item y) {
		List<Integer> sharedUsers = new ArrayList<Integer>();
		for (Rating r : x.getAllRatings()) {
			// same user rated the item
			if (y.getUserRating(r.getUserId()) != null) {
				sharedUsers.add(r.getUserId());
			}
		}
		return sharedUsers.toArray(new Integer[sharedUsers.size()]);
	}

	/*
	 * Unique id in the dataset.
	 */
	private int id;

	/*
	 * Name.
	 */
	private String name;

	/*
	 * All ratings for this item. Supports only one rating per item for a user.
	 * Mapping: userId -> rating
	 */
	private Map<Integer, Rating> ratingsByUserId;

	private Content itemContent;

	public Item(Integer id, List<Rating> ratings) {
		this(id, String.valueOf(id), ratings);
	}

	public Item(Integer id, String name) {
		this(id, name, new ArrayList<Rating>(3));
	}

	public Item(Integer id, String name, List<Rating> ratings) {
		this.id = id;
		this.name = name;
		// load ratings into userId -> rating map.
		ratingsByUserId = new HashMap<Integer, Rating>(ratings.size());
		for (Rating r : ratings) {
			ratingsByUserId.put(r.getUserId(), r);
		}
	}

	/**
	 * Updates existing user rating or adds a new user rating for this item.
	 * 
	 * @param r
	 *            rating to add.
	 */
	public void addUserRating(Rating r) {
		ratingsByUserId.put(r.getUserId(), r);
	}

	/**
	 * Returns all ratings that we have for this item.
	 * 
	 * @return
	 */
	public Collection<Rating> getAllRatings() {
		return ratingsByUserId.values();
	}

	public double getAverageRating() {
		double allRatingsSum = 0.0;
		Collection<Rating> allItemRatings = ratingsByUserId.values();
		for (Rating rating : allItemRatings) {
			allRatingsSum += rating.getRating();
		}
		// use 2.5 if there are no ratings.
		return allItemRatings.size() > 0 ? allRatingsSum
				/ allItemRatings.size() : 2.5;
	}

	public int getId() {
		return id;
	}

	public Content getItemContent() {
		return itemContent;
	}

	public String getName() {
		return name;
	}

	/*
	 * Utility method to extract array of ratings based on array of user ids.
	 */
	public double[] getRatingsForItemList(Integer[] userIds) {
		double[] ratings = new double[userIds.length];
		for (int i = 0, n = userIds.length; i < n; i++) {
			Rating r = getUserRating(userIds[i]);
			if (r == null) {
				throw new IllegalArgumentException(
						"Item doesn't have rating by specified user id ("
								+ "userId=" + userIds[i] + ", itemId="
								+ getId());
			}
			ratings[i] = r.getRating();
		}
		return ratings;
	}

	/**
	 * Returns rating that specified user gave to the item.
	 * 
	 * @param userId
	 *            user
	 * @return user rating or null if user hasn't rated this item.
	 */
	public Rating getUserRating(Integer userId) {
		return ratingsByUserId.get(userId);
	}

	public void setItemContent(Content content) {
		this.itemContent = content;
	}

}
