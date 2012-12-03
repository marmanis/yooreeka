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
package org.yooreeka.algos.reco.collab.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import org.yooreeka.config.YooreekaConfigurator;

/**
 * Implementation of <code>Store</code> interface. Uses files to store objects
 * using java serialization. Each object instance is stored in a separate file.
 */
public class FileStore implements Store {

	private static final Logger LOG = Logger.getLogger(FileStore.class.getName());

	private File dataDir;

	public FileStore(File dir) {

		LOG.setLevel(YooreekaConfigurator.getLevel(FileStore.class.getName()));

		if (!dir.exists()) {
			dir.mkdirs();
		}
		this.dataDir = dir;
	}

	/**
	 * Creates a new instance that will use specified directory to store
	 * objects.
	 * 
	 * @param dir
	 *            directory that should be used to store/retrieve objects.
	 */
	public FileStore(String dir) {
		this(new File(dir));
	}

	public boolean exists(String key) {
		File f = getFile(key);
		return f.exists();
	}

	public Object get(String key) {
		Object o = null;
		try {
			File f = getFile(key);
			if (f.exists()) {
				FileInputStream fInStream = new FileInputStream(f);
				BufferedInputStream bufInStream = new BufferedInputStream(
						fInStream);
				ObjectInputStream objInStream = new ObjectInputStream(
						bufInStream);
				o = objInStream.readObject();
				objInStream.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error while loading data from file (dir: '" + dataDir
							+ "', filename: '" + key + "').", e);
		}
		return o;
	}

	/*
	 * Derives filename from the key and returns instance of <code>File</code>
	 */
	private File getFile(String key) {
		// key is used as a filename
		return new File(dataDir, key + ".tmp");
	}

	public void put(String key, Object o) {
		try {
			File f = getFile(key);
			FileOutputStream foutStream = new FileOutputStream(f);
			BufferedOutputStream boutStream = new BufferedOutputStream(
					foutStream);
			ObjectOutputStream objOutputStream = new ObjectOutputStream(
					boutStream);
			objOutputStream.writeObject(o);
			objOutputStream.flush();
			boutStream.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while saving data into file (dir: '" + dataDir
							+ "', filename: '" + key + "').", e);
		}
	}

	public void remove(String key) {
		File f = getFile(key);
		if (f.exists()) {
			f.delete();
		}
	}
}
