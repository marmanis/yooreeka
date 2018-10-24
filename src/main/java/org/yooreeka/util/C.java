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
 *   Copyright (c) 2009-2012 Marmanis Group LLC and individual contributors as indicated by the @author tags.  
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

/**
 * Various constants to avoid typing literals and values in the code.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class C {

	/*
	 * NUMERICAL CONSTANTS
	 */
	public final static int  ZERO_INT = 0;
	public final static long ZERO_LONG = 0;
	public final static double ZERO_DOUBLE = 0.0d;
	
	public final static double SMALL_DOUBLE = 0.000001d;
	public final static double MILLI_DOUBLE = 0.001d;
	public final static double CENTI_DOUBLE = 0.01d;
	public final static double DECI_DOUBLE = 0.1d;
	

	public final static int  ONE_INT = 1;
	public final static long ONE_LONG = 1;
	public final static double ONE_DOUBLE = 1.0;

	public final static int  TWO_INT = 2;
	public final static long TWO_LONG = 2;
	public final static double TWO_DOUBLE = 2.0;
	
	public final static double GOLDEN_RATIO_PHI_PLUS = (1.0d+(Math.sqrt(5.0d)))/2.0d;
	public final static double GOLDEN_RATIO_PHI_MINUS = (1.0d-(Math.sqrt(5.0d)))/2.0d;
	
	/*
	 * LITERAL CONSTANTS
	 */
	public static final String EMPTY_STRING="";
	public static final String LINE_FEED="\n";
	public static final String UNDERSCORE="_";
	public static final String DASH="-";
	public static final String SEMICOLON=";";
	public static final String COMMA=",";
	public static final String COLON=":";
	public static final String DOT=".";
	
}
