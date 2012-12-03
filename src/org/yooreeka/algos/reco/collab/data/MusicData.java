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
package org.yooreeka.algos.reco.collab.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.yooreeka.algos.reco.collab.model.Rating;

/**
 * Utility class that we use as the source for Music data.
 */
public class MusicData {

	public static final String[] USERS = { "Albert", "Alexandra", "Athena",
			"Aurora", "Babis", "Bill", "Bob", "Carl", "Catherine", "Charlie",
			"Constantine", "Dmitry", "Elena", "Eric", "Frank", "George",
			"Jack", "John", "Maria", "Lukas", "Nick", "Terry", "Todd" };

	public static final String[] MUSIC_SAMPLES = {
			"You've Lost That Lovin' Feelin'", "Mrs. Robinson",
			"Wind Beneath My Wings", "Fiddler On The Roof", "La Bamba",
			"Wizard Of Oz", "White Christmas", "Let It Be", "Yesterday",
			"Singing In The Rain", "Sunday, Bloody Sunday", "Tears In Heaven",
			"Beethoven: Symphony No. 9 in D minor",
			"Bach: The Brandenburg Concerti", "Mozart: Symphony #41 (Jupiter)",
			"What A Wonderful World", "I Love Rock And Roll",
			"Albinoni: Adagio In G Minor", "Vivaldi: Four Seasons" };

	/**
	 * Builds data set with all the users where each user rates 80% of all the
	 * songs. User ratings created randomly with bias:
	 * <ul>
	 * <li>Users whose name starts from A to D will have ratings between 3 and
	 * 5.</li>
	 * <li>Users whose name starts from E to Z will have ratings between 1 and
	 * 3.</li>
	 * </ul>
	 */
	public static BaseDataset createDataset() {
		BaseDataset ds = new BaseDataset();

		double percentOfAllSongs = 0.80;

		/* Create items first */
		MusicItem[] allItems = loadAllMusicItems();

		for (int i = 0, n = USERS.length; i < n; i++) {
			int userId = i;
			String userName = USERS[i];
			int lowRating = 1;
			int highRating = 5;

			if (userName.toLowerCase().charAt(0) <= 'd') {
				// range of ratings for users whose name starts from A to D
				lowRating = 4;
				highRating = 5;
			} else {
				// range of ratings for users whose name starts from E to Z
				lowRating = 1;
				highRating = 3;
			}
			MusicItem[] items = pickRandomSongs(allItems, percentOfAllSongs);

			RatingBuilder ratingBuider = new RatingBuilder();
			List<Rating> ratings = ratingBuider.createBiasedRatings(userId,
					items, lowRating, highRating);

			MusicUser mu = new MusicUser(userId, userName, ratings);

			ds.add(mu);
		}
		return ds;
	}

	private static MusicItem createItem(String song) {
		int id = -1;
		for (int i = 0, n = MUSIC_SAMPLES.length; i < n; i++) {
			if (MUSIC_SAMPLES[i].equalsIgnoreCase(song)) {
				id = i;
				break;
			}
		}
		if (id < 0) {
			throw new IllegalArgumentException("Invalid song name: '" + song
					+ "'. This song is not on the list of predefined songs.");
		}

		return new MusicItem(id, MUSIC_SAMPLES[id]);
	}

	private static MusicUser createUser(String name) {
		int id = -1;
		for (int i = 0, n = USERS.length; i < n; i++) {
			if (USERS[i].equalsIgnoreCase(name)) {
				id = i;
				break;
			}
		}
		if (id < 0) {
			throw new IllegalArgumentException("Invalid user name: '" + name
					+ "'. Name is not on the list of predefined user names.");
		}

		return new MusicUser(id, name);
	}

	/**
	 * Returns array of new MusicItem instances for every songs listed in
	 * <code>MUSIC_SAMPLES</code> array.
	 */
	private static MusicItem[] loadAllMusicItems() {
		MusicItem[] allItems = new MusicItem[MusicData.MUSIC_SAMPLES.length];
		for (int i = 0, n = allItems.length; i < n; i++) {
			int id = i;
			String name = MusicData.MUSIC_SAMPLES[i];
			MusicItem item = new MusicItem(id, name);
			allItems[i] = item;
		}
		return allItems;
	}

