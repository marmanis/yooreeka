package org.yooreeka.examples.newsgroups.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Cluster of newsStories.
 */
public class NewsStoryGroup implements java.io.Serializable {

    private static final long serialVersionUID = -6287755225044786570L;

    private String groupId;

    private NewsStory representativeStory;

    /*
     * NewsCategory assigned to this cluster of newsStories as a result of classification.
     */
    private NewsCategory newsCategory;

    /*
     * For evaluation of cluster classification.
     * Allows to keep track of correct newsCategory for the cluster.
     */
    private NewsCategory expectedTopic;

    private Set<NewsStory> newsStories = new HashSet<NewsStory>();

    public NewsStoryGroup() {
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public List<NewsStory> getStories() {
        return new ArrayList<NewsStory>(newsStories);
    }

    public void addStory(NewsStory s) {
        newsStories.add(s);
    }

    public static void printSimilarStories(
            List<NewsStoryGroup> allGroups, boolean skipSingletons) {

        for(NewsStoryGroup sg : allGroups) {
            if( skipSingletons && sg.getStories().size() <= 1 ) {
                continue;
            }

            StringBuffer titles = new StringBuffer();
            for(NewsStory s : sg.getStories()) {
                titles.append(s.getTitle());
                titles.append(", ");
            }
            System.out.println(titles.toString());

        }

    }

    public NewsCategory getTopic() {
        return newsCategory;
    }

    public void setTopic(NewsCategory newsCategory) {
        this.newsCategory = newsCategory;
    }

    public NewsCategory getExpectedTopic() {
        return expectedTopic;
    }

    public void setExpectedTopic(NewsCategory expectedTopic) {
        this.expectedTopic = expectedTopic;
    }

    public NewsStory getRepresentativeStory() {
        return representativeStory;
    }

    public void setRepresentativeStory(NewsStory representativeStory) {
        this.representativeStory = representativeStory;
    }

    public String getClusterLabel() {
        String label = null;
        if( representativeStory != null ) {
        	StringBuilder temp = new StringBuilder(representativeStory.getTitle());
        	int extensionIndex = temp.lastIndexOf(".html");
            label = temp.delete(extensionIndex, temp.length()).toString();
        }
        else {
            label = groupId;
        }

        label += " (" + newsStories.size() + " stories)";

        return label;
    }

    public static void sortByLabel(List<NewsStoryGroup> values) {
        Collections.sort(values, new Comparator<NewsStoryGroup>() {

            public int compare(NewsStoryGroup f1, NewsStoryGroup f2) {
                return f1.getClusterLabel().compareTo(f2.getClusterLabel());
            }
        });
    }

    public static void sortBySize(List<NewsStoryGroup> values) {
        Collections.sort(values, new Comparator<NewsStoryGroup>() {

            public int compare(NewsStoryGroup f1, NewsStoryGroup f2) {
            	int f1Size = f1.getStories().size();
            	int f2Size = f2.getStories().size();
            	int val=0;

            	if (f1Size < f2Size) {
            		val=1;
            	} else if (f1Size > f2Size) {
            		val=-1;
            	}
                return val;
            }
        });
    }

}