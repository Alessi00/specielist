/***************************************************************************
 * Copyright (C) 2010 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ***************************************************************************/
package org.ala.dao;

import java.util.List;
import java.util.Map;
/**
 * A store helper provides basic DAO functionality on top of a backend store.
 * This is intended to hide the details of the underlying backend store in use
 * which maybe HBase, Cassandra or BerkeleyDB.
 * 
 * TODO Use generics properly.
 *
 * @author Dave Martin (David.Martin@csiro.au)
 */
public interface StoreHelper {

	/**
	 * Initialise the store helper, establishing network connections etc.
	 * 
	 * @throws Exception
	 */
	void init() throws Exception;
	
	/**
	 * Get the current object in the specified table/row/column.
	 * 
	 * @param table
	 * @param columnFamily
	 * @param columnName
	 * @param guid
	 * @param theClass
	 * @return null if nothing found in the row.
	 * @throws Exception
	 */
	Comparable get(String table, String columnFamily, String columnName, String guid, Class theClass) throws Exception;
	
	/**
	 * Get a list of objects in this table/row/column.
	 * 
	 * @param table
	 * @param columnFamily
	 * @param columnName
	 * @param guid
	 * @param theClass
	 * @return
	 * @throws Exception
	 */
	List<Comparable> getList(String table, String columnFamily, String columnName, String guid, Class theClass) throws Exception;

	/**
	 * Put a single instance into this row, overwriting the current instance if present.
	 * 
	 * @param table
	 * @param columnFamily
	 * @param columnName
	 * @param guid
	 * @param object
	 * @return true if successful
	 * @throws Exception
	 */
	boolean putSingle(String table, String columnFamily, String columnName, String guid, Comparable object) throws Exception;

	/**
	 * Put a single instance into this row, appending to a list if there is data already.
	 * 
	 * @param table
	 * @param columnFamily
	 * @param columnName
	 * @param guid
	 * @param object
	 * @return true if successful
	 * @throws Exception
	 */
	boolean put(String table, String columnFamily, String columnName, String guid, Comparable object) throws Exception;

	/**
	 * Put a single instance into this row, appending to a list if there is data already.
	 * 
	 * @param table
	 * @param columnFamily
	 * @param columnName
	 * @param guid
	 * @param object
	 * @return true if successful
	 * @throws Exception
	 */
	boolean put(String table, String columnFamily, String superColumn, String columnName, String guid, Comparable object) throws Exception;
	
	/**
	 * Put a list of instances into this row.
	 * 
	 * @param table
	 * @param columnFamily
	 * @param columnName
	 * @param guid
	 * @param object
	 * @param append whether to append to existing instances, or replace
	 * @return true if successful
	 * @throws Exception
	 */
	boolean putList(String table, String columnFamily, String columnName, String guid, List<Comparable> object, boolean append) throws Exception;
	
	/**
	 * Get a scanner for the specified table/columnfamily/column.
	 * 
	 * @param table
	 * @param columnFamily
	 * @param column
	 * @return an instance of Scanner, initialised and read to be used
	 * @throws Exception
	 */
	Scanner getScanner(String table, String columnFamily, String column) throws Exception;
	
	public Map<String, Object> getSubColumnsByGuid(String guid) throws Exception;
}