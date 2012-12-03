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
package org.yooreeka.algos.taxis.networks.neural;

import java.util.Arrays;

import org.yooreeka.algos.taxis.networks.neural.core.BaseNN;
import org.yooreeka.algos.taxis.networks.neural.core.intf.Layer;

public class XORNetwork extends BaseNN {

	private static final long serialVersionUID = -511246579251846775L;

	private static final double TINY_NUMBER = 0.00001d;

	public static void main(String[] args) {
		XORNetwork nn = new XORNetwork("XOR Test");

		nn.create();

		System.out.println("Classification using untrained network:");

		double[] x = { 0, 0 };
		double[] y = nn.classify(x);

		// Results before training

		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

		x = new double[] { 0, 1 };
		y = nn.classify(x);
		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

		x = new double[] { 1, 0 };
		y = nn.classify(x);
		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

		x = new double[] { 1, 1 };
		y = nn.classify(x);
		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

		System.out.println("Training...");

		double nearZero = 0;
		for (int i = 0; i < 16 * 1024; i++) {

			nn.train(new double[] { nearZero, nearZero }, new double[] { 0.0 });
			nn.train(new double[] { 1 + nearZero, 1 + nearZero },
					new double[] { 0.0 });
			nn.train(new double[] { 1 + nearZero, nearZero },
					new double[] { 1.0 });
			nn.train(new double[] { nearZero, 1 + nearZero },
					new double[] { 1.0 });

			if (Math.random() < 0.5) {
				nearZero = 0.0d + Math.random() * TINY_NUMBER;
			} else {
				nearZero = -(1.0d - Math.random() * TINY_NUMBER);
			}

			// nn.printWeights();
		}

		System.out.println("Trained");

		// After training

		System.out.println("Classification using trained network:");

		x = new double[] { 0, 0 };
		y = nn.classify(x);

		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

		x = new double[] { 0, 1 };
		y = nn.classify(x);
		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

		x = new double[] { 1, 0 };
		y = nn.classify(x);
		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

		x = new double[] { 1, 1 };
		y = nn.classify(x);
		System.out.println(Arrays.toString(x) + " -> " + Arrays.toString(y));

	}

	public XORNetwork(String name) {
		super(name);
	}

	/*
	 * Creates: 2 -> 3 -> 1 network.
	 */
	public void create() {

		// 1. Define Layers, Nodes and Node Biases
		Layer inputLayer = createInputLayer(0, // layer id
				2 // number of nodes
		);

		Layer hiddenLayer = createHiddenLayer(1, // layer id
				3, // number of nodes
				new double[] { 1, 1, 1 } // node biases
		);

		Layer outputLayer = createOutputLayer(2, // layer id
				1, // number of nodes
				new double[] { 2.25 } // node biases
		);

		setInputLayer(inputLayer);
		setOutputLayer(outputLayer);
		addHiddenLayer(hiddenLayer);

		// 2. Define links and weights between nodes
		// Id format: <layerId:nodeIdwithinLayer>
		setLink("0:0", "1:0", 0.25);
		setLink("0:0", "1:1", 0.5);
		setLink("0:0", "1:2", 0.25);

		setLink("0:1", "1:0", 0.25);
		setLink("0:1", "1:1", 0.5);
		setLink("0:1", "1:2", 0.25);

		setLink("1:0", "2:0", 0.8);
		setLink("1:1", "2:0", 0.4);
		setLink("1:2", "2:0", 0.8);

		System.out.println("NN created");

	}

}
