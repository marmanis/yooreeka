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
package org.yooreeka.algos.search.ranking;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yooreeka.config.YooreekaConfigurator;

/**
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public abstract class Rank {

	private static final Logger LOG = Logger.getLogger(Rank.class.getName());

	public static final double DEFAULT_ALPHA   = 0.8;
	public static final double DEFAULT_EPSILON = 0.001;

	/**
	 * This is the percentage of time that a random surfer follows the structure
	 * of the web.
	 */
	private double alpha = DEFAULT_ALPHA;

	/** This is the error tolerance for convergence */
	private double epsilon = DEFAULT_EPSILON;

	double[] pR;

	public Rank() {
		LOG.setLevel(YooreekaConfigurator.getLevel(Rank.class.getName()));
	}

	public void build() throws Exception {

		// check the results
		// getH().print();

		findPageRank(alpha, epsilon);
	}

	public void findPageRank(double alpha, double epsilon) {

		// auxiliary variable
		PageRankMatrixH matrixH = getH();

		// The H matrix has size nxn and the PageRank vector has size n
		int n = matrixH.getSize();

		// auxiliary variable
		double inv_n = (double) 1 / n;

		// This is the actual nxn matrix of double values
		double[][] H = matrixH.getMatrix();

		// A dummy variable that holds our error --
		// arbitrarily set to an initial value of 1
		double error = 1;

		// This holds the values of the PageRank vector
		pR = new double[n];

		// This is a copy of the PageRank vector from the previous iteration
		double[] tmpPR = new double[n];

		// Set the initial values (ad hoc)
		for (int i = 0; i < n; i++) {
			pR[i] = inv_n;
		}

		/*
		 * Book Section 2.3 -- Altering the H matrix: Dangling nodes
		 */
		double[][] dNodes = getDanglingNodeMatrix();

		/**
		 * TODO: 2.5 -- Altering the G matrix: Teleportation (Book Section 2.3)
		 * 
		 * The following code defines the contribution of the dangling nodes,
		 * i.e. jumping randomly on a page that is not connected with the one
		 * that our surfer is currently viewing
		 * 
		 * Notice that it is the same for all pages. An interesting variation of
		 * the algorithm would introduce a "teleportation" contribution that
		 * relates the probability of an arbitrary transition to the degree of
		 * interest that a user has for the content of a page.
		 * 
		 * Exercise: Could that be done? If so, how? What problems can you see
		 * with that variation?
		 */
		double tNodes = (1 - alpha) * inv_n;

		// Replace the H matrix with the G matrix
		for (int i = 0; i < n; i++) {

			for (int j = 0; j < n; j++) {

				H[i][j] = alpha * H[i][j] + dNodes[i][j] + tNodes;
			}
		}

		// Iterate until convergence.

		// A counter for our iterations
		int k = 0;

		// We have found the PageRank values if our error is smaller than
		// epsilon
		while (error >= epsilon) {

			// Make a copy of the PageRank vector before we update it
			for (int i = 0; i < n; i++) {
				tmpPR[i] = pR[i];
			}

			double dummy = 0;
			// Now we get the next point in the iteration
			for (int i = 0; i < n; i++) {

				dummy = 0;

				for (int j = 0; j < n; j++) {

					dummy += tmpPR[j] * H[j][i];
				}

				pR[i] = dummy;
			}

			// Get the error, so that we can check convergence
			error = norm(pR, tmpPR);

			// DEBUG ONLY: Display the progress
			if (LOG.getLevel() == Level.FINE) {
				LOG.fine("\n Iteration: " + k
						+ ",   PageRank convergence error: " + error);
				for (int i = 0; i < n; i++) {
					LOG.fine("Index: " + i + " -->  PageRank: " + pR[i]);
				}
			}
			// increase the value of the counter by one
			k++;
		}

		// Report the final values

		List<RelevanceScore> allRankings = new ArrayList<RelevanceScore>();
		for (int i = 0; i < n; i++) {
			String url = matrixH.getIndexMapping().getValue(i);
			RelevanceScore r = new RelevanceScore(url, pR[i]);
			allRankings.add(r);
		}
		RelevanceScore.sort(allRankings);
		LOG.info("\n______________  Calculation Results  _______________\n");
		LOG.info("\nIterations: " + k);
		LOG.info("\n____________________________________________________\n");
		for (RelevanceScore r : allRankings) {
			LOG.info(MessageFormat.format(
					"Page URL: %-42s  -->  Rank: %.15f\n", r.getId(),
					r.getScore()));
		}
		LOG.info("\n____________________________________________________\n");
	}

	/**
	 * @return the alpha
	 */
	public double getAlpha() {
		return alpha;
	}

	/**
	 * TODO: 2.4 -- Altering the G matrix: Dangling nodes (Book Section 2.3)
	 * 
	 * The following code defines the contribution of the dangling nodes, i.e.
	 * nodes that do not link to any other node.
	 * 
	 * Notice that the 1/n contribution is arbitrary. Given that we have no
	 * other information about the random surfer's habits or preferences, the
	 * 1/n value is fair. However, an interesting variation would take into
	 * account some statistics related to the number of visits a page gets.
	 * 
	 * Exercise: Change the algorithm, so that a dangling node's contribution
	 * depends on some page visit statistic. You can practice with a small set
	 * of pages and examine the effect on the ranking of the pages.
	 */
	private double[][] getDanglingNodeMatrix() {

		PageRankMatrixH matrixH = getH();

		int n = matrixH.getSize();

		double inv_n = (double) 1 / n;

		// The dangling node vector
		int[] dangling = matrixH.getDangling();

		double[][] dNodes = new double[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				if (dangling[i] == 0) {
					dNodes[i][j] = 0;
				} else {
					dNodes[i][j] = alpha * inv_n;
				}
			}
		}

		return dNodes;
	}

	/**
	 * @return the epsilon
	 */
	public double getEpsilon() {
		return epsilon;
	}

	public abstract PageRankMatrixH getH();

	/**
	 * @return the pR
	 */
	public double getPageRank(String url) {

		int i = getH().getIndexMapping().getIndex(url);

		return pR[i];
	}

	private double norm(double[] a, double[] b) {

		double norm = 0;

		int n = a.length;

		for (int i = 0; i < n; i++) {
			norm += Math.abs(a[i] - b[i]);
		}
		return norm;
	}

	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @param epsilon
	 *            the epsilon to set
	 */
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
}
