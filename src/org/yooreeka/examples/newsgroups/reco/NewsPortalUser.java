package org.yooreeka.examples.newsgroups.reco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsPortalUser {

    private String id;
    private String name;
    private Map<String, NewsRating> allRatingsByStoryId = new HashMap<String, NewsRating>();

    public NewsPortalUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NewsRating> getAllRatings() {
        return new ArrayList<NewsRating>(allRatingsByStoryId.values());
    }

    public void addRating(NewsRating r) {
        if( id.equals(r.getUserId())) {
            allRatingsByStoryId.put(r.getStoryId(), r);
        }
        else {
            throw new RuntimeException("User ids don't match: userId=" +
                    id + ", rating userId=" + r.getUserId());
        }
    }
}