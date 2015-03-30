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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yooreeka.util.internet.crawling.model.Outlink;
import org.yooreeka.util.internet.crawling.util.DocumentIdUtils;
import org.yooreeka.util.internet.crawling.util.FileUtils;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class ProcessedDocsDB {

	private enum FileType {
		CONTENT(".content", "content"), TXT(".txt", "txt"), PROPERTIES(
				".properties", "properties"), OUTLINKS(".outlinks", "outlinks");

		private final String ext;
		private final String dir;

		FileType(String ext, String dir) {
			this.ext = ext;
			this.dir = dir;
		}

		public String getDir() {
			return dir;
		}

		public String getExt() {
			return ext;
		}
	}
	private File rootDirFile = null;
	private Map<String, File> groupFiles = null;

	private DocumentIdUtils docIdUtils = new DocumentIdUtils();

	public ProcessedDocsDB(File rootDir) {
		this.rootDirFile = rootDir;
	}

	private File createDir(File parent, String dirName) {
		File newDir = new File(parent, dirName);
		if (!newDir.exists()) {
			newDir.mkdir();
		}
		return newDir;
	}

	/*
	 * Creates directories for a new group if they don't exist yet.
	 */
	private void createGroup(String groupId) {
		File groupFile = groupFiles.get(groupId);
		if (groupFile == null) {
			groupFile = new File(rootDirFile, String.valueOf(groupId));
			groupFile.mkdir();
			createDir(groupFile, FileType.CONTENT.getDir());
			createDir(groupFile, FileType.PROPERTIES.getDir());
			createDir(groupFile, FileType.OUTLINKS.getDir());
			createDir(groupFile, FileType.TXT.getDir());
			groupFiles.put(groupFile.getName(), groupFile);
		}
	}

	public void delete() {
		FileUtils.deleteDir(rootDirFile);
	}

	public List<String> getAllGroupIds() {
		return new ArrayList<String>(groupFiles.keySet());
	}

	private byte[] getBytes(String text) {
		try {
			return text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error while saving data: ", e);
		}
	}

	private File getContentFile(String documentId) {
		return getDocumentFile(documentId, FileType.CONTENT);
	}

	private File getDocumentFile(String documentId, FileType type) {
		String groupId = docIdUtils.getDocumentGroupId(documentId);
		File groupDirFile = new File(rootDirFile, groupId);
		File docDirFile = new File(groupDirFile, type.getDir());
		String itemId = DocumentIdUtils.getDocumentSequence(documentId);
		File docFile = new File(docDirFile, itemId + type.getExt());
		return docFile;
	}

	public List<String> getDocumentIds() {
		List<String> documentIds = new ArrayList<String>();
		for (File groupFile : groupFiles.values()) {
			documentIds.addAll(getDocumentIds(groupFile));
		}
		return documentIds;
	}

	private List<String> getDocumentIds(File setFile) {
		if (setFile == null) {
			return new ArrayList<String>();
		}
		final FileType type = FileType.CONTENT;
		File dir = new File(setFile, type.dir);
		File[] dataFiles = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(type.ext)) {
					return true;
				} else {
					return false;
				}
			}
		});

		String groupId = setFile.getName();
		List<String> documentIds = new ArrayList<String>();
		for (File f : dataFiles) {
			String name = f.getName();
			String itemId = name.substring(0, name.indexOf("."));
			String documentId = DocumentIdUtils.getDocumentId(groupId, itemId);
			documentIds.add(documentId);
		}
		return documentIds;
	}

	public List<String> getDocumentIds(String groupId) {
		return getDocumentIds(groupFiles.get(groupId));
	}

	private File getOutlinksFile(String documentId) {
		return getDocumentFile(documentId, FileType.OUTLINKS);
	}

	private File getPropertiesFile(String documentId) {
		return getDocumentFile(documentId, FileType.PROPERTIES);
	}

	private String getText(byte[] data) {
		try {
			return new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error loading data: ", e);
		}
	}

	private File getTextFile(String documentId) {
		return getDocumentFile(documentId, FileType.TXT);
	}

	public void init() {
		init(true);
	}

	private void init(boolean keepExistingData) {
		groupFiles = new HashMap<String, File>();

		if (rootDirFile.exists()) {
			if (keepExistingData) {
				/* load all existing file groups */
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
				/* delete all existing data and create brand new directory */
				FileUtils.deleteDir(rootDirFile);
				rootDirFile.mkdirs();
			}
		} else {
			rootDirFile.mkdirs();
		}
	}

	public List<ProcessedDocument> loadAllDocumentsInGroup(String groupId) {
		List<ProcessedDocument> allDocsInGroup = new ArrayList<ProcessedDocument>();

		for (String docId : getDocumentIds(groupId)) {
			ProcessedDocument doc = loadDocument(docId);
			allDocsInGroup.add(doc);
		}

		return allDocsInGroup;
	}

	private String loadContent(File f) {
		byte[] data = loadData(f);
		return getText(data);
	}

	private byte[] loadData(File f) {
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

	/**
	 * Loads previously saved document details.
	 * 
	 * @param documentId
	 * @return
	 */
	public ProcessedDocument loadDocument(String documentId) {
		File propertiesFile = getPropertiesFile(documentId);
		Map<String, String> properties = loadProperties(propertiesFile, ":");

		File contentFile = getContentFile(documentId);
		String content = loadContent(contentFile);

		File textFile = getTextFile(documentId);
		String text = loadText(textFile);

		File outlinksFile = getOutlinksFile(documentId);
		List<Outlink> outlinks = loadOutlinks(outlinksFile);

		ProcessedDocument doc = new ProcessedDocument();
		doc.setDocumentType(properties.get("doctype"));
		doc.setDocumentURL(properties.get("url"));
		doc.setText(text);
		doc.setContent(content);
		doc.setOutlinks(outlinks);
		doc.setDocumentId(documentId);
		doc.setDocumentTitle(properties.get("title"));

		return doc;
	}

	private List<Outlink> loadOutlinks(File f) {
		List<Outlink> outlinks = new ArrayList<Outlink>();
		Map<String, String> props = loadProperties(f, "|");

		for (String key : props.keySet()) {
			String url = key;
			String anchorText = props.get(key);
			Outlink o = new Outlink(url, anchorText);
			outlinks.add(o);
		}
		return outlinks;
	}

	private Map<String, String> loadProperties(File f, String delimiter) {
		Map<String, String> props = new HashMap<String, String>();
		try {
			InputStreamReader r = new InputStreamReader(new FileInputStream(f), "UTF-8");
			BufferedReader reader = new BufferedReader(r);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0 || line.indexOf(delimiter) < 0) {
					continue;
				}

				int delimiterIndex = line.indexOf(delimiter);
				String key = line.substring(0, delimiterIndex);
				String value = line.substring(delimiterIndex + 1);
				props.put(key, value);
			}
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading metadata from file: '"
							+ f.getAbsolutePath() + "'", e);
		}
		return props;
	}

	private String loadText(File f) {
		byte[] data = loadData(f);
		return getText(data);
	}

	private void saveContent(File f, String content) {
		saveData(f, getBytes(content));
	}

	private void saveData(File f, byte[] content) {
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

	/**
	 * Persists the document.
	 * 
	 * @param doc
	 */
	public void saveDocument(ProcessedDocument doc) {
		String groupId = docIdUtils.getDocumentGroupId(doc.getDocumentId());
		createGroup(groupId);

		File contentFile = getContentFile(doc.getDocumentId());
		saveContent(contentFile, doc.getContent());

		File textFile = getTextFile(doc.getDocumentId());
		saveText(textFile, doc.getText());

		File propertiesFile = getPropertiesFile(doc.getDocumentId());
		Map<String, String> props = new HashMap<String, String>();
		props.put("url", doc.getDocumentURL());
		props.put("title", doc.getDocumentTitle());
		props.put("doctype", doc.getDocumentType());
		saveProperties(propertiesFile, props, ":");

		File outlinksFile = getOutlinksFile(doc.getDocumentId());
		saveOutlinks(outlinksFile, doc.getOutlinks());
	}

	private void saveOutlinks(File f, List<Outlink> outlinks) {
		Map<String, String> props = new HashMap<String, String>();
		for (Outlink outlink : outlinks) {
			props.put(outlink.getLinkUrl(), outlink.getText());
		}
		saveProperties(f, props, "|");
	}

	private void saveProperties(File f, Map<String, String> props,
			String delimiter) {
		try {
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(
					f), "UTF-8");
			BufferedWriter bw = new BufferedWriter(w);
			for (String key : props.keySet()) {
				String value = props.get(key);
				writeProperty(bw, key, value, delimiter);
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void saveText(File f, String text) {
		saveData(f, getBytes(text));
	}

	private void writeProperty(BufferedWriter w, String key, String value,
			String delimiter) throws IOException {
		w.write(key);
		w.write(delimiter);
		if (value != null) {
			w.write(value);
		}
		w.newLine();
	}
}
