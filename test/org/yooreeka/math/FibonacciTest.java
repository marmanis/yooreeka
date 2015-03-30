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

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.yooreeka.util.C;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class FibonacciTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.yooreeka.math.Fibonacci#Fibonacci(int, int)}.
	 */
	@Test
	public void testFibonacci() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.yooreeka.math.Fibonacci#get(int)}.
	 */
	@Test
	public void testGet() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.yooreeka.math.Fibonacci#recursive(int, int)}.
	 */
	@Test
	public void testRecursive() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link org.yooreeka.math.Fibonacci#powerSeries(int, int, double, double)}.
	 */
	@Test
	public void testPowerSeries() {
		int terms=32, points=20; double dx=0.05;
		MyFibonacci f2 = new MyFibonacci(2,terms);
		
		double[] gX=new double[points], gY; 
		double start=-0.5;
		
		gY = f2.powerSeries(terms, points, start, dx);

		for (int i=0; i<points; i++) {
			
			gX[i] = start + i*dx;
			
			Assert.assertEquals(gY[i], (gX[i])/(1-gX[i]-gX[i]*gX[i]), C.MILLI_DOUBLE);
		}
		
		
//		XyGui g = new org.yooreeka.util.gui.XyGui ("Fibonacci",gX,gY);
//		g.plot();
	}

}
