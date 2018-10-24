package org.yooreeka.examples.newsgroups.classification;

import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.core.NewsStoryGroup;

public interface ClassificationStrategy {
    public void assignTopicToCluster(NewsStoryGroup cluster);
    public void assignTopicToStory(NewsStory newsStory);
}