/**
 * Copyright (c) CSIRO Australia, 2009
 *
 * @author $Author: dos009 $
 * @version $Id:  $
 */

package csiro.diasb.dao;

import csiro.diasb.datamodels.HtmlPageDTO;
import csiro.diasb.datamodels.ImageDTO;
import csiro.diasb.datamodels.TaxonNameDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for the data access of Fedora Commons documents/objects
 *
 * @author "Nick dos Remedios (dos009) <Nick.dosRemedios@csiro.au>"
 */
public interface FedoraDAO {
    /**
     * Return a list of TaxonNameDTOs for a given list of Taxon Name idenitifiers (urn.*)
     * 
     * @param hasTaxonNames
     * @return
     */
    public List<TaxonNameDTO> getTaxonNamesForUrns(List<String> hasTaxonNames);

    /**
     * Return a list of ImageDTOs for a given list of scientific names
     * 
     * @param scientificNames
     * @return
     */
    public List<ImageDTO> getImagesForScientificNames(ArrayList<String> scientificNames);

    /**
     * Return a list of HtmlPageDTOs for a given list of scientific names
     * 
     * @param scientificNames
     * @return
     */
    public List<HtmlPageDTO> getHtmlPagesForScientificNames(ArrayList<String> scientificNames);
    
    /**
     * Get a FC PID for a LSID/GUID
     * 
     * @param lsid
     * @return
     */
    public String getPidForLsid(String lsid);

}
