package org.yooreeka.examples.newsgroups.core;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.yooreeka.examples.newsgroups.classification.ClassificationStrategy;
import org.yooreeka.examples.newsgroups.classification.ClassificationStrategyImpl;
import org.yooreeka.examples.newsgroups.classification.NBStoryClassifier;
import org.yooreeka.examples.newsgroups.clustering.NewsClusterBuilder;
import org.yooreeka.examples.newsgroups.clustering.TopicalNewsClusterBuilder;
import org.yooreeka.examples.newsgroups.indexing.LuceneIndexBuilder;
import org.yooreeka.util.internet.crawling.util.FileUtils;

public class NewsProcessor {

    private NewsDataset trainingDataset;

    private ClassificationStrategy topicSelector;

    private boolean isVerbose = false;

    private boolean clusterStories = true;
    private boolean classifyClusters = true;
    private boolean classifyStories = false;
    private boolean clusterStoriesWithinTopics = false;
    private boolean indexStories = true;

    private File luceneIndexDir = null;

    private NewsOracle oracle = null;

    public NewsProcessor() {
    	// Create an instance. Useful if you just want to cluster
    }

    public NewsProcessor(NewsDataset dataset) {
    	this.trainingDataset = dataset;
    }

    /**
     * Takes trainingDataset that contains documents with known topics (categories) and
     * trains classifier on it.
     */
    public void trainClassifier() {

        // NewsCategory Selector classifies individual stories or clusters
        if( topicSelector == null ) {

            // configure and train classifier
            NBStoryClassifier storyClassifier = new NBStoryClassifier("NewsStoryClassifier", trainingDataset);
            storyClassifier.train();

            ClassificationStrategyImpl defaultTopicSelector =
                new ClassificationStrategyImpl();
            defaultTopicSelector.setStoryClassifier(storyClassifier);

            topicSelector = defaultTopicSelector;
        }
    }

    public void setTopicSelector(ClassificationStrategy topicSelector) {
        this.topicSelector = topicSelector;
    }

    /**
     * Takes trainingDataset that contains uncategorized documents and assigns topics
     * (categories) to all documents.
     *
     * @param ds
     * @throws IOException
     */
    public void process(NewsDataset ds) throws IOException {

        if( topicSelector == null ) {
            throw new RuntimeException("You have to provide topicSelector.");
        }

        if( this.clusterStories && this.clusterStoriesWithinTopics ) {
            throw new IllegalStateException("Choose only one type of clustering");
        }

        if( this.clusterStoriesWithinTopics && this.classifyStories == false ) {
            throw new IllegalStateException("Choose classifyStories to be able to cluster within topics");
        }

        if( this.clusterStoriesWithinTopics && this.classifyClusters ) {
            throw new IllegalStateException("There is no point in classifying clusters when they are created within topic.");
        }


        if( this.clusterStories ) {
            System.out.println("-- Running clustering...");
            createClusters(ds);
            System.out.println("-- Finished clustering.");

        }

        if( this.classifyStories ) {
            System.out.println("-- Running classification for individual stories...");
            classifyStories(ds);
            System.out.println("--- Finished classification for individual stories.");
        }

        if( this.clusterStoriesWithinTopics ) {
            System.out.println("-- Running clustering within topics...");
            createClustersWithinTopics(ds);
            System.out.println("-- Finished clustering within topics.");
        }

        if( this.classifyClusters ) {
            System.out.println("-- Running classification for story clusters (using representative story for every cluster)...");
            classifyClusters(ds);
            System.out.println("--- Finished classification for story clusters.");
        }


        if( this.indexStories ) {
            System.out.println("-- Running story indexing...");
            File indexDir = buildIndexDir();
            LuceneIndexBuilder indexBuilder = new LuceneIndexBuilder(indexDir, ds);
            indexBuilder.run();
            ds.setIndexDir(indexBuilder.getIndexDir());
            System.out.println("    indexed " +
                    indexBuilder.getIndexedDocCount() + " documens.");
            System.out.println("    index dir '" + indexBuilder.getIndexDir() + "'.");
            System.out.println("--- Finished story index creation.");
        }
    }

    public File buildIndexDir(NewsDataset ds) throws IOException {

    	System.out.println("--- Creating index directory ... ");

        StringBuilder indexDir = new StringBuilder(ds.getDocumentDir());
        indexDir.append(System.getProperty("file.separator"));
        indexDir.append("lucene-index-");
        indexDir.append(System.currentTimeMillis());

        luceneIndexDir = new File(indexDir.toString());

        // Delete the index directory, if it exists
        FileUtils.deleteDir(luceneIndexDir);

        luceneIndexDir.mkdirs();

        System.out.println("--- Index directory created successfully at: "+luceneIndexDir.getCanonicalPath());

        return luceneIndexDir;
    }

    /**
     * Auxiliary method for working with the current training set of the NewsProcessor
     */
    public File buildIndexDir() throws IOException {
    	return buildIndexDir(trainingDataset);
    }

