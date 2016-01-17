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
package org.yooreeka.util;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class P {
	
	public final static String HLINE = "---------- ---------- ---------- ---------- ---------- ----------";
	public final static String ERROR = "Error: ";
	
	/**
	 * Print errors
	 */
	public static void error(String s) {
		hline();
		println(ERROR);
		println(s);
		hline();
	}
	
	/**
	 * Print a horizontal line with 65 characters.
	 */
	public static void hline() {
		println(HLINE);
	}
	
	
	/**
	 * Auxiliary method for sending time information to the standard output. 
	 * Time is measured in milliseconds, see the documentation
	 * of <tt>System.currentTimeMillis()</tt> for details.
	 * 
	 */
	public static void time() {
		println("Time: "+System.currentTimeMillis());
	}
	
	/**
	 * Auxiliary method for sending time information to the standard output,
	 * with a specific message. 
	 * Time is measured in milliseconds, see the documentation
	 * of <tt>System.currentTimeMillis()</tt> for details.
	 * 
	 * @param String the message to print
	 */
	public static void time(String msg) {
		println("Time: "+System.currentTimeMillis()+" || "+msg);
	}

	/**
	 * Auxiliary method for sending time information to the standard output.
	 * The time is given in milliseconds and in relation to a given moment
	 * in the past, determined by the value of the argument <tt>t</tt>. 
	 * 
	 */
	public static void timePassedSince(long t) {
		println("Time: "+ (System.currentTimeMillis()-t));
	}
	
	public static void main(String[] args) {
		println(Charset.defaultCharset().displayName());
		println("" + P.class.getName());
	}

	public static void print(String s) {
		System.out.print(s);
	}
	
	public static void println(String s) {
		System.out.println(s);
	}
	
	public static void println(String s, Path path) {
		System.out.format(s, path);
		System.out.println();
	}

	public static void println(double[] x, boolean withIndex) {
		for (int i=0; i<x.length; i++) {
			if (withIndex)
				System.out.println(i+", "+x[i]);
			else
				System.out.println(x[i]);
		}
	}

	public static void println(double[] x, double[] y) {
		for (int i=0; i<x.length; i++) {
			System.out.println(x[i]+", "+y[i]);
		}
	}
}
