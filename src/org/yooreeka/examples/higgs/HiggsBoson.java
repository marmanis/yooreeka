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
 *   Copyright (c) 2007-2009    Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-2014 Marmanis Group LLC and individual contributors as indicated by the @author tags.  
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
package org.yooreeka.examples.higgs;

import java.io.IOException;

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.csv.CSVEntry;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class HiggsBoson {

	private AtlasDataLoader loader=null;
	
	public HiggsBoson(String dataFileName) throws IOException {
	
		// This will automatically load the file
		loader = new AtlasDataLoader(dataFileName);
		
		// This will print the headers
		loader.getF().printHeaders();
		
	}
	
	/**
	 * Generative method call for learning
	 * 
	 */
	public void learn() {
	
		//TODO
	}
	
	/**
	 * Generative method for querying the data
	 * @return 
	 * 
	 */
	public CSVEntry query(int id) {
		return loader.getF().getDoc().getCsvData().get(id);	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HiggsBoson higgs = null;
		try {
			higgs = new HiggsBoson("/home/babis/code/HiggsBoson/data/training.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		higgs.learn();
		
		P.println(higgs.query(100001).toString());

	}

}
