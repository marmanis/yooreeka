/*
 *   ________________________________________________________________________________________
 *   
 *   Y O O R E E K A
 *   A library for data mining, machine learning, soft computing, and mathematical analysis
 *   ________________________________________________________________________________________ 
 *    
 *   The Yooreeka project started with the code of the book "Algorithms of the Intelligent Web " 
 *   (Manning 2009). Although the term "Web" prevailed in the title, in essence, the algorithms 
 *   are valuable in any software application.
 *  
 *   Copyright (c) 2007-2009 Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-${year} Marmanis Group LLC and individual contributors as indicated by the @author tags.  
 * 
 *   Certain library functions depend on other Open Source software libraries, which are covered 
 *   by different license agreements. See the NOTICE file distributed with this work for additional 
 *   information regarding copyright ownership and licensing.
 * 
 *   Marmanis Group LLC licenses this file to You under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License.  
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under 
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *   either express or implied. See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package org.yooreeka.examples.newsgroups.core;

import java.util.List;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.newsgroups.crawling.CrawlResultsNewsDataset;
import org.yooreeka.examples.newsgroups.crawling.FileListNewsDataset;
import org.yooreeka.util.internet.crawling.core.CrawlData;
import org.yooreeka.util.internet.crawling.db.ProcessedDocsDB;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class NewsDatasetBuilder {

    public static int TOP_N_TERMS = 50;
    public static final String TRAINING_FILES_DIR_CH7 = YooreekaConfigurator.getHome()+"/data/ch07/training";
    public static final String TEST_FILES_DIR_CH7 = YooreekaConfigurator.getHome()+"/data/ch07/test";

    public static NewsDataset createNewsDatasetFromFileList(
            String datasetName, String dir) {

    	FileListNewsDataset ds = new FileListNewsDataset(datasetName,dir);
        ds.setTopTerms(TOP_N_TERMS);
        ds.loadTopics();
        ds.loadStories();

        return ds;
    }

    public static NewsDataset createNewsDatasetFromCrawledData(String datasetName, String crawlDataDir) {

        CrawlData crawlData = new CrawlData(crawlDataDir);
        crawlData.init();

        ProcessedDocsDB processedDocsDB = crawlData.getProcessedDocsDB();

        CrawlResultsNewsDataset dataset = new CrawlResultsNewsDataset(datasetName, crawlDataDir);
        dataset.setTopTerms(TOP_N_TERMS);
        dataset.loadTopics();
        dataset.loadStories();
        /* Load all document groups into dataset */
        List<String> allGroups = processedDocsDB.getAllGroupIds();
        for(String groupId : allGroups) {
            List<ProcessedDocument> docs =
                processedDocsDB.loadAllDocumentsInGroup(groupId);
            dataset.addDocuments(docs);
        }

        return dataset;
    }
}