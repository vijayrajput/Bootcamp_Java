package com.sap.sdc.hcp.bootcamp.odata;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.model.JPAEdmExtension;
import org.eclipse.persistence.config.PersistenceUnitProperties;

public class ApplicationODataJPAServiceFactory extends ODataJPAServiceFactory {

	public static final String PUNIT_NAME = "JobEnrollmentDemo";
	private DataSource ds;

	@Override
	public ODataJPAContext initializeODataJPAContext()
			throws ODataJPARuntimeException {
		// TODO Auto-generated method stub
		ODataJPAContext oDataJPAContext = getODataJPAContext();
		try {
		
		InitialContext ctx = new InitialContext();
        ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
		oDataJPAContext.setEntityManagerFactory(Persistence
				.createEntityManagerFactory(PUNIT_NAME, properties));
		oDataJPAContext.setPersistenceUnitName(PUNIT_NAME);
		oDataJPAContext.setJPAEdmExtension((JPAEdmExtension) new JobEnrollmentProcessingExtension());
		
		}
		catch (NamingException e) {
            //throw new ServletException(e);
        }

		return oDataJPAContext;
	}

}