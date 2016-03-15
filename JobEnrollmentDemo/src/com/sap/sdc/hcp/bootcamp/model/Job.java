package com.sap.sdc.hcp.bootcamp.model;

import java.io.Serializable;
import java.lang.String;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Entity implementation class for Entity: Job
 *
 */
@Entity
@Table(name="\"SYSTEM\".\"bootcamp.DB::JobModel.Job\"")

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
	@Column(name = "\"SYSTEM.CREATED_ON\"") 
	private Timestamp CREATED_ON; 
	@Column(name = "\"SYSTEM.CREATED_BY\"", nullable = true, length = 10) 
	private String CREATED_BY; 
	
	@Transient
	private int remainingTime;
	@Transient
	private String remainingTimeUnit;
	
	@OneToMany(mappedBy="job") 
	private List<Enrollment> enrollment;
	@OneToMany(mappedBy="job") 
	private List<Analytic> analytic;
	

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
	public List<Enrollment> getEnrollment() {
		return enrollment;
	}
	public void setEnrollment(List<Enrollment> enrollment) {
		this.enrollment = enrollment;
	}
	public List<Analytic> getAnalytic() {
		return analytic;
	}
	public void setAnalytic(List<Analytic> analytic) {
		this.analytic = analytic;
	}
	
	public void addEnrollment (Enrollment param){
		if (this.enrollment == null)
		{
			this.enrollment = new ArrayList<Enrollment>();
		}
		this.enrollment.add(param);
	}
	
	@PrePersist
	private void onInsert() {
		DateTime currentDate = new DateTime();
		setCREATED_ON(new Timestamp(currentDate.getMillis()));
	}
	public int getRemainingTime() {
		DateTime currentDate = new DateTime();
		int days = 0;
		Duration duration = null;
		try{
			duration = new Duration(currentDate,new DateTime(this.getVALID_TILL().getTime()));
		}catch(Exception ex)
		{
			return days;
		}
		days = (int) duration.getStandardDays();
		return days;
	}
	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}
	public String getRemainingTimeUnit() {
		String unit = null; 
		int day = getRemainingTime();
		if(day > 1)
		{
			unit = new String("Days");
		}
		else if (day > 0)
		{
			unit = new String("Day");
		}
		else
		{
			unit = new String("Expired");
		}
		return unit;
	}
	public void setRemainingTimeUnit(String remainingTimeUnit) {
		this.remainingTimeUnit = remainingTimeUnit;
	}
	
	
	
   
}
