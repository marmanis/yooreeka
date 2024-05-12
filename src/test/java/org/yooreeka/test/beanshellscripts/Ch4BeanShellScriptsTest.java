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
package org.yooreeka.test.beanshellscripts;


import org.yooreeka.algos.clustering.dbscan.DBSCANAlgorithm;
import org.yooreeka.algos.clustering.hierarchical.AverageLinkAlgorithm;
import org.yooreeka.algos.clustering.hierarchical.MSTSingleLinkAlgorithm;
import org.yooreeka.algos.clustering.hierarchical.SingleLinkAlgorithm;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.model.Dendrogram;
import org.yooreeka.algos.clustering.partitional.KMeansAlgorithm;
import org.yooreeka.algos.clustering.rock.ROCKAlgorithm;
import org.yooreeka.algos.clustering.test.SFData;
import org.yooreeka.algos.clustering.test.SFDataset;
import org.yooreeka.algos.clustering.utils.SortedArrayClustering;
import org.yooreeka.util.P;
import org.yooreeka.util.metrics.CosineDistance;

import junit.framework.TestCase;

public class Ch4BeanShellScriptsTest extends TestCase {

	private SFDataset employeeData;
    private DataPoint[] employeeDataPoints;
	private long t;
    
	public Ch4BeanShellScriptsTest(String name) {
        super(name);
        
        t= System.currentTimeMillis();
        
        employeeData = SFData.createDataset();

        employeeDataPoints = employeeData.getData();
    }
    
    public void test_evalCh4Scripts() throws Exception {
        // ScriptEvalUtils.runScripts("ch4");
    	
    	test_Ch4_1();
    	test_Ch4_2();
    	test_Ch4_3();
    	test_Ch4_4();
    	test_Ch4_5();	
    	
    	P.timePassedSince(t);
    }

	public void test_Ch4_1() {
		SortedArrayClustering.cluster(employeeDataPoints);
	}
	
	public void test_Ch4_2() {

		double[][] adjMatrix = employeeData.getAdjacencyMatrix();

		SingleLinkAlgorithm sla = new SingleLinkAlgorithm(employeeDataPoints,adjMatrix);

		Dendrogram dendroSLA = sla.cluster();

		dendroSLA.print(4);

		MSTSingleLinkAlgorithm sla2 = new MSTSingleLinkAlgorithm(employeeDataPoints,adjMatrix);

		Dendrogram dendroSLA2 = sla2.cluster();

		dendroSLA2.print(4);

		AverageLinkAlgorithm ala = new AverageLinkAlgorithm(employeeDataPoints,adjMatrix);

		Dendrogram dendroALA = ala.cluster();

		dendroALA.print(4);		
	}
	
	public void test_Ch4_3() {

		KMeansAlgorithm kMeans = new KMeansAlgorithm(8, employeeDataPoints);

		kMeans.cluster();
		        
		kMeans.print();
	}
	
	public void test_Ch4_4() {

		CosineDistance cosD = new CosineDistance();

		DBSCANAlgorithm dbscan = new DBSCANAlgorithm(employeeDataPoints, cosD, 0.8, 2, true);

		dbscan.printResults(dbscan.cluster());

	}
	
	public void test_Ch4_5() {

		ROCKAlgorithm rock = new ROCKAlgorithm(employeeDataPoints, 5, 0.2);

		Dendrogram dnd = rock.cluster();

		dnd.print(21);


	}

}
