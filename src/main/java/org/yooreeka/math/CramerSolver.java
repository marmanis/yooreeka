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

import org.yooreeka.util.P;

/**
 * This class provides a straightforward implementation of Cramer's rule for 3x3 linear systems
 *
 */
public class CramerSolver {

    private double[] a, b, c, d;

    public CramerSolver() {
        a = new double[3];
        b = new double[3];
        c = new double[3];
        d = new double[3];
    }

    public CramerSolver(final double[] a, final double[] b, final double[] c, final double[] d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public double getX() {
    	return (getDx()/getDeterminant());
    }

    public double getY() {
    	return (getDy()/getDeterminant());
    }

    public double getZ() {
    	return (getDz()/getDeterminant());
    }

    public double getDx() {
        double dX;

        dX = d[0] * ((b[1]*c[2]) - (c[1]*b[2])) - b[0] * ((d[1]*c[2])-(c[1]*d[2])) + c[0] * ((d[1]*b[2])-(b[1]*d[2]));

        return dX;
    }

    public double getDy() {
        double dY;

        dY = a[0] * ((d[1]*c[2]) - (c[1]*d[2])) - d[0] * ((a[1]*c[2])-(c[1]*a[2])) + c[0] * ((a[1]*d[2])-(d[1]*a[2]));

        return dY;
    }

    public double getDz() {
        double dZ;

        dZ = a[0] * ((b[1]*d[2]) - (d[1]*b[2])) - b[0] * ((a[1]*d[2])-(d[1]*a[2])) + d[0] * ((a[1]*b[2])-(b[1]*a[2]));

        return dZ;
    }

    public double getDeterminant() {

        double det;

        det = a[0] * ((b[1]*c[2]) - (c[1]*b[2])) - b[0] * ((a[1]*c[2])-(c[1]*a[2])) + c[0] * ((a[1]*b[2])-(b[1]*a[2]));

        return det;
    }

    /* Main class mainly for testing
     * no need to include it in the bytecode
     */
    public static void main(String[] args) {
        double[] a = new double[3];
        double[] b = new double[3];
        double[] c = new double[3];
        double[] d = new double[3];

        a[0] = 1; b[0] = 2; c[0] = 3; d[0] = 5;
        a[1] = 1; b[1] = 3; c[1] = 5; d[1] = 7;
        a[2] = 1; b[2] = 5; c[2] = 7; d[2] =11;

//        a[0] = -2; b[0] = -1; c[0] = -3; d[0] =3;
//        a[1] = 2; b[1] = -3; c[1] = 1; d[1] =-13;
//        a[2] = 2; b[2] = 0; c[2] = -3; d[2] =-11;

        CramerSolver solver = new CramerSolver(a, b, c, d);
        P.println("D = "+solver.getDeterminant());

        P.println("x = "+(solver.getDx()/solver.getDeterminant()));
        P.println("y = "+(solver.getDy()/solver.getDeterminant()));
        P.println("z = "+(solver.getDz()/solver.getDeterminant()));
    }

    public void setA(double[] a) {
        this.a = a;
    }

    public void setA(int i, double v) {
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("The index must be one of the following values: 0,1,2");

        this.a[i] = v;
    }

    public double[] getA() {
        return a;
    }

    public void setB(double[] b) {
        this.b = b;
    }

    public void setB(int i, double v) {
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("The index must be one of the following values: 0,1,2");

        this.b[i] = v;
    }

    public double[] getB() {
        return b;
    }

    public void setC(double[] c) {
        this.c = c;
    }

    public void setC(int i, double v) {
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("The index must be one of the following values: 0,1,2");

        this.c[i] = v;
    }

    public double[] getC() {
        return c;
    }

    public void setD(double[] d) {
        this.d = d;
    }

    public void setD(int i, double v) {
        if (i < 0 || i > 2)
            throw new IllegalArgumentException("The index must be one of the following values: 0,1,2");

        this.d[i] = v;
    }

    public double[] getD() {
        return d;
    }
}
