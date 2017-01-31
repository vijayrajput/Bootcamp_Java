package com.sap.sdc.hcp.bootcamp.model;

import java.io.Serializable;
import java.lang.String;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.joda.time.DateTime;

/**
 * Entity implementation class for Entity: Enrollment
 *
 */
@Entity
@Table(name = "\"bootcamp.DB::JobModel.Enrollment\"")

@IdClass(EnrollmentPK.class)
public class Enrollment implements Serializable {

	@Id
	@Column(name = "\"JOBID\"", nullable = false, length = 10)
	private String JOBID;
	@Id
	@Column(name = "\"PERSONID\"", nullable = false, length = 10)
	private String PERSONID;
	@Column(name = "\"FIRST_NAME\"", nullable = false, length = 50)
	private String FIRST_NAME;
	@Column(name = "\"LAST_NAME\"", nullable = true, length = 50)
	private String LAST_NAME;
	@Column(name = "\"EMAIL\"", nullable = false, length = 50)
	private String EMAIL;
	@Column(name = "\"LOCATION\"", nullable = true, length = 50)
	private String LOCATION;
	@Column(name = "\"SYSTEM.CREATED_ON\"")
	private Timestamp CREATED_ON;
	@Column(name = "\"SYSTEM.CREATED_BY\"", length = 10)
	private String CREATED_BY;

	@ManyToOne
	private Job job;

	private static final long serialVersionUID = 1L;

	public Enrollment() {
		super();
	}

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

	public String getFIRST_NAME() {
		return this.FIRST_NAME;
	}

	public void setFIRST_NAME(String FIRST_NAME) {
		this.FIRST_NAME = FIRST_NAME;
	}

	public String getLAST_NAME() {
		return LAST_NAME;
	}

	public void setLAST_NAME(String lAST_NAME) {
		LAST_NAME = lAST_NAME;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getLOCATION() {
		return LOCATION;
	}

	public void setLOCATION(String lOCATION) {
		LOCATION = lOCATION;
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

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@PrePersist
	private void onInsert() {
		DateTime currentDate = new DateTime();
		setCREATED_ON(new Timestamp(currentDate.getMillis()));
		if (getJob() == null) {
			EntityManager em = null;
			EntityManagerFactory emf = null;
			try {

				InitialContext ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
				emf = Persistence.createEntityManagerFactory("JobEnrollmentDemo", properties);
				em = emf.createEntityManager();
				Job job = em.find(Job.class, getJOBID());
				if (job != null) {
					setJob(job);
				}
			} catch (NamingException e) {
				// throw new ServletException(e);
			} finally {
				if (em.isOpen())
					em.close();
				if (emf.isOpen())
					emf.close();
			}
		}

	}

}
