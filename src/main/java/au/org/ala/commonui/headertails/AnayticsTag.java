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
package au.org.ala.commonui.headertails;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Simple tag that writes out the Google Analytics JS block for an ALA web application.
 *
 * @author Nick dos Remedios (nick.dosremedios@csiro.au)
 */
public class AnayticsTag extends TagSupport {

    protected static Logger logger = Logger.getLogger(BannerTag.class);

    private String returnUrlPath = "";
    private String returnLogoutUrlPath = "";
    private Boolean populateSearchBox = true;

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    public int doStartTag() throws JspException {
        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

            HeaderAndTailUtil hatu = new HeaderAndTailUtil(pageContext);
            String html = hatu.getFooterJs();
            pageContext.getOut().print(html);
        } catch (Exception e) {
            logger.error("BannerMenuTag: " + e.getMessage(), e);
            throw new JspTagException("BannerMenuTag: " + e.getMessage());
        }

        return super.doStartTag();
    }

    public String getReturnUrlPath() {
        return returnUrlPath;
    }

    public void setReturnUrlPath(String returnUrlPath) {
        this.returnUrlPath = returnUrlPath;
    }

    /**
     * @param populateSearchBox the populateSearchBox to set
     */
    public void setPopulateSearchBox(Boolean populateSearchBox) {
        this.populateSearchBox = populateSearchBox;
    }

    public Boolean getPopulateSearchBox() {
        return populateSearchBox;
    }

    /**
     * @return
     */
    public String getReturnLogoutUrlPath() {
        return returnLogoutUrlPath;
    }

    /**
     * @param returnLogoutUrlPath
     */
    public void setReturnLogoutUrlPath(String returnLogoutUrlPath) {
        this.returnLogoutUrlPath = returnLogoutUrlPath;
    }

}