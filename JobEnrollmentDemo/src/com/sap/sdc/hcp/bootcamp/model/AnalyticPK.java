package com.sap.sdc.hcp.bootcamp.model;

import java.io.Serializable;
import java.lang.String;

/**
 * ID class for entity: Analytic
 *
 */ 
public class AnalyticPK  implements Serializable {   
   
	         
	private String JOBID;         
	private String LOCATION;
	private static final long serialVersionUID = 1L;

	public AnalyticPK() {}

	

	public String getJOBID() {
		return this.JOBID;
	}

	public void setJOBID(String JOBID) {
		this.JOBID = JOBID;
	}
	

	public String getLOCATION() {
		return this.LOCATION;
	}

	public void setLOCATION(String LOCATION) {
		this.LOCATION = LOCATION;
	}
	
   
	/*
	 * @see java.lang.Object#equals(Object)
	 */	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AnalyticPK)) {
			return false;
		}
		AnalyticPK other = (AnalyticPK) o;
		return true
			&& (getJOBID() == null ? other.getJOBID() == null : getJOBID().equals(other.getJOBID()))
			&& (getLOCATION() == null ? other.getLOCATION() == null : getLOCATION().equals(other.getLOCATION()));
	}
	
	/*	 
	 * @see java.lang.Object#hashCode()
	 */	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getJOBID() == null ? 0 : getJOBID().hashCode());
		result = prime * result + (getLOCATION() == null ? 0 : getLOCATION().hashCode());
		return result;
	}
   
   
}
