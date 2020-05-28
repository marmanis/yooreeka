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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.util.DocumentIdUtils;
import org.yooreeka.util.internet.crawling.util.FileUtils;

public class FetchedDocsDB {

	private File rootDirFile = null;
	private Map<String, File> groupFiles = null;
	private DocumentIdUtils docIdUtils = new DocumentIdUtils();

	private boolean verbose=false;
	
	public FetchedDocsDB(File rootDirFile) {
		this.rootDirFile = rootDirFile;
	}

	/*
	 * Creates directories for a new group if they don't exist yet.
	 */
	private void createGroup(String groupId) {
		File groupFile = groupFiles.get(groupId);
		if (groupFile == null) {
			groupFile = new File(rootDirFile, String.valueOf(groupId));
			groupFile.mkdir();
			groupFiles.put(groupFile.getName(), groupFile);
		}
	}

	public void delete() {
		FileUtils.deleteDir(rootDirFile);
	}

	private String geFetchedFilePropertiesExt() {
		return ".meta";
	}

	public List<String> getAllGroupIds() {
		List<String> groupIds = new ArrayList<String>(groupFiles.keySet());
		Collections.sort(groupIds);
		return groupIds;
	}

	private File getDataFile(String documentId) {
		return getDocumentFile(documentId, getFetchedFileExt());
	}

	// document id contains the set encoded in it
	public FetchedDocument getDocument(String documentId) {
		File dataFile = getDataFile(documentId);
		if (!dataFile.exists()) {
			throw new RuntimeException("Document with id: '" + documentId
					+ "' doesn't exist.");
		}
		FetchedDocument doc = new FetchedDocument();
		doc.setDocumentId(documentId);

		byte[] data = readData(dataFile);
		doc.setDocumentContent(data);

		File propsFile = getPropertiesFile(documentId);
		if (!propsFile.exists()) {
			throw new RuntimeException("Properties for document with id: '"
					+ documentId + "' don't exist.");
		}
		readMetaData(propsFile, doc);

		return doc;
	}

	private File getDocumentFile(String documentId, String ext) {
		String groupId = docIdUtils.getDocumentGroupId(documentId);
		File docDirFile = new File(rootDirFile, groupId);
		String docFilename = DocumentIdUtils.getDocumentSequence(documentId) + ext;
		File docFile = new File(docDirFile, docFilename);
		return docFile;
	}

	public List<String> getDocumentIds() {
		List<String> documentIds = new ArrayList<String>();
		for (File setFile : groupFiles.values()) {
			documentIds.addAll(getDocumentIds(setFile));
		}
		return documentIds;
	}

	private List<String> getDocumentIds(File setFile) {
		File[] dataFiles = setFile.listFiles(new FilenameFilter() {
			String ext = getFetchedFileExt();

			public boolean accept(File dir, String name) {
				if (name.endsWith(ext)) {
					return true;
				} else {
					return false;
				}
			}
		});

		List<String> documentIds = new ArrayList<String>();
		String groupId = setFile.getName();
		if (dataFiles != null) {
			for (File f : dataFiles) {
				String name = f.getName();
				String itemId = name.substring(0, name.indexOf("."));
				String documentId = DocumentIdUtils.getDocumentId(groupId, itemId);
				documentIds.add(documentId);
			}
		}
		return documentIds;
	}

	public List<String> getDocumentIds(String groupId) {
		return getDocumentIds(new File(rootDirFile, groupId));
	}

	private String getFetchedFileExt() {
		return ".fetched";
	}

	private File getPropertiesFile(String documentId) {
		return getDocumentFile(documentId, geFetchedFilePropertiesExt());
	}

	public void init() {
		init(true);
	}

	private void init(boolean keepExistingData) {
		groupFiles = new HashMap<String, File>();
		if (rootDirFile.exists()) {
			if (keepExistingData) {
				/* Load information about existing groups */
				File[] existingFileGroups = rootDirFile
						.listFiles(new FileFilter() {
							public boolean accept(File f) {
								return f.isDirectory();
							}
						});
				for (File groupDirFile : existingFileGroups) {
					groupFiles.put(groupDirFile.getName(), groupDirFile);
				}
			} else {
				/* load all existing file groups */
				FileUtils.deleteDir(rootDirFile);
				rootDirFile.mkdirs();
			}
		} else {
			rootDirFile.mkdirs();
		}
	}

	private byte[] readData(File f) {
		byte[] data = new byte[(int) f.length()];
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(f));
			in.read(data);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException("Error while reading file: '"
					+ f.getAbsolutePath() + "'", e);
		}
		return data;
	}

	private void readMetaData(File f, FetchedDocument doc) {
		try {
			InputStreamReader is = new InputStreamReader(
					new FileInputStream(f), "UTF-8");
			BufferedReader reader = new BufferedReader(is);
			Map<String, String> metadata = new HashMap<String, String>();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}

				String[] values = line.split(":", 2);
				String key = values[0];
				String value = values[1];
				if ("url".equalsIgnoreCase(key)) {
					doc.setDocumentURL(value);
				} else if ("host".equalsIgnoreCase(key)) {
					// skip, do nothing
				} else if ("Content-Type".equalsIgnoreCase(key)) {
					doc.setContentType(value);
				} else if ("Charset".equalsIgnoreCase(key)) {
					doc.setContentCharset(value);
				} else {
					metadata.put(key, value);
				}
			}
			reader.close();
			doc.setDocumentMetadata(metadata);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading metadata from file: '"
							+ f.getAbsolutePath() + "'", e);
		}

	}

	private void saveContent(File f, byte[] content) {
		if (verbose)
			P.println("Writing file: "+f.getName());
		
		try {
			FileOutputStream fout = new FileOutputStream(f);
			BufferedOutputStream bout = new BufferedOutputStream(fout);
			bout.write(content);
			bout.flush();
			bout.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void saveDocument(FetchedDocument doc) {
		
		if (verbose)
			P.println("Saving fetched document with ID:"+doc.getDocumentId());
		
		/* create directory for current group if it doesn't exist yet. */
		String groupId = docIdUtils.getDocumentGroupId(doc.getDocumentId());
		createGroup(groupId);

		File dataFile = getDataFile(doc.getDocumentId());
		saveContent(dataFile, doc.getDocumentContent());

		File metadataFile = getPropertiesFile(doc.getDocumentId());
		saveMetadata(metadataFile, doc);
	}

	private void saveMetadata(File f, FetchedDocument doc) {
		try {
			OutputStreamWriter ow = new OutputStreamWriter(
					new FileOutputStream(f), "UTF-8");
			BufferedWriter bw = new BufferedWriter(ow);

			writeProperty(bw, "url", doc.getDocumentURL());
			writeProperty(bw, "Content-Type", doc.getContentType());
			writeProperty(bw, "Charset", doc.getContentCharset());

			Map<String, String> metadata = doc.getDocumentMetadata();
			for (String key : metadata.keySet()) {
				writeProperty(bw, key, metadata.get(key));
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeProperty(BufferedWriter w, String key, String value)
			throws IOException {
		w.write(key);
		w.write(":");
		if (value != null) {
			w.write(value);
		}
		w.newLine();
	}
}
