package com.sap.sdc.hcp.bootcamp.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

public class CmisHelper {

	public Session cmisSession = null;

	// The folder name for the repository of all documents of your application
	static private String REPO_APPFOLDER = "SDC_BOOTCAMP_DOCUMENTS";

	// This is the unique name of that repository
	// REMEMBER: ON THE TRIAL ACCOUNT THE QUOTA FOR DOC SERVICE REPOS IS 3!
	// SO MAKE USE OF THEM WISELY AND REMEMBER THE REPO_NAME!!!
	static private String REPO_NAME = "com.sap.sdc.hcp.bootcamp.myrepository";

	// This is the secret key that your application uses to access the
	// repository.
	// DON'T FORGET THIS KEY!
	static private String REPO_PRIVATEKEY = "thisIsYourSecretKey";

	// Maximum size of a file (in Bytes) that you can upload at a time to your
	// repository
	//static private int MAX_FILE_SIZE = 10000000;

	// The directory to store the uploaded files temporarily
	static private String TEMP_UPLOAD_DIR = "uploads";

	/**
	 * Adds a document to the folder of your application
	 * 
	 * @param file
	 *            the file to be imported
	 */
	public void addDocument(File file, String fileName) throws IOException {

		// create a new file in the root folder
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, fileName);

		// Try to identify the mime type of the file
		String mimeType = URLConnection
				.guessContentTypeFromName(file.getName());
		if (mimeType == null || mimeType.length() == 0) {
			mimeType = "application/octet-stream";
		}

