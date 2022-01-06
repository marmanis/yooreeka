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
package org.yooreeka.examples.fraud.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.yooreeka.examples.fraud.data.Transaction;

public class FraudDataUtils {

	private static Random rnd = new Random();

	private static Random txnAmountRnd = new Random();

	public static List<Transaction> loadTransactions(String filename) {
		List<Transaction> txns = new ArrayList<Transaction>();
		try {
			FileReader fReader = new FileReader(filename);
			BufferedReader reader = new BufferedReader(fReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() > 0) {
					Transaction txn = new Transaction();
					txn.loadFromExternalString(line);
					txns.add(txn);
				}
			}

			fReader.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to load transactions from file: '" + filename
							+ "' ", e);
		}

		return txns;
	}

	static String[] loadTxnDescriptions(String filename) {

		List<String> descriptions = new ArrayList<String>();

		FileReader fReader = null;
		try {
			fReader = new FileReader(filename);
		} catch (FileNotFoundException fnfX) {
			fnfX.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(fReader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() > 0) {
					descriptions.add(line);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to load descriptions from file: '" + filename
							+ "' ", e);
		}

		try {
			fReader.close();
		} catch (IOException ioX) {
			ioX.printStackTrace();
		}

		return descriptions.toArray(new String[descriptions.size()]);
	}

	public static double nextTxnAmount(double mean, double std) {
		double amt = 0.0;
		do {
			// deriving gaussian with our custom std and mean from Standard
			// Normal Distribution.
			amt = txnAmountRnd.nextGaussian() * std + mean;
		} while (amt <= 0.0);

		BigDecimal db = new BigDecimal(amt);
		db = db.setScale(2, RoundingMode.HALF_UP);
		return db.doubleValue();
	}

	static int randomInt(int n) {
		return FraudDataUtils.randomInt(0, n);
	}

	static int randomInt(int min, int max) {
		return min + rnd.nextInt(max - min);
	}

	static void saveTransactions(String filename, List<Transaction> txns) {
		try {
			FileWriter fout = new FileWriter(filename);
			BufferedWriter writer = new BufferedWriter(fout);
			for (Transaction txn : txns) {
				writer.write(txn.toExternalString());
				writer.write("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Failed to load descriptions from file: '" + filename
							+ "' ", e);
		}
	}

}
