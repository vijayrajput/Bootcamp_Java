package com.sap.sdc.hcp.bootcamp.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.sap.sdc.hcp.bootcamp.data.UserDetails;
import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

/**
 * Servlet implementation class GetUserAttributes
 */
@WebServlet("/GetUserAttributes")
public class GetUserAttributes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserAttributes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getUserPrincipal() != null) {
			  try {
			    // UserProvider provides access to the user storage
			    UserProvider users = UserManagementAccessor.getUserProvider();

			    // Read the currently logged in user from the user storage
			    User user = users.getUser(request.getUserPrincipal().getName());

			    
			    UserDetails ud = new UserDetails();
			    ud.setName(request.getUserPrincipal().getName());
			    ud.setFirstName(user.getAttribute("firstname"));
			    ud.setLastname(user.getAttribute("lastname"));
			    ud.setEmail(user.getAttribute("email"));
			    if(user.getAttribute("country") != null )
			    	ud.setCountry(user.getAttribute("country"));
			    else
			    	ud.setCountry("IN");
			    
		
			    Gson gson = new Gson();

				// convert java object to JSON format,
				// and returned as JSON formatted string
				String json = gson.toJson(ud);
			    response.getWriter().println(json);
	
			    
			  } catch (Exception e) {
			    // Handle errors
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