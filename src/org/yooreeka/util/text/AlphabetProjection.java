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
package org.yooreeka.util.text;

import java.util.logging.Logger;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.C;
import org.yooreeka.util.P;
import org.yooreeka.util.metrics.EuclideanDistance;

import com.wcohen.ss.JaroWinkler;
import com.wcohen.ss.Level2Jaro;
import com.wcohen.ss.MongeElkan;
import com.wcohen.ss.NeedlemanWunsch;
import com.wcohen.ss.api.StringDistance;

/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class AlphabetProjection {

	private static final Logger LOG = Logger.getLogger(AlphabetProjection.class.getName());

	/**
	 * <tt>dimensionality</tt> determines the number of <tt>String</tt> vectors
	 * that we will use.
	 */
	public static final int DEFAULT_DIMENSIONALITY = 10;
	private int dimensionality;
	
	/**
	 * <tt>baseLength</tt> determines the length of the <TT>String</TT> vectors
	 * that we will use.
	 */
	public static final int DEFAULT_BASELENGTH = 10;
	private int baselength;
	
	// TODO: This covers only the English language. Create a separate character basis class
	//       that has all the character bases and invoke them statically as needed.
	
	public static final char[] DEFAULT_CHARACTER_BASIS = { 'e', 't', 'a', 'o', 'n', 'r', 'i', 's',
			'h', 'd', 'l', 'f', 'c', 'm', 'u', 'g', 'y', 'p', 'w', 'b', 'v',
			'k', 'x', 'j', 'q', 'z' };
	private char[] characterBasis;
	
	private String[] projectionBasis = null;

	//TODO: These should be passed to the projection class. Take them out and define an 
	//      appropriate encapsulation
	
	// String Edit Distance Metrics
	private NeedlemanWunsch needlemanWunch;
	private JaroWinkler jaroWinkler;
	private Level2Jaro level2Jaro;
	private MongeElkan mongeElkan;

	// String Distances
	private StringDistance needlemanWunchDistance = null;
	private StringDistance jaroWinklerDistance    = null;
    private StringDistance level2JaroDistance     = null;
    private StringDistance mongeElkanDistance     = null;
    
	// --------------------------------------------------------------------------------
	// CONSTRUCTORS
	// --------------------------------------------------------------------------------
	public AlphabetProjection(int dim, int length, char[] charBasis) {

		LOG.setLevel(YooreekaConfigurator.getLevel(AlphabetProjection.class.getName()));

		if (dim > 0) {
			dimensionality = dim;
		} else {
			dimensionality = AlphabetProjection.DEFAULT_DIMENSIONALITY;
		}
		
		if (length <=0) {
			baselength = length;
		} else {
			baselength = AlphabetProjection.DEFAULT_BASELENGTH;
		}
		
		if (charBasis != null) {
			characterBasis = charBasis;
		} else {
			characterBasis = AlphabetProjection.DEFAULT_CHARACTER_BASIS;
		}
		
		// Initialize the projection
		initProjection();
		
		// Initialize the String edit distance metrics
		initMetrics();
	}
	
	// --------------------------------------------------------------------------------
	// INITIALIZATION
	// --------------------------------------------------------------------------------
	/**
	 * Initialize the configuration space.
	 */
	private void initProjection() {

		projectionBasis = new String[dimensionality];

		// First define the String basis onto which we will project a given
		// String
		for (int i = 0; i < dimensionality; i++) {
			projectionBasis[i] = getEigenvector(characterBasis[i]);
		}
	}
	
	private void initMetrics() {
		needlemanWunch = new NeedlemanWunsch();
		jaroWinkler    = new JaroWinkler();
		level2Jaro     = new Level2Jaro();
		mongeElkan     = new MongeElkan();		
	}
	

	// --------------------------------------------------------------------------------
	// PROJECTION METHODS
	// --------------------------------------------------------------------------------
	/**
	 * 
	 * @param target
	 *            the String that we want to project onto the base vectors
	 * @param projections
	 *            of the <CODE>target</CODE> onto each one of the base vectors.
	 * 
	 * 
	 */
	public double[] project(String target) throws IllegalArgumentException {

		double[] projections = new double[dimensionality];

		if (target == null) {
			target = C.EMPTY_STRING;
		}

		target.toLowerCase();
		
		jaroWinklerDistance    = jaroWinkler.getDistance();
	    level2JaroDistance     = level2Jaro.getDistance();
	    mongeElkanDistance     = mongeElkan.getDistance();
		needlemanWunchDistance = needlemanWunch.getDistance();

	    double p = 0;
	    
		for (int i = 0; i < dimensionality; i++) {

			p=jaroWinklerDistance.score(projectionBasis[i], target);
			p += level2JaroDistance.score(projectionBasis[i], target);
			p += mongeElkanDistance.score(projectionBasis[i], target);
			p += needlemanWunchDistance.score(projectionBasis[i], target);
			
			projections[i] = p*0.25;
		}

		return projections;
	}

	/**
	 * 
	 * @param target
	 *            the String that we want to project onto the base vectors
	 * @param projections
	 *            of the <CODE>target</CODE> onto each one of the base vectors.
	 * 
	 * 
	 */
	public double[] project(String target, StringDistance d) throws IllegalArgumentException {

		double[] projections = new double[dimensionality];

		if (target == null) {
			target = C.EMPTY_STRING;
		}

		target.toLowerCase();
		
		for (int i = 0; i < dimensionality; i++) {

			projections[i] = d.score(projectionBasis[i], target);
		}

		return projections;
	}

	// --------------------------------------------------------------------------------
	// AUXILIARY METHODS
	// --------------------------------------------------------------------------------
    /**
     * Creates instance with default parameters (suitable when you are unaware of 
     * best parameters to constructor.
     * 
     * @return instance with default parameters applied.
     * 
     */
    public static AlphabetProjection getDefault() {
        return new AlphabetProjection(DEFAULT_DIMENSIONALITY, DEFAULT_BASELENGTH, DEFAULT_CHARACTER_BASIS);
    }
    
	public double distance(String val1, String val2) {

		EuclideanDistance euclid = new EuclideanDistance();

		return euclid.getDistance(project(val1), project(val2));
	}

	/**
	 * @param val
	 *            the single character of the base vector
	 * 
	 * @return the base vector for the <tt>val</tt> character.
	 */
	public String getEigenvector(char val) {

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < baselength; i++) {

			buf.append(val);
		}

		return buf.toString();
	}
	
	// --------------------------------------------------------------------------------
	// MAIN METHOD
	// --------------------------------------------------------------------------------
	public static void main(String[] args) throws Exception {

		AlphabetProjection aProjection = new AlphabetProjection(10,10,AlphabetProjection.DEFAULT_CHARACTER_BASIS);

		final String TEST_STRING_1 = "Андре́й Никола́евич Колмого́ров";//"Andrei Nikolaevitch Kolmogorov";
		final String TEST_STRING_2 = "Колмого́ров Андре́й Никола́евич";//"Kolmogorov Andrei Nikolaevitch";
		final String TEST_STRING_3 = "Nikolai";

		P.println("d[T1,T2] = "
				+ aProjection.distance(TEST_STRING_1, TEST_STRING_2));
		P.println("d[T1,T3] = "
				+ aProjection.distance(TEST_STRING_1, TEST_STRING_3));
	}

	// --------------------------------------------------------------------------------
	// GETTERS -- SETTERS
	// --------------------------------------------------------------------------------
	
	public static int[] getDefaultProjectionProperties() {
		return new int[] { DEFAULT_DIMENSIONALITY, DEFAULT_BASELENGTH };
	}

	public int getBaselength() {
		return baselength;
	}

	public void setBaselength(int baselength) {
		this.baselength = baselength;
	}

	public int getDimensionality() {
		return dimensionality;
	}

	public void setDimensionality(int dimensionality) {
		this.dimensionality = dimensionality;
	}

	/**
	 * @return the characterBasis
	 */
	public char[] getCharacterBasis() {
		return characterBasis;
	}

	/**
	 * @param characterBasis the characterBasis to set
	 */
	public void setCharacterBasis(char[] characterBasis) {
		this.characterBasis = characterBasis;
	}
}
