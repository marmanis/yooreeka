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
package org.yooreeka.examples.newsgroups.crawling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.reco.NewsPortalUser;
import org.yooreeka.examples.newsgroups.reco.NewsRating;

public abstract class BaseNewsDataset implements NewsDataset {

	private static final long serialVersionUID = -1733770863766398248L;

	private Map<String, NewsPortalUser> newsUsers = new HashMap<String, NewsPortalUser>();
    private List<NewsRating> allRatings = new ArrayList<NewsRating>();

    private String userAndRatingsFilename = null;

    public BaseNewsDataset() {
    	//DO NOTHING HERE
    }

    public void init() {
        loadTopics();
        loadStories();

        if( userAndRatingsFilename != null ) {
            loadUsersAndRatings(userAndRatingsFilename);
        }
        else {
            System.out.println("No users and ratings to load.");
        }
    }


    public NewsPortalUser getUser(String userId) {
        return newsUsers.get(userId);
    }


    public void setUserAndRatingsFilename(String fileName) {
        this.userAndRatingsFilename = fileName;
    }

    public void loadUsersAndRatings(String fileName) {

        if( newsUsers.size() > 0 ) {
            System.out.println("Users and ratings already loaded.");
            return;
        }

        System.out.println("Loading users and ratings...");
        try {
            File           file = new File(fileName);
            FileReader  fReader = new FileReader(file);

            BufferedReader bR = new BufferedReader(fReader);

            loadUsersAndRatings(bR);
        }
        catch(IOException e) {
            throw new RuntimeException(
                    "Error while loading user and rating data from file: '" +
                    fileName + "'");
        }
    }

    public void loadUsersAndRatings(BufferedReader bR) throws IOException {

        Map<String, NewsPortalUser> loadedUsers = new HashMap<String, NewsPortalUser>();

        String line = null;

        while ( (line = bR.readLine()) != null ) {

            // skip empty lines
            if( line.trim().length() == 0 ) {
                continue;
            }

            // skip comments
            if( line.startsWith("#") ) {
                continue;
            }

            String[] data = line.split(",");

            String userId = data[0];
            String userName = data[1];
            // here we are using story title as a unique identifier of the story.
            String storyTitle = data[2];
            int rating = Integer.parseInt(data[3]);

            NewsPortalUser user = loadedUsers.get(userId);
            if( user == null ) {
                user = new NewsPortalUser();
                user.setId(userId);
                user.setName(userName);
                loadedUsers.put(userId, user);
            }

            NewsStory story = findStoryByUniqueTitle(storyTitle);
            if( story == null ) {
                throw new RuntimeException(
                        "Failed to find story with title: '" + storyTitle + "'");
            }
            NewsRating r = new NewsRating(user.getId(), story.getId(), rating);
            user.addRating(r);
        }

        for(NewsPortalUser u : loadedUsers.values()) {
            // will add users and ratings
            addUser(u);
        }
    }

    private void addUser(NewsPortalUser newsUser) {

        newsUsers.put(newsUser.getId(), newsUser);

         List<NewsRating> userRatings = newsUser.getAllRatings();
         for(NewsRating r : userRatings) {
             addNewsRating(r);
         }
    }

    private void addNewsRating(NewsRating newsRating) {

        NewsPortalUser newsUser = newsUsers.get(newsRating.getUserId());
        NewsStory newsStory = getStoryById(newsRating.getStoryId());
        assert(newsUser != null);
        assert(newsStory != null);

        allRatings.add(newsRating);
    }


    private NewsStory findStoryByUniqueTitle(String title) {

        NewsStory result = null;

        List<NewsStory> stories = findStoriesByTitle(title);

        if( stories.size() == 0 ) {
            result = null;
        }
        else if (stories.size() > 1 ) {
            throw new RuntimeException(
                    "Found multiple stories with the same title: '" +
                    title + "', number of stories found: " + stories.size() +
                    ". You can't use this logic for cases when story title is" +
                    " not unique.");
        }
        else {
            result = stories.get(0);
        }


        return result;
    }

    public List<NewsPortalUser> getUsers() {
        return new ArrayList<NewsPortalUser>( newsUsers.values() );
    }

}