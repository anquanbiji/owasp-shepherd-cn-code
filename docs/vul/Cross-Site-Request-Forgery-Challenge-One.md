#  Cross Site Request Forgery Challenge One(跨站请求伪造 (CSRF) 1)

## 题介绍
是一个CSRF相关的题，需要另一个用户配合才能完成

## 功能实现 

点击提交消息 获得请求 

```
POST /challenges/s74a796e84e25b854906d88f622170c1c06817e72b526b3d1e9a6085f429cf52 HTTP/1.1


myMessage=http%3A%2F%2Fshepherd-cn.anquanbiji.com%2Fuser%2Fcsrfchallengeone%2Fplusplus%3Fuserid%3Df6a93701ead755aa4fd140215b9ca449ce7f3476&csrfToken=75755217751292205141503195192721769803
```
对应实现代码在  src/main/java/servlets/module/challenge/CsrfChallengeOne.java 中 

```
	String myMessage = request.getParameter("myMessage");
	log.debug("User Submitted - " + myMessage);
	myMessage = Validate.makeValidUrl(myMessage);  //验证提交的消息如果不以http开头则增加http
	
	log.debug("Updating User's Stored Message");
	String ApplicationRoot = getServletContext().getRealPath("");
	String moduleId = Getter.getModuleIdFromHash(ApplicationRoot, levelHash);
	String userId = (String)ses.getAttribute("userStamp");
	Setter.setStoredMessage(ApplicationRoot, myMessage, userId, moduleId);  // 存储用户提交的消息 到数据库core的results表中 URL字段是 resultSubmission
	
	log.debug("Retrieving user's class's forum");
	String classId = null;
	if(ses.getAttribute("userClass") != null)   //判断当前用户是否有userClass session 
		classId = (String)ses.getAttribute("userClass");
	String htmlOutput = Getter.getCsrfForumWithImg(ApplicationRoot, classId, moduleId, csrfGenerics); //没有userClass 则无法继续进行
	
	log.debug("Outputting HTML");
	out.write(htmlOutput);
```
Getter.getCsrfForumWithImg 实现如下 
```
public static String getCsrfForumWithImg (String ApplicationRoot, String classId, String moduleId, ResourceBundle bundle)
{
	log.debug("*** Getter.getCsrfForum ***");
	log.debug("Getting stored messages from class: " + classId);
	String htmlOutput = new String();
	Connection conn = Database.getCoreConnection(ApplicationRoot);
	try
	{
		if(classId != null)
		{   //根据classid和moduleid 查询用户提交的消息  最终SQL  SELECT userName, resultSubmission FROM results JOIN users USING (userId) JOIN class USING (classId) WHERE classId = theClassId AND moduleId = theModuleId;  相当于一个留言板功能 
			CallableStatement callstmt = conn.prepareCall("call resultMessageByClass(?, ?)");
			log.debug("Gathering resultMessageByClass ResultSet");
			callstmt.setString(1, classId);
			callstmt.setString(2, moduleId);
			ResultSet resultSet = callstmt.executeQuery();
			log.debug("resultMessageByClass executed");
			
			//Table Header
			htmlOutput = "<table><tr><th>" + bundle.getString("forum.userName") + "</th><th>" + bundle.getString("forum.image") + "</th></tr>";
			
			log.debug("Opening Result Set from resultMessageByClass");
			int counter = 0;
			while(resultSet.next())
			{
				counter++;
				//用户提交的URL地址，会作为src地址，进行自动请求，达到一个csrf效果 
				htmlOutput += "<tr><td>" + Encode.forHtml(resultSet.getString(1)) + "</td><td><img src=\"" + Encode.forHtmlAttribute(resultSet.getString(2)) + "\"/></td></tr>"; 
			}
			if(counter > 0)
				log.debug("Added a " + counter + " row table");
			else
				log.debug("No results from query");
			//Table end
			htmlOutput += "</table>";
		}
		else
		{
			log.error("User with Null Class detected");  //没有classId 则只会报错
			htmlOutput = "<p><font color='red'>" + bundle.getString("error.noClass") + "</font></p>";
		}
	}
	catch (SQLException e)
	{
		log.error("Could not execute query: " + e.toString());
		htmlOutput = "<p>" + bundle.getString("error.occurred") + "</p>";
	}
	catch (Exception e)
	{
		log.fatal("Could not return CSRF Forum: " + e.toString());
	}
	Database.closeConnection(conn);
	log.debug("*** END getCsrfForum ***");
	return htmlOutput;
}
```
img src为用户提交的地址 /user/csrfchallengeone/plusplus?userid=exampleId，对应实现代码在 src/main/java/servlets/module/challenge/CsrfChallengeTargetOne.java 文件中  
```
	ShepherdLogManager.setRequestIp(request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), ses.getAttribute("userName").toString());
	log.debug(levelName + " servlet accessed by: " + ses.getAttribute("userName").toString());
	String plusId = request.getParameter("userid");
	log.debug("User Submitted - " + plusId);
	String userId = (String)ses.getAttribute("userStamp");
	if(!userId.equals(plusId)) //只要session中的用户id和传入的用户id参数不同，则可以增加别人csrf 计数
	{
		String ApplicationRoot = getServletContext().getRealPath("");
		String userName = (String)ses.getAttribute("userName");
		String attackerName = Getter.getUserName(ApplicationRoot, plusId);
		if(attackerName != null)
		{
			log.debug(userName + " is been CSRF'd by " + attackerName);
			
			log.debug("Attempting to Increment ");
			String moduleHash = CsrfChallengeOne.getLevelHash();
			String moduleId = Getter.getModuleIdFromHash(ApplicationRoot, moduleHash);
			result = Setter.updateCsrfCounter(ApplicationRoot, moduleId, plusId);
		}
		else
		{
			log.error("UserId '" + plusId + "' could not be found.");
		}
	}
```


