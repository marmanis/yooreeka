package org.yooreeka.examples.newsgroups.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsStory;

public class LuceneIndexBuilder {

	public static final String INDEX_FIELD_DOC_ID = "docid";
	public static final String INDEX_FIELD_DOC_TYPE = "doctype";
	public static final String INDEX_FIELD_CONTENT = "content";
	public static final String INDEX_FIELD_TITLE = "title";
	public static final String INDEX_FIELD_URL = "url";

    private File indexDir;
	private IndexWriter indexWriter;

    private NewsDataset ds;
    private long indexedDocCount;
	private int RamBufferSizeMB = 128;

    public LuceneIndexBuilder(File indexDir, NewsDataset ds) {

    	this.indexDir = indexDir;
    	this.ds = ds;
        this.indexedDocCount = 0;

    	try {
			indexWriter = getIndexWriter(indexDir);
			
		} catch (IOException ioX) {
			throw new RuntimeException("Error while creating lucene index: ", ioX);
		}
    }

	private IndexWriter getIndexWriter(File file) throws IOException {
		FSDirectory dir = FSDirectory.open(file);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44,
				new StandardAnalyzer(Version.LUCENE_44));
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		config.setRAMBufferSizeMB(RamBufferSizeMB);
		return new IndexWriter(dir, config);
	}

    private void indexDocument(IndexWriter iw, NewsStory newsStory) throws IOException {

		org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

		FieldType customType = new FieldType(TextField.TYPE_NOT_STORED);
		customType.setStoreTermVectors(true);
		customType.setStoreTermVectorPositions(true);
		customType.setStoreTermVectorOffsets(false);

		doc.add(new Field(INDEX_FIELD_CONTENT, newsStory.getContent().getText(), customType));

		doc.add(new StringField(INDEX_FIELD_URL, newsStory.getUrl(), Field.Store.YES));

		doc.add(new StringField(INDEX_FIELD_DOC_ID, newsStory.getId(), Field.Store.YES));

		doc.add(new TextField(INDEX_FIELD_TITLE, newsStory.getTitle(), Field.Store.YES));

        iw.addDocument(doc);
    }

    public void run() {
        indexedDocCount = 0;
        try {

        	for(NewsStory newsStory : ds.getStories()) {
                indexDocument(indexWriter, newsStory);
                indexedDocCount++;
            }
        }
        catch(IOException ioX) {
            throw new RuntimeException("Error while creating lucene index: ", ioX);
        }
        finally {
            if( indexWriter != null ) {
                try {
                    indexWriter.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public long getIndexedDocCount() {
        return indexedDocCount;
    }

    public void setIndexedDocCount(long indexedDocCount) {
        this.indexedDocCount = indexedDocCount;
    }

    public String getIndexDir() {
        if( indexDir == null ) {
            return null;
        }

        try {
            return indexDir.getCanonicalPath();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}