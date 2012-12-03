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

import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.algos.reco.collab.model.Item;

public class DiggStoryItem extends Item {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 1924555535749825404L;

	private String link;
	private String description;
	private String topic;
	private String username;
	private String category;

	public DiggStoryItem(int storyId, String title, String description) {
		super(storyId, title);
		this.description = description;
		String text = title + " " + description;
		Content content = new Content(String.valueOf(storyId), text);
		setItemContent(content);
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return getName();
	}

	public String getTopic() {
		return topic;
	}

	public String getUsername() {
		return username;
	}

	public void print() {
		System.out
				.println("---------------------------------------------------------------------");
		System.out.println("Category: " + this.getCategory()
				+ "     -- NewsCategory: " + this.getTopic());
		System.out.println("Title: " + this.getTitle());
		System.out
				.println("_____________________________________________________________________");
		System.out.println("Description:\n" + this.getDescription());
		System.out
				.println("_____________________________________________________________________");
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
