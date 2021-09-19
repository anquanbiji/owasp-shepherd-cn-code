# Failure to Restrict URL Access Challenge 3(没有限制URL访问 3)

## 题介绍

需要找到超级管理员权限  

## 功能实现 
请求数据包  
```
POST /challenges/e40333fc2c40b8e0169e433366350f55c77b82878329570efa894838980de5b4 HTTP/1.1

Cookie: current=WjNWbGMzUXhNZz09; SubSessionID=TURBd01EQXdNREF3TURBd01EQXdNUT09; securityMisconfigLesson=8a3a691885e0014f25a930658c371cc376d7b2734953a536c0f834e318a6ece9; currentPerson=YUd1ZXN0; JSESSIONID=97FD1A94D1A96ACD1BB31BFB000F40D8; token=50163458658349300026575546979087684293

userId=d3d9446802a44259755d38e6d163e820&secure=true
```

对应功能代码   
```
log.debug(levelName + " servlet accessed by: " + ses.getAttribute("userName").toString());
Cookie userCookies[] = request.getCookies();
int i = 0;
Cookie theCookie = null;
for(i = 0; i < userCookies.length; i++)
{
	if(userCookies[i].getName().compareTo("currentPerson") == 0)
	{
		theCookie = userCookies[i];
		break; //End Loop, because we found the token
	}
}
String htmlOutput = null;
if(theCookie != null)
{
	log.debug("Cookie value: " + theCookie.getValue());
	byte[] decodedCookieBytes = Base64.decodeBase64(theCookie.getValue());
	String decodedCookie = new String(decodedCookieBytes, "UTF-8");
	log.debug("Decoded Cookie: " + decodedCookie);

	if(decodedCookie.equals("MrJohnReillyTheSecond")) // cookie中保存的权限 
	{
		log.debug("Super Admin Cookie detected");
		// Get key and add it to the output
		String userKey = Hash.generateUserSolution(Getter.getModuleResultFromHash(getServletContext().getRealPath(""), levelHash), (String)ses.getAttribute("userName"));
		htmlOutput = "<h2 class='title'>" + bundle.getString("admin.superAdminClub") + "</h2>" +
				"<p>" +
				bundle.getString("admin.superAdminClub.keyMessage") + " " +
				"<a>" + userKey + "</a>" +
				"</p>";
	}
	else if (!decodedCookie.equals("aGuest"))
	{
		log.debug("Tampered role cookie detected: " + decodedCookie);
		htmlOutput = "<!-- " + bundle.getString("response.invalidUser") + " -->";
	}
```

怎样找到管理员的账号?

发现页面还有个隐藏的地址 e40333fc2c40b8e0169e433366350f55c77b82878329570efa894838980de5b4UserList  

查看代码存在SQL注入   
```
Cookie userCookies[] = request.getCookies();
int i = 0;
Cookie theCookie = null;
for(i = 0; i < userCookies.length; i++)
{
	if(userCookies[i].getName().compareTo("currentPerson") == 0)
	{
		theCookie = userCookies[i];
		break; //End Loop, because we found the token
	}
}
String currentUser = new String("aGuest");
if(theCookie != null)
{
	log.debug("Cookie value: " + theCookie.getValue());
	byte[] decodedCookieBytes = Base64.decodeBase64(theCookie.getValue());
	String decodedCookie = new String(decodedCookieBytes, "UTF-8");
	log.debug("Decoded Cookie: " + decodedCookie);
	currentUser = decodedCookie;
}
String ApplicationRoot = getServletContext().getRealPath("");
Connection conn = Database.getChallengeConnection(ApplicationRoot, "UrlAccessThree");
PreparedStatement callstmt;
callstmt = conn.prepareStatement("SELECT userName FROM users WHERE userRole = \"admin\" OR userName = \"" + currentUser + "\";");
log.debug("Getting User List");
htmlOutput = new String();
ResultSet rs = callstmt.executeQuery();
while(rs.next())
{
	htmlOutput += Encode.forHtml(rs.getString(1)) + "<br>";
	if(rs.getString(1).equalsIgnoreCase("MrJohnReillyTheSecond"))
	{
		log.debug("Super Admin contained in response");
	}
}
```

通过这个注入点，可以获得所有用户列表 
## 解题步骤  

逐个用户名替换cookie值，进行测试，最终发现 MrJohnReillyTheSecond 账号可以成功  

## 总结  

客户端参数不可信...