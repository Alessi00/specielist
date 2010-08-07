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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ala.model.TaxonConcept;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.type.TypeFactory;
import org.wyki.cassandra.pelops.Mutator;
import org.wyki.cassandra.pelops.Pelops;
import org.wyki.cassandra.pelops.Policy;
import org.wyki.cassandra.pelops.Selector;

/**
 * A StoreHelper implementation for Cassandra that uses Pelops over the
 * top of Thrift.
 * @author Natasha
 * 
 * History:
 * 4 Aug 2010 (MOK011): implement put, putList, putSingle and getScanner functions based on CassandraHelper.java.
 */
public class CassandraPelopsHelper implements StoreHelper  {
	protected static Logger logger = Logger.getLogger(CassandraPelopsHelper.class);

	protected static String keySpace = "bie";

	protected String host = "localhost";

	protected String pool = "ALA";

	protected int port = 9160;

	protected String charsetEncoding = "UTF-8";

	@Override
	public void init() throws Exception {
		//set up the connection pool
		Pelops.addPool(pool, new String[]{host}, port, false, keySpace, new Policy());
	}

    /**
     * @see org.ala.dao.StoreHelper#get(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Class)
     */
    @Override
    public Comparable get(String table, String columnFamily, String columnName, String guid, Class theClass) throws Exception {
        logger.debug("Pelops get table: " + table + " colFamily: " +columnFamily + " guid: " + guid);
        Selector selector = Pelops.createSelector(pool, keySpace);
        Column col = null;
        try{
            col = selector.getSubColumnFromRow(guid, columnFamily, columnFamily, columnName, ConsistencyLevel.ONE);
        }
        catch(Exception e){
            //expected behaviour. current thrift API doesnt seem
            //to support a retrieve null getter
        	if(logger.isTraceEnabled()){
        		logger.trace(e.getMessage(), e);
        	}
        }
        //initialise the object mapper
		ObjectMapper mapper = new ObjectMapper();
//		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		mapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		//read the existing value
		if(col!=null){
			String value = new String(col.value,charsetEncoding);
//			logger.info(value);
			return (Comparable) mapper.readValue(value, theClass);
		} else {
			return null;
		}
    }

    /**
     * @see org.ala.dao.StoreHelper#getList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Class)
     */
    @Override
    public List<Comparable> getList(String table, String columnFamily, String columnName, String guid, Class theClass) throws Exception {
        logger.debug("Pelops getList table: " + table + " colFamily: " +columnFamily + " guid: " + guid);
        Selector selector = Pelops.createSelector(pool, keySpace);
        Column col = null;
        try{ 
            col=selector.getSubColumnFromRow(guid, columnFamily, columnFamily, columnName, ConsistencyLevel.ONE);
        }
        catch(Exception e){
            //expected behaviour. current thrift API doesnt seem
            //to support a retrieve null getter
        	if(logger.isTraceEnabled()){
        		logger.trace(e.getMessage(), e);
        	}
        }
         //initialise the object mapper
		ObjectMapper mapper = new ObjectMapper();
//		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		mapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		//read the existing value
		List<Comparable> objectList = null;
		if(col!=null){
			String value = new String(col.value, charsetEncoding);
//			logger.info(value);
			objectList = mapper.readValue(value, TypeFactory.collectionType(ArrayList.class, theClass));
		} else {
			objectList = new ArrayList<Comparable>();
		}
		logger.debug("Pelops getList returning.");
		return objectList;
    }

    /**
     * @see org.ala.dao.StoreHelper#putSingle(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Comparable)
     */
    @Override
    public boolean putSingle(String table, String columnFamily, String columnName, String guid, Comparable object) throws Exception {
    	logger.debug("Pelops putSingle table: " + table + " colFamily: " +columnFamily + " guid: " + guid);
    	Mutator mutator = Pelops.createMutator(pool, keySpace);
    	
		guid =  StringUtils.trimToNull(guid);
		if(guid==null){
			logger.warn("Null or empty guid supplied. Unable to add to row ["+guid+"] column ["+columnName+"] object: "+object);
			return false;
		}

        //initialise the object mapper
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		
		//convert to JSON
		String json = mapper.writeValueAsString(object); 
		
		//insert into table
		try{
			mutator.writeSubColumn(guid, columnFamily, columnFamily, mutator.newColumn(columnName, json));
			mutator.execute(ConsistencyLevel.ONE);
			logger.debug("Pelops putSingle returning");
			return true;
		} catch (Exception e){
			logger.error(e.getMessage(),e);
			return false;
		}
    }

