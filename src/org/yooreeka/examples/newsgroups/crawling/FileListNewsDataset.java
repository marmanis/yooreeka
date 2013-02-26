package org.yooreeka.examples.newsgroups.crawling;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.yooreeka.algos.reco.collab.data.HTMLContent;
import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.examples.newsgroups.core.NewsCategory;
import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.core.NewsStoryGroup;

/**
 * Dataset that is based on documents (files) from file system directory.
 */
public class FileListNewsDataset extends BaseNewsDataset {

    private static final long serialVersionUID = 7768246175864823358L;

    private String datasetName;
    private String documentDir;
    private String[] documentNames;

    private int topTerms = 25;

    private Map<String, NewsStory> allStoriesMap = null; //new HashMap<String, NewsStory>();
    private Map<String, NewsCategory> allTopicsMap = null; // new HashMap<String, NewsCategory>();
    private Map<String, NewsStoryGroup> allStoryGroups = new HashMap<String, NewsStoryGroup>();

    private String indexDir;




    public FileListNewsDataset(String datasetName) {
        this.datasetName = datasetName;
    }

    public FileListNewsDataset(String datasetName, String docDir) {
        this.datasetName = datasetName;
        this.documentDir = docDir;
    }

    public void loadStories() {
    	loadStories(true);
    }

    public void loadStories(boolean assignTopics) {

    	if (documentDir == null) {
    		throw new UnsupportedOperationException("ERROR:"+
    				"  You must set the directory before you use the load() method!");
    	}

    	if (allTopicsMap == null || allTopicsMap.size() == 0) {
    		throw new UnsupportedOperationException("ERROR:"+
			"  You must load the topics before you use the load() method!");
    	}

    	if (documentNames == null) {
	    	File dirFile = new File(documentDir);
	    	documentNames = dirFile.list(
	                // skip directories
	                new FilenameFilter() {
	                    public boolean accept(File dir, String name) {
	                        File f = new File(dir, name);
	                        return f.isFile();
	                    }
	                });
    	}

    	allStoriesMap = new HashMap<String, NewsStory>();

        for(int i = 0, n = documentNames.length; i < n; i++) {

        	String id = String.valueOf(i);
            String name = documentNames[i];

            NewsStory newsStory = createStory(id, name, topTerms);

            if( assignTopics ) {

                String topicName = getTopicKey(name);

                NewsCategory newsCategory = allTopicsMap.get(topicName);

                newsStory.setTopic(newsCategory);
            }

            addStory(newsStory);
        }
    }

    public void loadTopics() {
    	if (allTopicsMap == null) {
    		allTopicsMap = new HashMap<String, NewsCategory>();

    		/*
    		 * Here we rely on the fact that document filenames follow naming
    		 * convention: <TopicMapKey>-<the rest of filename>
    		 */
	        allTopicsMap.put("biz", new NewsCategory(NewsCategory.BUSINESS));
	        allTopicsMap.put("usa", new NewsCategory(NewsCategory.US));
	        allTopicsMap.put("world", new NewsCategory(NewsCategory.WORLD));
	        allTopicsMap.put("health", new NewsCategory(NewsCategory.HEALTH));
	        allTopicsMap.put("sport",  new NewsCategory(NewsCategory.SPORTS));
	        allTopicsMap.put("tech", new NewsCategory(NewsCategory.TECHNOLOGY));
    	} else {
    		System.out.println("Topics already loaded:\n");
    		for (NewsCategory t : getTopics()) {
    			System.out.println(t.getName());
    		}
    	}
    }

    public void resetTopics() {
    	allTopicsMap = new HashMap<String, NewsCategory>();

    	/*
    	 * Here we rely on the fact that document filenames follow naming
    	 * convention: <TopicMapKey>-<the rest of filename>
    	 */
    	allTopicsMap.put("biz", new NewsCategory(NewsCategory.BUSINESS));
    	allTopicsMap.put("usa", new NewsCategory(NewsCategory.US));
    	allTopicsMap.put("world", new NewsCategory(NewsCategory.WORLD));
    	allTopicsMap.put("health", new NewsCategory(NewsCategory.HEALTH));
    	allTopicsMap.put("sport",  new NewsCategory(NewsCategory.SPORTS));
    	allTopicsMap.put("tech", new NewsCategory(NewsCategory.TECHNOLOGY));
    }

    public void addTopic(String key, NewsCategory newsCategory) {
    	if (allTopicsMap == null) {
    		allTopicsMap = new HashMap<String, NewsCategory>();
    		allTopicsMap.put(key,newsCategory);
    	} else {
    		allTopicsMap.put(key,newsCategory);
    	}
    }

