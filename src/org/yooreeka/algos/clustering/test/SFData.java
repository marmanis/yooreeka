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
package org.yooreeka.algos.clustering.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.yooreeka.algos.clustering.model.Attribute;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.partitional.NearestNeighborAlgorithm;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.metrics.NumericDistance;
import org.yooreeka.util.metrics.EuclideanDistance;

public class SFData {

	/*
	 * All available attributes.
	 */
	private static String[] allAvailableAttributeNames = { "Age",
			"IncomeRange", "Education", "Skills", "Social", "isPaid" };

	public static SFDataset createDataset() {
		return createDataset(allAvailableAttributeNames);
	}

	/**
	 * Creates dataset that uses only attributes with specified names. Other
	 * attributes will not be loaded.
	 * 
	 * @param attrNames
	 *            attribute names to use.
	 * @return dataset that uses only specified attributes.
	 */
	public static SFDataset createDataset(String[] attrNames) {

		// check that attribute names are valid
		validateAttrNames(attrNames, allAvailableAttributeNames);

		DataPoint[] allData = loadDataFromFile(YooreekaConfigurator.getHome()
				+ "/data/ch04/clusteringSF.dat", attrNames);

		NumericDistance dist = new EuclideanDistance();
		SFDataset sfDataset = new SFDataset(allData, dist);
		return sfDataset;
	}

	private static DataPoint[] loadDataFromFile(String filename,
			String[] attrNames) {
		List<DataPoint> allData = new ArrayList<DataPoint>();
		CsvListReader csvReader = null;
		
		final CsvPreference CsvPref = 
				new CsvPreference.Builder(CsvPreference.EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true).build();
		
		try {
			csvReader = new CsvListReader(new BufferedReader(new FileReader(filename)), CsvPref);

			// Load all available headers from CSV file
			String[] csvHeaders = csvReader.getHeader(true);

			// Map attribute names to field IDs from CSV file using header names
			int[] attrFieldIndexes = new int[attrNames.length];
			for (int i = 0; i < attrFieldIndexes.length; i++) {
				String header = attrNames[i];
				int csvHeaderId = -1;
				for (int j = 0; j < csvHeaders.length; j++) {
					if (header.equalsIgnoreCase(csvHeaders[j])) {
						csvHeaderId = j;
						break;
					}
				}
				// If there is no header found it means we have wrong attribute
				// name or wrong file.
				if (csvHeaderId == -1) {
					throw new IllegalStateException(
							"Attribute name mismatch. "
									+ "Failed to find attribute name: '"
									+ header
									+ "' among cvs file headers. All available headers: "
									+ Arrays.toString(csvHeaders));
				} else {
					attrFieldIndexes[i] = csvHeaderId;
				}
			}

			// Read file and include only selected attributes
			List<String> line = null;
			while ((line = csvReader.read()) != null) {
				try {
					String label = line.get(0);
					Attribute[] attributes = new Attribute[attrNames.length];
					for (int i = 0, n = attrNames.length; i < n; i++) {
						int attrFieldIndex = attrFieldIndexes[i];
						String value = line.get(attrFieldIndex);
						attributes[i] = new Attribute(attrNames[i],
								Double.valueOf(value));
					}
					DataPoint dataPoint = new DataPoint(label, attributes);
					allData.add(dataPoint);
				} catch (Exception e) {
					throw new RuntimeException("Error while reading line: '"
							+ line + "'", e);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading SF data from csv file: '" + filename
							+ "'. ", e);
		} finally {
			try {
				if (csvReader != null) {
					csvReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("From file: " + filename);
		System.out.println("Using attribute names: "
				+ Arrays.toString(attrNames));
		System.out.println("Loaded " + allData.size() + " data points.");

		return allData.toArray(new DataPoint[allData.size()]);
	}

	public static void main(String[] args) {

		// Creates dataset that uses all available attributes
		SFDataset ds = SFData.createDataset();

		// Creates dataset that uses only a subset of available attributes
		// SFDataset ds = SFData.createDataset(new String[] {"IncomeRange",
		// "Age"});
		// SFDataset ds = SFData.createDataset(new String[] {"Age"});

		ds.printDistanceMatrix();

		// Dendrogram dnd = null;

		// Uncomment one of these two run clustering

		// // Run Single Link Clustering
		// SingleLinkAlgorithm sla = new SingleLinkAlgorithm(ds.getData(),
		// ds.getDistanceMatrix());
		// dnd = sla.cluster();
		// dnd.print();

		// // Run MST Single Link Clustering
		// MSTSingleLinkAlgorithm msla = new
		// MSTSingleLinkAlgorithm(ds.getData(), ds.getDistanceMatrix());
		// dnd = msla.cluster();
		// dnd.print();

		// // Run Average Link Clustering
		// AverageLinkAlgorithm ala = new AverageLinkAlgorithm(ds.getData(),
		// ds.getDistanceMatrix());
		// dnd = ala.cluster();
		// dnd.print();

		// double T = 5.0;

		NearestNeighborAlgorithm nna = new NearestNeighborAlgorithm(
				ds.getData(), ds.getAdjacencyMatrix(), 5.0);
		nna.run();
	}

	private static void validateAttrNames(String[] actualAttrNames,
			String[] validAttrNames) {
		List<String> validNames = Arrays.asList(validAttrNames);
		for (String actualAttrName : actualAttrNames) {
			if (!validNames.contains(actualAttrName)) {
				throw new IllegalArgumentException("Invalid attribute name: '"
						+ actualAttrName + "'. " + "Valid names are: "
						+ Arrays.toString(allAvailableAttributeNames));
			}
		}
	}
}
