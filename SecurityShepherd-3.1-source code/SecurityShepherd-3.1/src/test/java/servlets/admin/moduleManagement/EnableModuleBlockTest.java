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
import utils.ModuleBlock;

/**
 * This class just tests the servlet code. The Setter code is better tested in the SetterTest test's / GetModule tests
 * @author Mark Denihan
 *
 */
public class EnableModuleBlockTest
{
	private static org.apache.log4j.Logger log = Logger.getLogger(EnableModuleBlockTest.class);
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
        ModuleBlock.reset();
        if(ModuleBlock.blockerEnabled)
        {
        	String message = "Unable to disable module block";
        	log.fatal(message);
        	fail(message);
        }
	}

	public String doThePost(String csrfToken, String blockedMessage, String moduleId) throws Exception
	{
		try
		{
			int expectedResponseCode = 302;
			String moduleClassName = "EnableModuleBlock";
			log.debug("Creating "+moduleClassName+" Servlet Instance");
			EnableModuleBlock servlet = new EnableModuleBlock();
			servlet.init(new MockServletConfig(moduleClassName));
			request.addParameter("moduleId", moduleId);
			request.addParameter("blockedMessage", blockedMessage);
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
				String moduleId = "853c98bd070fe0d31f1ec8b4f2ada9d7fd1784c5"; //CSRF 7
				String blockedMessage = "Custom Blocked Message";
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost(csrfToken, blockedMessage, moduleId);
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
				String moduleId = "853c98bd070fe0d31f1ec8b4f2ada9d7fd1784c5"; //CSRF 7
				String blockedMessage = "Custom Blocked Message";
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost(csrfToken, blockedMessage, moduleId);
				if(responseBody.contains("Block Enabled"))
				{
					log.debug("The Module Block was Enabled");
					if(ModuleBlock.getBlockerMessage().equalsIgnoreCase(blockedMessage))
					{
						log.debug("Module Block Message is Correctly Set");
						if(ModuleBlock.blockerId.equalsIgnoreCase(moduleId))
							log.debug("Module Block Id is Correctly Set");
						else
						{
							String message = "Module Block was incorrectly set";
							log.fatal(message);
							fail(message);
						}
					}
					else
					{
						String message = "Custom Block Message was not set";
						log.fatal(message);
						fail(message);
					}
				}
				else
				{
					String message = "Admin unable to use close all modules servlet";
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
	public void testWithAdminAuthInvalidModule()
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
				String moduleId = "invalidModuleId";
				String blockedMessage = "Custom Blocked Message";
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost(csrfToken, blockedMessage, moduleId);
				if(responseBody.contains("Invalid data recieved"))
				{
					log.debug("Invalid Data Error Recieved");
				}
				else
				{
					String message = "Module Id Validation Failed";
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
	public void testWithAdminAuthDefaultMessage()
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
				String moduleId = "853c98bd070fe0d31f1ec8b4f2ada9d7fd1784c5"; //CSRF 7
				String blockedMessage = ""; //Empty for Default Message
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost(csrfToken, blockedMessage, moduleId);
				if(responseBody.contains("Block Enabled"))
				{
					log.debug("The Module Block was Enabled");
					if(ModuleBlock.getBlockerMessage().equalsIgnoreCase("Ask your administrator when these modules will be made available"))
					{
						log.debug("Module Block Message is Correctly Set");
						if(ModuleBlock.blockerId.equalsIgnoreCase(moduleId))
							log.debug("Module Block Id is Correctly Set");
						else
						{
							String message = "Module Block was incorrectly set";
							log.fatal(message);
							fail(message);
						}
					}
					else
					{
						String message = "Custom Block Message was not set";
						log.fatal(message);
						fail(message);
					}
				}
				else
				{
					String message = "Admin unable to use close all modules servlet";
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
				String moduleId = "853c98bd070fe0d31f1ec8b4f2ada9d7fd1784c5"; //CSRF 7
				String blockedMessage = "Custom Blocked Message";
				//Add Cookies from Response to outgoing request
				request.setCookies(response.getCookies());
				String responseBody = doThePost("wrongToken", blockedMessage, moduleId);
				if(responseBody.isEmpty())
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
