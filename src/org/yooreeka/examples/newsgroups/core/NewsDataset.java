package org.yooreeka.examples.newsgroups.core;

import java.util.Iterator;
import java.util.List;

import org.yooreeka.examples.newsgroups.reco.NewsPortalUser;

public interface NewsDataset extends java.io.Serializable {

	public void init();

	public List<NewsCategory> getTopics();

    public Iterator<NewsStory> getIteratorOverStories();

    public List<NewsStory> getStories();

    public List<NewsStory> getStories(NewsCategory t);

    public String getDatasetName();

    public NewsStory getStoryById(String id);

    // getNumberOfStories();
    public int getSize();

    public void loadStories();

    public void addStoryGroup(NewsStoryGroup newsStoryGroup);

    public NewsStoryGroup findStoryGroup(String groupId);

    public NewsStoryGroup findStoryGroupByStoryId(String storyId);

    public List<NewsStoryGroup> getStoryGroups();

    public List<NewsStoryGroup> getStoryGroupsForTopic(NewsCategory newsCategory);

    public int getNumberOfGroups();

	public String getDocumentDir();

	public void setDocumentDir(String documentDir);

	public String[] getDocumentNames();

	public void setDocumentNames(String[] docNames);

	public int getTopTerms();

	public void setTopTerms(int topNTerms);

	public void setIndexDir(String indexDir);

	public String getIndexDir();

	public List<NewsStory> findStoriesByTitle(String title);

	public NewsPortalUser getUser(String userId);

	public List<NewsPortalUser> getUsers();

	public void loadUsersAndRatings(String fileName);

	/*
	 * Configures filename to use for loading users and ratings. If null loading
	 * of users and ratings will be skipped.
	 */
	public void setUserAndRatingsFilename(String fileName);

	public void loadTopics();
}