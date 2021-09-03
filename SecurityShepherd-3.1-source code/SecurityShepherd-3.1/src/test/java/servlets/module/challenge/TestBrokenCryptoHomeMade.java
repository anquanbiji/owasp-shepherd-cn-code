package servlets.module.challenge;

import static org.junit.Assert.fail;

import java.util.Locale;
import java.util.ResourceBundle;

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

public class TestBrokenCryptoHomeMade
{
	private static String lang = "en_GB";
	private static org.apache.log4j.Logger log = Logger.getLogger(TestBrokenCryptoHomeMade.class);
	private static String applicationRoot = new String();
	private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ResourceBundle bundle = ResourceBundle.getBundle("i18n.servlets.challenges.insecureCryptoStorage.insecureCryptoStorage", new Locale(lang));

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
        BrokenCryptoHomeMade.initLists();
        //Open All modules
        if(!Setter.openAllModules(applicationRoot))
        	fail("Could not Mark All Modules As Open");
	}

	public String moduleDoPost(String theSubmission, String csrfToken, int expectedResponseCode) throws Exception
	{
		try
		{
			String servletClassName = "BrokenCryptoHomeMade";
			log.debug("Creating " + servletClassName + " Servlet Instance");
			BrokenCryptoHomeMade servlet = new BrokenCryptoHomeMade();
			servlet.init(new MockServletConfig(servletClassName));

			//Setup Servlet Parameters and Attributes
			log.debug("Setting Up Params and Atrributes");
			request.addParameter("theSubmission", theSubmission);
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
	
	public String moduleDoGet(String name, String csrfToken, int expectedResponseCode) throws Exception
	{
		try
		{
			String servletClassName = "BrokenCryptoHomeMade";
			log.debug("Creating " + servletClassName + " Servlet Instance");
			BrokenCryptoHomeMade servlet = new BrokenCryptoHomeMade();
			servlet.init(new MockServletConfig(servletClassName));

			//Setup Servlet Parameters and Attributes
			log.debug("Setting Up Params and Atrributes");
			request.addParameter("name", name);
			//Adding Correct CSRF Token (Token Submitted)
			request.addParameter("csrfToken", csrfToken);

			log.debug("Running doPost");
			servlet.doGet(request, response);

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
				String solutionString = BrokenCryptoHomeMade.generateUserSolutionKeyOnly(BrokenCryptoHomeMade.challenges.get(4).get(1), userName); //Just Create the Key From Source And then Send it to the servlet
				String servletResponse = moduleDoPost(solutionString, csrfToken, 302);
				if(servletResponse.contains(bundle.getString("result.failed")))
				{
					String message = new String("Valid Key Returned Funky Error");
					log.fatal(message);
					fail(message);
				}
				else if(!servletResponse.contains(bundle.getString("result.youDidIt"))) 
				{
					String message = new String("Valid Solution did not return Result Key");
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
		String userName = "badLessonTester";
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
				String solutionString = "IncorrectAnswer";
				String servletResponse = moduleDoPost(solutionString, csrfToken, 302);
				if(servletResponse.contains(bundle.getString("result.failed")))
				{
					String message = new String("Valid Key Returned Funky Error");
					log.fatal(message);
					fail(message);
				}
				else if(!servletResponse.contains(bundle.getString("insecureCyrptoStorage.homemade.badanswer.warning"))) 
				{
					String message = new String("Invalid Solution did not return Wrong Answer Response");
					log.fatal(message);
					fail(message);
				}
				else if(!servletResponse.contains(bundle.getString("insecureCyrptoStorage.homemade.badanswer.notLockedOut")))
				{
					String message = new String("Response Did not contain a 'You are not locked out yet' message");
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
	public void testLevelInvalidAnswerLockedOut()
	{
		String userName = "badLessonTester";
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
				String solutionString = "testin";
				request.getSession().setAttribute("homemadebadanswers", 55);
				String servletResponse = moduleDoPost(solutionString, csrfToken, 302);
				if(servletResponse.contains(bundle.getString("result.failed")))
				{
					String message = new String("Valid Key Returned Funky Error");
					log.fatal(message);
					fail(message);
				}
				else if(servletResponse.contains(bundle.getString("insecureCyrptoStorage.homemade.badanswer.notLockedOut")))
				{
					String message = new String("Response contained a 'You are not locked out yet' message when we should be locked out");
					log.fatal(message);
					fail(message);
				}
				else if(!servletResponse.contains(bundle.getString("insecureCyrptoStorage.homemade.badanswer.lockedOut")))
				{
					String message = new String("Response Did not contain a 'You are locked out' message when we should be locked out");
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
	public void testLevelInvalidCsrfToken()
	{
		String userName = "badLessonTester";
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
				String solutionString = "IncorrectAnswer";
				String servletResponse = moduleDoPost(solutionString, "WrongToken", 302);
				if(!servletResponse.isEmpty())
				{
					String message = new String("Expected Blank Response for Bad CSRF Token. Got Data Instead");
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
	public void testLevelCustomName()
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
				String name = "~~~~~";
				String solutionString = BrokenCryptoHomeMade.generateUserSolutionKeyOnly(BrokenCryptoHomeMade.challenges.get(0).get(1), name); //Just Create the That should be in table and search for that
				String servletResponse = moduleDoGet(name, csrfToken, 302);
				if(servletResponse.contains(bundle.getString("result.failed")))
				{
					String message = new String("Valid Key Returned Funky Error");
					log.fatal(message);
					fail(message);
				}
				else if(!servletResponse.contains(solutionString)) 
				{
					String message = new String("Response did not contain expected key: " + solutionString);
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
	public void testLevelCustomNameTooShort()
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
				String name = "123";
				String servletResponse = moduleDoGet(name, csrfToken, 302);
				if(servletResponse.contains(bundle.getString("result.failed")))
				{
					String message = new String("Valid Key Returned Funky Error");
					log.fatal(message);
					fail(message);
				}
				else if(!servletResponse.contains(bundle.getString("insecureCyrptoStorage.homemade.nameTooShort"))) 
				{
					String message = new String("Response did not contain Name Too Short Error");
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
	public void testLevelNoAuthPost()
	{
		try
		{
			request.getSession().setAttribute("lang", lang);
			String servletResponse = moduleDoPost("Anything", "Anything", 200); //Mock response is 200 for Unauthenticated response for some reason
			if(!servletResponse.isEmpty()) 
			{
				String message = new String("Expected Blank Response for Unauthenticated Call. Got Data Back");
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
