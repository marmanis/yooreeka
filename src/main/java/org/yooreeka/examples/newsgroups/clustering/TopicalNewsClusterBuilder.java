package org.yooreeka.examples.newsgroups.clustering;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yooreeka.algos.clustering.model.Cluster;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.model.Dendrogram;
import org.yooreeka.algos.clustering.rock.ROCKAlgorithm;
import org.yooreeka.examples.newsgroups.core.NewsCategory;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.core.NewsStoryGroup;

public class TopicalNewsClusterBuilder {

    private NewsDataset ds;

    public TopicalNewsClusterBuilder() {
    }

    public void setNewsDataset(NewsDataset ds) {
        this.ds = ds;
    }

    public void cluster() {

        for(NewsCategory topic : ds.getTopics() ) {
            List<NewsStory> stories = ds.getStories(topic);
            DataPoint[] dataPoints = createDataPoints(stories);
            int k = dataPoints.length / 3;
            double th = 0.15;
            ROCKAlgorithm rock = new ROCKAlgorithm(dataPoints, k, th);
            Dendrogram dnd = rock.cluster();
            List<NewsStoryGroup> storyGroups = createStoryGroups(topic, dnd);
            System.out.println("Topic: " + topic.getName() + " created clusters: " + storyGroups.size());
            // save clusters in the dataset
            for(NewsStoryGroup cluster : storyGroups) {
                ds.addStoryGroup(cluster);
            }
        }

        boolean skipSingletons = false;
        NewsStoryGroup.printSimilarStories(ds.getStoryGroups(), skipSingletons);
        // dnd.printAll();
    }

    private List<NewsStoryGroup> createStoryGroups(NewsCategory topic, Dendrogram dnd) {

        int groupId = 1;
        List<NewsStoryGroup> allGroups = new ArrayList<NewsStoryGroup>();

        int topLevel = dnd.getTopLevel();
        List<Cluster> allClusters = dnd.getClustersForLevel(topLevel);

        for(Cluster c : allClusters) {
            String uniqueGroupId = topic.getName() + "-" + String.valueOf(groupId);
            NewsStoryGroup sg = createStoryGroup(uniqueGroupId, c);
            sg.setTopic(topic);
            NewsStory rpStory = selectRepresentativeStory(sg.getStories());
            sg.setRepresentativeStory(rpStory);
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
    private DataPoint[] createDataPoints(List<NewsStory> stories) {
        int nDataPoints = stories.size();
        DataPoint[] allDataPoints = new DataPoint[nDataPoints];
        Iterator<NewsStory> iter = stories.iterator();
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

}