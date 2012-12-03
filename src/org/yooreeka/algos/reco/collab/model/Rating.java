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

/**
 * Generic representation of a rating given by user to a product (item).
 */
public class Rating implements java.io.Serializable {

	/**
	 * SVUID
	 */
	private static final long serialVersionUID = 1438346522502387789L;

	protected Item item;

	private int userId;
	private int itemId;
	private int rating;

	public Rating(int userId, int bookId, int rating) {
		this.userId = userId;
		this.itemId = bookId;
		this.rating = rating;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Rating other = (Rating) obj;
		if (itemId != other.itemId)
			return false;
		if (rating != other.rating)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	public int getItemId() {
		return itemId;
	}

	public int getRating() {
		return rating;
	}

	public int getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + itemId;
		result = prime * result + rating;
		result = prime * result + userId;
		return result;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	public void setItemId(int bookId) {
		this.itemId = bookId;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[userId: " + userId
				+ ", itemId: " + itemId + ", rating: " + rating + "]";
	}
}
