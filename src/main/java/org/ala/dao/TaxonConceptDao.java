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

import au.org.ala.checklist.lucene.model.NameSearchResult;
import org.ala.dto.ExtendedTaxonConceptDTO;
import org.ala.dto.SearchResultsDTO;
import org.ala.dto.SearchTaxonConceptDTO;
import org.ala.model.Classification;
import org.ala.model.CommonName;
import org.ala.model.ConservationStatus;
import org.ala.model.ExtantStatus;
import org.ala.model.Habitat;
import org.ala.model.Image;
import org.ala.model.PestStatus;
import org.ala.model.Publication;
import org.ala.model.Reference;
import org.ala.model.OccurrencesInGeoregion;
import org.ala.model.SimpleProperty;
import org.ala.model.TaxonConcept;
import org.ala.model.TaxonName;
import org.ala.model.Triple;
import org.ala.util.StatusType;
import org.apache.lucene.queryParser.ParseException;
import au.org.ala.data.model.LinnaeanRankClassification;

/**
 * Interface for creating, changing and searching taxon concept
 * profiles.
 *
 * @author Dave Martin (David.Martin@csiro.au)
 */
public interface TaxonConceptDao {

	/**
	 * Retrieve the synonyms for the Taxon Concept with the supplied guid.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<TaxonConcept> getSynonymsFor(String guid) throws Exception;

	/**
	 * Add an alternative identifier (GUID) for this taxon concept or 
	 * a concept asserted to be congruent.
	 * 
	 * @param guid
	 * @param alternativeIdentifier
	 * @return
	 * @throws Exception
	 */
	boolean addIdentifier(String guid, String alternativeIdentifier) throws Exception;
	
	/**
	 * Retrieve the images associated with this taxon concept.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<Image> getImages(String guid) throws Exception;
	
	/**
	 * Retrieve the images illustrating the distribution of this taxon concept.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<Image> getDistributionImages(String guid) throws Exception;

	/**
	 * Retrieve a list of alternative identifiers (guids) associated with
	 * this taxon concept.
	 * 
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<String> getIdentifiers(String guid) throws Exception;
	
	/**
	 * Retrieve the pest status associated with this taxon concept.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<PestStatus> getPestStatuses(String guid) throws Exception;

	/**
	 * Retrieve the conservation status associated with this taxon concept.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<ConservationStatus> getConservationStatuses(String guid) throws Exception;

	/**
	 * Retrieve the extant status associated with this taxon concept.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<ExtantStatus> getExtantStatuses(String guid) throws Exception;

	/**
	 * Retrieve the habitat associated with this taxon concept.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<Habitat> getHabitats(String guid) throws Exception;
	
    /**
     * Retreive the region(s) associated with this taxon concept.
     * @param guid
     * @return
     * @throws Exception
     */
    List<OccurrencesInGeoregion> getRegions(String guid) throws Exception;
    
	/**
	 * Retrieve the child concepts for the Taxon Concept with the supplied guid.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<TaxonConcept> getChildConceptsFor(String guid) throws Exception;

	/**
	 * Retrieve the parent concepts for the Taxon Concept with the supplied guid.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<TaxonConcept> getParentConceptsFor(String guid) throws Exception;

	/**
	 * Retrieve the common names for the Taxon Concept with the supplied guid.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<CommonName> getCommonNamesFor(String guid) throws Exception;

	/**
	 * Retrieve the text properties for the Taxon Concept with the supplied guid.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<SimpleProperty> getTextPropertiesFor(String guid) throws Exception;

	
	/**
	 * Retrieve the references for this taxon concept.
	 * 
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<Reference> getReferencesFor(String guid) throws Exception;

    /**
     * Retrieves the earliest reference for this taxon concept.
     *
     * @param guid
     * @return
     * @throws Exception
     */
    Reference getEarliestReferenceFor(String guid) throws Exception;

    /**
     * Retrieve the publications that are marked against this taxon concept.
     * 
     * @param guid
     * @return
     * @throws Exception
     */
    List<Reference> getPublicationReferencesFor(String guid) throws Exception;

	/**
	 * Store the following taxon concept
	 *
	 * @param tc
	 * @return
	 * @throws Exception
	 */
	boolean create(TaxonConcept tc) throws Exception;

	/**
	 * Update the taxon concept.
	 * 
	 * @param tc
	 * @return
	 * @throws Exception
	 */
	boolean update(TaxonConcept tc) throws Exception;
	
