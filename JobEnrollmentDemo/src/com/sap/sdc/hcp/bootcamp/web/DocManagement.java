package com.sap.sdc.hcp.bootcamp.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.sap.sdc.hcp.bootcamp.document.*;


/**
 * Servlet implementation class DocManagement
 */
@WebServlet("/DocManagement")
public class DocManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocManagement() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String docName = request.getParameter("empid");
		String type = request.getParameter("type");
     	CmisHelper cmisHelper = retrieveCmisHelperClass(request);
		String docId = cmisHelper.getDocIDByName(type.concat(docName));
		cmisHelper.streamOutDocument(response, docId);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Use a unique name with package semantics e.g. com.foo.MyRepository
		String emId = request.getParameter("empid");
		String type = request.getParameter("type");
	
		String picid = type.concat(emId);

		String realPathOfApp = getServletContext().getRealPath("");
		CmisHelper cmis = retrieveCmisHelperClass(request);
		File file = cmis.uploadDocument(realPathOfApp, request);
		cmis.addDocument(file,picid);
		file.delete();
	}


	private CmisHelper retrieveCmisHelperClass(HttpServletRequest request) {
		CmisHelper result = null;
		if (request != null) {
			HttpSession httpSession = request.getSession();
			if (httpSession != null) {
				CmisHelper cmisHelperHttpSession = (CmisHelper) httpSession
						.getAttribute("myCmisHelper");
				// If an instance of CmisHelper is already there, use it
				if (cmisHelperHttpSession != null) {
					result = cmisHelperHttpSession;
				}
				// If there isn't one, create a new one and store it in the
				// session of the HttpServletRequest so that it
				// can be re-used the next time
				else {
					result = new CmisHelper();
					httpSession.setAttribute("myCmisHelper", result);
				}
			}
		}
		return result;
	}
	

}