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
package org.yooreeka.algos.clustering.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.config.YooreekaConfigurator;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class SortedArrayClustering {

	private static final Logger LOG = Logger.getLogger(SortedArrayClustering.class.getName());

	public static void cluster(DataPoint[] points) {

		LOG.setLevel(YooreekaConfigurator.getLevel(SortedArrayClustering.class.getName()));

		Arrays.sort(points, new Comparator<DataPoint>() {
			public int compare(DataPoint p1, DataPoint p2) {
				int result = 0;
				// sort based on score value
				if (p1.getR() < p2.getR()) {
					result = 1; // sorting in descending order
				} else if (p1.getR() > p2.getR()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}
		});

		for (int i = 0; i < points.length; i++) {
			System.out.println(points[i].toShortString());
		}
	}
}
