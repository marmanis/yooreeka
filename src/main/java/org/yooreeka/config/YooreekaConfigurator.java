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
package org.yooreeka.config;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;

import org.yooreeka.util.P;

/**
 * Central place to access to application properties.
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 */
public class YooreekaConfigurator {

	public static final String DATA_DIR = "yooreeka.data.dir";
	public static final String CRAWL_DATA_DIR="yooreeka.crawl.dir";
	public static final String TEMP_DIR = "yooreeka.temp.dir";
	public static final String MOVIELENS_DATA_DIR = "yooreeka.movielens.data.dir";
	public static final String MOVIELENSTEST_DATA_DIR = "yooreeka.movielenstest.data.dir";

	public static final String LOG_LEVEL_SEVERE = "SEVERE";
	public static final String LOG_LEVEL_WARNING = "WARNING";
	public static final String LOG_LEVEL_INFO = "INFO";
	public static final String LOG_LEVEL_CONFIG = "CONFIG";
	public static final String LOG_LEVEL_FINE = "FINE";
	public static final String LOG_LEVEL_FINER = "FINER";
	public static final String LOG_LEVEL_FINEST = "FINEST";
		
	/*
	 * System property name that can be used to override default properties
	 * file.
	 */
	private static String systemPropertyName = "yooreeka.configuration";

	/*
	 * Default resource name that will be used to load properties.
	 * The path is relative to Yooreeka's root directory, which should be set
	 * by defining the property yooreeka.home
	 */
	private static String defaultResourceName = "/deploy/conf/yooreeka.properties";

	private static Properties properties = new Properties();

	private static Properties logProps = new Properties();
	
	static {
		
		// logger.debug("Initializing application properties...");
		String resourceName = System.getProperty(systemPropertyName);
		
		String resourcePath = System.getProperty("yooreeka.home"); 

		if (resourcePath == null) 
			resourcePath=System.getProperty("user.dir");
			
		if (resourceName == null) {
			resourceName = resourcePath+defaultResourceName;
			// logger.debug("System property '" + systemPropertyName +
			// "' not found. Loading configuration from default resource: '" +
			// defaultResourceName + "'.");
		} else {
			System.out
					.println("Loading configuration from resource defined through system property: "
							+ systemPropertyName + "=" + resourceName);
		}

		readProperties(resourceName);
	}

	public static String getHome() {

		return System.getProperty("yooreeka.home");
	}

	public static Level getLevel(String cName) {

		String logLevel = getLogProperty("log.level." + cName);

		if (logLevel == null)
			logLevel = LOG_LEVEL_WARNING;

		Level l = null;

		switch (logLevel) {
		case LOG_LEVEL_SEVERE:
			l = Level.SEVERE;
			break;
		case LOG_LEVEL_WARNING:
			l = Level.WARNING;
			break;
		case LOG_LEVEL_INFO:
			l = Level.INFO;
			break;
		case LOG_LEVEL_CONFIG:
			l = Level.CONFIG;
			break;
		case LOG_LEVEL_FINE:
			l = Level.FINE;
			break;
		case LOG_LEVEL_FINER:
			l = Level.FINER;
			break;
		case LOG_LEVEL_FINEST:
			l = Level.FINEST;
			break;
		default:
			l = Level.WARNING;
			break;
		}
		return l;
	}

	public static String getLogProperty(String key) {
		return logProps.getProperty(key);
	}

	/**
	 * First checks if there is a system property with the same key. Then
	 * attempts to load property from the configuration file.
	 * 
	 * @return null if property not found.
	 */
	public static String getProperty(String key) {
		// allow to override property using -D<property name>=<property value>
		return System.getProperty(key, properties.getProperty(key));
	}

	/**
	 * First checks if there is a system property with the same key. Then
	 * attempts to load property from the configuration file.
	 * 
	 * @param key
	 *            identifies property.
	 * @param defaultValue
	 *            default value that will be used if property is not found.
	 * @return property value or default value.
	 */
	public static String getProperty(String key, String defaultValue) {
		// allow to override property using -D<property name>=<property value>
		return System.getProperty(key, properties.getProperty(key, defaultValue));
	}

	public static void readProperties(String resourceName) {

		//P.hline(); P.println("resourceName: "+resourceName); P.hline();
		
		File propsFile = new File(resourceName);
		
		try {

			if (propsFile.exists() && propsFile.canRead()) {
				properties.load(new FileReader(propsFile));
			} else {
				printNoPropertiesFound();
				setStaticProperties();
			}
		} catch (Exception e) {
			String message = "Failed to load properties from resource: '"
					+ resourceName + "'.";
			System.out.println("ERROR:\n" + message + "\n" + e.getMessage());
			throw new RuntimeException(message, e);
		}
	}
	
	/**
	 * Set the following values if <tt>yooreeka.properties</tt> cannot be found:
	 * <pre>
	 *    yooreeka.home=C:/iWeb2
	 *    yooreeka.data.dir=C:/iWeb2/data
	 *    yooreeka.crawl.dir=C:/iWeb2/data/crawls
	 *    yooreeka.temp.dir=C:/iWeb2/deploy/temp
	 *    yooreeka.movielens.data.dir=C:/iWeb2/data/ch03/MovieLens
	 *    yooreeka.movielenstest.data.dir=C:/iWeb2/data/ch03/MovieLens/test
	 * </pre>
	 * 
	 * NOTE: This shouldn't happen but rather than having people getting stuck with setting up properties
	 * we can provide a default set of values (which is what they would get from the "Download" distro by
	 * default anyway) ...
	 * 
	 * Obviously, this will only work on MS Windows ... 
	 * change the <tt>rootDir</tt> value for another OS
	 */
	public static void setStaticProperties() {
		String rootDir="C:/iWeb2";
		properties.put("yooreeka.home", rootDir);
		properties.put("yooreeka.data.dir", rootDir+"/data");
		properties.put("yooreeka.crawl.dir", rootDir+"/data/crawls");
		properties.put("yooreeka.temp.dir", rootDir+"/deploy/temp");
		properties.put("yooreeka.movielens.data.dir", rootDir+"/data/ch03/MovieLens");
	}
	
	private static void printNoPropertiesFound() {
		P.hline();
		P.println("  Oops!");
		P.println("  The file __ yooreeka.properties __ was not found!");
		P.println("  Did you set up the system properly?");
		P.hline();
		P.println("  WARNING: Loading DEFAULT property values ...");
		P.hline();
	}
}
