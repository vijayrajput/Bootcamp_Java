package com.sap.sdc.hcp.bootcamp.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.sdc.hcp.bootcamp.model.Enrollment;
import com.sap.sdc.hcp.bootcamp.model.EnrollmentPK;

/**
 * Servlet implementation class MailServlet
 */
@WebServlet("/MailServlet")
public class MailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "mail/destmail")
    private Session mailSession;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Search enrollment entry from database:
	    
        Transport transport = null;
        EntityManager em = null;
		 EntityManagerFactory emf = null;
		 String pid = request.getParameter("personid");
		 String jid = request.getParameter("jobid");
        try {
        	InitialContext ctx = new InitialContext();
	        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

	        Map<String, Object> properties = new HashMap<String, Object>();
	        properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
			emf = Persistence.createEntityManagerFactory("JobEnrollmentDemo", properties);
			em = emf.createEntityManager();
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			
		//	Person person = em.find(Person.class, pid);
			EnrollmentPK epk = new EnrollmentPK();
			epk.setJOBID(jid);
			epk.setPERSONID(pid);
			
			Enrollment enrollment = em.find(Enrollment.class,epk);
			    
			String to = enrollment.getEMAIL();
			String tofname= enrollment.getFIRST_NAME();
			String tolname= enrollment.getLAST_NAME();
            // Parse form parameters
            String from = "hcpdemotest@gmail.com"; //request.getParameter("fromaddress");
           // String to = to
            String subjectText = "Interview for Job: "+jid;
            String mailText = "Hi "+ tofname+" "+ tolname+",\n"+"You have been shortlisted for interview for Job"+" "+jid;
            if (from.isEmpty() || to.isEmpty()) {
                throw new RuntimeException("Form parameters From and To may not be empty!");
            }

            // Construct message from parameters
            MimeMessage mimeMessage = new MimeMessage(mailSession);
            InternetAddress[] fromAddress = InternetAddress.parse(from);
            InternetAddress[] toAddresses = InternetAddress.parse(to);
            mimeMessage.setFrom(fromAddress[0]);
            mimeMessage.setRecipients(RecipientType.TO, toAddresses);
            mimeMessage.setSubject(subjectText, "UTF-8");
            MimeMultipart multiPart = new MimeMultipart("alternative");
            MimeBodyPart part = new MimeBodyPart();
            part.setText(mailText, "utf-8", "plain");
            multiPart.addBodyPart(part);
            mimeMessage.setContent(multiPart);

            // Send mail
            transport = mailSession.getTransport();
            transport.connect();
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

            // Confirm mail sending
            response.getWriter().println(
                    "E-mail was sent (in local scenario stored in '<local-server>/work/mailservice'"
                            + " - in cloud scenario using configured mail session).");
        } catch (Exception e) {
            LOGGER.error("Mail operation failed", e);
            throw new ServletException(e);
        } 
        
        finally {
            // Close transport layer
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    throw new ServletException(e);
                }
            }
        }
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}