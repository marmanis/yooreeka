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
package org.yooreeka.algos.reco.content.digg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.headzoo.net.services.digg.Rooster;
import com.headzoo.net.services.digg.exceptions.DiggRequestException;

import de.thesuntoucher.jigg.Jigg;
import de.thesuntoucher.jigg.args.StoriesArguments;
import de.thesuntoucher.jigg.data.Container;
import de.thesuntoucher.jigg.data.Story;
import de.thesuntoucher.jigg.data.User;

public class DiggService {

	private static final int MAX_ITEM_COUNT_PER_REQUEST = 100;
	private static final int DEFAULT_ITEM_COUNT_PER_CATEGORY = 20;
	private int itemCountPerCategory = 20;
	private String API_KEY = "http://www.manning.com"; // "http://code.google.com/p/jigg";
	private Jigg jigg;

	public DiggService() {
		jigg = new Jigg(API_KEY);
		setItemCountPerCategory(DEFAULT_ITEM_COUNT_PER_CATEGORY);
	}

	/**
	 * Get popular stories in a specific container
	 * 
	 * @throws IOException
	 * @throws DiggRequestException
	 */
	public List<DiggStoryItem> fetchPopular(String container)
			throws DiggRequestException, IOException {
		/*
		 * The first thing you need to do is create an instance of the Rooster
		 * class. You will need to pass your application key as a constructor
		 * parameter.
		 * 
		 * @link http://apidoc.digg.com/ApplicationKeys
		 */
		Rooster rooster = new Rooster("http://www.manning.com/marmanis");

		com.headzoo.net.services.digg.types.collections.StoryList stories = null;
		// com.headzoo.net.services.digg.types.collections.Container c =
		// getDiggContainer(container);
		stories = rooster.stories().fetchAll(); // .fetchPopularInContainer(c);

		ArrayList<DiggStoryItem> storiesList = new ArrayList<DiggStoryItem>(
				stories.size());
		for (com.headzoo.net.services.digg.types.Story s : stories) {
			DiggStoryItem dsi = new DiggStoryItem((int) s.getId(),
					s.getTitle(), s.getDescription());
			if (s.getUser() != null) {
				dsi.setUsername(s.getUser().getName());
			}
			if (s.getLink() != null) {
				dsi.setLink(s.getLink().toExternalForm());
			}
			storiesList.add(dsi);
		}
		return storiesList;
	}

	/**
	 * Utility method to retrieve a set of stories from each category.
	 * 
	 * @return list of stories.
	 */
	public List<DiggStoryItem> getAllStories() {
		List<DiggStoryItem> newsItems = new ArrayList<DiggStoryItem>();
		for (DiggCategory c : DiggCategory.getAllCategories()) {
			newsItems.addAll(getStories(c));
		}
		return newsItems;
	}

	public com.headzoo.net.services.digg.types.collections.Container getDiggContainer(
			String val) {
		com.headzoo.net.services.digg.types.collections.Container c;

		if (val.equalsIgnoreCase("tech")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"Technology", "technology");

		} else if (val.equalsIgnoreCase("world")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"World & Business", "world_business");

		} else if (val.equalsIgnoreCase("biz")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"World & Business", "world_business");

		} else if (val.equalsIgnoreCase("sci")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"Science", "science");

		} else if (val.equalsIgnoreCase("game")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"Gaming", "gaming");

		} else if (val.equalsIgnoreCase("life")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"Lifestyle", "lifestyle");

		} else if (val.equalsIgnoreCase("fun")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"Entertainment", "entertainment");

		} else if (val.equalsIgnoreCase("sport")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"Sports", "sports");

		} else if (val.equalsIgnoreCase("offb")) {

			c = new com.headzoo.net.services.digg.types.collections.Container(
					"Offbeat", "offbeat");

		} else {
			StringBuilder str = new StringBuilder(
					"Not known Container alias.\n");
			str.append("Try one of the following: \n");
			str.append("    tech   -->  Container(\"Technology\", \"technology\")");
			str.append("    world  -->  Container(\"World & Business\", \"world_business\")");
			str.append("    biz    -->  Container(\"World & Business\", \"world_business\")");
			str.append("    sci    -->  Container(\"Science\", \"science\")");
			str.append("    game   -->  Container(\"Gaming\", \"gaming\")");
			str.append("    life   -->  Container(\"Lifestyle\", \"lifestyle\")");
			str.append("    fun    -->  Container(\"Entertainment\", \"entertainment\")");
			str.append("    sport  -->  Container(\"Sports\", \"sports\")");
			str.append("    offb   -->  Container(\"Offbeat\", \"offbeat\")");

			throw new IllegalArgumentException();
		}

		return c;
	}

	public int getItemCountPerCategory() {
		return this.itemCountPerCategory;
	}

	/**
	 * 
	 * @param category
	 * @return
	 */
	public List<DiggStoryItem> getStories(DiggCategory category) {

		StoriesArguments storiesArgs = new StoriesArguments();
		storiesArgs.setCount(itemCountPerCategory);

		List<Story> stories = jigg.getPopularStories(category, storiesArgs);

		List<DiggStoryItem> items = new ArrayList<DiggStoryItem>();

		for (Story story : stories) {

			int itemId = story.getId();
			String itemName = story.getTitle();
			String description = story.getDescription();

			DiggStoryItem item = new DiggStoryItem(itemId, itemName,
					description);
			item.print();

			// additional fields
			item.setLink(story.getLink());
			item.setTopic(story.getTopic().getName());
			if (story.getUser() != null) {
				item.setUsername(story.getUser().getName());
			}

			items.add(item);
		}
		return items;
	}

	/**
	 * Retrieves a set of stories submitted by user.
	 * 
	 * @param userId
	 *            Digg username
	 * @param maxStories
	 *            max number of stories to retrieve
	 * @return list of stories or empty list if the user doesn't have any.
	 */
	public List<DiggStoryItem> getUserStories(String userId, int maxStories) {
		User user = new User(userId);
		StoriesArguments args = new StoriesArguments();
		args.setCount(maxStories);
		List<Story> stories = jigg.getStories(user, args);
		List<DiggStoryItem> items = new ArrayList<DiggStoryItem>();
		for (Story story : stories) {
			DiggStoryItem item = new DiggStoryItem(story.getId(),
					story.getTitle(), story.getDescription());
			item.setLink(story.getLink());
			item.setTopic(story.getTopic().getName());
			Container container = story.getContainer();
			String categoryName = container.getName();
			item.setCategory(categoryName);
			if (story.getUser() != null) {
				item.setUsername(story.getUser().getName());
			}
			items.add(item);
		}
		return items;
	}

	public void setItemCountPerCategory(int count) {
		this.itemCountPerCategory = Math.min(MAX_ITEM_COUNT_PER_REQUEST, count);
	}
}
