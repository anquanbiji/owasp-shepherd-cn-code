package servlets.module.lesson;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import testUtils.TestProperties;
import utils.InstallationException;
import dbProcs.GetterTest;
import dbProcs.Setter;

public class TestPoorValidationLesson
{
	private static String lang = "en_GB";
	private static org.apache.log4j.Logger log = Logger.getLogger(TestPoorValidationLesson.class);
	private static String applicationRoot = new String();
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
	public void setup()
	{
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        //Open All modules
        if(!Setter.openAllModules(applicationRoot))
        	fail("Could not Mark All Modules As Open");
	}

	public String getModuleDoPost(String userdata, String csrfToken, int expectedResponseCode) throws Exception
	{
		try
		{
			String servletClassName = "PoorValidationLesson";
			log.debug("Creating " + servletClassName + " Servlet Instance");
			PoorValidationLesson servlet = new PoorValidationLesson();
			servlet.init(new MockServletConfig(servletClassName));

			//Setup Servlet Parameters and Attributes
			log.debug("Setting Up Params and Atrributes");
			request.addParameter("userdata", userdata);
			//Adding Correct CSRF Token (Token Submitted)
			request.addParameter("csrfToken", csrfToken);

			log.debug("Running doPost");
			servlet.doPost(request, response);

			if(response.getStatus() != expectedResponseCode)
				fail(servletClassName + " Servlet Returned " + response.getStatus() + " Code. " + expectedResponseCode + " Expected");
			else
			{
				log.debug("302 OK Detected");
				log.debug(servletClassName + " Successful, returning location retrieved: " + response.getContentAsString());
				return(response.getContentAsString());
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return null;
	}
	
	@Test
	public void testLevelValidAnswer()
	{
		String userName = "lessonTester";
		try
		{
			//Verify User Exists in DB
			GetterTest.verifyTestUser(applicationRoot, userName, userName);
			//Sign in as Normal User
			log.debug("Signing in as " + userName + " Through LoginServlet");
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
				request.setCookies(response.getCookies());
				String submittedUserData = "-1";
				String servletResponse = getModuleDoPost(submittedUserData, csrfToken, 302);
				if(servletResponse.contains("You must be getting funky")) 
				{
					String message = new String("General 'Funky' Error Detected");
					log.fatal(message);
					fail(message);
				}
				else if(!servletResponse.contains("Validation Bypassed"))
				{
					String message = new String("Valid Solution did not yeild Result Key");
					log.fatal(message);
					fail(message);
				}
			}
		}
		catch(Exception e)
		{
			log.fatal("Could not Complete: " + e.toString());
			fail("Could not Complete: " + e.toString());
		}
	}
	
	@Test
	public void testLevelInvalidAnswer()
	{
		String userName = "lessonTester";
		try
		{
			//Verify User Exists in DB
			GetterTest.verifyTestUser(applicationRoot, userName, userName);
			//Sign in as Normal User
			log.debug("Signing in as " + userName + " Through LoginServlet");
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
				request.setCookies(response.getCookies());
				String submittedUserData = "1";
				String servletResponse = getModuleDoPost(submittedUserData, csrfToken, 302);
				if(servletResponse.contains("You must be getting funky")) 
				{
					String message = new String("General 'Funky' Error Detected");
					log.fatal(message);
					fail(message);
				}
				else if(servletResponse.contains("Validation Bypassed"))
				{
					String message = new String("Valid Solution returned Result Key");
					log.fatal(message);
					fail(message);
				} 
				else if(!servletResponse.contains("Valid Number Submitted"))
				{
					String message = new String("Unexpected Lesson Response");
					log.fatal(message);
					fail(message);
				}
			}
		}
		catch(Exception e)
		{
			log.fatal("Could not Complete: " + e.toString());
			fail("Could not Complete: " + e.toString());
		}
	}
	
	
	@Test
	public void testLevelNoAuth()
	{
		try
		{
			request.getSession().setAttribute("lang", lang);
			String csrfToken = "wrong";
			String submittedUserData = "1";
			String servletResponse = getModuleDoPost(submittedUserData, csrfToken, 200); //Mock response is 200 for Unauthenticated response for some reason
			if(servletResponse.contains("You must be getting funky")) 
			{
				String message = new String("General 'Funky' Error Detected");
				log.fatal(message);
				fail(message);
			}
			else if(!servletResponse.isEmpty())
			{
				String message = new String("Did not get 'Unauthenticated' Response");
				log.fatal(message);
				fail(message);
			} 
		}
		catch(Exception e)
		{
			log.fatal("Could not Complete: " + e.toString());
			fail("Could not Complete: " + e.toString());
		}
	}
}