	/**
	 * What about multiple taxon names for each taxon concept???
	 *
	 * @param guid
	 * @param tn
	 * @throws Exception
	 */
	boolean addTaxonName(String guid, TaxonName tn) throws Exception;

	/**
	 * Add this common name to the Taxon Concept.
	 *
	 * @param guid
	 * @param commonName
	 * @throws Exception
	 */
	boolean addCommonName(String guid, CommonName commonName) throws Exception;

	/**
	 * Add this conservation status to the Taxon Concept.
	 *
	 * @param guid
	 * @param conservationStatus
	 * @throws Exception
	 */
	boolean addConservationStatus(String guid, ConservationStatus conservationStatus) throws Exception;

	/**
	 * Add this pest status to the Taxon Concept.
	 *
	 * @param guid
	 * @param pestStatus
	 * @throws Exception
	 */
	boolean addPestStatus(String guid, PestStatus pestStatus) throws Exception;

	/**
	 * Add extant status list to the Taxon Concept.
	 *
	 * @param guid
	 * @param extantStatusList
	 * @throws Exception
	 */
	boolean addExtantStatus(String guid, List<ExtantStatus> extantStatusList) throws Exception;

	/**
	 * Add habitat list to the Taxon Concept.
	 *
	 * @param guid
	 * @param habitatList
	 * @throws Exception
	 */
	boolean addHabitat(String guid, List<Habitat> habitatLSist) throws Exception;

	/**
	 * Add this list of regions to the Taxon Concept.
	 *
	 * @param guid
	 * @param regions
	 * @throws Exception
	 */
	boolean addRegions(String guid, List<OccurrencesInGeoregion> regions) throws Exception;

	/**
	 * Add this image to the Taxon Concept.
	 * 
	 * @param guid
	 * @param image
	 * @throws Exception
	 */
	boolean addImage(String guid, Image image) throws Exception;

	/**
	 * Add this image illustrating the distribution of a the Taxon Concept.
	 * 
	 * @param guid
	 * @param image
	 * @throws Exception
	 */
	boolean addDistributionImage(String guid, Image image) throws Exception;
	
	/**
	 * Add a synonym to this concept.
	 *
	 * @param guid
	 * @param synonym
	 * @throws Exception
	 */
	boolean addSynonym(String guid, TaxonConcept synonym) throws Exception;

	/**
	 * Add a congruent concept.
	 * 
	 * @param guid
	 * @param congruent
	 * @throws Exception
	 */
	boolean addIsCongruentTo(String guid, TaxonConcept congruent) throws Exception;

	/**
	 * Add a child taxon to this concept.
	 *
	 * @param guid
	 * @param childConcept
	 * @throws Exception
	 */
	boolean addChildTaxon(String guid, TaxonConcept childConcept) throws Exception;

	/**
	 * Add a parent taxon to this concept.
	 *
	 * @param guid
	 * @param parentConcept
	 * @throws Exception
	 */
	boolean addParentTaxon(String guid, TaxonConcept parentConcept) throws Exception;

	/**
	 * Add a text property to this concept.
	 *
	 * @param guid
	 * @param parentConcept
	 * @throws Exception
	 */
	boolean addTextProperty(String guid, SimpleProperty textProperty) throws Exception;

	/**
	 * Create a batch of taxon concepts.
	 *
	 * @param taxonConcepts
	 * @throws Exception
	 */
	void create(List<TaxonConcept> taxonConcepts) throws Exception;

	/**
	 * Retrieve the taxon concept by guid.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	TaxonConcept getByGuid(String guid) throws Exception;

	/**
	 * Retrieve the entire profile data for a taxon concept by guid.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	ExtendedTaxonConceptDTO getExtendedTaxonConceptByGuid(String guid) throws Exception;

	/**
	 * Retrieve the Taxon Name for the supplied GUID.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	TaxonName getTaxonNameFor(String guid) throws Exception;

	/**
	 * Search for taxon concept with the following scientific name.
	 *
	 * @param scientificName
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	List<SearchTaxonConceptDTO> findByScientificName(String input, int limit) throws Exception;

	/**
	 * Search for taxon concept with the following scientific name
	 *
	 * @param input
	 * @param startIndex
	 * @param pageSize
	 * @param sortField
	 * @param sortDirection
	 * @return
	 * @throws Exception
	 */
	SearchResultsDTO findByScientificName(String input,
			Integer startIndex, Integer pageSize, String sortField,
			String sortDirection) throws Exception;

