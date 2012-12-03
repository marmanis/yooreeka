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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.yooreeka.util.internet.crawling.util.FileUtils;

public class PageLinkDB {
	private static final String DB_FILENAME = "pagelinkdb.dat";

	private Map<String, Set<String>> pageOutLinks = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> pageInLinks = new HashMap<String, Set<String>>();

	private File rootDir = null;
	private File dbFile = null;

	public PageLinkDB(File f) {
		this.rootDir = f;
	}

	public void addLink(String pageUrl) {
		Set<String> outlinks = pageOutLinks.get(pageUrl);
		if (outlinks == null) {
			outlinks = new TreeSet<String>();
			pageOutLinks.put(pageUrl, outlinks);
		}
	}

	public void addLink(String pageUrl, String outlinkUrl) {
		Set<String> outLinks = pageOutLinks.get(pageUrl);
		if (outLinks == null) {
			outLinks = new TreeSet<String>();
			pageOutLinks.put(pageUrl, outLinks);
		}
		outLinks.add(outlinkUrl);

		Set<String> inLinks = pageInLinks.get(outlinkUrl);
		if (inLinks == null) {
			inLinks = new TreeSet<String>();
			pageInLinks.put(outlinkUrl, inLinks);
		}
		inLinks.add(pageUrl);
	}

	public void delete() {
		FileUtils.deleteDir(rootDir);
	}

	public Set<String> getInlinks(String url) {
		Set<String> result = pageInLinks.get(url);
		return result != null ? result : new TreeSet<String>();
	}

	public Set<String> getOutlinks(String url) {
		Set<String> result = pageOutLinks.get(url);
		return result != null ? result : new TreeSet<String>();
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

	private void load() {
		try {
			InputStreamReader r = new InputStreamReader(new FileInputStream(
					dbFile), "UTF-8");
			BufferedReader br = new BufferedReader(r);
			String line = null;
			String currentPage = null;
			while ((line = br.readLine()) != null) {
				int delimiterIndex = line.indexOf("|");
				String type = line.substring(0, delimiterIndex);
				String value = line.substring(delimiterIndex + "|".length());
				if ("page".equalsIgnoreCase(type)) {
					currentPage = value;
				} else {
					String outlink = value;
					addLink(currentPage, outlink);
				}
			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to load data: ", e);
		}
	}

	public void save() {
		try {
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(
					dbFile), "UTF-8");
			BufferedWriter bw = new BufferedWriter(w);
			for (Map.Entry<String, Set<String>> mapEntry : pageOutLinks
					.entrySet()) {
				String pageUrl = mapEntry.getKey();
				writeRecord(bw, "page", pageUrl);
				for (String outlink : mapEntry.getValue()) {
					writeRecord(bw, "outlink", outlink);
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to save data: ", e);
		}
	}

	private void writeRecord(BufferedWriter w, String id, String value)
			throws IOException {
		w.write(id + "|" + value);
		w.newLine();
	}
}
