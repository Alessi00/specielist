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
package org.ala.lucene;

import java.util.ArrayList;
import java.util.TreeSet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.gbif.ecat.model.ParsedName;
import org.gbif.ecat.parser.NameParser;
/**
 * Reusable methods for lucene searching or index creation.
 *
 * @author Dave Martin (David.Martin@csiro.au)
 */
public class LuceneUtils {
    private static final String SCI_NAME = "scientificName";
    private static final String SCI_NAME_RAW = "scientificNameRaw";
	/**
	 * Adds a scientific name to the lucene index in multiple forms to increase
	 * chances of matches
	 * 
	 * @param doc
	 * @param scientificName
	 */
	public static void addScientificNameToIndex(Document doc, String scientificName){
		
		NameParser nameParser = new NameParser();
		
		//remove the subgenus
		String normalized = "";
		
		if(scientificName!=null){
			normalized = scientificName.replaceFirst("\\([A-Za-z]{1,}\\) ", "");
		}

		ParsedName parsedName = nameParser.parseIgnoreAuthors(normalized);
        // store scientific name values in a set before adding to Lucene so we don't get duplicates
        TreeSet<String> sciNames = new TreeSet<String>();

    	if(parsedName!=null){
    		if(parsedName.isBinomial()){
    			//add multiple versions
                sciNames.add(parsedName.buildAbbreviatedCanonicalName().toLowerCase());
                sciNames.add(parsedName.buildAbbreviatedFullName().toLowerCase());
    		}

            //add lowercased version
            sciNames.add(parsedName.buildCanonicalName().toLowerCase());
            // add to Lucene
            for (String sciName : sciNames) {
                doc.add(new Field(SCI_NAME, sciName, Store.YES, Index.NOT_ANALYZED_NO_NORMS));
            }
    	} else {
    		//add lowercased version if name parser failed			    		
	    	doc.add(new Field(SCI_NAME, normalized.toLowerCase(), Store.YES, Index.NOT_ANALYZED_NO_NORMS));
    	}
    	
    	if(scientificName!=null){
    		doc.add(new Field(SCI_NAME_RAW, scientificName, Store.YES, Index.NOT_ANALYZED_NO_NORMS));
    	}
	}
}
