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
package org.yooreeka.examples.newsgroups.core;

import java.util.Iterator;
import java.util.List;

import org.yooreeka.examples.newsgroups.reco.NewsPortalUser;

public interface NewsDataset extends java.io.Serializable {

	public void init();

	public List<NewsCategory> getTopics();

    public Iterator<NewsStory> getIteratorOverStories();

    public List<NewsStory> getStories();

    public List<NewsStory> getStories(NewsCategory t);

    public String getDatasetName();

    public NewsStory getStoryById(String id);

    // getNumberOfStories();
    public int getSize();

    public void loadStories();

    public void addStoryGroup(NewsStoryGroup newsStoryGroup);

    public NewsStoryGroup findStoryGroup(String groupId);

    public NewsStoryGroup findStoryGroupByStoryId(String storyId);

    public List<NewsStoryGroup> getStoryGroups();

    public List<NewsStoryGroup> getStoryGroupsForTopic(NewsCategory newsCategory);

    public int getNumberOfGroups();

	public String getDocumentDir();

	public void setDocumentDir(String documentDir);

	public String[] getDocumentNames();

	public void setDocumentNames(String[] docNames);

	public int getTopTerms();

	public void setTopTerms(int topNTerms);

	public void setIndexDir(String indexDir);

	public String getIndexDir();

	public List<NewsStory> findStoriesByTitle(String title);

	public NewsPortalUser getUser(String userId);

	public List<NewsPortalUser> getUsers();

	public void loadUsersAndRatings(String fileName);

	/*
	 * Configures filename to use for loading users and ratings. If null loading
	 * of users and ratings will be skipped.
	 */
	public void setUserAndRatingsFilename(String fileName);

	public void loadTopics();
}