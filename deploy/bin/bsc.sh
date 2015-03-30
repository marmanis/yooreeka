#!/bin/bash

Y_HOME=/home/babis/code/iweb2
Y_LIBS=$Y_HOME/lib
Y_DROOLS_LIBS=$Y_LIBS/drools
Y_CRAWL_LIBS=$Y_LIBS/crawler4j-libs

export LIBJARS=
export LIBJARS=$LIBJARS:$Y_LIBS/commons-lang3-3.1.jar
export LIBJARS=$LIBJARS:$Y_LIBS/commons-logging-1.1.1.jar
export LIBJARS=$LIBJARS:$Y_LIBS/httpclient-4.2.2.jar
export LIBJARS=$LIBJARS:$Y_LIBS/httpcore-4.2.2.jar
export LIBJARS=$LIBJARS:$Y_LIBS/jcommon-1.0.18.jar
export LIBJARS=$LIBJARS:$Y_LIBS/jfreechart-1.0.15.jar

export LIBJARS=$LIBJARS:$Y_LIBS/jigg-0.1.jar
export LIBJARS=$LIBJARS:$Y_LIBS/jsr173_1.0_api.jar
export LIBJARS=$LIBJARS:$Y_LIBS/junit-4.1.jar
export LIBJARS=$LIBJARS:$Y_LIBS/juniversalchardet-1.0.3.jar
export LIBJARS=$LIBJARS:$Y_LIBS/log4j-1.2.17.jar

export LIBJARS=$LIBJARS:$Y_LIBS/lucene-analyzers-common-4.0.0-BETA.jar
export LIBJARS=$LIBJARS:$Y_LIBS/lucene-core-4.0.0-BETA.jar
export LIBJARS=$LIBJARS:$Y_LIBS/lucene-demo-4.0.0-BETA.jar
export LIBJARS=$LIBJARS:$Y_LIBS/lucene-memory-4.0.0-BETA.jar
export LIBJARS=$LIBJARS:$Y_LIBS/lucene-queryparser-4.0.0-BETA.jar

export LIBJARS=$LIBJARS:$Y_LIBS/nekohtml.jar
export LIBJARS=$LIBJARS:$Y_LIBS/ojalgo-34.0.jar
export LIBJARS=$LIBJARS:$Y_LIBS/poi-3.0.2-FINAL-20080204.jar
export LIBJARS=$LIBJARS:$Y_LIBS/resolver.jar
export LIBJARS=$LIBJARS:$Y_LIBS/rooster.jar

export LIBJARS=$LIBJARS:$Y_LIBS/secondstring-20070327.jar
export LIBJARS=$LIBJARS:$Y_LIBS/serializer.jar
export LIBJARS=$LIBJARS:$Y_LIBS/super-csv-2.1.0.jar
export LIBJARS=$LIBJARS:$Y_LIBS/tm-extractors-1.0.jar
export LIBJARS=$LIBJARS:$Y_LIBS/xercesImpl.jar
export LIBJARS=$LIBJARS:$Y_LIBS/xml-apis.jar

export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/antlr-runtime-3.3.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/drools-compiler-5.5.0.Final.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/drools-core-5.5.0.Final.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/ecj-3.5.1.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/knowledge-api-5.5.0.Final.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/knowledge-internal-api-5.5.0.Final.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/mvel2-2.1.3.Final.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/slf4j-api-1.6.4.jar
export LIBJARS=$LIBJARS:$Y_DROOLS_LIBS/slf4j-jdk14-1.7.2.jar

export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/apache-mime4j-core-0.7.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/apache-mime4j-dom-0.7.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/asm-3.1.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/boilerpipe-1.1.0.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/commons-compress-1.3.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/crawler4j-3.3.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/geronimo-stax-api_1.0_spec-1.0.1.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/je-4.0.92.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/metadata-extractor-2.4.0-beta-1.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/tagsoup-1.2.1.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/tika-core-1.0.jar
export LIBJARS=$LIBJARS:$Y_CRAWL_LIBS/tika-parsers-1.0.jar

export CLASSPATH=$Y_HOME/deploy/lib/bsh-2.0b4.jar:$LIBJARS
export CLASSPATH=$CLASSPATH:$Y_HOME/deploy/lib/yooreeka-2.0.jar:$Y_HOME/deploy/conf/

java -Xms256M -Xmx256M -Dyooreeka.home=$Y_HOME -Duser.home=$Y_HOME/deploy/bin -cp $CLASSPATH bsh.Interpreter


