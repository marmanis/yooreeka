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
package org.yooreeka.examples.credit.util;

import java.util.Map;
import java.util.Random;

import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Instance;

/**
 * Builds bootstrap training sets from the original training set.
 */
public class BootstrapTrainingSetBuilder {

	private TrainingSet originalTrainingSet;

	/**
	 * 
	 * @param originalTrainingSet
	 *            bootstrap training sets will be derived from this training
	 *            set.
	 * @param bootstrapSampleSize
	 *            size of bootstrap training sets that should be produced.
	 */
	public BootstrapTrainingSetBuilder(TrainingSet originalTrainingSet) {

		this.originalTrainingSet = originalTrainingSet;
	}

	public TrainingSet buildBootstrapSet() {

		int N = originalTrainingSet.getSize();

		Map<Integer, Instance> instances = originalTrainingSet.getInstances();

		Instance[] selectedInstances = new Instance[N];
		/*
		 * Building a new training set of size N by sampling N instances from
		 * the original data set with replacement. As a result, some instances
		 * from the original data set will be missing and some will be
		 * duplicated.
		 */
		Random rnd = new Random();

		// pick a center
		int center = rnd.nextInt(N);

		int countN = 0;

		while (countN < N) {

			if (countN % (N / 5) == 0) {
				center = rnd.nextInt(N);
			}

			int selectedInstanceId = pickInstanceId(N, center);

			Instance selectedInstance = instances.get(selectedInstanceId);
			selectedInstances[countN] = selectedInstance;
			countN++;
		}

		TrainingSet tS = new TrainingSet(selectedInstances);

		return tS;
	}

	private int pickInstanceId(int N, int center) {

		Random rnd = new Random();
		boolean loop = true;
		int selectedInstanceId = -1;

		// create the scale factor
		double scale = (N / 2) / 4.0d;

		while (loop) {

			// center the distribution to be N/2 left and right of the center
			// with almost certainty
			selectedInstanceId = new Double(center + rnd.nextGaussian() * scale)
					.intValue();

			// do not break the loop unless we found a valid instance
			if (selectedInstanceId >= 0 && selectedInstanceId < N) {
				loop = false;
			}
		}
		return selectedInstanceId;
	}

}
