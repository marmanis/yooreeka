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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.SimpleFSDirectory;
import org.yooreeka.algos.search.lucene.analyzer.TextDocumentTerms;
import org.yooreeka.util.internet.crawling.core.CrawlDataProcessor;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class DocRankMatrixBuilder implements CrawlDataProcessor {

	// private final int TERMS_TO_KEEP = 3;

	private int termsToKeep = 0;

	private String indexDir;
	private PageRankMatrixH matrixH;

	public DocRankMatrixBuilder(String indexDir) {
		this.indexDir = indexDir;
	}

	private PageRankMatrixH buildMatrixH(IndexReader idxR) throws IOException {

		// only consider URLs that with fetched and parsed content
		List<Integer> allDocs = getProcessedDocs(idxR);

		PageRankMatrixH docMatrix = new PageRankMatrixH(allDocs.size());

		for (int i = 0, n = allDocs.size(); i < n; i++) {

			for (int j = 0, k = allDocs.size(); j < k; j++) {

				double similarity = 0.0d;

				Document docX = idxR.document(i);
				String xURL = docX.get("url");

				if (i == j) {

					// Avoid shameless self-promotion ;-)
					docMatrix.addLink(xURL, xURL, similarity);

				} else {

					TextDocumentTerms xDocumentTerms = new TextDocumentTerms(
							docX.get("content"));

					Document docY = idxR.document(j);
					TextDocumentTerms yDocumentTerms = new TextDocumentTerms(
							docY.get("content"));

					similarity = getImportance(xDocumentTerms, yDocumentTerms);

					// add link from docX to docY
					String yURL = docY.get("url");

					docMatrix.addLink(xURL, yURL, similarity);
				}
			}
		}

		docMatrix.calculate();

		return docMatrix;
	}

	/*
	 * Checks if the index entry belongs to the category that we want to use
	 * DocRank on.
	 */
	private boolean eligibleForDocRank(String doctype) {
		return ProcessedDocument.TYPE_MSWORD.equalsIgnoreCase(doctype);
	}

	public PageRankMatrixH getH() {
		return matrixH;
	}

	/*
	 * Calculates importance of document Y in the context of document X
	 */
	private double getImportance(TextDocumentTerms xTerms,
			TextDocumentTerms yTerms) {

		// sharedTerms is the intersection of the two sets
		Set<String> sharedTerms = xTerms.getTf().keySet();
		sharedTerms.retainAll(yTerms.getTf().keySet());

		double sharedTermsSum = 0.0;

		// Notice that this way of assigning importance is not symmetric.
		// That is, if you swap X with Y then you get a different value;
		// unless the frequencies are equal, of course!

		double xF, yF;
		for (String term : sharedTerms) {

			xF = xTerms.getTf().get(term).doubleValue();
			yF = yTerms.getTf().get(term).doubleValue();

			sharedTermsSum += Math.round(Math.tanh(yF / xF));
		}

		return sharedTermsSum;
	}

	/*
	 * Collects doc ids from the index for documents with matching doc type.
	 */
	private List<Integer> getProcessedDocs(IndexReader idxR) throws IOException {
		List<Integer> docs = new ArrayList<Integer>();
		for (int i = 0, n = idxR.maxDoc(); i < n; i++) {
			if (idxR.hasDeletions() == false) {
				Document doc = idxR.document(i);
				if (eligibleForDocRank(doc.get("doctype"))) {
					docs.add(i);
				}
			}
		}
		return docs;

	}

	// private Map<String, Integer> buildFreqMap(String[] terms, int[] freq) {
	//
	// int topNTermsToKeep = (termsToKeep == 0)? TERMS_TO_KEEP: termsToKeep;
	//
	// Map<String, Integer> freqMap =
	// TermFreqMapUtils.getTopNTermFreqMap(terms, freq, topNTermsToKeep);
	//
	// return freqMap;
	// }

	/**
	 * @return the termsToKeep
	 */
	public int getTermsToKeep() {
		return termsToKeep;
	}

	public void run() {
		try {
			DirectoryReader idxR = DirectoryReader.open(new SimpleFSDirectory(
					new File(indexDir)));
			matrixH = buildMatrixH(idxR);
		} catch (Exception e) {
			throw new RuntimeException("Error while building matrix: ", e);
		}
	}

	/**
	 * @param termsToKeep
	 *            the termsToKeep to set
	 */
	public void setTermsToKeep(int termsToKeep) {
		this.termsToKeep = termsToKeep;
	}

}
