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

package org.yooreeka.algos.taxis.rules;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.yooreeka.examples.spamfilter.ClassificationResult;
import org.yooreeka.examples.spamfilter.data.Email;

public class RuleEngine {

	private KieContainer kContainer;
	
	public RuleEngine(String rulesFile) throws RuleEngineException {

		try {
			KieServices kieServices = KieServices.Factory.get();
			KieFileSystem kfs = kieServices.newKieFileSystem();
			
			// Load the DRL file resource into the standard KieFileSystem path
			kfs.write("src/main/resources/rules.drl", ResourceFactory.newFileResource(rulesFile));
			
			KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
			if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
				throw new RuntimeException("Unable to compile the DRL file: " + rulesFile + " -> " + kieBuilder.getResults().getMessages());
			}
			
			kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());

		} catch (Exception e) {
			throw new RuleEngineException("Could not load/compile rules from DRL file: '" 
		                                 + rulesFile+ "' ", e);
		}
	}

	public void executeRules(ClassificationResult classificationResult, Email email) {
		KieSession kSession = kContainer.newKieSession();
		try {
			kSession.setGlobal("classificationResult", classificationResult);
			kSession.insert(email);
			kSession.fireAllRules();
		} finally {
			kSession.dispose();
		}
	}
}