echo off

title Bean Shell


set IWEB2_HOME=C:\iWeb2

set LIBJARS=
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\commons-codec-1.3.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\commons-httpclient-3.1.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\commons-lang-2.3.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\commons-logging-1.1.1.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\f2jutil.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\jfreechart.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\jgraph.jar

set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\jigg-0.1.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\rooster.jar

set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\junit-4.1.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\lucene-analyzers-common-4.0.0-BETA.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\lucene-core-4.0.0-BETA.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\lucene-demo-4.0.0-BETA.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\lucene-memory-4.0.0-BETA.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\lucene-queryparser-4.0.0-BETA.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\nekohtml.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\poi-3.0.2-FINAL-20080204.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\resolver.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\secondstring-20070327.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\serializer.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\SuperCSV-1.16.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\tm-extractors-1.0.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\xercesImpl.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\xml-apis.jar

set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\drools-core-4.0.4.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\drools-compiler-4.0.4.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\antlr-runtime-3.0.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\janino-2.5.10.jar
set LIBJARS=%LIBJARS%;%IWEB2_HOME%\deploy\lib\mvel14-1.2.21.jar


set CLASSPATH=%IWEB2_HOME%\deploy\lib\bsh-2.0b4.jar;%LIBJARS%
set CLASSPATH=%CLASSPATH%;%IWEB2_HOME%\deploy\lib\iweb2.jar;%IWEB2_HOME%\deploy\conf

echo ---
set PATH
echo ---
set CLASSPATH
echo ---
set JAVA_HOME
echo ---


@rem BeanShell uses "user.home" variable to locate file with startup instructions: ".bshrc".
"%JAVA_HOME%"\bin\java -Duser.home=%IWEB2_HOME%\deploy\bin -Xms256M -Xmx1280M -cp %CLASSPATH% bsh.Interpreter
