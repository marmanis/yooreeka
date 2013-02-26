package org.yooreeka.examples.newsgroups.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.yooreeka.algos.reco.collab.model.Content;

/**
 * This class holds all relevant information about the news item.
 */
public class NewsStory implements java.io.Serializable {

    private static final long serialVersionUID = 1911984399009763173L;

    /*
     * Unique identifier
     */
    private String id;

    /*
     * NewsStory title
     */
    private String title;
//    private String summary;
    private String url;

    private NewsCategory newsCategory;

    /*
     * For evaluation of cluster classification.
     * Allows to keep track of correct newsCategory for the cluster.
     */
    private NewsCategory expectedTopic;


    /*
     * Relevance score within the newsCategory.
     */
    //private double score;

    private Content content;

    public NewsStory() {

    }

    public NewsStory(String id, String title, Content content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Content getContent() {
        return content;
    }

    public String[] getTopNTerms() {
        return content.getTFMap().keySet().toArray(new String[0]);
    }

    public void setContent(Content c) {
        content = c;
    }


//    public String getSummary() {
//        return summary;
//    }
//
//    public void setSummary(String summary) {
//        this.summary = summary;
//    }
//
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public NewsCategory getTopic() {
        return newsCategory;
    }

    public void setTopic(NewsCategory newsCategory) {
        this.newsCategory = newsCategory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NewsStory other = (NewsStory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public NewsCategory getExpectedTopic() {
        if( expectedTopic == null ) {
            expectedTopic = newsCategory;
        }

        return expectedTopic;
    }

    public void setExpectedTopic(NewsCategory expectedTopic) {
        this.expectedTopic = expectedTopic;
    }


    public static void sortByTitle(List<NewsStory> values) {
        Collections.sort(values, new Comparator<NewsStory>() {

            public int compare(NewsStory f1, NewsStory f2) {
                return f1.getTitle().compareTo(f2.getTitle());
            }
        });
    }


}