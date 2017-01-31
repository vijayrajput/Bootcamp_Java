package com.sap.sdc.hcp.bootcamp.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.joda.time.DateTime;

import com.sap.sdc.hcp.bootcamp.model.Job;

/**
 * Servlet implementation class CreateJobs
 */
@WebServlet("/CreateJobs")
public class CreateJobs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateJobs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAdd(request);
		PrintWriter out = response.getWriter();
		out.println("Record Inserted");
	}
	
	private void doAdd(HttpServletRequest request) {
		EntityManager em = null;
		EntityManagerFactory emf = null;
	    String username = request.getRemoteUser();
	    DateTime currentDate = new DateTime();
	    
	    
	    
		 
			try {
				
				InitialContext ctx = new InitialContext();
		        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

		        Map<String, Object> properties = new HashMap<String, Object>();
		        properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
				emf = Persistence.createEntityManagerFactory("JobEnrollmentDemo", properties);
				em = emf.createEntityManager();
				EntityTransaction transaction = em.getTransaction();
				transaction.begin();
				
			Job job1 = new Job();
			job1.setJOBID("JB2031");
			job1.setDEPARTMENT("SAP");
			job1.setCREATED_BY(username);
			DateTime nextWeekDate = currentDate.plusWeeks(1);
			job1.setVALID_TILL(new Timestamp(nextWeekDate.getMillis()));
			job1.setDESCRIPTION("SAP SuccessFactors Consultant");
			
			

			Job job2 = new Job();
			job2.setJOBID("JB2032");
			job2.setCREATED_BY(username);
			job2.setDEPARTMENT("Oracle");
			DateTime next5Days = currentDate.plusDays(5);
			job2.setVALID_TILL(new Timestamp(next5Days.getMillis()));
			job2.setDESCRIPTION("Oracle Apps Consultant");
			
			Job job3 = new Job();
			job3.setJOBID("JB2033");
			job3.setCREATED_BY(username);
			job3.setDEPARTMENT("SAP");
			DateTime next4Days = currentDate.plusDays(4);
			job3.setVALID_TILL(new Timestamp(next4Days.getMillis()));
			job3.setDESCRIPTION("SAP Cloud For Customer Consultant");
			
			Job job4 = new Job();
			job4.setJOBID("JB2034");
			job4.setCREATED_BY(username);
			job4.setDEPARTMENT("IT");
			DateTime next2Days = currentDate.plusDays(2);
			job4.setVALID_TILL(new Timestamp(next2Days.getMillis()));
			job4.setDESCRIPTION("System Administrator");
				//jobs.add(job);
	
			
			
				em.persist(job1);
				em.persist(job2);
				em.persist(job3);
				em.persist(job4);
			transaction.commit();	
			
		
				
				
			}
			catch (NamingException e) {
				System.out.println("Error");
	            //throw new ServletException(e);
	        }
			finally{
				if(em.isOpen())
						em.close();
				if(emf.isOpen())
						emf.close();
			}
}

}
