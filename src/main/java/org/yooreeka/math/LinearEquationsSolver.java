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
package org.yooreeka.math;

import org.ojalgo.OjAlgoUtils;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.R064Store;
import org.ojalgo.matrix.store.RawStore;
import org.ojalgo.matrix.task.iterative.ConjugateGradientSolver;
import org.ojalgo.netio.BasicLogger;
import org.ojalgo.type.context.NumberContext;

/**
 * This class constructs and solves a 3x3 linear system
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class LinearEquationsSolver {
	
	private RawStore matrix;
	private MatrixStore<Double> unknowns;
	private R064Store rhs;

	public LinearEquationsSolver(double[][] matrixData, double[] vector) {
		
		 BasicLogger.debug();
		 BasicLogger.debug(LinearEquationsSolver.class);
		 BasicLogger.debug(OjAlgoUtils.getTitle());
		 BasicLogger.debug(OjAlgoUtils.getDate());
		 BasicLogger.debug();

		matrix = RawStore.wrap(matrixData);
		rhs = R064Store.FACTORY.column(vector);
	}


	public MatrixStore<Double> solve() {

		ConjugateGradientSolver conjugateGradient = new ConjugateGradientSolver();

        // BasicLogger.debug(matrixA.get(3)+", "+matrixA.get(4)+", "+matrixA.get(5));

		// Theoretically a conjugate gradient solver should be able to solve a 4x4 system within 4 iterations
		conjugateGradient.configurator().debug(BasicLogger.DEBUG).iterations(100).accuracy(NumberContext.of(8));
		BasicLogger.debug();
		BasicLogger.debug("Conjugate Gradient");
		unknowns = conjugateGradient.solve(matrix, rhs).get();

		return unknowns;
	}
	
	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

//        double[][] data = { { 3.0, 5.0, 7.0, 11.0 },
//				            { 5.0, 7.0, 11.0, 13.0 },
//				            { 7.0, 11.0, 13.0, 17.0 },
//				            { 11.0, 13.0, 17.0, 19.0},
//						};
//        double[] v = {13.0, 17.0, 19.0, 23.0};
        
		// String primes100M = "D:\\Data\\PRIMES\\primes.100m.txt";
//		String primes100M = "D:\\Data\\PRIMES\\primes-to-100k.txt";
//
//        long[] primes = null;
//
//        try {
//			primes = Primes.loadPrimes(primes100M,10000);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//        int n=7;
//
//        double[][] data = new double[n][n];
//        double[] v = new double[n];
//
//        for (int i=0; i<n; i++) {
//        	for (int j=0; j<n; j++) {
//
//            	data[i][j] = primes[i+j+1];
//            }
//        	v[i] = primes[i+(n+1)];
//        }
//
//        LinearEquationsSolver solver = new LinearEquationsSolver(data,v);
//        double[] x = solver.solve();
//
//        for(int i=0;i<x.length;i++) {
//        	P.println("x["+i+"] = "+x[i]);
//        }
	}

}