	/**
	 * @see org.ala.dao.StoreHelper#put(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Comparable)
	 */
	@Override
	public boolean put(String table, String columnFamily, String superColumn,
			String columnName, String guid, Comparable object) throws Exception {
		logger.debug("Pelops put table: " + table + " colFamily: " +columnFamily + " guid: " + guid);
    	Mutator mutator = Pelops.createMutator(pool, keySpace);
    	Selector selector = Pelops.createSelector(pool, keySpace);
    	
		guid =  StringUtils.trimToNull(guid);
		if(guid==null || object==null){
			logger.warn("Null or empty guid supplied. Unable to add to row ["+guid+"] column ["+columnName+"] object: "+object);
			return false;
		}
		
		Column col = null;
		try{
			col = selector.getSubColumnFromRow(guid, columnFamily, superColumn, columnName, ConsistencyLevel.ONE);
		}catch (Exception e){
        	//expected behaviour. current thrift API doesnt seem
        	//to support a retrieve null getter
        	if(logger.isTraceEnabled()){
        		logger.trace(e.getMessage(), e);
        	}
        }
    	
        //initialise the object mapper
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		
		//read the existing value
		List<Comparable> objectList = null;
		if(col!=null){
			String value = new String(col.value, charsetEncoding);
			objectList = mapper.readValue(value, TypeFactory.collectionType(ArrayList.class, object.getClass()));
		} else {
			objectList = new ArrayList<Comparable>();
		}

		//add to the collection and sort the objects
		if(objectList.contains(object)){
			int idx = objectList.indexOf(object);
			//replace with this version
			objectList.remove(idx);
			objectList.add(object);
		} else {
			objectList.add(object);
		}
		Collections.sort(objectList);

		//convert to JSON
		String json = mapper.writeValueAsString(objectList); 
		
		//insert into table
		try{
			mutator. writeSubColumn(guid, columnFamily, superColumn, mutator.newColumn(columnName, json));
			mutator.execute(ConsistencyLevel.ONE);
			logger.debug("Pelops put returning");
			return true;
		} catch (Exception e){
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	/**
	 * @see org.ala.dao.StoreHelper#put(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Comparable)
	 */
    @Override
    public boolean put(String table, String columnFamily, String columnName, String guid, Comparable object) throws Exception {
        logger.debug("Pelops put table: " + table + " colFamily: " +columnFamily + " guid: " + guid);
		return put(table, columnFamily, columnFamily, columnName, guid, object);
    }

    /**
     * @see org.ala.dao.StoreHelper#putList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, boolean)
     */
    @Override
    public boolean putList(String table, String columnFamily, String columnName, String guid, List<Comparable> objects, boolean append) throws Exception {
    	logger.debug("Pelops putList table: " + table + " colFamily: " +columnFamily + " guid: " + guid);
    	Mutator mutator = Pelops.createMutator(pool, keySpace);
    	Selector selector = Pelops.createSelector(pool, keySpace);
    	
		guid =  StringUtils.trimToNull(guid);
		if(guid==null){
			logger.warn("Null or empty guid supplied. Unable to add to row ["+guid+"] column ["+columnName+"] object: "+objects);
			return false;
		}		

		Column col = null;
		try{
			col = selector.getSubColumnFromRow(guid, columnFamily, columnFamily, columnName, ConsistencyLevel.ONE);
		}catch (Exception e){
        	//expected behaviour. current thrift API doesnt seem
        	//to support a retrieve null getter
        	if(logger.isTraceEnabled()){
        		logger.trace(e.getMessage(), e);
        	}
        }
        
        //initialise the object mapper
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

		String json = null;
		
		if(append){		
			//read the existing value
			List<Comparable> objectList = null;
			if(col!=null){
				String value = new String(col.value, charsetEncoding);
				
				if(!objects.isEmpty()){
					Object first = objects.get(0);
					objectList = mapper.readValue(value, TypeFactory.collectionType(ArrayList.class, first.getClass()));
				} else {
					objectList = new ArrayList<Comparable>();
				}
			} else {
				objectList = new ArrayList<Comparable>();
			}
			//FIXME not currently checking for duplicates
			objectList.addAll(objects);
			json = mapper.writeValueAsString(objectList);
			
		} else {			
			Collections.sort(objects);
			//convert to JSON
			json = mapper.writeValueAsString(objects);
		}
		//insert into table
		try{
			mutator. writeSubColumn(guid, columnFamily, columnFamily, mutator.newColumn(columnName, json));
			mutator.execute(ConsistencyLevel.ONE);
			logger.debug("Pelops putList returning");
			return true;
		} catch (Exception e){
			logger.error(e.getMessage(),e);
			return false;
		}
    }

    /**
     * @see org.ala.dao.StoreHelper#getScanner(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Scanner getScanner(String table, String columnFamily, String column) throws Exception {
    	return new CassandraScanner(Pelops.getDbConnPool(pool).getConnection().getAPI(), keySpace, columnFamily, column);
    }

    public static void main(String[] args) throws Exception {
    	CassandraPelopsHelper helper = new CassandraPelopsHelper();
    	helper.init();
    	
    	TaxonConcept t = null;	
    	List<Comparable> l = new ArrayList<Comparable>();
   	
		for(int i=0; i< 10; i++){
	        t =  new TaxonConcept();
	        t.setId(i);
	        t.setGuid("urn:lsid:"+i);
	        t.setNameString("Aus bus");
	        t.setAuthor("Smith");
	        t.setAuthorYear("2008");
	        t.setInfoSourceName("AFD");
	        t.setInfoSourceURL("http://afd.org.au");
	        helper.putSingle("taxonConcept", "tc", "taxonConcept", t.getGuid(), t);
	        
	        l.add(t);
	        if(i % 1000==0){
	        	System.out.println("id: "+i);
	        }
		}
		helper.putList("taxonConcept", "tc", "taxonConcept", "128", l, true);
/* 		
//        CommonName c1 = new CommonName();
//        c1.setNameString("Dave");
//
//        CommonName c2 = new CommonName();
//        c2.setNameString("Frank");
//
//        helper.putSingle("taxonConcept", "tc", "taxonConcept", "123", t);
//        helper.put("taxonConcept", "tc", "commonName", "123", c1);
//        helper.put("taxonConcept", "tc", "commonName", "123", c2);
//        helper.putSingle("taxonConcept", "tc", "taxonConcept", "124", t);
//        
//        TaxonConcept tc = (TaxonConcept) helper.get("taxonConcept", "tc", "taxonConcept", "123", TaxonConcept.class);
//        System.out.println("Retrieved: "+tc.getNameString());
//        
//        List<CommonName> cns = (List) helper.getList("taxonConcept", "tc", "commonName", "123", CommonName.class);
//        System.out.println("Retrieved: "+cns);
*/
        //cassandra scanning
    	Scanner scanner = helper.getScanner("taxonConcept", "tc", "taxonConcept");
    	for(int i = 0; i < 10; i++){
    		System.out.println(new String(scanner.getNextGuid()));
    	}
		System.exit(0);
    }

	/**
	 * @param keySpace the keySpace to set
	 */
	public static void setKeySpace(String keySpace) {
		CassandraPelopsHelper.keySpace = keySpace;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(String pool) {
		this.pool = pool;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param charsetEncoding the charsetEncoding to set
	 */
	public void setCharsetEncoding(String charsetEncoding) {
		this.charsetEncoding = charsetEncoding;
	}
}
