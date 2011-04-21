/**************************************************************************
 *  Copyright (C) 2010 Atlas of Living Australia
 *  All Rights Reserved.
 *
 *  The contents of this file are subject to the Mozilla Public
 *  License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 *  the License at http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an "AS
 *  IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  rights and limitations under the License.
 ***************************************************************************/

package org.ala.model;

import java.util.Map;

//import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Ranking VO
 * 
 * @author mok011
 */
public class BaseRanking implements Comparable<BaseRanking> {
	protected String userIP;
	protected String userId;
	protected String fullName;
	protected boolean positive;
	protected boolean isBlackListed = false;
	
	//image field name = 'identifier', common name field name = 'nameString'
	protected Map<String, String> compareFieldValue;
	
	@Override
	public int compareTo(BaseRanking o) {
		return 0;
	}

	public Map<String, String> getCompareFieldValue() {
		return compareFieldValue;
	}

	public void setCompareFieldValue(Map<String, String> compareFieldValue) {
		this.compareFieldValue = compareFieldValue;
	}
	
	/**
	 * @return the userIP
	 */
	public String getUserIP() {
		return userIP;
	}

	/**
	 * @param userIP the userIP to set
	 */
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public boolean isBlackListed() {
		return isBlackListed;
	}

	public void setBlackListed(boolean isBlackListed) {
		this.isBlackListed = isBlackListed;
	}

	/**
	 * @return the positive
	 */
	public boolean isPositive() {
		return positive;
	}

	/**
	 * @param positive the positive to set
	 */
	public void setPositive(boolean positive) {
		this.positive = positive;
	}

}
