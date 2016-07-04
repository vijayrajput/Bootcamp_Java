package com.sap.sdc.hcp.bootcamp.odata;

import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.annotation.processor.core.datasource.DataSource;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmFunctionImport;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;

import com.sap.sdc.hcp.bootcamp.odata.model.Ojob;
import com.sap.sdc.hcp.bootcamp.service.OdataJobService;
import com.sap.sdc.hcp.bootcamp.service.OdataJobServiceImpl;

public class OdataDataSource implements DataSource {
	OdataJobService jobService;
	
	public OdataDataSource() {
		jobService = new OdataJobServiceImpl();
		
	}
	@Override
	public List<?> readData(EdmEntitySet entitySet)
			throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub
		if(entitySet.getName().equals("OjobSet"))
		{
			return jobService.getJobEntitySet();
		}
		return null;
	}

	@Override
	public Object readData(EdmEntitySet entitySet, Map<String, Object> keys)
			throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub
		if(entitySet.getName().equals("OjobSet"))
		{
			return jobService.getJobEntity("JB2032");
		}
		return null;
	}

	@Override
	public Object readData(EdmFunctionImport function, Map<String, Object> parameters, Map<String, Object> keys)
			throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object readRelatedData(EdmEntitySet sourceEntitySet, Object sourceData, EdmEntitySet targetEntitySet,
			Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException,
					ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BinaryData readBinaryData(EdmEntitySet entitySet, Object mediaLinkEntryData)
			throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object newDataObject(EdmEntitySet entitySet)
			throws ODataNotImplementedException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeBinaryData(EdmEntitySet entitySet, Object mediaLinkEntryData, BinaryData binaryData)
			throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteData(EdmEntitySet entitySet, Map<String, Object> keys)
			throws ODataNotImplementedException, ODataNotFoundException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createData(EdmEntitySet entitySet, Object data)
			throws ODataNotImplementedException, EdmException, ODataApplicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRelation(EdmEntitySet sourceEntitySet, Object sourceData, EdmEntitySet targetEntitySet,
			Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException,
					ODataApplicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeRelation(EdmEntitySet sourceEntitySet, Object sourceData, EdmEntitySet targetEntitySet,
			Map<String, Object> targetKeys) throws ODataNotImplementedException, ODataNotFoundException, EdmException,
					ODataApplicationException {
		// TODO Auto-generated method stub

	}

}
