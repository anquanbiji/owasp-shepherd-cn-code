#  Session Management Challenge Two(会话管理 2)

## 题介绍

需要输入管理员的账号和密码才能获得通关密钥  


## 功能实现 
输入系统默认的账号和密码(admin/password)发现无法登陆 ，请求数据包  

```
POST /challenges/d779e34a54172cbc245300d3bc22937090ebd3769466a501a5e7ac605b9f34b7 HTTP/1.1


subName=admin&subPassword=password 
```
查看后台代码 在 src/main/java/servlets/module/challenge/SessionManagement2.java 中  
详细代码如下:  
```
Object nameObj = request.getParameter("subName");
Object passObj = request.getParameter("subPassword");
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

Connection conn = Database.getChallengeConnection(ApplicationRoot, "BrokenAuthAndSessMangChalTwo");
log.debug("Checking credentials");
PreparedStatement callstmt;

log.debug("Committing changes made to database");
callstmt = conn.prepareStatement("COMMIT");
callstmt.execute();
log.debug("Changes committed.");

callstmt = conn.prepareStatement("SELECT userName, userAddress FROM users WHERE userName = ? AND userPassword = SHA(?)");
callstmt.setString(1, subName);
callstmt.setString(2, subPass);
log.debug("Executing authUser");

```
发现进行了预编译SQL查询，杜绝了SQL注入问题 ，同时连接的BrokenAuthAndSessMangChalTwo数据库，所以这个点没有账号和密码无法验证通过 

继续查看代码   
```
ResultSet resultSet = callstmt.executeQuery();
if(resultSet.next())
{
	log.debug("Successful Login");
	// Get key and add it to the output
	String userKey = Hash.generateUserSolution(Getter.getModuleResultFromHash(ApplicationRoot, levelHash), (String)ses.getAttribute("userName"));
	htmlOutput = "<h2 class='title'>" + bundle.getString("response.welcome") + " " + Encode.forHtml(resultSet.getString(1)) + "</h2>" +
			"<p>" +
			bundle.getString("response.resultKey") + " <a>" + userKey + "</a>" +
			"</p>";
}
else
{   //查询失败后，通过用户名查询用户的邮箱地址 
	log.debug("Incorrect credentials, checking if user name correct");
	callstmt = conn.prepareStatement("SELECT userAddress FROM users WHERE userName = ?");
	callstmt.setString(1, subName);
	log.debug("Executing getAddress");
	resultSet = callstmt.executeQuery();
	if(resultSet.next())  //找到邮箱 则返回到前端 
	{
		log.debug("User Found");
		userAddress = bundle.getString("response.badPass") + " <a>" + Encode.forHtml(resultSet.getString(1)) + "</a><br/>";
	}
	else
	{
		userAddress = bundle.getString("response.badUser") + "<br/>";
	}
	htmlOutput = makeTable(userAddress, bundle);
}
Database.closeConnection(conn);
```

如果用户名存在，则会返回对应邮箱地址到前端展示   

同时前端还有个功能，可以通过邮箱找回用户的密码  

请求地址  
```
POST /challenges/f5ddc0ed2d30e597ebacf5fdd117083674b19bb92ffc3499121b9e6a12c92959 HTTP/1.1

subEmail=zoidberg22%40shepherd.com

```
对应功能  功能在 src/main/java/servlets/module/challenge/SessionManagement2ChangePassword.java 中 

```
Object emailObj = request.getParameter("subEmail");
String subEmail = new String();
if(emailObj != null)
	subEmail = (String) emailObj;
log.debug("subEmail = " + subEmail);

log.debug("Getting ApplicationRoot");
String ApplicationRoot = getServletContext().getRealPath("");

String newPassword = Hash.randomString(); //随机生成新密码
try
{
	Connection conn = Database.getChallengeConnection(ApplicationRoot, "BrokenAuthAndSessMangChalTwo");
	log.debug("Checking credentials");
	PreparedStatement callstmt = conn.prepareStatement("UPDATE users SET userPassword = SHA(?) WHERE userAddress = ?"); //根据邮箱重置密码 
	callstmt.setString(1, newPassword);
	callstmt.setString(2, subEmail);
	log.debug("Executing resetPassword");
	callstmt.execute();
	log.debug("Statement executed");
	
	log.debug("Committing changes made to database");
	callstmt = conn.prepareStatement("COMMIT");
	callstmt.execute();
	log.debug("Changes committed.");
	
	htmlOutput = Encode.forHtml(newPassword); //编码新密码
	Database.closeConnection(conn);
}
catch(SQLException e)
{
	log.error(levelName + " SQL Error: " + e.toString());
}
log.debug("Outputting HTML");
out.write(bundle.getString("response.changedTo") + " " + htmlOutput); //输出密码到客户端 
```
使用预编译根据邮箱地址重置用户密码，并且将新生成的密码返回到客户端进行展示 

## 解题步骤   
- 输入错误的密码，可以获得用户的注册邮箱
- 通过使用邮箱找回密码功能，获得最新生成的新密码
- 使用新密码登录，即可获得通关密钥


## 总结  

返回到客户端的内容一定要严格进行校验，防止将敏感信息返回到客户端 