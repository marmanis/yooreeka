package org.yooreeka.examples.newsgroups.core;


public class StorySearchResult {
    private double score;
    private NewsStory newsStory;

    public StorySearchResult() {
        //
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public NewsStory getStory() {
        return newsStory;
    }

    public void setStory(NewsStory newsStory) {
        this.newsStory = newsStory;
    }


}