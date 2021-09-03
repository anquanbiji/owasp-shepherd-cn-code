package servlets.admin.config;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import testUtils.TestProperties;
import utils.FeedbackStatus;
import utils.InstallationException;

public class EnableFeedbackTest
{
	private static org.apache.log4j.Logger log = Logger.getLogger(EnableCheats.class);
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
        //Disable Feedback before Each test
        FeedbackStatus.setDisabled();
        if(FeedbackStatus.isEnabled())
        {
        	log.fatal("Unable to Disable Feedback");
        	fail("Unable to Disable Feedback");
        }
	}

	/**
	 * Method to Simulate the interaction with the enableFeedback servlet.
	 * @param csrfToken The CSRF Token of the User
	 * @return The Content of the Response (Which is supposed to be the location of the module)
	 * @throws Exception
	 */
	public String doThePost(String csrfToken) throws Exception
	{
		String servletClassName = "EnableFeedback";
		try
		{
			int expectedResponseCode = 302;

			log.debug("Creating " + servletClassName + " Servlet Instance");
			EnableFeedback servlet = new EnableFeedback();
			servlet.init(new MockServletConfig(servletClassName));

			//Adding Correct CSRF Token (Token Submitted)
			request.addParameter("csrfToken", csrfToken);

			log.debug("Running doPost");
			servlet.doPost(request, response);

			if(response.getStatus() != expectedResponseCode)
				fail(servletClassName + " Servlet Returned " + response.getStatus() + " Code. " + expectedResponseCode + " Expected");
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
	 * This test checks that non admin users get access errors when enabling the feedback
	 */
	@Test
	public void testUserEnableFeedbackCall()
	{
		String userName = "configUserTester";
		String password = userName;
		//Verify / Create user in DB
		try
		{
			TestProperties.verifyTestUser(log, applicationRoot, userName, password);
			//Sign in as Normal User
			log.debug("Signing in as User Through LoginServlet");
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
				String responseBody = doThePost(csrfToken);
				if(responseBody.contains("try non administrator functions"))
				{
					log.debug("No Admin Access Expected Result Recieved");
					if(FeedbackStatus.isEnabled())
					{
						String message = "Feedback enabled in what should have been a failed request";
						log.fatal(message);
						fail(message);
					}
				}
				else
				{
					String message = "Normal user did not get error when performing admin function";
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

	/**
	 * This test checks that admin users can enable the feedback
	 */
	@Test
	public void testAdminEnableFeedbackCall()
	{
		String userName = "configAdminTester";
		String password = userName;
		//Verify / Create user in DB
		try
		{
			TestProperties.verifyTestAdmin(log, applicationRoot, userName, password);
			//Sign in as Admin User
			log.debug("Signing in as User Through LoginServlet");
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
				String responseBody = doThePost(csrfToken);
				if(responseBody.contains("Feedback Enabled"))
				{
					log.debug("Feedback Enabled");
					if(FeedbackStatus.isDisabled())
					{
						String message = "Feedback was not actually Enabled for players";
						log.fatal(message);
						fail(message);
					}
				}
				else
				{
					String message = "Admin was unable to enable Feedback";
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
			log.debug("Signing in as User Through LoginServlet");
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
				String responseBody = doThePost("wrongToken");
				if(responseBody.contains("Enable Feedback Failure"))
				{
					log.debug("CSRF Error Occurred");
					if(FeedbackStatus.isEnabled())
					{
						String message = "Feedback was enabled after what should have been a failed request";
						log.fatal(message);
						fail(message);
					}
				}
				else
				{
					String message = "No CSRF Error Detected";
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
