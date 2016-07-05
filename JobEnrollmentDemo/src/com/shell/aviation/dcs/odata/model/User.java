package com.shell.aviation.dcs.odata.model;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmFacets;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmMediaResourceContent;
import org.apache.olingo.odata2.api.annotation.edm.EdmMediaResourceMimeType;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;
import org.apache.olingo.odata2.api.annotation.edm.EdmType;

@EdmEntityType(name = "User")
@EdmEntitySet(name = "UserSet")
public class User {
	@EdmKey
	@EdmProperty(name = "UserId", type = EdmType.STRING, facets = @EdmFacets(nullable = false))
	private String userId;
	
	@EdmProperty(name = "UserFirstName", facets = @EdmFacets(maxLength = 40))
	private String userFirstName;
	
	@EdmProperty(name = "UserLastName", facets = @EdmFacets(maxLength = 40))
	private String userLastName;
	
	@EdmMediaResourceMimeType
	private String imageType;
	
	@EdmMediaResourceContent
	private byte[] profilePicture;
	
}
