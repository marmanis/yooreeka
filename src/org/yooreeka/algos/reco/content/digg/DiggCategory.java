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
package org.yooreeka.algos.reco.content.digg;

import java.util.ArrayList;
import java.util.List;

import de.thesuntoucher.jigg.data.Container;

public class DiggCategory extends Container {
	private static final List<DiggCategory> allCategories = new ArrayList<DiggCategory>();

	public static final DiggCategory TECHNOLOGY = new DiggCategory(
			"Technology", "technology");
	public static final DiggCategory WORLD_AND_BUSINESS = new DiggCategory(
			"World&Business", "world_business");
	public static final DiggCategory SPORTS = new DiggCategory("Sports",
			"sports");
	public static final DiggCategory SCIENCE = new DiggCategory("Science",
			"science");
	public static final DiggCategory GAMING = new DiggCategory("Gaming",
			"gaming");
	public static final DiggCategory ENTERTAINMENT = new DiggCategory(
			"Entertainment", "entertainment");
	public static final DiggCategory VIDEOS = new DiggCategory("Videos",
			"videos");

	public static List<DiggCategory> getAllCategories() {
		return DiggCategory.allCategories;
	}

	public static DiggCategory valueOf(String name) {
		DiggCategory match = null;
		for (DiggCategory c : allCategories) {
			if (c.getName().equalsIgnoreCase(name)) {
				match = c;
				break;
			}
		}
		return match;
	}

	private DiggCategory(String name, String shortName) {
		super(name, shortName);
		allCategories.add(this);
	}

	// Note that default Container.toString() implementation in jigg library
	// won't work with digg api call.
	@Override
	public String toString() {
		return getShortName();
	}

}
