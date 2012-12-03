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
package org.yooreeka.examples.fraud;

import org.yooreeka.algos.taxis.networks.neural.core.BaseNN;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Layer;

public class TransactionNN extends BaseNN {

	private static final long serialVersionUID = -3840865001527729603L;

	public TransactionNN(String name) {
		super(name);

		createNN351();
	}

	/*
	 * Creates: 3 -> 5 -> 1 network.
	 */
	private void createNN351() {

		// 1. Define Layers, Nodes and Node Biases
		Layer inputLayer = createInputLayer(0, // layer id
				3 // number of nodes
		);

		Layer hiddenLayer = createHiddenLayer(1, // layer id
				5, // number of nodes
				new double[] { 1, 1.5, 1, 0.5, 1 } // node biases
		);

		Layer outputLayer = createOutputLayer(2, // layer id
				1, // number of nodes
				new double[] { 1.5 } // node biases
		);

		setInputLayer(inputLayer);
		setOutputLayer(outputLayer);
		addHiddenLayer(hiddenLayer);

		// 2. Define links and weights between nodes
		// Id format: <layerId:nodeIdwithinLayer>

		// Weights for links from Input Layer to Hidden Layer
		setLink("0:0", "1:0", 0.25);
		setLink("0:0", "1:1", -0.5);
		setLink("0:0", "1:2", 0.25);
		setLink("0:0", "1:3", 0.25);
		setLink("0:0", "1:4", -0.5);

		setLink("0:1", "1:0", 0.25);
		setLink("0:1", "1:1", -0.5);
		setLink("0:1", "1:2", 0.25);
		setLink("0:1", "1:3", 0.25);
		setLink("0:1", "1:4", -0.5);

		setLink("0:2", "1:0", 0.25);
		setLink("0:2", "1:1", -0.5);
		setLink("0:2", "1:2", 0.25);
		setLink("0:2", "1:3", 0.25);
		setLink("0:2", "1:4", -0.5);

		// Weights for links from Hidden Layer to Output Layer

		setLink("1:0", "2:0", -0.5);
		setLink("1:1", "2:0", 0.5);
		setLink("1:2", "2:0", -0.5);
		setLink("1:3", "2:0", -0.5);
		setLink("1:4", "2:0", 0.5);

		if (isVerbose()) {
			System.out.println("NN created");
		}

	}

}