	public static MusicUser[] loadExample() {
		MusicUser[] mu = new MusicUser[3];

		mu[0] = createUser("Frank");
		mu[1] = createUser("Constantine");
		mu[2] = createUser("Catherine");

		MusicItem[] mi = new MusicItem[11];

		mi[0] = createItem("Tears In Heaven");
		mi[1] = createItem("La Bamba");
		mi[2] = createItem("Mrs. Robinson");
		mi[3] = createItem("Yesterday");
		mi[4] = createItem("Wizard Of Oz");
		mi[5] = createItem("Mozart: Symphony #41 (Jupiter)");
		mi[6] = createItem("Beethoven: Symphony No. 9 in D minor");
		mi[7] = createItem("Fiddler On The Roof");
		mi[8] = createItem("What A Wonderful World");
		mi[9] = createItem("Let It Be");
		mi[10] = createItem("Sunday, Bloody Sunday");

		ArrayList<Rating> mr0 = new ArrayList<Rating>();
		ArrayList<Rating> mr1 = new ArrayList<Rating>();
		ArrayList<Rating> mr2 = new ArrayList<Rating>();

		/*
		 * Tears In Heaven <- 0 La Bamba <- 1 Mrs. Robinson <- 2 Yesterday <- 3
		 * Wizard Of Oz <- 4 Mozart: Symphony #41 (Jupiter) <- 5 Beethoven:
		 * Symphony No. 9 in D <- 6
		 */
		mr0.add(new MusicRating(mu[0].getId(), mi[0].getId(), 5));
		mr0.add(new MusicRating(mu[0].getId(), mi[1].getId(), 4));
		mr0.add(new MusicRating(mu[0].getId(), mi[2].getId(), 5));
		mr0.add(new MusicRating(mu[0].getId(), mi[3].getId(), 4));
		mr0.add(new MusicRating(mu[0].getId(), mi[4].getId(), 5));
		mr0.add(new MusicRating(mu[0].getId(), mi[5].getId(), 4));
		mr0.add(new MusicRating(mu[0].getId(), mi[6].getId(), 5));

		/*
		 * Tears In Heaven <- 0 Fiddler On The Roof <- 7 Mrs. Robinson <- 2 What
		 * A Wonderful World <- 8 Wizard Of Oz <- 4 Let It Be <- 9 Mozart:
		 * Symphony #41 (Jupiter) <- 5
		 */

		mr1.add(new MusicRating(mu[1].getId(), mi[0].getId(), 5));
		mr1.add(new MusicRating(mu[1].getId(), mi[7].getId(), 5));
		mr1.add(new MusicRating(mu[1].getId(), mi[2].getId(), 5));
		mr1.add(new MusicRating(mu[1].getId(), mi[8].getId(), 4));
		mr1.add(new MusicRating(mu[1].getId(), mi[4].getId(), 4));
		mr1.add(new MusicRating(mu[1].getId(), mi[9].getId(), 5));
		mr1.add(new MusicRating(mu[1].getId(), mi[5].getId(), 5));

		/*
		 * Tears In Heaven <- 0 Mrs. Robinson <- 2 Yesterday <- 3 Beethoven:
		 * Symphony No. 9 in D <- 6 Sunday, Bloody Sunday <- 10 Yesterday <- 3
		 * Let It Be <- 9
		 */
		mr2.add(new MusicRating(mu[2].getId(), mi[0].getId(), 1));
		mr2.add(new MusicRating(mu[2].getId(), mi[2].getId(), 2));
		mr2.add(new MusicRating(mu[2].getId(), mi[3].getId(), 2));
		mr2.add(new MusicRating(mu[2].getId(), mi[6].getId(), 3));
		mr2.add(new MusicRating(mu[2].getId(), mi[10].getId(), 1));
		mr2.add(new MusicRating(mu[2].getId(), mi[3].getId(), 1));
		mr2.add(new MusicRating(mu[2].getId(), mi[9].getId(), 2));

		mu[0].setRatings(mr0);
		mu[1].setRatings(mr1);
		mu[2].setRatings(mr2);

		return mu;
	}

	/**
	 * Returns a random selection of songs.
	 * 
	 * @param songs
	 *            list of songs to pick from
	 * @param percentOfAllSongs
	 *            determines size of returned selection.
	 * 
	 * @return array of songs.
	 */
	private static MusicItem[] pickRandomSongs(MusicItem[] songs,
			double percentOfAllSongs) {

		if (percentOfAllSongs < 0.0 || percentOfAllSongs > 1.0) {
			throw new IllegalArgumentException(
					"Value for 'percentOfAllSongs' argument should be between 0 and 1.");
		}
		Random rand = new Random();
		int sampleSize = (int) Math.round(percentOfAllSongs * songs.length);
		Map<Integer, MusicItem> pickedItems = new HashMap<Integer, MusicItem>();
		while (pickedItems.size() < sampleSize) {
			int songId = rand.nextInt(songs.length);
			MusicItem song = songs[songId];
			if (!pickedItems.containsKey(song.getId())) {
				pickedItems.put(song.getId(), song);
			}
		}

		return pickedItems.values().toArray(new MusicItem[pickedItems.size()]);
	}
}