	/**
	 * Find all TCs with a pest/conservation status (any value)
	 *
	 * @param statusType
	 * @param startIndex
	 * @param pageSize
	 * @param sortField
	 * @param sortDirection
	 * @return
	 * @throws ParseException
	 * @throws Exception
	 */
	SearchResultsDTO findAllByStatus(StatusType statusType,
			Integer startIndex, Integer pageSize, String sortField,
			String sortDirection) throws ParseException, Exception;

	/**
	 * Get LSID from Checklist Bank by kingdom, genus and scientific name.
	 * 
	 * @param scientificName Required.
	 * @param classification Required.
	 * @param taxonRank Can be null.
	 * @return LSID or null.
	 */
	String findLsidByName(String scientificName, LinnaeanRankClassification classification, String taxonRank);
	
	/**
	 * Get LSID from Checklist Bank by scientific name.
	 * 
	 * @param scientificName Required.
	 * @param taxonRank Can be null.
	 * @return LSID or null.
	 */
	String findLsidByName(String scientificName, String taxonRank);
	
	/**
	 * Get Checklist Bank entry by scientific name.
	 * 
	 * @param scientificName Required.
	 * @param classification Required.
	 * @param rank Can be null.
	 * @return 
	 */
	NameSearchResult findCBDataByName(String scientificName, LinnaeanRankClassification classification, String rank) throws Exception;
	
	/**
	 * Retrieve a list of concepts with the supplied parent guid.
	 *
	 * @param parentGuid
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	List<SearchTaxonConceptDTO> getByParentGuid(String parentGuid, int limit) throws Exception;

	/**
	 * Delete the TaxonConcept for the supplied guid
	 *
	 * @param guid
	 * @return true if a delete was performed
	 * @throws Exception
	 */
	boolean delete(String guid) throws Exception;
	
	/**
	 * Delete the TaxonConcept for the supplied guid
	 *
	 * @param guid
	 * @return true if a delete was performed
	 * @throws Exception
	 */
	boolean deleteForInfosources(String[] infoSourceId) throws Exception;

	/**
	 * Synchronises these triples to a taxon concept in hbase.
	 *
	 * @return true if we where able to add these properties to an existing
	 * taxon. False otherwise
	 *
	 * @param infosourceId the infosource supplying the triples
	 * @param document the document supplying the triples
	 * @param triples the triples to add
	 * @param the filepath of the document
	 * @throws Exception
	 */
	boolean syncTriples(org.ala.model.Document document, List<Triple> triples, Map<String,String> dublinCore) throws Exception;

	/**
	 * Clear the associated properties from each taxon concept.
	 *
	 * Clear the triples in the "raw:" column family.
	 *
	 * @throws Exception
	 */
	void clearRawProperties() throws Exception;

	/**
	 * Create a index to support searching.
	 *
	 * @throws Exception
	 */
	void createIndex() throws Exception;

	/**
	 * Retrieve the raw properties
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	Map<String, String> getPropertiesFor(String guid) throws Exception;

	/**
	 * Add a classification to this taxon.
	 *
	 * @param guid
	 * @param classification
	 */
	boolean addClassification(String guid, Classification classification) throws Exception;

	/**
	 * Retrieve the classifications associated with this taxon.
	 *
	 * @param guid
	 * @return
	 * @throws Exception
	 */
	List<Classification> getClassifications(String guid) throws Exception;
	
	/**
	 * Adds a (literature) reference to this taxon.
	 * 
	 * @param reference
	 */
	public abstract boolean addReferences(String guid, List<Reference> references) throws Exception;

    /**
     * Adds the "earliest" reference to this taxon.
     *
     * @param guid
     * @param reference
     * @return
     * @throws Exception
     */
    boolean addEarliestReference(String guid, Reference reference) throws Exception;

    /**
     * Adds the publication reference for this taxon concept.
     * 
     * @param guid
     * @param reference
     * @return
     * @throws Exception
     */
    boolean addPublicationReference(String guid, List<Reference> reference) throws Exception;

	/**
	 * Add a publication to the profile.
	 * 
	 * @param guid
	 * @param publication
	 */
	boolean addPublication(String guid, Publication publication) throws Exception;


	boolean setRankingOnImage(String taxonGuid, String imageUri, boolean positive) throws Exception;
	
    /**
     * Get the location of an index
     *
     * @return
     */
    String getIndexLocation();

}