    /**
     * Auxiliary method for working with the current training set of the NewsProcessor
     */
    public void runIndexing() {
    	runIndexing(trainingDataset);
    }

    public void runIndexing(NewsDataset ds) {

        System.out.println("--- Indexing new stories ...");

        LuceneIndexBuilder indexBuilder = new LuceneIndexBuilder(luceneIndexDir, ds);
        indexBuilder.run();
        ds.setIndexDir(indexBuilder.getIndexDir());

        System.out.print(indexBuilder.getIndexedDocCount() + " documents indexed in: ");
        System.out.println(indexBuilder.getIndexDir());
        System.out.println("--- Indexing completed successfully!");

        //Now, create an oracle, so that we can search
        oracle = new NewsOracle(ds);
    }

    public void search(String query, int numberOfMatches) {
    	//Simply delegate to the NewsOracle
    	oracle.search(query,numberOfMatches);
    }

    public void createClusters(NewsDataset ds) {
        NewsClusterBuilder clusterBuilder = new NewsClusterBuilder();
        clusterBuilder.setNewsDataset(ds);
        clusterBuilder.cluster();
    }

    public void createClustersWithinTopics(NewsDataset ds) {
        TopicalNewsClusterBuilder clusterBuilder = new TopicalNewsClusterBuilder();
        clusterBuilder.setNewsDataset(ds);
        clusterBuilder.cluster();
    }


    public void classifyClusters(NewsDataset ds) {
        List<NewsStoryGroup> clusters = ds.getStoryGroups();
        int total = ds.getNumberOfGroups();
        int correct = 0;
        boolean originalTopicsAvailable = false;

        System.out.println("\nRunning classification of clusters:");
        for(NewsStoryGroup cluster : clusters) {

        	//This is the actual topic for this cluster (if known)
        	NewsCategory expectedTopic = cluster.getExpectedTopic();

            topicSelector.assignTopicToCluster(cluster);

            //This is the assigned topic for this cluster
            NewsCategory matchedTopic = cluster.getTopic();

            //If we know the actual (expected) topic then we can count the number of times
            //that the classification was correct
            if( expectedTopic != null && expectedTopic.equals(matchedTopic) ) {
                originalTopicsAvailable = true;
                correct++;
            }
        }

        if( isVerbose && originalTopicsAvailable ) {
            double accuracy = (double) correct / (double) total;
            System.out.printf("Classification results: " +
                    "total clusters %d, correct %d, accuracy %.4f\n",
                    total, correct, accuracy);
        }
    }

    /**
     * Classify every story in the trainingDataset and, if the topic is known, assess
     * the accuracy of classification.
     *
     * @param ds
     */
    public void classifyStories(NewsDataset ds) {
        Iterator<NewsStory> iter = ds.getIteratorOverStories();
        int total = ds.getSize();
        int correct = 0;
        boolean originalTopicsAvailable = false;

        System.out.println("\nRunning classification of stories:");
        while(iter.hasNext()) {

            NewsStory newsStory = iter.next();

            //This is the actual topic for this news story (if known)
            NewsCategory expectedTopic = newsStory.getTopic();

            topicSelector.assignTopicToStory(newsStory);

            //This is the assigned topic for this news story
            NewsCategory matchedTopic = newsStory.getTopic();

            //If we know the actual (expected) topic then we can count the number of times
            //that the classification was correct
            if( expectedTopic != null && expectedTopic.equals(matchedTopic) ) {
                originalTopicsAvailable = true;
                correct++;
            }
        }

        if( isVerbose && originalTopicsAvailable ) {
            double accuracy = (double) correct / (double) total;
            System.out.printf("Classification results: " +
                    "total stories %d, correct %d, accuracy %.4f\n",
                    total, correct, accuracy);
        }
    }

    public boolean isIndexStories() {
        return indexStories;
    }

    public void setIndexStories(boolean indexStories) {
        this.indexStories = indexStories;
    }

    public boolean isClusterStories() {
        return clusterStories;
    }

    public boolean isClassifyClusters() {
        return classifyClusters;
    }

    public boolean isClassifyStories() {
        return classifyStories;
    }

    public void setClassifyClusters(boolean flag) {
        this.classifyClusters = flag;
    }

    public void setClassifyStories(boolean flag) {
        this.classifyStories = flag;
    }

    public void setClusterStories(boolean flag) {
        this.clusterStories = flag;
    }

    public void setTrainingDataset(NewsDataset trainingDataset) {
        this.trainingDataset = trainingDataset;
    }

    public boolean isClusterStoriesWithinTopics() {
        return clusterStoriesWithinTopics;
    }

    public void setClusterStoriesWithinTopics(boolean clusterStoriesWithinTopics) {
        this.clusterStoriesWithinTopics = clusterStoriesWithinTopics;
    }


	/**
	 * @return the isVerbose
	 */
	public boolean isVerbose() {
		return isVerbose;
	}

	/**
	 * @param isVerbose the isVerbose to set
	 */
	public void setVerbose(boolean isVerbose) {
		this.isVerbose = isVerbose;
	}
}