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
package org.yooreeka.algos.taxis.ensemble;

import java.util.HashMap;
import java.util.Map;

import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.util.C;

public class ConceptMajorityVoter {

	private Map<Concept, Integer> votes = new HashMap<Concept, Integer>();

	private Instance i;

	public ConceptMajorityVoter(Instance i) {
		this.i = i;
	}

	public void addVote(Concept c) {

		Integer conceptVoteCount = votes.get(c);

		if (conceptVoteCount == null) {
			
			conceptVoteCount = Integer.valueOf(C.ONE_INT); 
			
		} else {
			conceptVoteCount = conceptVoteCount + C.ONE_INT;

		}
		votes.put(c, conceptVoteCount);
	}

	public Concept getWinner() {

		int winnerVoteCount = 0;
		Concept winnerConcept = null;

		for (Map.Entry<Concept, Integer> e : votes.entrySet()) {
			if (e.getValue() > winnerVoteCount) {
				winnerConcept = e.getKey();
				winnerVoteCount = e.getValue();
			}
		}

		return winnerConcept;
	}

	public int getWinnerVoteCount() {
		Concept winner = getWinner();
		return votes.get(winner);
	}

	public void print() {
		System.out.println("Votes for instace [" + i + "] : " + votes);
		System.out.println("Winner concept: " + getWinner());
	}

}
