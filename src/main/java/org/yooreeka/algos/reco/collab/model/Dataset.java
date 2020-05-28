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

import java.util.Collection;

/**
 * Defines service that provides access to all users, items, and ratings.
 * Recommender and similarity implementations rely on this service to access
 * data.
 */
public interface Dataset {

	/**
	 * For content-based dataset returns array of terms that represent document
	 * space.
	 * 
	 * @return
	 */
	public String[] getAllTerms();

	/**
	 * Provides the average rating for this item
	 * 
	 * @param itemId
	 * @return
	 */
	public double getAverageItemRating(int itemId);

	/**
	 * Provides the average rating for this user
	 * 
	 * @param userId
	 * @return
	 */
	public double getAverageUserRating(int userId);

	/**
	 * Retrieves a specific item.
	 * 
	 * @param itemId
	 *            item id.
	 * @return item.
	 */
	public Item getItem(Integer itemId);

	/**
	 * Total number of all available items.
	 * 
	 * @return number of items.
	 */
	public int getItemCount();

	/**
	 * Retrieves all items.
	 * 
	 * @return collection of all items.
	 */
	public Collection<Item> getItems();

	/**
	 * Logical name for the dataset instance.
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Provides access to all ratings.
	 * 
	 * @return collection of ratings.
	 */
	public Collection<Rating> getRatings();

	/**
	 * Total number of all available item ratings.
	 * 
	 * @return number of item ratings by users.
	 */
	public int getRatingsCount();

	/**
	 * Retrieves a specific user.
	 * 
	 * @param userId
	 *            user id.
	 * @return user.
	 */
	public User getUser(Integer userId);

	/**
	 * Total number of all available users.
	 * 
	 * @return number of users.
	 */
	public int getUserCount();

	/**
	 * Retrieves all users.
	 * 
	 * @return collection of users.
	 */
	public Collection<User> getUsers();

	/**
	 * Provides information about user and item ids returned by this dataset.
	 * 
	 * @return true if ids aren't in sequence and can't be used as array
	 *         indexes. false if user or items ids can be treated as sequences
	 *         that start with 1. In this case index will be derived from id:
	 *         index = id - 1.
	 */
	public boolean isIdMappingRequired();
}
