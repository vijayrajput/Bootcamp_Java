package com.sap.sdc.hcp.bootcamp.odata.model;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;

@EdmEntityType
@EdmEntitySet
public class Ojob {
	@EdmKey
	@EdmProperty
	private String JobID; 
	@EdmProperty
	private String Description;
	@EdmProperty
	private String Deparment;
	@EdmProperty
	private Integer DaysLeft;
	public String getJobID() {
		return JobID;
	}
	public void setJobID(String jobID) {
		JobID = jobID;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDeparment() {
		return Deparment;
	}
	public void setDeparment(String deparment) {
		Deparment = deparment;
	}
	public Integer getDaysLeft() {
		return DaysLeft;
	}
	public void setDaysLeft(Integer daysLeft) {
		DaysLeft = daysLeft;
	} 
	
	
	 
}
