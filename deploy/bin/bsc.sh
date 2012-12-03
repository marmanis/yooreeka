#!/bin/bash

IWEB2_HOME=/home/babis/code/iweb2

LIBJARS=$IWEB2_HOME/lib/commons-codec-1.3.jar:$IWEB2_HOME/lib/commons-httpclient-3.1.jar:$IWEB2_HOME/lib/commons-lang-2.3.jar:$IWEB2_HOME/lib/commons-logging-1.1.1.jar:$IWEB2_HOME/lib/f2jutil.jar:$IWEB2_HOME/lib/jfreechart.jar:$IWEB2_HOME/lib/jigg-0.1.jar:$IWEB2_HOME/lib/junit-4.1.jar:$IWEB2_HOME/lib/lucene-analyzers-2.3.0.jar:$IWEB2_HOME/lib/lucene-core-2.3.0.jar:$IWEB2_HOME/lib/lucene-demos-2.3.0.jar:$IWEB2_HOME/lib/lucene-memory-2.3.0.jar:$IWEB2_HOME/lib/nekohtml.jar:$IWEB2_HOME/lib/poi-3.0.2-FINAL-20080204.jar:$IWEB2_HOME/lib/resolver.jar:$IWEB2_HOME/lib/secondstring-20070327.jar:$IWEB2_HOME/lib/serializer.jar:$IWEB2_HOME/lib/tm-extractors-1.0.jar:$IWEB2_HOME/lib/xercesImpl.jar:$IWEB2_HOME/lib/xml-apis.jar

CLASSPATH=$IWEB2_HOME/deploy/lib/bsh-2.0b4.jar:$LIBJARS:$IWEB2_HOME/deploy/lib/iweb2.jar:$IWEB2_HOME/deploy/conf/

java -Xms256M -Xmx256M -Diweb2.home=$IWEB2_HOME -Duser.home=$IWEB2_HOME/deploy/bin -cp $CLASSPATH bsh.Interpreter



