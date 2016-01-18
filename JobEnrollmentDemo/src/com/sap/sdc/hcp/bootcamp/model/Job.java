package com.sap.sdc.hcp.bootcamp.model;

import java.io.Serializable;
import java.lang.String;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Job
 *
 */
@Entity
@Table(name="\"i044067trial.bootcamphana.DB::JOB\"")

public class Job implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id 
	@Column(name = "\"JOBID\"", nullable = false, length = 10) 
	private String JOBID; 
	@Column(name = "\"DESCRIPTION\"", nullable = false, length = 100) 
	private String DESCRIPTION; 
	@Column(name = "\"DEPARTMENT\"", nullable = false, length = 20) 
	private String DEPARTMENT; 
	@Column(name = "\"VALID_TILL\"") 
	private Timestamp VALID_TILL; 
	@Column(name = "\"CREATED_ON\"") 
	private Timestamp CREATED_ON; 
	@Column(name = "\"CREATED_BY\"", nullable = true, length = 10) 
	private String CREATED_BY; 
	

	public Job() {
		super();
	}   
	public String getJOBID() {
		return this.JOBID;
	}

	public void setJOBID(String JOBID) {
		this.JOBID = JOBID;
	}   
	public String getDESCRIPTION() {
		return this.DESCRIPTION;
	}

	public void setDESCRIPTION(String DESCRIPTION) {
		this.DESCRIPTION = DESCRIPTION;
	}   
	public String getDEPARTMENT() {
		return this.DEPARTMENT;
	}

	public void setDEPARTMENT(String DEPARTMENT) {
		this.DEPARTMENT = DEPARTMENT;
	}   
	public Timestamp getVALID_TILL() {
		return this.VALID_TILL;
	}

	public void setVALID_TILL(Timestamp VALID_TILL) {
		this.VALID_TILL = VALID_TILL;
	}
	public Timestamp getCREATED_ON() {
		return CREATED_ON;
	}
	public void setCREATED_ON(Timestamp cREATED_ON) {
		CREATED_ON = cREATED_ON;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
   
}
