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
package org.yooreeka.test;

import org.yooreeka.math.Fibonacci;
import org.yooreeka.math.MyFibonacci;
import org.yooreeka.util.C;
import org.yooreeka.util.P;
import org.yooreeka.util.gui.XyLogGui;



/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class TestSandbox {

	/**
	 * Throw your code in the main method for quick tests.
	 * This is useful when you are testing Beanshell scripts.
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
/*		int maxOrder = 16;
		int numberOfPoints = 16;
		
		double[][] x = new double[maxOrder][numberOfPoints], y = new double[maxOrder][numberOfPoints];
		
		StringBuilder msg = new StringBuilder();
		msg.append("Fib [").append(maxOrder-1).append("] = ");

		MyFibonacci[] fibonacci = new MyFibonacci[maxOrder-2];
		
			for (int j=2; j<maxOrder; j++) {
				for (int i=0; i<numberOfPoints; i++) {
				//Create the classic Fibonacci series and all higher orders up to maxOrder
				fibonacci[j-2] = new MyFibonacci(j,numberOfPoints);
				
//				if (i == numberOfPoints-1) {
					x[j-2][i] = (double) i;
					y[j-2][i] = (double) fibonacci[j-2].get(i);
					
					if (j<maxOrder-1)
						msg.append(fibonacci[j-2].get(i)).append(", ");
					else
						msg.append(fibonacci[j-2].get(i));
//				}
			}
		}
		P.println(msg.toString());
		P.hline();
		
		double[] gX=new double[numberOfPoints],gY=new double[numberOfPoints]; 
		
		int eval = 2;
		
		for (int i=0; i<numberOfPoints; i++) {
			gX[i] = x[eval][i];
			if (y[eval][i]>0)
				gY[i] = y[eval][i];
			else 
				gY[i]=C.DECI_DOUBLE;
		}
		
		P.println(gX, gY);

		XyLogGui g = new org.yooreeka.util.gui.XyLogGui ("Generalized Fibonacci",gX,gY);
		
		while (eval < 15) {
		
			eval++;
			
			for (int i=0; i<numberOfPoints; i++) {
				gX[i] = x[eval][i];
				if (y[eval][i]>0)
					gY[i] = y[eval][i];
				else 
					gY[i]=C.DECI_DOUBLE;
			}
			g.addSeries("Eval-"+eval, gX, gY);
		}
		
		g.plot();
*/
		
	}
}