所以这题的classid是非常关键的，只有用户存在classid才会被攻击，session中的userClass是在用户登录时进行设置的 在文件 src/main/java/servlets/Login.java 中   
```
 String user[] = Getter.authUser(ApplicationRoot, p_login, p_pwd);
if(user != null && !user[0].isEmpty())
{
   
   //Kill Session and Create a new one with user logged in
   log.debug("Creating new session for " + user[2] + " " + user[1]);
   ses.invalidate();
   ses = request.getSession(true);
   ses.setAttribute("userStamp", user[0]);
   ses.setAttribute("userName", user[1]);
   ses.setAttribute("userRole", user[2]);
   ses.setAttribute("lang", language);
   log.debug("userClassId = " + user[4]);
   
   ses.setAttribute("userClass", user[4]);  //设置的userClass 
   log.debug("Setting CSRF cookie");
   Cookie token = new Cookie("token", Hash.randomString());
   if(request.getRequestURL().toString().startsWith("https"))//If Requested over HTTPs
	   token.setSecure(true);
   response.addCookie(token);
   mustRedirect = true;
   
#Getter.authUser
if(userFound)
{
	//Authenticate User
	callstmt = conn.prepareCall("call authUser(?, ?)");
	log.debug("Gathering authUser ResultSet");
	callstmt.setString(1, userName);
	callstmt.setString(2, password);
	ResultSet loginAttempt = callstmt.executeQuery();
	log.debug("Opening Result Set from authUser");
	try
	{
		loginAttempt.next();
		goOn = true; //Valid password for user submitted
	}
	catch (SQLException e)
	{
		//... Outer Catch has preference to this one for some reason... This code is never reached!
		// But I'll leave it here just in case. That includes the else block if goOn is false
		log.debug("Incorrect Credentials");
		goOn = false;
	}
	if(goOn)
	{
		//ResultSet Not Empty => Credentials Correct
		result = new String[5];
		result[0] = loginAttempt.getString(1); //Id
		result[1] = loginAttempt.getString(2); //userName
		result[2] = loginAttempt.getString(3); //role
		result[4] = loginAttempt.getString(6); //classId  设置的classid  来自core.users表中 
```

由于admin管理员是内置账号，因此在数据库core表users中admin账号没有classid ，admin用户在界面上看不到别人发的消息，进而不会被其他人csrf，同时别人也无法看到admin发送的消息，因此admin用户基本上无法完成这个题，但是可以把admin生成的url，发送给其他用户点击，实现csrf的效果 

## 解题步骤  

按要求，提交一个完整url地址，等待别的用户查看这个页面时，触发csrf，然后攻击者这边即可看到通关密钥  

## 总结  

题目本身有点绕，核心是一个csrf攻击，需要校验请求的referer，增加token

本例中校验referer没有效果，增加随机token可以，同时特别核心功能，不建议使用GET方法进行请求 