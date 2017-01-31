package com.sap.sdc.hcp.bootcamp.odata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmSchemaView;
import org.apache.olingo.odata2.jpa.processor.core.model.JPAEdmMappingImpl;

public class JobEnrollmentProcessingExtension implements JPAEdmExtension {

	@Override
	public void extendWithOperation(JPAEdmSchemaView view) {
		 

	}

	@Override
	public void extendJPAEdmSchema(JPAEdmSchemaView view) {
		Schema edmSchema = view.getEdmSchema();
		int count = 0;
		
		for (EntityType entityType : edmSchema.getEntityTypes()) {
			// Job Entity Change
			if (entityType.getName().equals("Job")) {
				List<Property> jobProperties = entityType.getProperties();
				List<Property> filteredJobPrpperties = new ArrayList<Property>();

				for (Property prop : jobProperties) {
					switch (prop.getName()) {

					case "JOBID":
					case "DEPARTMENT":
					case "DESCRIPTION":
					case "VALID_TILL":
						filteredJobPrpperties.add(prop);
						break;
					default:
						break;
					}
				}
				
				
				Facets facets = new Facets();
			    facets.setNullable(true);
			    SimpleProperty property1 = new SimpleProperty();
			    property1.setName("RemainingTime");
			    property1.setType(EdmSimpleTypeKind.Int16);
			    JPAEdmMappingImpl mapImpl1 = new JPAEdmMappingImpl();
			    mapImpl1.setInternalName("RemainingTime");
			    property1.setMapping(mapImpl1);
			    property1.setFacets(facets);
			    
			    SimpleProperty property2 = new SimpleProperty();
			    property2.setName("RemainingTimeUnit");
			    property2.setType(EdmSimpleTypeKind.String);
			    JPAEdmMappingImpl mapImpl2 = new JPAEdmMappingImpl();
			    mapImpl2.setInternalName("RemainingTimeUnit");
			    property2.setMapping(mapImpl2);
			    property2.setFacets(facets);
			    
			    filteredJobPrpperties.add(property1);
			    filteredJobPrpperties.add(property2);
				
				edmSchema.getEntityTypes().get(count).setProperties(filteredJobPrpperties);
				
			}
			count ++;
		}

	}


	@Override
	public InputStream getJPAEdmMappingModelStream() {
		// TODO Auto-generated method stub
		return null;
	}

}
