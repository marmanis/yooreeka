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
package org.yooreeka.math;

import org.yooreeka.util.C;
import org.yooreeka.util.P;
import org.yooreeka.util.gui.XyGui;

/**
 * A class that can create generalized Fibonacci sequences.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class MyFibonacci {

	private int memorySize=C.ZERO_INT;
	private int order;
	private int size;
	private double[] memory;
	private double[] weights;
	private long[] initialValues;
	
	/**
	 * This constructor allows us to create an implementation that is more efficient
	 * than the recursive implementation. Note that, unlike the <tt>Fibonacci</tt> class,
	 * <tt>MyFibonacci</tt> admits arbitrary initial values and arbitrary weights. Those
	 * values must be set after the constructor is called. If the setters for initial values
	 * and weights are not called then the behaviour is the same as that of the <tt>Fibonacci</tt> class.
	 * 
	 * @param order is the order of the generalized Fibonacci
	 * @param size is the number of Fibonacci numbers that we intend to evaluate
	 */
	public MyFibonacci(int order, int size) {
		this.order = order;
		this.size  = size;
		memory = new double[size];
		weights= new double[order];
		initialValues= new long[order];
		init();
	}
	
	/**
	 * We initialize the values of the sequence as follows:
	 * <UL>
	 *   <LI>Between <tt>0</tt> and <tt>order</tt> we set the value of the Fibonacci number equal to the index</LI>
	 *   <LI>Between <tt>order</tt> and <tt>size</tt> we set the value of the Fibonacci number equal to minus 1</LI>
	 * </UL>
	 * 
	 */
	private void init() {
		// Default initialization
		for (int i=0; i<order; i++) {
			initialValues[i] = i;
			weights[i]=C.ONE_DOUBLE;
		}

		for (int i=0; i<size; i++) {
			if (i<order) {
				memory[i] = initialValues[i];
			} else {
				memory[i] = Long.MIN_VALUE;
			}
		}
		memorySize=order;
	}
	
	/**
	 * Print out the Fibonacci values that are stored in memory
	 *  
	 */
	public void printMemory() {
		P.hline();
		for (int i=0; i<memory.length; i++) {
			P.println("memory["+i+"] --> "+memory[i]);
		}
		P.hline();		
	}

	/**
	 * This method stores previously computed values in "memory". The recursive method
	 * is called only when needed and it doesn't really recurse! 
	 * 
	 * @param n is the index of the generalized Fibonacci number that we want to compute
	 * 
	 * @return the nth generalized Fibonacci number
	 */
	public double get(int n) {
		if (n >= order && memory[n] == Long.MIN_VALUE) {
			memory[n] = recursive(order,n);
		} 
		return memory[n];
	}
	
	/**
	 * @return the weights
	 */
	public double[] getWeights() {
		return weights;
	}

	/**
	 * @param weights the weights to set
	 */
	public void setWeights(double[] vals) {
		for (int i=0; i<order; i++) {
			this.weights[i] = vals[i];
		}
	}

	/**
	 * @return the initialValues
	 */
	public long[] getInitialValues() {
		return initialValues;
	}

	/**
	 * @param initialValues the initial values to set
	 */
	public void setInitialValues(long[] vals) {
		for (int i=0; i<order; i++) {
			this.initialValues[i] = vals[i];
		}
	}

	/**
	 * This is a recursive implementation of a generalization of the Fibonacci sequence
	 * that uses a memory for Fibonacci numbers that were already calculated.
	 * 
	 * @param order the order of generalization
	 * @param n the number
	 * @return the generalized Fibbonacci number
	 */
	private double recursive(final int order, int n) {
		
		double val=0;
		
		if (n < memorySize) {
			val = memory[n];
			//P.println("memory["+n+"]="+val);
		} else {
			for (int i=1; i<=order; i++) {
				//P.println("val="+val+"\t\t "+weights[order-i]+"*recursive("+order+", "+(n-i)+")");
				val = val+(weights[order-i]*recursive(order,n-i));
			}
			
			memory[n] = val;
			memorySize++;
		}
		return val;
	}

	public double[] powerSeries(int terms, int points, double start, double dx) {
		
		double[] y = new double[points];
		
		double val=0, x;
		for (int point=0; point < points; point++) {
			x = start+point*dx;
			for (int i=1; i < terms; i++) {
				val += get(i)*Math.pow(x, i);
			}
			y[point] = val;
			val=0;
		}
		return y;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int terms=128;
		//AWESOME: {-0.7, 0.20, 1.0};
		double[] mf3Weights = {-0.7, 0.20, 1.0};
		
		MyFibonacci mf3 = new MyFibonacci(3,terms);
		mf3.setWeights(mf3Weights);
		
		double[] gX=new double[terms];
		double[] gF3=new double[terms];
		for (int i=0; i < terms; i++) {
			gX[i] = i;
			gF3[i] = mf3.get(i);
		}

		double[] gXX=new double[2*terms];
		double[] sinOfX=new double[2*terms];
		for (int i=0; i < 2*terms; i++) {
			gXX[i] = 0.5d*i;
			sinOfX[i] = C.TWO_DOUBLE*Math.sin(gXX[i]);
		}
		
		XyGui g = new org.yooreeka.util.gui.XyGui ("MyFibonacci vs. Sin(x)", gX);
		g.addSeries("MyFibonacci (3,terms)", gX, gF3);
		g.addSeries("Sin(x)", gXX, sinOfX);
		g.plot();

		mf3.printMemory();
		
//		StringBuilder msg = new StringBuilder();
//		
//		for (int i=0; i < terms; i++) {
//			msg.append(i).append(" \t");
//			msg.append(mf3.get(i)).append(" \t\t");
//
//			msg.append(P.HLINE).append("\n");;
//			
//		}
//		P.print(msg.toString());
	}
}
