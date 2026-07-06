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
package org.yooreeka.test.nlp;

import java.util.ArrayList;

import org.yooreeka.util.P;
import org.yooreeka.util.text.AlphabetProjection;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class AlphabetProjectionTests {
	
	AlphabetProjection aProjection;

	public AlphabetProjectionTests(AlphabetProjection aProjection) {
		this.aProjection = aProjection;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		AlphabetProjectionTests apt = new AlphabetProjectionTests(new AlphabetProjection(12,16,AlphabetProjection.DEFAULT_CHARACTER_BASIS));
		
		apt.test_00();

	}

	private void test_00() {


		ArrayList<String> strings = new ArrayList<String>();
		strings.add("Andrei");
		strings.add("Nikolaevitch");
		strings.add("Kolmogorov");
		strings.add("Andrei Nikolaevitch");
		strings.add("Andrei Kolmogorov");
		strings.add("Nikolaevitch Kolmogorov");
		strings.add("Andrei Nikolaevitch Kolmogorov");
		
		int base =strings.size()-1;
		
		for (int l=6; l<AlphabetProjection.DEFAULT_CHARACTER_BASIS.length; l++) {

			this.aProjection.setDimensionality(l);
			P.hline();
			P.println("Base length= "+l);
			
			for (int i=0; i <base+1; i++) {
				
				
				P.println("d[T"+i+",T"+base+"] = "	+ aProjection.distance(strings.get(i), strings.get(base)));
			}
			
		}
	}

}
