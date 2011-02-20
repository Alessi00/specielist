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
package org.ala.web; 

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.ala.dao.FulltextSearchDao;
import org.ala.dao.TaxonConceptDao;
import org.ala.dto.ExtendedTaxonConceptDTO;
import org.ala.dto.SearchDTO;
import org.ala.dto.SearchResultsDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for serving region pages and views around regions.
 *
 * @author Dave Martin (David.Martin@csiro.au)
 */
@Controller("imageSearchController")
public class ImageSearchController {
	
	@Inject
	FulltextSearchDao searchDao;
	
	@Inject
	RepoUrlUtils repoUrlUtils;
	
	@Inject
	TaxonConceptDao taxonConceptDao;
	
	@RequestMapping("/image/search")
	public String search(
			@RequestParam(value="q", required=false) String query, 
			@RequestParam(value="fq", required=false) String[] fq,
			@RequestParam(value="start", required=false, defaultValue="0") Integer startIndex,
			@RequestParam(value="pageSize", required=false, defaultValue ="104") Integer pageSize,
			@RequestParam(value="sort", required=false, defaultValue="score") String sortField,
			@RequestParam(value="dir", required=false, defaultValue ="asc") String sortDirection,
			@RequestParam(value="state", required=false) String state,
			@RequestParam(value="rank", required=false) String rank,
			@RequestParam(value="group", required=false) String group,
			Model model) throws Exception {
		
		List<String> filterQueries = new ArrayList<String>();
		filterQueries.add("idxtype:TAXON");
		filterQueries.add("hasImage:true");
		if(fq!=null && fq.length>0){
			for(String f: fq) { filterQueries.add(f); }
		}
		
		//state?
		if(state!=null) filterQueries.add("state:"+state);
		if(rank!=null) filterQueries.add("rank:"+rank);
		
		SearchResultsDTO<SearchDTO> results = searchDao.doFullTextSearch(query, (String[]) filterQueries.toArray(new String[0]), startIndex, pageSize, sortField, sortDirection);
		model.addAttribute("results", repoUrlUtils.fixRepoUrls(results));
		return "images/search";
	}
	
	@RequestMapping("/images/infoBox")
	public String getImageInfoBox(@RequestParam("q") String guid, Model model) throws Exception {
		ExtendedTaxonConceptDTO etc = taxonConceptDao.getExtendedTaxonConceptByGuid(guid);
		model.addAttribute("extendedTaxonConcept",repoUrlUtils.fixRepoUrls(etc));
		model.addAttribute("spatialPortalMap", PageUtils.getSpatialPortalMap(etc.getTaxonConcept().getGuid()));
		model.addAttribute("commonNames", PageUtils.dedup(etc.getCommonNames()));
		return "images/infoBox";
	}

	/**
	 * @param searchDao the searchDao to set
	 */
	public void setSearchDao(FulltextSearchDao searchDao) {
		this.searchDao = searchDao;
	}

	/**
	 * @param repoUrlUtils the repoUrlUtils to set
	 */
	public void setRepoUrlUtils(RepoUrlUtils repoUrlUtils) {
		this.repoUrlUtils = repoUrlUtils;
	}

	/**
	 * @param taxonConceptDao the taxonConceptDao to set
	 */
	public void setTaxonConceptDao(TaxonConceptDao taxonConceptDao) {
		this.taxonConceptDao = taxonConceptDao;
	}
}
