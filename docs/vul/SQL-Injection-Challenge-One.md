# SQL Injection Challenge One(SQL 注入 1)

## 题介绍
这个是SQL注入挑战题，需要通过输入框这个注入点，获得通关密钥

## 功能实现 
随便输入一个数，点击 查询用户 按钮，查看请求包 
```
POST /challenges/e1e109444bf5d7ae3d67b816538613e64f7d0f51c432a164efc8418513711b0a HTTP/1.1


aUserId=1
```
对应的后端文件 src/main/java/servlets/module/challenge/SqlInjection1.java

```
String aUserId = request.getParameter("aUserId");
log.debug("User Submitted - " + aUserId);
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );

log.debug("Getting Connection to Database");
Connection conn = Database.getChallengeConnection(ApplicationRoot, "SqlChallengeOne");
Statement stmt = conn.createStatement();
log.debug("Gathering result set");
ResultSet resultSet = stmt.executeQuery("SELECT * FROM customers WHERE customerId = \"" + aUserId + "\"");  // SQL语句 使用的 双引号 ,需要进行闭合双引号 

```
## 解题步骤  

由于后端SQL语句使用的是双引号，因此注入时，需要闭合双引号才能到达注入的目的  
所以当输入 1" or "1"="1  
最终SQL语句  SELECT * FROM customers WHERE customerId = "1" or "1"="1"  
是一个永真式，达到注入的目的 

## 总结  
SQL注入的核心是通过控制问题SQL语句，注入SQL指令，需要保证原始SQL语法正确  

- 研发的SQL使用的是单引号，注入时则需要使用单引号
- 研发的SQL使用双引号，注入时则需要使用双引号 
- 研发的SQL是一个int型，注入时则不需要进行任何闭合 

研发防御方案基本类似:  

- 用户输入参数进行白名单校验  
- SQL查询使用预编译查询 

## 汉化说明
添加文件 
```
src/main/resources/i18n/challenges/injection/e1e109444bf5d7ae3d67b816538613e64f7d0f51c432a164efc8418513711b0a_zh.properties
```