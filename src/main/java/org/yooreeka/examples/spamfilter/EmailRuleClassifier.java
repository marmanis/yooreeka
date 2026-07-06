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
package org.yooreeka.examples.spamfilter;

import org.yooreeka.algos.taxis.core.BaseConcept;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.rules.RuleEngine;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.examples.spamfilter.data.Email;
import org.yooreeka.examples.spamfilter.data.EmailData;
import org.yooreeka.examples.spamfilter.data.EmailDataset;

public class EmailRuleClassifier {

    private String ruleFilename;
    private RuleEngine re;
    private Concept spam;
    private Concept notSpam;
    
    public EmailRuleClassifier(String ruleFilename) {
        this.ruleFilename = ruleFilename;
    }
    
    public void train() {
        re = new RuleEngine(ruleFilename);
        spam = new BaseConcept("SPAM");
        notSpam = new BaseConcept("NOT-SPAM");
        
    }
    
    public Concept classify(Email email) {
        ClassificationResult result = new ClassificationResult();
        re.executeRules(result, email);
        if( result.isSpamEmail() ) {
            return spam;
        }
        else {
            return notSpam;
        }
    }
    
    public void run(EmailDataset ds, String msg) {
        System.out.println("\n");        
        System.out.println(msg);
        System.out.println("__________________________________________________");        
        for(Email email : ds.getEmails() ) {
            System.out.println("\nClassifying email: " + email.getId() + " ...");

            Concept c = classify(email);
            
            System.out.println("Rules classified email: " + email.getId() + " as: " + c.getName());
        }
        System.out.println("__________________________________________________");        
    }
    
    public static void main(String[] args) {
        EmailDataset ds = EmailData.createTestDataset();
        
        EmailRuleClassifier classifier = new EmailRuleClassifier(
        		YooreekaConfigurator.getHome()+"/data/ch05/spamRules.drl");

        classifier.train();

        System.out.println("1. Expecting one spam email...");
        System.out.println("\n");        
        for(Email email : ds.getEmails() ) {
            System.out.println("\nClassifying email: " + email.getId() + " ...");

            Concept c = classifier.classify(email);
            
            System.out.println("Rules classified email: " + email.getId() + 
                    " as: " + c.getName());
        }
        System.out.println("\n");
        
        
        /* 
         * There should be no spam emails - rule that checks for known email address
         * should win over rules that detect spam content.
         */
        classifier = new EmailRuleClassifier(
        		YooreekaConfigurator.getHome()+"/data/ch05/spamRulesWithConflict.drl");

        classifier.train();
        System.out.println("2. There should be no spam emails.\n" + 
                "Rule that checks for known email address should\n" +
                "win over rules that detect spam content.");
        System.out.println("\n");
        
        for(Email email : ds.getEmails() ) {
            Concept c = classifier.classify(email);
            
            System.out.println("Email: " + email.getId() + " classified as: " + c.getName());
        }

        
    }
}