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
package org.yooreeka.examples.credit.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.yooreeka.examples.credit.data.users.User;

public class CreditDataUtils {

	public static List<User> loadUsers(String filename) {
		List<User> users = new ArrayList<User>();

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
					User user = new User();
					user.loadFromExternalString(line);
					users.add(user);
				}
			}
		} catch (IOException ioX) {
			throw new RuntimeException("Failed to load users from file: '"
					+ filename + "' ", ioX);
		}

		try {
			fReader.close();
		} catch (IOException ioX) {
			ioX.printStackTrace();
		}

		return users;
	}

	public static void saveUsers(String filename, List<User> users) {
		try {
			FileWriter fout = new FileWriter(filename);
			BufferedWriter writer = new BufferedWriter(fout);
			for (User user : users) {
				writer.write(user.toExternalString());
				writer.write("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed to save users in file: '"
					+ filename + "' ", e);
		}
	}

	private CreditDataUtils() {
		// empty
	}

}
