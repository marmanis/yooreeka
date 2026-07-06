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
package org.yooreeka.examples.newsgroups.data;

import org.yooreeka.config.YooreekaConfigurator;

/**
 * Various constants, some from our work on chapter 2 and some new for chapter 7
 */
public class Ch7Constants {

    // References from chapter 2
    public static final String CH2_FILES_DIR = YooreekaConfigurator.getHome()+"/data/ch02/";

    public static final String[] CH2_TRAINING_DOC_SAMPLES =
        {"biz-01.html",
         "biz-02.html",
         "biz-03.html",
         "biz-04.html",
         "biz-05.html",
         "sport-01.html",
         "sport-02.html",
         "usa-01.html",
         "usa-02.html",
         "world-02.html",
         "world-03.html",
         "world-04.html",
         "world-05.html"};

    public static final String[] CH2_TEST_DOC_SAMPLES =
    {
         "biz-06.html",
         "biz-07.html",
         "sport-03.html",
         "usa-03.html",
         "usa-04.html",
         "world-01.html",
    };

    // Chapter 7
    public static final String CRAWL_DATA_ROOT_DIR = YooreekaConfigurator.getHome()+"/data/ch07/news-crawls";

    public static final String TRAINING_FILES_DIR = YooreekaConfigurator.getHome()+"/data/ch07/training";

    public static final String TEST_FILES_DIR = YooreekaConfigurator.getHome()+"/data/ch07/test";

    public static final String TEMP_DIR = YooreekaConfigurator.getHome()+"/data/ch07/temp/";


}