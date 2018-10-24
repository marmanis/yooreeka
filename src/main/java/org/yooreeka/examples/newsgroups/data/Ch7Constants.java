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