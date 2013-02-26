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
package org.yooreeka.test.beanshellscripts;

import org.yooreeka.test.Chapter_06;

import junit.framework.TestCase;

public class Ch6BeanShellScriptsTest extends TestCase {

    public Ch6BeanShellScriptsTest(String name) {
        super(name);
        
        Chapter_06 c6 = new Chapter_06();
		c6.run();
    }
    
    public void test_evalCh6DependentScripts() throws Exception {
//        ScriptEvalUtils.runDependentScripts(
//                new String[] {
//                    "ch6_2_CreditScoreNB.txt",
//                    "ch6_3_CreditScoreDT.txt",
//                    "ch6_4_CreditScoreNN.txt",
//                    "ch6_5_Comparisons.txt"
//                });
    }
    
    public void test_evalCh6IndependentScripts() throws Exception {
//        ScriptEvalUtils.runIndependentScripts(
//                new String[] {
//                "ch6_1_CreditScoreData.txt",                        
//                "ch6_6_Bagging.txt",
//                "ch6_6_Bagging_DT.txt",
//                "ch6_6_Bagging_NB.txt",
//                "ch6_6_Bagging_NN.txt",
//                "ch6_6_DT-Bagging.txt"
//        } );
    }
    
}
