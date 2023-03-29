/*
 * Copyright (C) 2022 Atlas of Living Australia
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
 */

package au.org.ala.specieslist

import au.org.ala.web.AuthService
import org.apache.http.HttpStatus


class WebServiceInterceptor {

    LocalAuthService localAuthService
    AuthService authService

    WebServiceInterceptor() {
        match(controller: 'webService', action: /(getListDetails|getListItemDetails|queryListItemOrKVP|getSpeciesListItemKvp|speciesListItemKvp|saveList|markAsPublished|getTaxaOnList)/)
    }

    boolean before() {
        //ensure that the supplied druid is valid
        log.debug("Prevalidating...")
        if (params.druid) {
            def list = SpeciesList.findByDataResourceUid(params.druid)
            if (list) {
                params.splist = list
            } else {
                response.sendError(HttpStatus.SC_NOT_FOUND, "Unable to locate species list ${params.druid}")
                return false
            }
        }

        // view permissions
        if (actionName == 'saveList' || actionName == 'markAsPublished') {
            return checkEditSecurity(params.druid, authService, localAuthService)
        } else {
            return checkViewSecurity(params.druid, authService, localAuthService)
        }
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }

    // The auth and localAuth services need to be passed in in order to use the same instance that the filters
    // closure has - this is an issue when unit testing because the closure gets the mock services, but this method
    // gets the 'real' injected services unless we pass them in
    private boolean checkViewSecurity(String druid, AuthService authService, LocalAuthService localAuthService) {
        SecurityUtil securityUtil = new SecurityUtil(localAuthService: localAuthService, authService: authService)

        if (!securityUtil.checkViewAccess(druid)) {
            response.sendError(HttpStatus.SC_UNAUTHORIZED, "Not authorised")
            false
        } else {
            true
        }
    }

    // The auth and localAuth services need to be passed in in order to use the same instance that the filters
    // closure has - this is an issue when unit testing because the closure gets the mock services, but this method
    // gets the 'real' injected services unless we pass them in
    private boolean checkEditSecurity(String druid, AuthService authService, LocalAuthService localAuthService) {
        SecurityUtil securityUtil = new SecurityUtil(localAuthService: localAuthService, authService: authService)

        if (!securityUtil.checkEditAccess(druid)) {
            response.sendError(HttpStatus.SC_UNAUTHORIZED, "Not authorised")
            false
        } else {
            true
        }
    }
}
