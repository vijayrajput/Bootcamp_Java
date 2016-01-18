package com.sap.sdc.hcp.bootcamp.model;

import java.io.Serializable;
import java.lang.String;

/**
 * ID class for entity: Enrollment
 *
 */ 
public class EnrollmentPK  implements Serializable {   
   
	         
	private String JOBID;         
	private String PERSONID;
	private static final long serialVersionUID = 1L;

	public EnrollmentPK() {}

	

	public String getJOBID() {
		return this.JOBID;
	}

	public void setJOBID(String JOBID) {
		this.JOBID = JOBID;
	}
	

	public String getPERSONID() {
		return this.PERSONID;
	}

	public void setPERSONID(String PERSONID) {
		this.PERSONID = PERSONID;
	}
	
   
	/*
	 * @see java.lang.Object#equals(Object)
	 */	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof EnrollmentPK)) {
			return false;
		}
		EnrollmentPK other = (EnrollmentPK) o;
		return true
			&& (getJOBID() == null ? other.getJOBID() == null : getJOBID().equals(other.getJOBID()))
			&& (getPERSONID() == null ? other.getPERSONID() == null : getPERSONID().equals(other.getPERSONID()));
	}
	
	/*	 
	 * @see java.lang.Object#hashCode()
	 */	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getJOBID() == null ? 0 : getJOBID().hashCode());
		result = prime * result + (getPERSONID() == null ? 0 : getPERSONID().hashCode());
		return result;
	}
   
   
}
