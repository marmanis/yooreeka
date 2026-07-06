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

package org.yooreeka.examples.newsgroups.clustering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.model.Dendrogram;
import org.yooreeka.algos.clustering.rock.ROCKAlgorithm;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.core.NewsStoryGroup;

public class NewsClusterBuilder {

    private NewsDataset ds;

    private boolean isVerbose = false;

    public NewsClusterBuilder() {
    }

    public void setNewsDataset(NewsDataset ds) {
        this.ds = ds;
    }

    /**
     * Use a heuristic for setting the values of <tt>k</tt> and <tt>linkThreshold</tt>.
     *
     */
    public void cluster() {

        DataPoint[] dataPoints = createDataPoints(ds);
        int k = dataPoints.length / 3;
        double linkThreshold = 0.15;

        ROCKAlgorithm rock = new ROCKAlgorithm(dataPoints, k, linkThreshold);
        Dendrogram dnd = rock.cluster();
        List<NewsStoryGroup> storyGroups = createStoryGroups(dnd);

        // save clusters in the dataset
        for(NewsStoryGroup cluster : storyGroups) {
            ds.addStoryGroup(cluster);
        }

        if (isVerbose) {
	        boolean skipSingletons = false;
	        NewsStoryGroup.printSimilarStories(storyGroups, skipSingletons);
        }
    }

    /**
     * Expose variables in the method call for setting
     * the values of <tt>k</tt> and <tt>linkThreshold</tt>.
     *
     * @param k
     * @param linkThreshold
     */
    public void cluster(int k, double linkThreshold) {

        DataPoint[] dataPoints = createDataPoints(ds);

        ROCKAlgorithm rock = new ROCKAlgorithm(dataPoints, k, linkThreshold);

        Dendrogram dnd = rock.cluster();

        List<NewsStoryGroup> storyGroups = createStoryGroups(dnd);

        // save clusters in the dataset
        for(NewsStoryGroup cluster : storyGroups) {
            ds.addStoryGroup(cluster);
        }

        if (isVerbose) {
	        boolean skipSingletons = false;
	        NewsStoryGroup.printSimilarStories(storyGroups, skipSingletons);
        }
    }


    private List<NewsStoryGroup> createStoryGroups(Dendrogram dnd) {

        int groupId = 1;
        List<NewsStoryGroup> allGroups = new ArrayList<NewsStoryGroup>();

        int topLevel = dnd.getTopLevel();
        List<Cluster> allClusters = dnd.getClustersForLevel(topLevel);

        for(Cluster c : allClusters) {
            NewsStoryGroup sg = createStoryGroup(
                    String.valueOf(groupId), c);
            allGroups.add(sg);
            groupId++;
        }

        return allGroups;
    }

    private NewsStoryGroup createStoryGroup(String groupId, Cluster c) {

        NewsStoryGroup sg = new NewsStoryGroup();
        sg.setGroupId(groupId);
        for(DataPoint dp : c.getElements()) {
            NewsStory newsStory = toStory(dp);
            sg.addStory(newsStory);
        }

        return sg;
    }

    /*
     * Converts all documents from the dataset into training set.
     */
    private DataPoint[] createDataPoints(NewsDataset ds) {
        int nDataPoints = ds.getSize();
        DataPoint[] allDataPoints = new DataPoint[nDataPoints];
        Iterator<NewsStory> iter = ds.getIteratorOverStories();
        int i = 0;
        while(iter.hasNext()) {
            NewsStory newsStory = iter.next();
            DataPoint dataPoint = toDataPoint(newsStory);
            allDataPoints[i] = dataPoint;
            i++;
        }
        return allDataPoints;
    }

    public DataPoint toDataPoint(NewsStory newsStory) {
        return new DataPoint(newsStory.getId(), newsStory.getTopNTerms());
        //return new DataPoint(story.getTitle(), story.getTopNTerms());
    }

    public NewsStory toStory(DataPoint dp) {
        String storyId = dp.getLabel();
        return ds.getStoryById(storyId);
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