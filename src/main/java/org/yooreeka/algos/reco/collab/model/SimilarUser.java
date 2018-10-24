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
package org.yooreeka.algos.reco.collab.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Utility class that acts as a holder for user and similarity value that was
 * assigned to the user.
 */
public class SimilarUser {

	public static SimilarUser[] getTopNFriends(List<SimilarUser> similarUsers,
			int topN) {

		// sort friends based on itemAgreement
		SimilarUser.sort(similarUsers);

		// select top N friends
		List<SimilarUser> topFriends = new ArrayList<SimilarUser>();
		for (SimilarUser f : similarUsers) {
			if (topFriends.size() >= topN) {
				// have enough friends.
				break;
			}

			// This is useful when we compose results from different
			// recommenders
			if (!topFriends.contains(f)) {
				topFriends.add(f);
			}
		}

		return topFriends.toArray(new SimilarUser[topFriends.size()]);
	}

	/**
	 * Prints a list of user names with their similarities.
	 * 
	 * @param friends
	 *            similar users
	 * @param header
	 *            title that will be printed at the top of the list.
	 */
	public static void print(SimilarUser[] friends, String header) {
		System.out.println("\n" + header + "\n");
		for (SimilarUser f : friends) {
			System.out.printf("name: %-36s, similarity: %f\n", f.getName(),
					f.getSimilarity());
		}
	}

	public static void sort(List<SimilarUser> similarUsers) {

		Collections.sort(similarUsers, new Comparator<SimilarUser>() {
			public int compare(SimilarUser f1, SimilarUser f2) {
				int result = 0;
				if (f1.getSimilarity() < f2.getSimilarity()) {
					result = 1; // reverse order
				} else if (f1.getSimilarity() > f2.getSimilarity()) {
					result = -1;
				} else {
					result = 0;
				}
				return result;
			}

			@Override
			public Comparator<SimilarUser> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarUser> thenComparing(
					Comparator<? super SimilarUser> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<SimilarUser> thenComparing(
					Function<? super SimilarUser, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<SimilarUser> thenComparing(
					Function<? super SimilarUser, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarUser> thenComparingInt(
					ToIntFunction<? super SimilarUser> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarUser> thenComparingLong(
					ToLongFunction<? super SimilarUser> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<SimilarUser> thenComparingDouble(
					ToDoubleFunction<? super SimilarUser> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}

	/*
	 * The friend User .
	 */
	private User friend;

	/*
	 * Similarity
	 */
	private double similarity = -1;

	public SimilarUser(User user, double similarity) {
		friend = user;
		this.similarity = similarity;
	}

	public int getId() {
		return friend.getId();
	}

	public String getName() {
		return friend.getName();
	}

	/**
	 * @return the similarity
	 */
	public double getSimilarity() {
		return similarity;
	}

	public User getUser() {
		return friend;
	}
}
