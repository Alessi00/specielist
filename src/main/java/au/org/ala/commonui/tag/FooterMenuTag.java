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
package au.org.ala.commonui.tag;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

/**
 * Simple tag that writes out the footer menu list for an ALA web application.
 * 
 * @author Peter Flemming
 */
public class FooterMenuTag extends TagSupport {

	private static final long serialVersionUID = -6406031197753714478L;
	protected static Logger logger = Logger.getLogger(FooterMenuTag.class);
	private static final String GOOGLE_ANALYTICS_KEY = "UA-4355440-1";
	private String returnUrlPath = "";
	
	/**
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {

		StringBuilder html = new StringBuilder(
			"<div id='footer-nav'>" +
				"<ul id='menu-footer-site'>" +
					"<li id='menu-item-1046' class='menu-item menu-item-type-post_type current-menu-item page_item page-item-97 current_page_item menu-item-1046'><a href='http://test.ala.org.au/'>Home</a></li>" +
					"<li id='menu-item-1051' class='menu-item menu-item-type-post_type menu-item-1051'><a href='http://test.ala.org.au/tools-services/'>Tools</a></li>" +
					"<li id='menu-item-1050' class='menu-item menu-item-type-post_type menu-item-1050'><a href='http://test.ala.org.au/support/'>Support</a></li>" +
					"<li id='menu-item-1048' class='menu-item menu-item-type-post_type menu-item-1048'><a href='http://test.ala.org.au/contact-us/'>Contact Us</a></li>");
		
		if (returnUrlPath.equals("")) {
			html.append(
					"<li id='menu-item-1047' class='last menu-item menu-item-type-post_type menu-item-1047'><a href='http://test.ala.org.au/about/'>About the Atlas</a></li>");
		} else {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			Principal principal = request.getUserPrincipal();
			String casServer = pageContext.getServletContext().getInitParameter("casServerName");

			String loginLogoutAnchor;
			if (principal == null) {
				loginLogoutAnchor = "<a href='" + casServer + "/cas/login?service=" + returnUrlPath + "'>Log in</a>";
			} else {
				loginLogoutAnchor = "<a href='" + casServer + "/cas/logout?url=" + returnUrlPath + "'>Log out</a>";
			}

			html.append(
					"<li id='menu-item-1047' class='menu-item menu-item-type-post_type menu-item-1047'><a href='http://test.ala.org.au/about/'>About the Atlas</a></li>" +
					"<li id='menu-item-1052' class='last menu-item menu-item-type-custom menu-item-1052'>" + loginLogoutAnchor + "</li>");
		}

		html.append(
				"</ul>" +
				"<ul id='menu-footer-legal'>" +
					"<li id='menu-item-3090' class='menu-item menu-item-type-post_type menu-item-3090'><a href='http://test.ala.org.au/site-map/'>Site Map</a></li>" +
					"<li id='menu-item-1042' class='menu-item menu-item-type-post_type menu-item-1042'><a href='http://test.ala.org.au/about/media-centre/terms-of-use/citing-the-atlas/'>Citing the Atlas</a></li>" +
					"<li id='menu-item-1043' class='menu-item menu-item-type-post_type menu-item-1043'><a href='http://test.ala.org.au/about/media-centre/terms-of-use/disclaimer/'>Disclaimer</a></li>" +
					"<li id='menu-item-1045' class='last menu-item menu-item-type-post_type menu-item-1045'><a href='http://test.ala.org.au/about/media-centre/terms-of-use/'>Terms of Use</a></li>" +
				"</ul>" +
			"</div>" +
			"<div class='copyright'>" +
				"<p>" +
					"<a href='http://creativecommons.org/licenses/by/2.5/au/' title='External link to Creative Commons' class='left no-pipe'>" +
						"<img src='http://test.ala.org.au/wp-content/themes/ala/images/somerights20.png' width='88' height='31' alt=''/>" +
					"</a>This work is licensed under a <a href='http://creativecommons.org/licenses/by/2.5/au/' title='External link to Creative Commons'>Creative Commons Attribution 2.5 Australia License</a>" +
				"</p>" +
			"</div>\n" +
            "<script type='text/javascript'> " +
                "var gaJsHost = (('https:' == document.location.protocol) ? 'https://ssl.' : 'http://www.');" +
                "document.write(unescape('%3Cscript src='' + gaJsHost + 'google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E'));" +
            "</script> " +
            "<script type='text/javascript'> " +
                "var pageTracker = _gat._getTracker('" + GOOGLE_ANALYTICS_KEY + "');" +
                "pageTracker._initData();" +
                "pageTracker._trackPageview();" +
            "</script>\n" );
		
		try {
			pageContext.getOut().print(html.toString());
		} catch (Exception e) {
			logger.error("FooterMenuTag: " + e.getMessage(), e);
			throw new JspTagException("FooterMenuTag: " + e.getMessage());
		}
		
		return super.doStartTag();
	}

	public String getReturnUrlPath() {
		return returnUrlPath;
	}

	public void setReturnUrlPath(String returnUrlPath) {
		this.returnUrlPath = returnUrlPath;
	}
}