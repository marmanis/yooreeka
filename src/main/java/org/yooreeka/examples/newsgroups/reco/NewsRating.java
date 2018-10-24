package org.yooreeka.examples.newsgroups.reco;
public class NewsRating {

    private String userId;
    private String storyId;
    private int rating;

    public NewsRating(String userId, String storyId, int rating) {
        this.userId = userId;
        this.storyId = storyId;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public String getStoryId() {
        return storyId;
    }

    public int getRating() {
        return rating;
    }



}