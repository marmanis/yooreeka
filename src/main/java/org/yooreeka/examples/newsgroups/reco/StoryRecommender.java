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
package org.yooreeka.examples.newsgroups.reco;

import java.util.ArrayList;
import java.util.List;

import org.yooreeka.algos.reco.collab.model.RecommendationType;
import org.yooreeka.algos.reco.collab.model.User;
import org.yooreeka.algos.reco.collab.recommender.Delphi;
import org.yooreeka.algos.reco.collab.recommender.PredictedItemRating;
import org.yooreeka.algos.reco.collab.recommender.Recommender;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsStory;

public class StoryRecommender {

    private DatasetAdapter rDs;
    private Recommender delphi;

    public StoryRecommender(NewsDataset ds) {
        this.rDs = new DatasetAdapter(ds);
    }

    public void calculateRecommendations() {
        Delphi d = new Delphi(rDs, RecommendationType.ITEM_PENALTY_BASED, false);
        d.setVerbose(true);

        this.delphi = d;
    }

    public List<PredictedNewsStoryRating> recommendStories(String newsUserId) {

        if( delphi == null ) {
            String msg = "Recommender not initialized. You need calculate recommnedations first.";
            throw new RuntimeException(msg);
        }
        // Mapping news user to User used by recommender
        User user = rDs.getUserForNewsUserId(newsUserId);

        List<PredictedItemRating> predictedRatings = delphi.recommend(user);

        List<PredictedNewsStoryRating> ratings =
            new ArrayList<PredictedNewsStoryRating>();

        for(PredictedItemRating iR : predictedRatings) {
            PredictedNewsStoryRating r = new PredictedNewsStoryRating();
            r.setUserId(newsUserId);
            r.setRating(iR.getRating());
            // map Item into NewsStory
            NewsStory newsStory = rDs.getNewsStoryForItemId(iR.getItemId());
            r.setStoryId(newsStory.getId());

            ratings.add(r);
        }

        return ratings;
    }

}