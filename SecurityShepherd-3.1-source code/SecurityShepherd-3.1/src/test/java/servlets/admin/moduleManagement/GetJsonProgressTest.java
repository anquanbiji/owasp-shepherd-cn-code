package servlets.admin.moduleManagement;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import testUtils.TestProperties;
import utils.InstallationException;

/**
 * This class just tests the servlet code. The Setter code is better tested in the GetterTest test's
 * @author Mark Denihan
 *
 */
public class GetJsonProgressTest
{
	private static org.apache.log4j.Logger log = Logger.getLogger(GetJsonProgressTest.class);
	private static String applicationRoot = new String();
	private static String lang = "en_GB";
	private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    /**
	 * Creates DB or Restores DB to Factory Defaults before running tests
	 */
	@BeforeClass
	public static void resetDatabase() 
	{
		TestProperties.setTestPropertiesFileDirectory(log);
		try 
		{
			TestProperties.executeSql(log);
		} 
		catch (InstallationException e) 
		{
			String message = new String("Could not create DB: " + e.toString());
			log.fatal(message);
			fail(message);
		}
	}
    
	@Before
	public void setUp()
	{
		request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
	}

	public String doThePost(String csrfToken, String classId) throws Exception
	{
		try
		{
			int expectedResponseCode = 302;
			String moduleClassName = "GetJsonProgress";
			log.debug("Creating " + moduleClassName + " Servlet Instance");
			GetJsonProgress servlet = new GetJsonProgress();
			servlet.init(new MockServletConfig(moduleClassName));
			request.addParameter("classId", classId);
			//Adding Correct CSRF Token (Token Submitted)
			request.addParameter("csrfToken", csrfToken);

			log.debug("Running doPost");
			servlet.doPost(request, response);

			if(response.getStatus() != expectedResponseCode)
				fail(moduleClassName + " Servlet Returned " + response.getStatus() + " Code. " + expectedResponseCode + " Expected");
			else
			{
				log.debug(expectedResponseCode + " Detected");
				log.debug("Servlet Successful, returning response retrieved: " + response.getContentAsString());
				return(response.getContentAsString());
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return null;
	}

	/**
	 *
	 */
	@Test
	public void testWithUserAuth()
	{
		String userName = "configUserTester";
		String password = userName;
		//Verify / Create user in DB
		try
		{
			TestProperties.verifyTestUser(log, applicationRoot, userName, password);
			//Sign in as Normal User
			log.debug("Signing in as Normal User Through LoginServlet");
			TestProperties.loginDoPost(log, request, response, userName, userName, null, lang);
			log.debug("Login Servlet Complete, Getting CSRF Token");
			if(response.getCookie("token") == null)
				fail("No CSRF Token Was Returned from Login Servlet");
			String csrfToken = response.getCookie("token").getValue();
			if(csrfToken.isEmpty())
			{
				String message = new String("No CSRF token returned from Login Servlet");
				log.fatal(message);
				fail(message);
			}
			else
			{
				String classId = "anything";
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost(csrfToken, classId);
				if(responseBody.contains("loggedOutSheep"))
				{
					log.debug("No Admin Access Result Recieved");
				}
				else
				{
					String message = "Did not get authoristion error for User accessing Admin Function";
					log.fatal(message);
					fail(message);
				}
			}
		}
		catch (Exception e)
		{
			log.fatal("Could not Complete: " + e.toString());
			fail("Could not Complete: " + e.toString());
		}
	}

	@Test
	public void testWithAdminAuth()
	{
		String userName = "configAdminTester";
		String password = userName;
		//Verify / Create user in DB
		try
		{
			TestProperties.verifyTestAdmin(log, applicationRoot, userName, password);
			//Sign in as Normal User
			log.debug("Signing in as Admin User Through LoginServlet");
			TestProperties.loginDoPost(log, request, response, userName, userName, null, lang);
			log.debug("Login Servlet Complete, Getting CSRF Token");
			if(response.getCookie("token") == null)
				fail("No CSRF Token Was Returned from Login Servlet");
			String csrfToken = response.getCookie("token").getValue();
			if(csrfToken.isEmpty())
			{
				String message = new String("No CSRF token returned from Login Servlet");
				log.fatal(message);
				fail(message);
			}
			else
			{
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost(csrfToken, "Anything");
				if(responseBody.contains("loggedOutSheep") || responseBody.equalsIgnoreCase("Error Occurred!"))
				{
					String message = "Get JSON Progress Failed";
					log.fatal(message);
					fail(message);
				}
				else
				{
					log.debug("Got Feedback");
				}
			}
		}
		catch (Exception e)
		{
			log.fatal("Could not Complete: " + e.toString());
			fail("Could not Complete: " + e.toString());
		}
	}

	@Test
	public void testCsrf()
	{
		String userName = "configAdminTester";
		String password = userName;
		//Verify / Create user in DB
		try
		{
			TestProperties.verifyTestAdmin(log, applicationRoot, userName, password);
			//Sign in as Admin User
			log.debug("Signing in as Admin Through LoginServlet");
			TestProperties.loginDoPost(log, request, response, userName, userName, null, lang);
			log.debug("Login Servlet Complete, Getting CSRF Token");
			if(response.getCookie("token") == null)
				fail("No CSRF Token Was Returned from Login Servlet");
			String csrfToken = response.getCookie("token").getValue();
			if(csrfToken.isEmpty())
			{
				String message = new String("No CSRF token returned from Login Servlet");
				log.fatal(message);
				fail(message);
			}
			else
			{
				String moduleId = "anything"; //
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost("wrongToken", moduleId);
				if(responseBody.equalsIgnoreCase("Error Occurred!"))
				{
					log.debug("CSRF Error Occurred (Expected Empty Response)");
				}
				else
				{
					String message = "CSRF Error Not Detected with Bad CSRF Token";
					log.fatal(message);
					fail(message);
				}
			}
		}
		catch (Exception e)
		{
			log.fatal("Could not Complete: " + e.toString());
			fail("Exception Caught: " + e.toString());
		}
	}
}