    private static String getTopicKey(String docName) {

        if( docName == null ) {
            return null;
        }

        String parts[] = docName.split("-");
        return parts[0].toLowerCase();
    }


    private void addStory(NewsStory newsStory) {
        allStoriesMap.put(newsStory.getId(), newsStory);
    }

    private NewsStory createStory(String docId, String docName, int topNTerms) {

        Content content = loadContent(documentDir, docName, topNTerms);

        File file = new File(documentDir, docName);

        NewsStory docItem = new NewsStory(docId, docName, content);

        try {

        	docItem.setUrl(file.toURI().toURL().toExternalForm());

        } catch(MalformedURLException e) {
            throw new RuntimeException("Failed to create file url for doc:" + docName);
        }

        return docItem;
    }

    private Content loadContent(String path, String docName, int topNTerms) {
        File docFile = new File(path, docName);
        return new HTMLContent(docName, docFile, topNTerms);
    }


    public List<NewsCategory> getTopics() {
        List<NewsCategory> allTopicsList = new ArrayList<NewsCategory>();
        allTopicsList.addAll(allTopicsMap.values());
        return allTopicsList;
    }

    public List<NewsStory> getTopNStories(NewsCategory newsCategory, int n) {
        throw new UnsupportedOperationException("todo...");
    }

    public NewsStory getStoryById(String id) {
        return allStoriesMap.get(id);
    }

    public Iterator<NewsStory> getIteratorOverStories() {
        return allStoriesMap.values().iterator();
    }

    /* Create items first */


    public String getDatasetName() {
        return this.datasetName;
    }

    public int getSize() {
        return allStoriesMap.size();
    }

    public List<NewsStoryGroup> getStoryGroups() {
        return new ArrayList<NewsStoryGroup>(allStoryGroups.values());
    }

    public void addStoryGroup(NewsStoryGroup newsStoryGroup) {
        allStoryGroups.put(newsStoryGroup.getGroupId(), newsStoryGroup);
    }

    public NewsStoryGroup findStoryGroup(String groupId) {
        return allStoryGroups.get(groupId);
    }

    public NewsStoryGroup findStoryGroupByStoryId(String storyId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public int getNumberOfGroups() {
        return allStoryGroups.size();
    }

    public List<NewsStoryGroup> getStoryGroupsForTopic(NewsCategory newsCategory) {

    	List<NewsStoryGroup> topicGroups = new ArrayList<NewsStoryGroup>();

        for(NewsStoryGroup sg : allStoryGroups.values()) {

            if( newsCategory.equals(sg.getTopic()) ) {
                topicGroups.add(sg);
            }
        }

        return topicGroups;
    }

	/**
	 * @return the documentDir
	 */
	public String getDocumentDir() {
		return documentDir;
	}

	/**
	 * @param documentDir the documentDir to set
	 */
	public void setDocumentDir(String documentDir) {
		this.documentDir = documentDir;
	}

	/**
	 * @return the topTerms
	 */
	public int getTopTerms() {
		return topTerms;
	}

	/**
	 * @param topTerms the topTerms to set
	 */
	public void setTopTerms(int topNTerms) {
		this.topTerms = topNTerms;
	}

	/**
	 * @return the documentNames
	 */
	public String[] getDocumentNames() {
		return documentNames;
	}

	/**
	 * @param documentNames the documentNames to set
	 */
	public void setDocumentNames(String[] docNames) {
		this.documentNames = docNames;
	}

    public List<NewsStory> getStories() {
        return new ArrayList<NewsStory>(allStoriesMap.values());
    }

    public List<NewsStory> getStories(NewsCategory t) {
    	ArrayList<NewsStory> storiesByTopic = new ArrayList<NewsStory>();
    	for (NewsStory s : allStoriesMap.values()) {
    		if (s.getTopic().equals(t)) {
    			storiesByTopic.add(s);
    		}
    	}
        return storiesByTopic;
    }

    public String getIndexDir() {
        return indexDir;
    }

    public void setIndexDir(String indexDir) {
        this.indexDir = indexDir;
    }

    public List<NewsStory> findStoriesByTitle(String title) {
        List<NewsStory> results = new ArrayList<NewsStory>();
        if( title != null ) {
            for(NewsStory s : allStoriesMap.values()) {
                if( title.equalsIgnoreCase(s.getTitle()) ) {
                    results.add(s);
                }
            }
        }
        return results;
    }

}