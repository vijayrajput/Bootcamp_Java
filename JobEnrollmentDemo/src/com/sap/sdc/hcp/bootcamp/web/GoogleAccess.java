package com.sap.sdc.hcp.bootcamp.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.core.connectivity.api.DestinationException;
import com.sap.core.connectivity.api.DestinationFactory;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
import com.sap.core.connectivity.api.http.HttpDestination;
import com.sap.sdc.hcp.bootcamp.data.google.CalendarListResponseData;
import com.sap.sdc.hcp.bootcamp.data.google.GoogleJsonTokenResponse;

/**
 * Servlet implementation class GoogleAccess
 */
@WebServlet("/GoogleAccess")
public class GoogleAccess extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    private String CLIENT_ID = null;
	private String CLIENT_SECRET = null; 
	static private final String SCOPE = "https://www.googleapis.com/auth/calendar";
	//static private final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	public String REDIRECT_URI = null;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoogleAccess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String auth_code  = request.getParameter("code");
		String state      = request.getParameter("title");
		String eventTitle = request.getParameter("state");
		String host = request.getServerName();
		int port = request.getServerPort();
		if (port == 443)
			host = "https://" + host;
		else
			host = "http://" + host + ":" + port;
		REDIRECT_URI	= host + "/JobEnrollmentDemo/GoogleAccess";
		
		Context ctxInitial = null;
		try {
			ctxInitial = new InitialContext();
			ConnectivityConfiguration configuration = (ConnectivityConfiguration) ctxInitial
					.lookup("java:comp/env/connectivityConfiguration");
			DestinationConfiguration destConfiguration = configuration.getConfiguration("google");
			// get all destination properties
			Map<String, String> allDestinationPropeties = destConfiguration.getAllProperties();
			
			CLIENT_ID = allDestinationPropeties.get("client_id");
			CLIENT_SECRET = allDestinationPropeties.get("client_secret"); 
		}
		catch (NamingException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (auth_code == null && state != null)
		{
			
			String url = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&scope="+SCOPE+"&state="+state;
			
			response.sendRedirect(url);
		}
		
		else if(auth_code != null){
			
			try {
				ctxInitial = new InitialContext();
				DestinationFactory destinationFactory = (DestinationFactory) ctxInitial
						.lookup(DestinationFactory.JNDI_NAME);
				HttpDestination destination = (HttpDestination) destinationFactory.getDestination("google");
				
				
				String sPayload = new String("{\"code\":\""+auth_code+"\",\"client_id\":\"" + CLIENT_ID
						+ "\",\"client_secret\":\"" + CLIENT_SECRET
						+ "\",\"redirect_uri\":\""+REDIRECT_URI+"\",\"grant_type\":\"authorization_code\"}");
				StringEntity inEntity = null;
				inEntity = new StringEntity(sPayload, HTTP.UTF_8);
				inEntity.setContentType("application/json");
				
				
				
				HttpClient httpClient = destination.createHttpClient();
				HttpPost post = new HttpPost("/oauth2/v3/token?code="+URLEncoder.encode(auth_code,HTTP.UTF_8)+"&client_id="+URLEncoder.encode(CLIENT_ID,HTTP.UTF_8)+"&client_secret="+URLEncoder.encode(CLIENT_SECRET,HTTP.UTF_8)+"&redirect_uri="+URLEncoder.encode(REDIRECT_URI,HTTP.UTF_8)+"&grant_type=authorization_code");
				//post.setEntity(inEntity);
				HttpResponse resp = httpClient.execute(post);
				HttpEntity outEntity = resp.getEntity();
				InputStream is = outEntity.getContent();
				BufferedInputStream in = new BufferedInputStream(is);
				Gson gson = new GsonBuilder().create();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
				GoogleJsonTokenResponse resptoken = gson.fromJson(br, GoogleJsonTokenResponse.class);
				EntityUtils.consume(outEntity);

				br.close();
				in.close();
				is.close();
				
				String calendarURL = new String("/calendar/v3/users/me/calendarList?minAccessRole=owner&access_token="+URLEncoder.encode(resptoken.getAccess_token(),HTTP.UTF_8));
				HttpGet get = new HttpGet(calendarURL);
				HttpResponse respget = httpClient.execute(get);
				HttpEntity entityGet = respget.getEntity();
				
				InputStream is2 = entityGet.getContent();
				BufferedInputStream in2 = new BufferedInputStream(is2);
				Gson gson2 = new GsonBuilder().create();
				BufferedReader br2 = new BufferedReader(new InputStreamReader(in2, "UTF-8"));
				CalendarListResponseData calList  = gson2.fromJson(br2, CalendarListResponseData.class);
				if(calList.getItems().size() > 0) {
				String CalId = calList.getItems().get(0).getId();
				//String text = new String("Meeting Schedule from BootCamp Application");			
				String postURL = new String ("/calendar/v3/calendars/"+CalId+"/events/quickAdd?text="+URLEncoder.encode(eventTitle,HTTP.UTF_8)+"&sendNotifications=true&access_token="+URLEncoder.encode(resptoken.getAccess_token(),HTTP.UTF_8));
				HttpPost post2 = new HttpPost(postURL);
				HttpResponse resp2 = httpClient.execute(post2);
				
				
				response.getWriter().append("Served at: ").append( Integer.toString(resp2.getStatusLine().getStatusCode()));
				}
				}
				catch (NamingException | DestinationException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}    
	}

}