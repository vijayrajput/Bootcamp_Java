package com.sap.sdc.hcp.bootcamp.odata;

import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.callback.TombstoneCallback;
import org.apache.olingo.odata2.api.ep.callback.TombstoneCallbackResult;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.eclipse.persistence.jpa.jpql.parser.EncapsulatedIdentificationVariableExpression;

public class TombstomeCallbackImpl implements TombstoneCallback {
	  private static final String DELTA_TOKEN_STRING = "?!deltatoken=";
	  private String baseUri;
	  private String deltaTokenValue;
	 private GetEntitySetUriInfo resultsView;
	  private EdmEntityType entityType;
	  private  ODataContext context;
	  
	  
	public TombstomeCallbackImpl(String baseUri, String deltaTokenValue, GetEntitySetUriInfo resultsView) {
		
		this.baseUri = baseUri;
		this.deltaTokenValue = deltaTokenValue;
		this.resultsView = resultsView;
	}
	
	


	/*public TombstomeCallbackImpl(EdmEntityType entityType, ODataContext context) {
		
		this.entityType = entityType;
		this.context = context;
	}*/




	@Override
	public TombstoneCallbackResult getTombstoneCallbackResult() {
		// TODO Auto-generated method stub
		
		TombstoneCallbackResult annotationTombstoneCallbackResult = new TombstoneCallbackResult();
		annotationTombstoneCallbackResult.setDeltaLink(buildToken());
		return annotationTombstoneCallbackResult;
	}
	
	private String buildToken() {
	    
		
		StringBuilder tokenBuilder = new StringBuilder();
	    if (baseUri != null) {
	      tokenBuilder.append(baseUri);
	    }
	    try {
	      if (resultsView != null) {
	        tokenBuilder.append(resultsView.getStartEntitySet().getName());
	      }
	    } catch (EdmException e) {
	      // Nothing
	    }
	    tokenBuilder.append(DELTA_TOKEN_STRING);
	    if (deltaTokenValue != null) {
	      tokenBuilder.append(deltaTokenValue);
	    }
	    return tokenBuilder.toString();
	  }

}
