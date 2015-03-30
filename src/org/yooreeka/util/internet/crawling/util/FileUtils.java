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
package org.yooreeka.util.internet.crawling.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for files and directories.
 */
public class FileUtils {

	/**
	 * Deletes directory with its content.
	 * 
	 * @param dir
	 *            directory to delete.
	 * @return true if delete was successful.
	 */
	public static boolean deleteDir(java.io.File dir) {

		if (dir == null || dir.isDirectory() == false) {
			return false;
		}

		for (String filename : dir.list()) {
			boolean success = false;
			File f = new File(dir, filename);
			if (f.isDirectory()) {
				success = deleteDir(f);
			} else {
				success = f.delete();
			}
			if (!success) {
				return success;
			}
		}

		return dir.delete();
	}

	/**
	 * Deletes directory with its content.
	 * 
	 * @param dir
	 *            directory to delete.
	 * @return true if delete was successful.
	 */
	public static boolean deleteDir(String dir) {
		File f = new File(dir);
		if (f.exists() && f.isDirectory()) {
			return deleteDir(f);
		} else {
			return false;
		}
	}

	/**
	 * Finds files that start with specified prefix.
	 * 
	 * @param directory
	 *            directory with files to search
	 * @param filenamePrefix
	 *            defines files that will be returned.
	 * @return files with names that start with specified prefix.
	 */
	public static File[] findMatchingFiles(final File directory,
			final String filenamePrefix) {
		return directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(filenamePrefix);
			}
		});
	}

	public static List<Path> listFiles(Path dir) throws IOException {
	       List<Path> result = new ArrayList<>();
	       try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
	           for (Path entry: stream) {
	               result.add(entry);
	           }
	       } catch (DirectoryIteratorException ex) {
	           // I/O error encountered during the iteration
	           throw ex.getCause();
	       }
	       return result;
	   }
	
	
	/**
	 * List all the files with the <tt>allowedExtensions</tt> within <tt>dir</tt> 
	 * 
	 * @param directoryPath
	 * @param allowedExtensions, e.g. <tt>{c:h:cpp:hpp:java}</tt>
	 * @return
	 * @throws IOException
	 */
	public static List<Path> listFiles(Path directoryPath, String allowedExtensions) throws IOException {
		
	       List<Path> result = new ArrayList<>();
	       
	       String[] ext = allowedExtensions.split(":");
	       
	       try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
	           for (Path entry: stream) {
	        	   if(entry.toFile().isDirectory()) {
	        		   //recurse
	        		   result.addAll(listFiles(entry,allowedExtensions));
	        	   } else {
	        		   for (String s : ext) {
	        			   if(entry.toString().endsWith(s)){
	        				   result.add(entry);
	        			   }
	        		   }
	        	   }
	           }
	       } catch (DirectoryIteratorException ex) {
	           // I/O error encounted during the iteration, the cause is an IOException
	           throw ex.getCause();
	       }
	       return result;
	   }

	public static void prepareDir(File dir, boolean useExisting)
			throws IOException {
		if (dir.exists()) {
			if (useExisting == false) {
				if (!FileUtils.deleteDir(dir)) {
					throw new IOException("Failed to delete directory: '"
							+ dir.getAbsolutePath() + "'");
				}
			}
		}
		if (!dir.exists()) {
			if (!dir.mkdir()) {
				throw new IOException("Failed to create directory: '"
						+ dir.getAbsolutePath() + "'");
			}
		}
	}

	/*
	 * All methods are static. There should be no instances of this class.
	 */
	private FileUtils() {
		// empty
	}

}
