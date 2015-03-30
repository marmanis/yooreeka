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
package org.yooreeka.math;

import org.yooreeka.util.P;

/**
 * A class that can create generalized Fibonacci sequences.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class Fibonacci {

	private int order;
	private int size;
	private long[] memory;
	
	/**
	 * This constructor allows us to create an implementation that is more efficient
	 * than the recursive implementation.
	 * 
	 * @param order is the order of the generalized Fibonacci
	 * @param size is the number of Fibonacci numbers that we intend to evaluate
	 */
	public Fibonacci(int order, int size) {
		this.order = order;
		this.size  = size;
		memory = new long[size];
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
		for (int i=0; i<size; i++) {
			if (i<order)
				memory[i] = i;
			else
				memory[i] = -1;
		}
	}
	
	/**
	 * Diagnostic utility
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
	public long get(int n) {
		if (memory[n] < 0) {
			memory[n] = recursive(order,n);
		} 
		return memory[n];
	}
	
	/**
	 * This is a recursive implementation of a generalization of the Fibonacci sequence.
	 * DO NOT USE this, it has exponential complexity. 
	 * 
	 * @param order the order of generalization
	 * @param n the number
	 * @return the generalized Fibbonacci number
	 */
	public long recursive(final int order, int n) {
		long val=0;

		if (n<order) {
			val = memory[n];
		} else {
			
			for (int i=1; i<=order; i++) {
				val += recursive(order,n-i);				
			}
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

		int terms=32; //, points=30; double dx=0.05;
		Fibonacci f2 = new Fibonacci(2,terms);
		
//		double[] gX=new double[points], gXX=new double[points], gY; 
//		double start=-0.75;
//		
//		gY = f2.powerSeries(terms, points, start, dx);
//
//		for (int i=0; i<points; i++) {
//			gX[i] = start + i*dx;
//			gXX[i] = (gX[i])/(1-gX[i]-gX[i]*gX[i]);
//		}
//		
//		
//		XyGui g = new org.yooreeka.util.gui.XyGui ("Fibonacci",gX,gY);
//		g.addSeries("Exact", gX, gXX);
//		g.plot();
		
		Fibonacci f3 = new Fibonacci(3,terms);
		Fibonacci f4 = new Fibonacci(4,terms);
		Fibonacci f5 = new Fibonacci(5,terms);
		Fibonacci f6 = new Fibonacci(6,terms);
		Fibonacci f7 = new Fibonacci(7,terms);
		Fibonacci f8 = new Fibonacci(8,terms);
		StringBuilder msg = new StringBuilder("F2 \t\tF3 \t\tF4 \t\tF5 \t\tF6 \t\tF7 \t\tF8\n");
		msg.append("------- ------- ------- -------\n");
		
		for (int i=2; i < terms; i++) {
			msg.append(f2.get(i)).append(" \t\t");
			msg.append(f3.get(i)).append(" \t\t");
			msg.append(f4.get(i)).append(" \t\t");
			msg.append(f5.get(i)).append(" \t\t");
			msg.append(f6.get(i)).append(" \t\t");
			msg.append(f7.get(i)).append(" \t\t");
			msg.append(f8.get(i)).append("\n");			
		}
		P.print(msg.toString());
	}
}
