package org.ala.dao;

import java.util.List;

import junit.framework.TestCase;

import org.ala.model.TaxonConcept;

public class TaxonConceptDaoTest extends TestCase {

	public void testVernacularName() throws Exception {
		TaxonConceptDao tcDao = new TaxonConceptDao();
		boolean isVernacular = tcDao.isVernacularConcept("urn:lsid:biodiversity.org.au:afd.taxon:3b915d97-2376-4c40-bd04-7ae0acbaa34b");
		assertTrue(isVernacular);
	}

	public void testAddSynonym() throws Exception {
		
		TaxonConceptDao tcDao = new TaxonConceptDao();
		tcDao.delete("urn:lsid:afd:taxon:123");
		
		TaxonConcept tc = new TaxonConcept();
		tc.guid = "urn:lsid:afd:taxon:123";
		tc.nameString = "Sarcophilus harrisii";
		tcDao.create(tc);
		
		TaxonConcept synonym1 = new TaxonConcept();
		synonym1.guid = "urn:lsid:afd:taxon:124";
		synonym1.nameString = "Sarcophilus satanius";
		tcDao.addSynonym("urn:lsid:afd:taxon:123", synonym1);
		
		List<TaxonConcept> synonyms = tcDao.getSynonymsFor("urn:lsid:afd:taxon:123");
		
		assertEquals(synonyms.size(), 1);
		assertEquals(synonyms.get(0).guid, "urn:lsid:afd:taxon:124");
		assertEquals(synonyms.get(0).nameString, "Sarcophilus satanius");
		
		TaxonConcept synonym2 = new TaxonConcept();
		synonym2.guid = "urn:lsid:afd:taxon:125";
		synonym2.nameString = "Sarcophilus satanius";
		tcDao.addSynonym("urn:lsid:afd:taxon:123", synonym2);		
		
		//refresh local
		synonyms = tcDao.getSynonymsFor("urn:lsid:afd:taxon:123");
		
		//sort order should place Sarcophilus satanius below
		assertEquals(synonyms.size(), 2);
		assertEquals(synonyms.get(1).guid, "urn:lsid:afd:taxon:124");
		assertEquals(synonyms.get(1).nameString, "Sarcophilus satanius");
		
		//clear up after test
		tcDao.delete("urn:lsid:afd:taxon:123");
	}
}
