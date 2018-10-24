package org.yooreeka.examples.newsgroups.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.yooreeka.algos.search.lucene.LuceneIndexBuilder;

public class NewsOracle {

	private static final String PRETTY_LINE =
		"_______________________________________________________________________";

    private NewsDataset dataset;

    public NewsOracle(NewsDataset dataset) {
        this.dataset = dataset;
    }

    public List<NewsCategory> getTopics() {
        return dataset.getTopics();
    }

    public NewsCategory findTopic(String topicName) {
        NewsCategory matchedTopic = null;
        List<NewsCategory> allTopics = getTopics();
        for(NewsCategory t : allTopics) {
            if( topicName.equalsIgnoreCase(t.getName())) {
                matchedTopic = t;
                break;
            }
        }
        return matchedTopic;
    }

    public List<NewsStory> getTopNStoriesForTopic(NewsCategory newsCategory, int n) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public List<NewsStoryGroup> getStoryGroups() {
        return dataset.getStoryGroups();
    }

    public List<NewsStoryGroup> getStoryGroupsByTopic(NewsCategory newsCategory) {
        return dataset.getStoryGroupsForTopic(newsCategory);
    }

    public List<NewsStory> getStoriesByTopic(NewsCategory t) {
    	return dataset.getStories(t);
    }

    public List<StorySearchResult> search(String query, int numberOfMatches) {

        List<StorySearchResult> results = new ArrayList<StorySearchResult>();

        IndexSearcher is = null;
        String indexDir = dataset.getIndexDir();

		Directory dir = null;
		try {
			dir = FSDirectory.open(new File(indexDir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DirectoryReader dirReader = null;
		try {

			dirReader = DirectoryReader.open(dir);
			is = new IndexSearcher(dirReader);

		} catch (IOException ioX) {
			System.out.println("ERROR: " + ioX.getMessage());
		}

		StandardQueryParser queryParserHelper = new StandardQueryParser();
		Query q = null;

		try {

			q = queryParserHelper.parse(query,
					LuceneIndexBuilder.INDEX_FIELD_CONTENT);

		} catch (QueryNodeException e) {
			e.printStackTrace();
		}

		TopDocs hits = null;
        try {
			hits = is.search(q, numberOfMatches);

			for (int i = 0; i < hits.scoreDocs.length; i++) {

				Document hitDoc = is.doc(hits.scoreDocs[i].doc);

				String id = hitDoc.get("docid");
                NewsStory newsStory = dataset.getStoryById(id);
                
                StorySearchResult sResult = new StorySearchResult();
                sResult.setStory(newsStory);
                sResult.setScore(hits.scoreDocs[i].score);
                results.add(sResult);
            }

			dirReader.close();
			dir.close();

        } catch (IOException ioX) {
            System.out.println("ERROR: "+ioX.getMessage());
        }

        String header = "Search results using Lucene index scores:";
        boolean showTitle = true;
        printResults(header, "Query: " + query, results, showTitle);

        return results;
    }

    private void printResults(String header, String query, List<StorySearchResult> values, boolean showDocTitle) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        boolean printEntrySeparator = false;
        if( showDocTitle ) { // multiple lines per entry
            printEntrySeparator = true;
        }

        pw.print("\n");
        pw.println(header);
        if( query != null ) {
            pw.println(query);
        }
        pw.print("\n");
        for(int i = 0, n = values.size(); i < n; i++) {
            if( showDocTitle ) {
                pw.printf("Document Title: %s\n", values.get(i).getStory().getTitle());
            }
            pw.printf("Document Terms: ");
            for (String term : values.get(i).getStory().getTopNTerms()) {
            	pw.printf(" %s, ", term);
            }
            pw.printf("\n");
            pw.printf("Document URL: %-46s  -->  Relevance Score: %.15f\n",
            		values.get(i).getStory().getUrl(), values.get(i).getScore());
            if( printEntrySeparator ) {
                pw.printf(PRETTY_LINE);
                pw.printf("\n");
            }
        }
        if( !printEntrySeparator ) {
            pw.print(PRETTY_LINE);
        }

        System.out.println(sw.toString());

    }
}