package org.yooreeka.examples.newsgroups.crawling;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.yooreeka.algos.reco.collab.model.Content;
import org.yooreeka.examples.newsgroups.core.NewsCategory;
import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.core.NewsStoryGroup;
import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * Dataset that is based on documents fetched by crawler.
 */
public class CrawlResultsNewsDataset extends BaseNewsDataset {

    private static final long serialVersionUID = 7768246175864823358L;

    private String datasetName;
    private String documentDir;
    private String[] documentNames;

    private int topTerms = 17;

    private Map<String, NewsStory> allStoriesMap = new HashMap<String, NewsStory>();
    private Map<String, NewsCategory> allTopicsMap = new HashMap<String, NewsCategory>();
    private Map<String, NewsStoryGroup> allStoryGroups = new HashMap<String, NewsStoryGroup>();

    private String indexDir;

    /**
     * Creates a new dataset instance.
     *
     * @param datasetName name that identifies this dataset.
     * @param crawlData access to crawler.
     */
    public CrawlResultsNewsDataset(String datasetName, String dir) {
    	this.datasetName = datasetName;
    	this.documentDir = dir;
    }

    public void addDocuments(List<ProcessedDocument> docs) {
        List<NewsStory> newsStories = convertDocsIntoStories(docs);
        for(NewsStory newsStory : newsStories) {
            addStory(newsStory);
        }
    }

    private List<NewsStory> convertDocsIntoStories(List<ProcessedDocument> docs) {
        List<NewsStory> loadedStories = new ArrayList<NewsStory>();
        for(ProcessedDocument doc : docs) {
            NewsStory newsStory = createStory(doc);
            loadedStories.add(newsStory);
        }
        return loadedStories;
    }

    private NewsStory createStory(ProcessedDocument doc) {
        NewsStory newsStory = new NewsStory();

        newsStory.setId(doc.getDocumentId());
        newsStory.setTitle(doc.getDocumentTitle());
        newsStory.setTopic(null);
        newsStory.setUrl(doc.getDocumentURL());
        Content content = new Content(doc.getDocumentId(), doc.getText(), topTerms);
        newsStory.setContent(content);

        /*
         * For testing/evaluation if we have a rule or convention that
         * we can use to derive 'correct' topic from the doc id/url/content.
         * Usually this value will be null.
         */
        NewsCategory expectedTopic = calculateExpectedTopic(newsStory);
        newsStory.setExpectedTopic(expectedTopic);
        return newsStory;
    }

    private NewsCategory calculateExpectedTopic(NewsStory newsStory) {
        try {
        URI uri = new URI(newsStory.getUrl());
        File f = new File(uri);
        String topicKey = f.getName().split("-")[0];
        return allTopicsMap.get(topicKey);
        }
        catch(URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadStories() {

    	if (allTopicsMap == null || allTopicsMap.size() == 0) {
    		throw new UnsupportedOperationException("ERROR:"+
			"  You must load the topics before you use the load() method!");
    	}

        allStoriesMap = new HashMap<String, NewsStory>();
    }

    public void loadTopics() {
    	if (allTopicsMap == null || allTopicsMap.size() == 0 ) {
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

    private void addStory(NewsStory newsStory) {
        allStoriesMap.put(newsStory.getId(), newsStory);
    }

    public List<NewsCategory> getTopics() {
        List<NewsCategory> allTopicsList = new ArrayList<NewsCategory>();
        allTopicsList.addAll(allTopicsMap.values());
        return allTopicsList;
    }

    public List<NewsStory> getTopNStories(NewsCategory newsCategory, int n) {
        throw new UnsupportedOperationException("todo...");
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

    public NewsStory getStoryById(String id) {
        return allStoriesMap.get(id);
    }

    public Iterator<NewsStory> getIteratorOverStories() {
        return allStoriesMap.values().iterator();
    }

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