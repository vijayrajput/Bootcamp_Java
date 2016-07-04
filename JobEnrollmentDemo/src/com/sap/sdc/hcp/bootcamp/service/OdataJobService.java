package com.sap.sdc.hcp.bootcamp.service;

import java.util.List;

import com.sap.sdc.hcp.bootcamp.odata.model.Ojob;

public interface OdataJobService {
	public List<Ojob> getJobEntitySet ();
	public Ojob getJobEntity(String Id);

}
