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
package org.yooreeka.algos.mappings;

import org.yooreeka.data.SammonTestData;
import org.yooreeka.data.VectorSet;
import org.yooreeka.util.C;
import org.yooreeka.util.P;
import org.yooreeka.util.gui.ScatterGui;

/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class SammonMap {

	private final double NUMBER_TWO_DOUBLE = 2.0d;
	
	public static final double ALPHA_LOWER_BOUND = 0.3d;
	public static final double ALPHA_UPPER_BOUND = 0.4d;
	
	public static final double EPSILON=0.000001d;
	public static final double E2=0.00000000001d;
	
	// This is the same set of points whose dimensionality has been reduced
	private VectorSet y;
	
	private int newDimensionality;
	
	//The range of alpha is between 0.3 and 0.4
	private double alpha=0.3;
	
	// This is a cache of sorts. It holds the normalizing constant of the original vector.
	private double c;

	@SuppressWarnings("unused")
	private SammonMap() {
		// FORCE the use of the constructor where we specify 
		// the number of new dimensions
	}
	
	/**
	 * The constructor specifies the new dimensionality
	 */
	public SammonMap(int newDimensionality) {
		this.newDimensionality = newDimensionality;
		y = new VectorSet(newDimensionality);		
	}

	public VectorSet map(VectorSet x) {
		
		int numOfPoints = x.getPoints().size();
		
		//Initialize
		x.computeDistanceMatrix();
		
		double range = Double.MIN_VALUE;
		for(int j=0; j<x.getDimensionality(); j++) {
		
			if (x.getRange(j) > range)
				range = x.getRange(j);
		}
		y.populate(numOfPoints, range);
		y.computeDistanceMatrix();
		//y.print();
		
		// we need two vector sets because we iterate
		// However, a more compact implementation is possible
		VectorSet nextY = new VectorSet(newDimensionality);
		
		int m=0;
		boolean hasConverged=false;
		
		while (!hasConverged) {

			if(m>0) {
				//Replace the old with the new (corrected) set of points
				y.replaceWith(nextY);
				c = y.getNormalizingConstant();
				y.computeDistanceMatrix();
				nextY.getPoints().clear();
			}

			//y2 = y - alpha* (num/denum);
			for(int p=0; p < numOfPoints; p++) {
				
				double[] point = y.getPoints().get(p);
				double[] newPoint = new double[newDimensionality];
				
				for (int q=0; q<newDimensionality; q++) {
					
					newPoint[q] = point[q] - alpha *(getNumerator(p,q,x)/getDenumerator(p,q,x));
				}

				// DEBUG
				//		P.println("   Point["+p+"]: "+Arrays.toString(point));
				//		P.println("newPoint["+p+"]: "+Arrays.toString(newPoint));
				
				nextY.add(newPoint);
			}
			
			P.println(m+" iterations completed");
			m++;
			
			if (y.diff(nextY)<C.SMALL_DOUBLE) {
				hasConverged=true;
			}
		}
		
		P.println("y.diff(y2) = "+y.diff(nextY));
		
		return y;
	}
	
	private double getNumerator(int p, int q, VectorSet x) {
		double num=0.0d;
		for(int j=0; j<x.getPoints().size(); j++) {			
			if (p!=j) {
				//original distances
				double dpjOld = x.getDistance(j,p);
				
				//distances between the points in the reduced dimensionality space
				double dpjNew = y.getDistance(j,p);
				
				double deltaD = dpjOld - dpjNew;
					
				double prodD = dpjOld * dpjNew;

				double deltaY = y.get(p, q) - y.get(j, q);

				num += (deltaD/prodD)*deltaY;
			}	
		}

		if (Math.abs(c)<EPSILON) {
			c += EPSILON;
		}

		return -(NUMBER_TWO_DOUBLE/c)*num;
	}
	
	private double getDenumerator(int p, int q, VectorSet x) {
		double denum=0.0d;
		for(int j=0; j<x.getPoints().size(); j++) {			
			if (p!=j) {
				
				//original distances
				double dpjOld = x.getDistance(j,p);
				
				//distances between the points in the reduced dimensionality space 
				double dpjNew = y.getDistance(j,p);
					
				double deltaD = dpjOld - dpjNew;
					
				double prodD = dpjOld * dpjNew;
					
				double deltaY = y.get(p, q) - y.get(j, q);	
				double deltaY2 = deltaY*deltaY;
					
				//P.println("("+j+") | dpj:"+dpjOld+" | deltaD: "+deltaD+" | prodD: "+prodD+" | deltaY: "+deltaY+" | deltaY2: "+deltaY2);
					
				double f01 = C.ONE_DOUBLE + (deltaD/dpjNew);
					
				double f02 = deltaY2/dpjNew;
				
				double f03 = f02*f01;
					
				denum = denum + (deltaD-f03)/prodD;
			}	
		}
		
		if (Math.abs(denum)<E2) {
			denum += E2;
		}
				
		if (Math.abs(c)<EPSILON) {
			c += EPSILON;
		}

		return -(NUMBER_TWO_DOUBLE/c)*denum;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		SammonTestData data = new SammonTestData(128,19); 
		VectorSet v = data.getSquareClusters(); //.getStraightLines();
	
		SammonMap sMap = new SammonMap(2);
		VectorSet w = sMap.map(v);
		
		String title = "Sammon Projection in 2D";
		ScatterGui ui = new org.yooreeka.util.gui.ScatterGui (title);
		ui.setWindowDimensionX(1000);
		ui.setWindowDimensionY(750);
		ui.plot(ui.createScatterPlot(w.getDimData(0),w.getDimData(1)));
	}

	/**
	 * @return the alpha
	 */
	public double getAlpha() {
		return alpha;
	}

	/**
	 * This method does not allow you to set the value of alpha
	 * outside the designated range.
	 * 
	 * @param val the alpha to set
	 */
	public void setAlpha(double val) {
		
		if (val < ALPHA_LOWER_BOUND) {
			
			alpha = ALPHA_LOWER_BOUND;
			
		} else if (val > ALPHA_UPPER_BOUND) {
			
			alpha = ALPHA_UPPER_BOUND;
			
		} else 
			alpha = val;
	}

	/**
	 * @return the y
	 */
	public VectorSet getY() {
		return y;
	}

	/**
	 * @return the newDimensionality
	 */
	public int getNewDimensionality() {
		return newDimensionality;
	}
}
