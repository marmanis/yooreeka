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
 *   Copyright (c) 2009-2013 Marmanis Group LLC and individual contributors as indicated by the @author tags.  
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.yooreeka.util.C;
import org.yooreeka.util.P;
import org.yooreeka.util.metrics.EuclideanDistance;

/**
 * A set of points for an arbitrary n-dimensional space. The space is considered
 * continuous, not discrete, hence the coordinates are of type <tt>double</tt>.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class VectorSet {

	// The dimensionality of the space
	private int dimensionality;
	
	//This is the list of all the points
	private ArrayList<double[]> points;
	
	private double[] valueMax, valueMin;
	
	// Normalizing constant
	private double c=C.ZERO_DOUBLE;
	
	private double[][] distances=null;
	
	/**
	 * 
	 */
	public VectorSet(int dimensionality) {
		this.dimensionality = dimensionality;

		points = new ArrayList<double[]>();

		valueMax = new double[dimensionality];
		for(int j=0; j<dimensionality; j++) {
			valueMax[j] = Double.MIN_VALUE;
		}

		valueMin = new double[dimensionality];
		for(int j=0; j<dimensionality; j++) {
			valueMin[j] = Double.MAX_VALUE;
		}
	}

	/**
	 * @return the dimensionality
	 */
	public int getDimensionality() {
		return dimensionality;
	}

	/**
	 * @return the points
	 */
	public ArrayList<double[]> getPoints() {
		return points;
	}

	/**
	 * This method finds the pairwise distances of all points in the <tt>VectorSet</tt>.
	 * 
	 * This implementation is not optimal from a storage perspective.
	 * 
	 * @return distances in matrix form
	 */
	public void computeDistanceMatrix() {

		points.trimToSize();
		int s = points.size(); // current size

		if (distances==null) 
			distances = new double[s][s];

		for (int i = 0; i < s; i++) {
			// Calculate only half the points (due to symmetry)
			for (int j = i + 1; j < points.size(); j++) {
				distances[i][j] = EuclideanDistance.calculate(points.get(i), points.get(j));
			}
		}
	}
		
	public double getNormalizingConstant() {
		
		c = C.ZERO_DOUBLE;
		points.trimToSize();
		int currentSize = points.size();
		
		for (int i=0; i < currentSize; i++) {
			for (int j=i+1; j < currentSize; j++) {
				c += distances[i][j];
			}
		}
		return c;
	}
	
	public double diff(VectorSet z) {
		double delta=0d;
		
		if (getDimensionality() != z.getDimensionality())
			throw new IllegalArgumentException("You can't diff two sets that do not have the same dimensionality");

		P.println("points.size() = "+points.size());
		P.println("z.getPoints().size() = "+z.getPoints().size());
		
		if (points.size() != z.getPoints().size())
			throw new IllegalArgumentException("You can't diff two sets that do not have the same number of points");
		
//		print();
//		P.hline();
//		z.print();
		
		double dp=0.0d;
		for (int i=0; i < points.size(); i++) {
			
			dp=0.0d;
			for(int d=0; d < dimensionality; d++) {
				dp += Math.pow(get(i, d) - z.get(i, d), 2);				
			}
			
			delta += dp;
		}

		P.println("delta: "+delta);
		return delta;
	}
	
	public void add(double[] p) {
		points.add(p);
		
		for(int j=0; j<dimensionality; j++) {
			
			if (p[j] > valueMax[j])
				valueMax[j]=p[j];
			
			if (p[j] < valueMin[j])
				valueMin[j]=p[j];
		}
	}
	
	public double[] get(int i) {
		return points.get(i);
	}
	
	public double get(int i, int j) {
	
		return points.get(i)[j];
	}
	
	public void set(int i, int j, double val) {
	
		points.get(i)[j] = val;
		
		if (points.get(i)[j] > valueMax[j])
			valueMax[j]=points.get(i)[j];
		
		if (points.get(i)[j] < valueMin[j])
			valueMin[j]=points.get(i)[j];		
	}
	
	public double getMaxValue(int dimIndex) {
		return valueMax[dimIndex];
	}
	
	public double getMinValue(int dimIndex) {
		return valueMin[dimIndex];
	}
	
	public double getRange(int dimIndex) {
		return valueMax[dimIndex]-valueMin[dimIndex];
	}
	
	public void replaceWith(VectorSet vs) {

		if (vs.dimensionality != dimensionality)
			throw new IllegalArgumentException("You can only replace a VectorSet with a VectorSet of the same dimensionality");
		
		points.clear();
		
		for (int i=0; i<vs.getPoints().size(); i++) {
			points.add(vs.get(i));
		}
	}
	
	public void populate(int numberOfPoints, double range) {
		
		points.clear();
		
		Random rand = new Random(99991); // this is a prime, just in case you wonder ...
		
		for (int i=0; i<numberOfPoints; i++) {
			
			double[] v = new double[dimensionality];
			
			for (int j=0; j<dimensionality; j++) {
				int sign = rand.nextBoolean() ? -1 : 1;
				v[j] = sign*rand.nextDouble()*range*0.5d;
			}
			points.add(v);
		}
	}
	
	public double[] getDimData(int dim) {
		int n = getPoints().size();
		
		double[] x = new double[n];
		
		for (int i=0; i < n; i++) {
			x[i] = get(i,dim);
		}
		
		return x;
	}
	
	public double getDistance(int i, int j) {
		double d;
		if (i==j) {
			d= C.ZERO_DOUBLE;
		} else {
			if (distances[i][j]>0) {
				d = distances[i][j];
			} else {
				d = distances[j][i];
			}
		}
		return d;
	}
	public void print() {
		P.hline();
		for (int i=0; i<points.size(); i++) {
			P.println(Arrays.toString(points.get(i))+"\n");
		}
		P.hline();
	}	
}