		if (file != null && file.isFile()) {

			InputStream stream = new FileInputStream(file);
			Session session = getRepositorySession();
			/* Check for old pic */
			String query = "SELECT cmis:name, cmis:objectId, cmis:createdBy FROM cmis:document WHERE cmis:name='"
					+ fileName + "'";
			String oldID = null;
			ItemIterable<QueryResult> results = session.query(query, false);
			for (QueryResult result : results) {
				oldID = result.getPropertyValueByQueryName("cmis:objectId");
				break;
			}
			if (oldID != null) {
				deleteDocument(oldID);
			}

			ContentStream contentStream = session.getObjectFactory()
					.createContentStream(file.getName(), file.length(),
							mimeType, stream);
			try {
				Folder yourFolder = getFolderOfYourApp(REPO_APPFOLDER);
				yourFolder.createDocument(properties, contentStream,
						VersioningState.NONE);

			} catch (CmisNameConstraintViolationException e) {
				// Document exists already, nothing to do
			}
			stream.close();
		} else {
			System.out.println("Can't read-in folders, just files!");
		}

	}


	/**
	 * This method deletes a file via it's document id
	 * 
	 * @param documentId
	 *            the document id of the file in the CMIS system
	 */
	public void deleteDocument(String documentId) {
		// Get the session
		Session session = getRepositorySession();
		if (session == null) {
			throw new IllegalArgumentException(
					"Session must be set, but is null!");
		}
		Document doc = getDocumentById(documentId);
		doc.delete(true);
	}

	public String getDocIDByName(String documentName) {
		String id = "";
		Session session = getRepositorySession();
		if (session == null) {
			throw new IllegalArgumentException(
					"Session must be set, but is null!");
		}
		if (documentName == null) {
			return null;
		}
		try {
			String query = "SELECT cmis:name, cmis:objectId, cmis:createdBy FROM cmis:document WHERE cmis:name='"
					+ documentName + "'";

			ItemIterable<QueryResult> results = session.query(query, false);
			for (QueryResult result : results) {
				id = result.getPropertyValueByQueryName("cmis:objectId");
				break;
			}
			return id;

		} catch (CmisObjectNotFoundException onfe) {
			return null;
			// throw new Exception("Document doesn't exist!", onfe);
		} catch (CmisBaseException cbe) {
			// throw new Exception("Could not retrieve the document:" +
			// cbe.getMessage(), cbe);
			return null;
		}

	}

	/**
	 * 
	 * @param documentId
	 *            the document id of the file in the CMIS system
	 * @return The document as a CMIS Document object
	 */
	private Document getDocumentById(String documentId) {
		Session session = getRepositorySession();
		if (session == null) {
			throw new IllegalArgumentException(
					"Session must be set, but is null!");
		}

		if (documentId == null) {
			return null;
		}

		try {
			Document doc = (Document) session.getObject(documentId);
			return doc;
		} catch (CmisObjectNotFoundException onfe) {
			return null;
			// throw new Exception("Document doesn't exist!", onfe);
		} catch (CmisBaseException cbe) {
			// throw new Exception("Could not retrieve the document:" +
			// cbe.getMessage(), cbe);
			return null;
		}
	}

	/**
	 * @param documentId
	 *            the document id of the file in the CMIS system
	 * @return the content of the file
	 */
	private ContentStream getDocumentStreamById(String documentId) {
		ContentStream contentStream = null;
		Document doc = getDocumentById(documentId);
		if (doc != null) {
			contentStream = doc.getContentStream();
		}
		return contentStream;
	}

	/**
	 * Initializes the document service repository
	 * 
	 * @param uniqueName
	 *            the unique name for the repository. Use a unique name with
	 *            package semantics e.g. com.sap.foo.myrepo1
	 * @param secretKey
	 *            the secret key only known to your app.. Should be at least 10
	 *            chars long.
	 * @param folder
	 *            the one folder where all your documents will be stored
	 * @return the corresponding CMIS session
	 */
	private Session getRepositorySession() {

		try {
			try {
				// Only connect to the repository if a session hasn't been
				// opened, yet
				if (cmisSession == null) {
					InitialContext ctx = new InitialContext();
					String lookupName = "java:comp/env/" + "EcmService";
					EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
					cmisSession = ecmSvc.connect(REPO_NAME, REPO_PRIVATEKEY);

				} else {
					return cmisSession;
				}

			} catch (CmisObjectNotFoundException e) {
				// repository does not exist, so try to create it
				RepositoryOptions options = new RepositoryOptions();
				options.setUniqueName(REPO_NAME);
				options.setRepositoryKey(REPO_PRIVATEKEY);
				options.setVisibility(Visibility.PROTECTED);
				InitialContext ctx = new InitialContext();
				String lookupName = "java:comp/env/" + "EcmService";
				EcmService ecmSvc = (EcmService) ctx.lookup(lookupName);
				ecmSvc.createRepository(options);
				// should be created now, so connect to it
				cmisSession = ecmSvc.connect(REPO_NAME, REPO_PRIVATEKEY);
			}
			// String id = openCmisSession.getRepositoryInfo().getId();

			return cmisSession;
		} catch (Exception e) {
			System.out
					.println("Exception found when initializing the database. Error message:\n"
							+ e.getMessage());
			return null;
		}
	}

	/**
	 * Creates a folder. If the folder already exists nothing is done
	 * 
	 * @param session
	 *            the CMIS session
	 * @param rootFolder
	 *            the root folder of your repository
	 * @param folderName
	 *            the name of the folder to be created
	 * @return returns true if the folder has been created and returns false if
	 *         the folder already exists
	 */
	private Folder getFolderOfYourApp(String folderName) {
		Session session = getRepositorySession();
		Folder rootFolder = session.getRootFolder();
		Folder appFolder = null;

		try {
			session.getObjectByPath("/" + folderName);
			appFolder = (Folder) session.getObjectByPath("/" + folderName);

		} catch (CmisObjectNotFoundException e) {
			// Create the folder if it doesn't exist, yet
			Map<String, String> newFolderProps = new HashMap<String, String>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newFolderProps.put(PropertyIds.NAME, folderName);
			appFolder = rootFolder.createFolder(newFolderProps);
		}
		return appFolder;
	}


	/**
	 * Enables the upload of a file from the client computer to the server
	 * 
	 * @param realPathOfApp
	 *            The physical path of the application on the server
	 * @param request
	 *            The HttpServletRquest
	 * @return A file object is stored on the server in the TEMP_UPLOAD_DR
	 */
	public File uploadDocument(String realPathOfApp, HttpServletRequest request) {

		File uploadedFile = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		String uploadPath = realPathOfApp + File.separator + TEMP_UPLOAD_DIR;
		File path = new File(uploadPath);
		// If the path doesn't exist, yet, create it
		if (!path.exists()) {
			path.mkdir();
		}
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<?> items = upload.parseRequest(request);
				Iterator<?> iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (!item.isFormField()) {
						String fileName = item.getName();
						uploadedFile = new File(path + File.separator
								+ fileName);
						item.write(uploadedFile);
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
			 try {
				InputStream inputStream = request.getInputStream();
				String baose64bytes = IOUtils.toString(inputStream);
				String[] base64DataPart = baose64bytes.split(",");
				String base64Image = base64DataPart.length == 1 ? base64DataPart[0] : base64DataPart[1];
						
				byte[] output = DatatypeConverter.parseBase64Binary(base64Image);
			
				uploadedFile = new File(path + File.separator + "temp.jpg");
				 OutputStream outStream = new FileOutputStream(uploadedFile);
				 outStream.write(output);
				
				    
				    IOUtils.closeQuietly(inputStream);
				    IOUtils.closeQuietly(outStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return uploadedFile;
	}

	/**
	 * 
	 * @param response
	 *            The HttpServletResponse
	 * @param docId
	 *            The document id of the document in your repository
	 */
	public void streamOutDocument(HttpServletResponse response, String docId) {

		try {
			ContentStream docStream = getDocumentStreamById(docId);
			if (docStream != null) {
				response.setContentType(docStream.getMimeType());
				IOUtils.copy(docStream.getStream(), response.getOutputStream());
				IOUtils.closeQuietly(docStream.getStream());
			} else {
				// If the document doesn't have any stream return
				// "file-not-found" status code in http responce
				response.setStatus(404);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
