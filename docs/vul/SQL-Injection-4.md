#  SQL Injection 4(SQL 注入 4)

## 题介绍
通过SQL注入点获得administrator 的信息 

## 功能实现 
请求数据包   
```
POST /challenges/1feccf2205b4c5ddf743630b46aece3784d61adc56498f7603ccd7cb8ae92629 HTTP/1.1


theUserName=admin&thePassword=password
```

对应实现代码   
```
String theUserName = request.getParameter("theUserName");
log.debug("User Submitted - " + theUserName);
theUserName = SqlFilter.levelFour(theUserName); //进行了安全过滤 
log.debug("Filtered to " + theUserName);
String thePassword = request.getParameter("thePassword");
log.debug("thePassword Submitted - " + thePassword);
thePassword = SqlFilter.levelFour(thePassword);
log.debug("Filtered to " + thePassword);
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );

log.debug("Getting Connection to Database");
Connection conn = Database.getChallengeConnection(ApplicationRoot, "SqlChallengeFour");
Statement stmt = conn.createStatement();
log.debug("Gathering result set");
ResultSet resultSet = stmt.executeQuery("SELECT userName FROM users WHERE userName = '" + theUserName + "' AND userPassword = '" + thePassword + "'");

//禁止输入单引号  
public static String SqlFilter.levelFour (String input)
{
	input = input.toLowerCase();
	while(input.contains("'"))
	{
		log.debug("Scrubbing ' from input");
		input = input.replaceAll("'", "");
	}
	return input;
}
```
## 解题步骤  

SQL语句 

```
"SELECT userName FROM users WHERE userName = '" + theUserName + "' AND userPassword = '" + thePassword + "'"
```
两个变量可控 ，但不允许输入单引号，需要将单引号进行转义  
用户名输入 \  
密码字段 输入 or 1=1 AND idusers = 7;-- 
但怎样获得的idusers = 7 仍然是个迷... 


## 总结  

以前一直认为过滤掉 单引号就万事大吉了，没有想到还是不足够的...