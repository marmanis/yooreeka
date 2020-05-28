package org.yooreeka.examples.newsgroups.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.yooreeka.examples.newsgroups.core.NewsCategory;
import org.yooreeka.examples.newsgroups.core.NewsDatabase;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsOracle;
import org.yooreeka.examples.newsgroups.core.NewsStory;
import org.yooreeka.examples.newsgroups.core.NewsStoryGroup;

public class NewsUI extends JPanel
                      implements TreeSelectionListener {

    private static final long serialVersionUID = 5044105786996289298L;

    private String DATA_DIR;

    // Content
    private NewsOracle newsOracle;

    // UI elements
    private JTree tree;
    private JEditorPane htmlPane;

    private boolean showClustersOnly = false;

    private NewsDataset loadedDataset = null;

    private boolean useSystemLookAndFeel = true;

    public NewsUI(NewsDataset dataset) {
    	// Create an instance
        super(new GridLayout(1,0));
        this.loadedDataset = dataset;
        setup();
    }

    public void setup() {

    	// If there is anything, clean it up
    	this.removeAll();

        // initialize viewer application
        newsOracle = new NewsOracle(loadedDataset);

        //Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("News");
        createNodes(top, newsOracle);

        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);

        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);

        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        JScrollPane htmlView = new JScrollPane(htmlPane);

        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        //Add the split pane to this panel.
        add(splitPane);
    }

    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();

        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            StoryInfo story = (StoryInfo)nodeInfo;
            displayURL(story.url);
        }
    }

    private class StoryInfo {
        public String title;
        public URL url;

        public StoryInfo(String title, String urlAsText) {
            this.title = title;
            try {
                url = new URL(urlAsText);
                if (url == null) {
                    System.err.println("Couldn't find url: '" + urlAsText + "'");
                }
            }
            catch(MalformedURLException e) {
                throw new RuntimeException("Invalid url: '"+urlAsText + "', title: '" + title + "'",e);
            }
        }

        @Override
		public String toString() {
            return title;
        }


    }

    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            }
            else {
                htmlPane.setText("Document Not Found");
            }
        } catch (IOException e) {
            System.err.println("Attempted to load a bad URL: '" + url + "'");
        }
    }

    private void createNodes(DefaultMutableTreeNode top, NewsOracle newsOracle) {

        List<NewsCategory> allTopics = newsOracle.getTopics();

        DefaultMutableTreeNode tnTopic = null;
        DefaultMutableTreeNode tnCluster = null;
        DefaultMutableTreeNode tnStory = null;

        int nClusters = newsOracle.getStoryGroups().size();

        // System.out.println("Found "+nClusters+" clusters");

        if ( showClustersOnly() ) {
        	if (nClusters > 0) {
        		// System.out.println("Show Clusters > Stories ... ");

        		List<NewsStoryGroup> clusters = newsOracle.getStoryGroups();
        		NewsStoryGroup.sortBySize(clusters);

	            for(NewsStoryGroup sg :  clusters) {

	            	String label = "Cluster-"+sg.getClusterLabel();

		            tnCluster = new DefaultMutableTreeNode(label);
		            top.add(tnCluster);

	                List<NewsStory> newsStories = sg.getStories();
	                NewsStory.sortByTitle(newsStories);

	                for(NewsStory s : newsStories) {
	                    tnStory = new DefaultMutableTreeNode(
	                            new StoryInfo(s.getTitle(), s.getUrl()) );
	                    tnCluster.add(tnStory);
	                }
	            }
        	} else {
        		System.out.println("No clusters to show!");
        	}
        } else {
        	// System.out.println("Show Topics > Clusters > Stories ... ");

	        for(NewsCategory newsCategory : allTopics) {
	            List<NewsStoryGroup> clusters = newsOracle.getStoryGroupsByTopic(newsCategory);
	            NewsStoryGroup.sortByLabel(clusters);

	            if ( nClusters > 0 ) {
		            String topicLabel = newsCategory.getName() + " (" + clusters.size() + " groups)";
		            tnTopic = new DefaultMutableTreeNode(topicLabel);
		            top.add(tnTopic );
		            for(NewsStoryGroup sg :  clusters) {
		                List<NewsStory> newsStories = sg.getStories();
		                NewsStory.sortByTitle(newsStories);
		                String clusterLabel = sg.getClusterLabel();
		                tnCluster = new DefaultMutableTreeNode(clusterLabel);
		                tnTopic.add(tnCluster);
		                for(NewsStory s : newsStories) {
		                    tnStory = new DefaultMutableTreeNode(
		                            new StoryInfo(s.getTitle(), s.getUrl()) );
		                    tnCluster.add(tnStory);
		                }
		            }
	            } else {
	                // when we don't have any clusters
		            List<NewsStory> newsStories = newsOracle.getStoriesByTopic(newsCategory);
		            NewsStory.sortByTitle(newsStories);
		            String topicLabel = newsCategory.getName()+" (" + newsStories.size() + " stories)";
		            tnTopic = new DefaultMutableTreeNode(topicLabel);
		            top.add(tnTopic);
	                for(NewsStory s : newsStories) {
	                    tnStory = new DefaultMutableTreeNode(
	                            new StoryInfo(s.getTitle(), s.getUrl()) );
	                    tnTopic.add(tnStory);
	                }
	            }
	        }
        }
    }

    public static void createAndShowUI(final NewsUI ui) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

//            	if (ui.hasSystemLookAndFeel()) {
//            		try {
//            			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            		} catch (Exception e) {
//            			System.err.println("Couldn't use system look and feel.");
//            		}
//            	}

            	JFrame frame = new JFrame(ui.getLoadedDataset().getDatasetName());
            	frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            	frame.add(ui);
            	frame.pack();
            	frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {

    	NewsDataset loadedDataset = NewsDatabase.loadDataset("c:/temp/ch2.dat");

    	NewsUI ui = new NewsUI(loadedDataset);

    	NewsUI.createAndShowUI(ui);
    }

	/**
	 * @return the dATA_DIR
	 */
	public String getDATA_DIR() {
		return DATA_DIR;
	}

	/**
	 * @param data_dir the dATA_DIR to set
	 */
	public void setDATA_DIR(String data_dir) {
		DATA_DIR = data_dir;
	}

	/**
	 * @return the useSystemLookAndFeel
	 */
	public boolean hasSystemLookAndFeel() {
		return useSystemLookAndFeel;
	}

	/**
	 * @param useSystemLookAndFeel the useSystemLookAndFeel to set
	 */
	public void useSystemLookAndFeel(boolean useSystemLookAndFeel) {
		this.useSystemLookAndFeel = useSystemLookAndFeel;
	}

	/**
	 * @return the loadedDataset
	 */
	public NewsDataset getLoadedDataset() {
		return loadedDataset;
	}

	/**
	 * @param loadedDataset the loadedDataset to set
	 */
	public void setLoadedDataset(NewsDataset loadedDataset) {
		this.loadedDataset = loadedDataset;
	}

	/**
	 * @return the showClustersOnly
	 */
	public boolean showClustersOnly() {
		return showClustersOnly;
	}

	/**
	 * @param showClustersOnly the showClustersOnly to set
	 */
	public void showClustersOnly(boolean showClustersOnly) {
		this.showClustersOnly = showClustersOnly;
		setup();
	}
}