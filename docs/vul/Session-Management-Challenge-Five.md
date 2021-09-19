#  Session Management Challenge Five(会话管理 5)

## 题介绍
需要输入账号和密码进行登录  

## 功能实现 
请求数据包  
```
POST /challenges/7aed58f3a00087d56c844ed9474c671f8999680556c127a19ee79fa5d7a132e1 HTTP/1.1


subUserName=test&subUserPassword=password
```

对应代码实现   
```
Object nameObj = request.getParameter("subUserName");
Object passObj = request.getParameter("subUserPassword");
String subName = new String();
String subPass = new String();
String userAddress = new String();
if(nameObj != null)
	subName = (String) nameObj;
if(passObj != null)
	subPass = (String) passObj;
log.debug("subName = " + subName);
log.debug("subPass = " + subPass);

log.debug("Getting ApplicationRoot");
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );

Connection conn = Database.getChallengeConnection(ApplicationRoot, "BrokenAuthAndSessMangChalFive");
log.debug("Checking credentials");
PreparedStatement callstmt;

log.debug("Committing changes made to database");
callstmt = conn.prepareStatement("COMMIT");
callstmt.execute();
log.debug("Changes committed.");

callstmt = conn.prepareStatement("SELECT userName, userRole FROM users WHERE userName = ?");  //参数化查询 是否存在用户 
callstmt.setString(1, subName);
log.debug("Executing findUser");
ResultSet resultSet = callstmt.executeQuery();
//Is the username valid?
if(resultSet.next())
{
	log.debug("User found");
	//Is the user an Admin?
	if(resultSet.getString(2).equalsIgnoreCase("admin"))  //判断用户是否是管理员 
	{
		log.debug("Admin Detected");
		callstmt = conn.prepareStatement("SELECT userName, userRole FROM users WHERE userName = ? AND userPassword = SHA(?)"); //判断用户名和密码是否正确 
		callstmt.setString(1, subName);
		callstmt.setString(2, subPass);
```

发现没有SQL注入问题   

找回密码功能代码   
```
POST /challenges/7aed58f3a00087d56c844ed9474c671f8999680556c127a19ee79fa5d7a132e1SendToken HTTP/1.1


subUserName=admin
```
对应实现    
```
Object nameObj = request.getParameter("subUserName");
String userName = new String();
if(nameObj != null)
	userName = (String) nameObj;
log.debug("subName = " + userName);

log.debug("Getting ApplicationRoot");
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );

Connection conn = Database.getChallengeConnection(ApplicationRoot, "BrokenAuthAndSessMangChalFive");
log.debug("Checking name");
PreparedStatement callstmt;

log.debug("Committing changes made to database");
callstmt = conn.prepareStatement("COMMIT");
callstmt.execute();
log.debug("Changes committed.");

callstmt = conn.prepareStatement("SELECT userName FROM users WHERE userName = ?"); //参数化查询
callstmt.setString(1, userName);
log.debug("Executing findUser");
ResultSet resultSet = callstmt.executeQuery();
//Is the username valid?
if(resultSet.next())
{
	log.debug("User found");  //可以判断用户名是否存在 
	htmlOutput = bundle.getString("setToken.sentTo.1") + " '" + Encode.forHtml(userName) + "' " +  bundle.getString("setToken.sentTo.2");
}
else
```

只能判断用户名是否存在  


从页面html中可以找到重置用户名和密码的链接  
```
POST /challenges/7aed58f3a00087d56c844ed9474c671f8999680556c127a19ee79fa5d7a132e1ChangePass HTTP/1.1


userName=admin&newPassword=password&resetPasswordToken=U3RhIFNlcCAxOCAwODoyODoxMCBCU1QgMjAyMQ==
```

前两个参数比较好理解，最后一个参数，黑盒是非常难想到的  
```
Object tokenObj = request.getParameter("resetPasswordToken");
String userName = new String();
String newPass = new String();
String token = new String();
if(passNewObj != null)
	newPass = (String) passNewObj;
if(userNewObj != null)
	userName = (String) userNewObj;
if(tokenObj != null)
	token = (String) tokenObj;
log.debug("userName = " + userName);
log.debug("newPass = " + newPass);
log.debug("token = " + token);
String tokenTime = new String();
try
{
	byte[] decodedToken = Base64.decodeBase64(token);
	tokenTime = new String(decodedToken, "UTF-8");
}
catch (UnsupportedEncodingException e)
{
	log.debug("Could not decode password token");
	errorMessage += "<p>" + bundle.getString("changePass.noDecode") + "</p>";
}
if(tokenTime.isEmpty())
{
	log.debug("Could not decode token. Ending Servlet.");
	out.write(errorMessage);
}
else
{
	log.debug("Decoded Token = " + tokenTime);
	
	//Get Time from Token and see if it is inside the last 10 minutes
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy"); //时间格式 必须是这种 
	try 
	{
		Date tokenDateTime = simpleDateFormat.parse(tokenTime);
		Date currentDateTime = new Date();
		//Get difference in minutes
		tokenLife = (int)((currentDateTime.getTime()/60000) - (tokenDateTime.getTime()/60000));
		log.debug("Token life = " + tokenLife);
	} 
	catch (ParseException e) 
```



## 解题步骤  

需要知道服务器当前的 EEE MMM d HH:mm:ss Z yyyy ，然后base64编码   
服务器的时间可能不是当前时间，所以执行 date命令 进行查看  


## 总结

有些问题不进行代码审计是很难发现的,所以保护好自己的源代码是非常有必要的   
  