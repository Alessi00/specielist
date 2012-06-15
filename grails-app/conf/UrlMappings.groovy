class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

//        "/species/$guid"{
//            controller = "species"
//            action = "show"
//        }
        "/species/$guid"(controller: "species", action: "show")
        "/image-search/showSpecies"(controller: "species", action: "imageSearch")
        "/image-search/infoBox"(controller: "species", action: "infoBox")
        "/logout"(controller: "species", action: "logout")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
