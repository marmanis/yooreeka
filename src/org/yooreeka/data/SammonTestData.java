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
package org.yooreeka.data;

import java.util.Random;

import org.yooreeka.util.C;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class SammonTestData {

	private int numberOfPoints;
	private int numberOfDimensions;
	private final double r = C.ONE_DOUBLE;
	private final double pi = Math.PI;
	private Random rand = new Random();
	
	/**
	 * 
	 */
	public SammonTestData(int N, int dim) {
		numberOfPoints=N;
		numberOfDimensions=dim;
	}
	
	public VectorSet getSquareClusters() {

		VectorSet v = new VectorSet(numberOfDimensions);

		//Create two clusters of N points
		for (int i=0; i<numberOfPoints; i++) {
			
			double[] point = new double[numberOfDimensions];

			//Upper right corner for any dimensionality
			if (i<numberOfPoints/2) {
				for (int j=0; j<numberOfDimensions; j++) {
					point[j] = C.ONE_DOUBLE+rand.nextDouble()*C.DECI_DOUBLE;
				}
			} else {
				for (int j=0; j<numberOfDimensions; j++) {
					point[j] = -C.ONE_DOUBLE+rand.nextDouble()*C.DECI_DOUBLE;
				}
			}
			v.add(point);
		}
		return v;
	}

	public VectorSet getStraightLines() {

		VectorSet v = new VectorSet(numberOfDimensions);

		double dx = 0.2d/numberOfPoints;
		
		//Create two straight lines of N points
		for (int i=0; i<numberOfPoints; i++) {
			
			double[] point = new double[numberOfDimensions];
			
			//Upper right corner for any dimensionality
			if (i%2==0) {
				for (int j=0; j<numberOfDimensions; j++) {
					point[j] = C.ONE_DOUBLE+dx*i;
				}
			} else {
				for (int j=0; j<numberOfDimensions; j++) {
					point[j] = -C.ONE_DOUBLE-dx*i;
				}
			}
			v.add(point);
		}
		return v;
	}

	/**
	 * 
	 * @return VectorSet points along two parts of a helix
	 */
	public VectorSet getHelix3D() {
		
		VectorSet v = new VectorSet(numberOfDimensions);

		double dt;
		for (int i=0; i<numberOfPoints; i++) {

			double[] point = new double[numberOfDimensions];
			
			dt = C.ONE_DOUBLE / numberOfPoints;
			double amp = 0.01d;
			
			//Points on a helix for 3-d with some perturbations added for x-y
			for (int j=0; j<numberOfDimensions; j++) {
				if (j==0) {
					point[j] = amp*Math.cos(dt*j)+amp*rand.nextDouble();
				} else if (j==1) {
					point[j] = amp*Math.sin(dt*j)+amp*rand.nextDouble();
				} else {
					if (i%2==0) {
						point[j] = C.ONE_DOUBLE + dt*i;
					} else {
						point[j] = -C.ONE_DOUBLE + dt*i;
					}
				}
			}
			v.add(point);
		}
		return v;
	}
	
		/**
		 * Points on two spheres
		 * 
		 * @return VectorSet that contains all the points
		 */
		public VectorSet getTwoSpheres() {
			
			VectorSet v = new VectorSet(numberOfDimensions);

			//Create two clusters of N points
			for (int i=0; i<numberOfPoints; i++) {
				
				double[] point = new double[numberOfDimensions];
				double theta = ((double) i/ (double)numberOfPoints)*2.0d*pi;
				double phi = ((double) i/ (double)numberOfPoints)*pi;

				if (i%2==0) {
					point[0] = 2.0d+r*Math.cos(theta)*Math.sin(phi);
					point[1] = 2.0d+r*Math.sin(theta)*Math.sin(phi);
					point[2] = 2.0d+r*Math.cos(phi);				
				} else {
					point[0] = -2.0d+r*Math.cos(theta)*Math.sin(phi);
					point[1] = -2.0d+r*Math.sin(theta)*Math.sin(phi);
					point[2] = -2.0d+r*Math.cos(phi);
				}
				
				v.add(point);
		}
		v.print();
		return v;
	}		


}
