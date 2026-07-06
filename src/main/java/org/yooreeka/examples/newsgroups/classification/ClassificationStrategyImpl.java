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

package org.yooreeka.examples.newsgroups.classification;

import java.util.Arrays;
import java.util.List;

import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.newsgroups.core.NewsCategory;
import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.core.NewsStoryGroup;

public class ClassificationStrategyImpl implements ClassificationStrategy {

    private NBStoryClassifier storyClassifier;

    private boolean isVerbose=false;

    public ClassificationStrategyImpl() {
    	// EMPTY
    }

    public NBStoryClassifier getStoryClassifier() {
        return storyClassifier;
    }


    public void setStoryClassifier(NBStoryClassifier storyClassifier) {
        this.storyClassifier = storyClassifier;
    }

    /**
     * The selection of a representative story can be construed in many ways.
     * This implementation delegates its job to the <tt>selectLongestStory</tt>.
     *
     * @param newsStories
     * @return
     *
     */
    private NewsStory selectRepresentativeStory(List<NewsStory> newsStories) {

        return selectLongestStory(newsStories);
    }

    private NewsStory selectLongestStory(List<NewsStory> newsStories) {

        NewsStory representativeStory = null;

        int maxContentLength = 0;

        for(NewsStory newsStory : newsStories) {

            int storyContentLength = newsStory.getContent().getText().length();

            if( storyContentLength > maxContentLength ) {
                maxContentLength = storyContentLength;
                representativeStory = newsStory;
            }
        }

        return representativeStory;
    }

    public void assignTopicToCluster(NewsStoryGroup cluster) {

        List<NewsStory> newsStories = cluster.getStories();

        NewsStory representativeStory = selectRepresentativeStory(newsStories);

        NewsCategory bestTopic = selectBestMatchingTopic(representativeStory);

        cluster.setTopic(bestTopic);
        cluster.setRepresentativeStory(representativeStory);

        if (isVerbose) {
	        boolean skipValidMatches = false;
	        evaluateAndPrintResult(representativeStory, bestTopic, skipValidMatches);
        }
    }

    public void assignTopicToStory(NewsStory newsStory) {

        Instance instance = storyClassifier.toInstance(newsStory);
        Concept concept = storyClassifier.classify(instance);

        NewsCategory bestTopic = storyClassifier.toTopic(concept);
        newsStory.setTopic(bestTopic);

        // for debugging purposes
        boolean skipValidMatches = false;
        evaluateAndPrintResult(newsStory, bestTopic, skipValidMatches);
    }

    private NewsCategory selectBestMatchingTopic(NewsStory newsStory) {
        Instance instance = storyClassifier.toInstance(newsStory);
        Concept concept = storyClassifier.classify(instance);
        return storyClassifier.toTopic(concept);
    }

    private boolean evaluateAndPrintResult(NewsStory newsStory, NewsCategory matchedTopic, boolean skipValidMatches) {
        // NewsCategory that was assigned to story during loading.
        // Derived from document name.
        NewsCategory actualTopic = newsStory.getTopic();
        boolean isCorrect = false;
        if( actualTopic != null && actualTopic.equals(matchedTopic) ) {
            isCorrect = true;
        }

        if( skipValidMatches && isCorrect ) {
            return isCorrect;
        }

        System.out.print(isCorrect + " " +
                newsStory.getTitle() + " -> " + matchedTopic.getName());

        // Show topN scores
        int topN = 4;
        List<ClassificationResult> scores = storyClassifier.getTopNScores(topN);
        System.out.print(" ( Top " + topN + " scores: ");
        for(ClassificationResult r : scores) {
             System.out.printf("%s -> %.3f, ", r.getConcept().getName(), r.getScore());
        }

        System.out.print(") ");
        System.out.println("Doc terms: " + Arrays.toString(newsStory.getTopNTerms()));

        return isCorrect;
    }

	/**
	 * @return the isVerbose
	 */
	public boolean isVerbose() {
		return isVerbose;
	}

	/**
	 * @param isVerbose the isVerbose to set
	 */
	public void setVerbose(boolean isVerbose) {
		this.isVerbose = isVerbose;
	}

}