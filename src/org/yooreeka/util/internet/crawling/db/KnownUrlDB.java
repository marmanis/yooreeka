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
package org.yooreeka.util.internet.crawling.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.model.KnownUrlEntry;
import org.yooreeka.util.internet.crawling.util.FileUtils;

public class KnownUrlDB {

	private static final String DB_FILENAME = "knownurlsdb.dat";

	private Map<String, KnownUrlEntry> processedURLs = new HashMap<String, KnownUrlEntry>();

	private Map<String, KnownUrlEntry> unprocessedURLs = new HashMap<String, KnownUrlEntry>();

	private File rootDir = null;
	private File dbFile = null;

	private static final String FIELD_DELIMITER = "|";

	public KnownUrlDB(File f) {
		this.rootDir = f;

	}

	public boolean addNewUrl(String url, int depth) {
		
		P.println("KnownUrlDB.addNewUrl("+url+", "+depth+")");

		boolean isAdded = false;

		if (isKnownUrl(url) == false) {

			String status = KnownUrlEntry.STATUS_UNPROCESSED;
			KnownUrlEntry r = new KnownUrlEntry();
			r.setUrl(url);
			r.setStatus(status);
			r.setDepth(depth);
			unprocessedURLs.put(url, r);
			isAdded = true;
		} else {
			isAdded = false;
		}

		return isAdded;
	}

	public void delete() {
		FileUtils.deleteDir(rootDir);
	}

	public List<String> findAllKnownUrls() {
		List<String> allUrls = new ArrayList<String>();
		allUrls.addAll(unprocessedURLs.keySet());
		allUrls.addAll(processedURLs.keySet());
		return allUrls;
	}

	public List<String> findProcessedUrls(String status) {
		ArrayList<String> selectedUrls = new ArrayList<String>();
		for (Map.Entry<String, KnownUrlEntry> mapEntry : processedURLs
				.entrySet()) {
			KnownUrlEntry urlEntry = mapEntry.getValue();
			if (status.equalsIgnoreCase(urlEntry.getStatus())) {
				selectedUrls.add(urlEntry.getUrl());
			}
		}
		return selectedUrls;
	}

	public List<String> findUnprocessedUrls() {
		return new ArrayList<String>(unprocessedURLs.keySet());
	}

	/**
	 * @deprecated will be removed. Use method with depth instead.
	 * 
	 * @param maxDocs
	 * @return
	 */
	@Deprecated
	public List<String> findUnprocessedUrls(int maxDocs) {
		return findUnprocessedUrls(maxDocs, 0);
	}

	public List<String> findUnprocessedUrls(int maxDocs, int depth) {
		List<String> selectedUrls = new ArrayList<String>();

		for (Map.Entry<String, KnownUrlEntry> e : unprocessedURLs.entrySet()) {
			if (selectedUrls.size() >= maxDocs) {
				break;
			}
			KnownUrlEntry ku = e.getValue();
			if (ku.getDepth() == depth) {
				selectedUrls.add(ku.getUrl());
			}
		}

		return selectedUrls;
	}

	public int getTotalUrlCount() {
		return unprocessedURLs.size() + processedURLs.size();
	}

	public void init() {
		rootDir.mkdirs();

		this.dbFile = new File(rootDir, DB_FILENAME);
		try {

			// creates a new file if the file doesn't exist
			dbFile.createNewFile();

		} catch (IOException e) {
			throw new RuntimeException("Can't create db file: '"
					+ dbFile.getAbsolutePath() + "'.", e);
		}

		load();
	}

	public boolean inProcessedUrl(String url) {
		return processedURLs.containsKey(url);
	}

	public boolean inUnprocessedUrl(String url) {
		return unprocessedURLs.containsKey(url);
	}

	public boolean isKnownUrl(String url) {
		return processedURLs.containsKey(url)
				|| unprocessedURLs.containsKey(url);
	}

	public boolean isSuccessfullyProcessed(String url) {
		KnownUrlEntry r = processedURLs.get(url);
		if (r != null
				&& KnownUrlEntry.STATUS_PROCESSED_SUCCESS.equalsIgnoreCase(r
						.getStatus())) {
			return true;
		} else {
			return false;
		}
	}

	private void load() {
		try {
			FileInputStream fis = new FileInputStream(dbFile);
			InputStreamReader r = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(r);
			String line = null;
			while ((line = br.readLine()) != null) {
				int delimiterIndex = line.indexOf(FIELD_DELIMITER);
				String status = line.substring(0, delimiterIndex);
				int secondDelimiterIndex = line.indexOf(FIELD_DELIMITER,
						delimiterIndex + 1);
				int depth = Integer.valueOf(line.substring(delimiterIndex
						+ FIELD_DELIMITER.length(), secondDelimiterIndex));
				String url = line.substring(secondDelimiterIndex
						+ FIELD_DELIMITER.length());
				loadUrl(url, status, depth);
			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load data: ", e);
		}
	}

	private void loadUrl(String url, String status, int depth) {
		
		P.println("KnownUrlDB.loadUrl("+url+", "+status+", "+depth+")");

		if (isKnownUrl(url) == false) {
			KnownUrlEntry r = new KnownUrlEntry();
			r.setUrl(url);
			r.setStatus(status);
			r.setDepth(depth);
			if (KnownUrlEntry.STATUS_PROCESSED_SUCCESS.equalsIgnoreCase(status)
					|| KnownUrlEntry.STATUS_PROCESSED_ERROR.equalsIgnoreCase(status)) {
				processedURLs.put(url, r);
			} else if (KnownUrlEntry.STATUS_UNPROCESSED
					.equalsIgnoreCase(status)) {
				unprocessedURLs.put(url, r);
			} else {
				throw new RuntimeException("Unsupported status value: '"
						+ status + "', url: '" + url + "'.");
			}
		} else {
			throw new RuntimeException("Duplicate url: '" + url + "'");
		}
	}

	public void save() {
		try {
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(dbFile), "UTF-8");
			BufferedWriter bw = new BufferedWriter(w);
			for (KnownUrlEntry r : unprocessedURLs.values()) {
				writeRecord(bw, r);
			}
			for (KnownUrlEntry r : processedURLs.values()) {
				writeRecord(bw, r);
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to save data: ", e);
		}
	}

	public void updateUrlStatus(String url, String status) {
		
		P.println("KnownUrlDB.updateUrlStatus("+url+", "+status+")");

		if (KnownUrlEntry.STATUS_PROCESSED_SUCCESS.equalsIgnoreCase(status) || 
				KnownUrlEntry.STATUS_PROCESSED_ERROR.equalsIgnoreCase(status)) {
			
			KnownUrlEntry r = unprocessedURLs.remove(url);
			if (r != null) {
				r.setStatus(status);
			} else {
				throw new RuntimeException("Unknown url: '" + url);
			}			
			processedURLs.put(url, r);
			
		} else if (KnownUrlEntry.STATUS_UNPROCESSED.equalsIgnoreCase(status)) {
			
			KnownUrlEntry r = processedURLs.remove(url);
			if (r != null) {
				r.setStatus(status);
			} else {
				throw new RuntimeException("Unknown url: '" + url);
			}
			unprocessedURLs.put(url, r);
		}
	}

	private void writeRecord(BufferedWriter w, KnownUrlEntry ku)
			throws IOException {

		w.write(ku.getStatus() + FIELD_DELIMITER
				+ String.valueOf(ku.getDepth()) + FIELD_DELIMITER + ku.getUrl());
		w.newLine();

	}

}
