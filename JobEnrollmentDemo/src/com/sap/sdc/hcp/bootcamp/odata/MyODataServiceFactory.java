package com.sap.sdc.hcp.bootcamp.odata;

import org.apache.olingo.odata2.annotation.processor.api.AnnotationServiceFactory;
import org.apache.olingo.odata2.annotation.processor.core.ListsProcessor;
import org.apache.olingo.odata2.annotation.processor.core.datasource.AnnotationInMemoryDs;
import org.apache.olingo.odata2.annotation.processor.core.datasource.AnnotationValueAccess;
import org.apache.olingo.odata2.annotation.processor.core.datasource.DataSource;
import org.apache.olingo.odata2.annotation.processor.core.datasource.ValueAccess;
import org.apache.olingo.odata2.annotation.processor.core.edm.AnnotationEdmProvider;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.rt.RuntimeDelegate;

public class MyODataServiceFactory extends ODataServiceFactory {
	final static String MODEL_PACKAGE = "com.sap.sdc.hcp.bootcamp.odata.model";
	//final static String MODEL_PACKAGE = "org.apache.olingo.odata2.annotation.processor.ref.model";

	@Override
	public ODataService createService(ODataContext arg0) throws ODataException {
		// TODO Auto-generated method stub
		//return AnnotationInstances.ANNOTATION_ODATA_SERVICE;
		 EdmProvider edmProvider = new AnnotationEdmProvider(MODEL_PACKAGE);
		 DataSource dataSource = new OdataDataSource();
		// DataSource dataSource = new AnnotationInMemoryDs(MODEL_PACKAGE);
		 ValueAccess valueAccess = new AnnotationValueAccess();

		    // Edm via Annotations and ListProcessor via AnnotationDS with AnnotationsValueAccess
		 return RuntimeDelegate.createODataSingleProcessorService(edmProvider,
		        new CustomListsProcessor(dataSource, valueAccess));
	}

}
