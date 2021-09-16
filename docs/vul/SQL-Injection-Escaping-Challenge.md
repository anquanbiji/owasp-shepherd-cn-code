#  SQL Injection Escaping Challenge(SQL 注入转义)

## 题介绍
需要利用SQL注入点获得本题的通关密钥，通过题的介绍，知道单引号已经被转义了

## 功能实现 
获得请求数据包  
```
POST /challenges/8c3c35c30cdbbb73b7be3a4f8587aa9d88044dc43e248984a252c6e861f673d4 HTTP/1.1


aUserId=%22+or+%221%22%3D%221
```
对应实现的代码 在 src/main/java/servlets/module/challenge/SqlInjectionEscaping.java   

```
String aUserId = request.getParameter("aUserId");
log.debug("User Submitted - " + aUserId);
aUserId = aUserId.replaceAll("'", "\\\\'"); //Replace ' with \'
log.debug("Escaped to - " + aUserId);
String ApplicationRoot = getServletContext().getRealPath("");

log.debug("Getting Connection to Database");
Connection conn = Database.getChallengeConnection(ApplicationRoot, "SqlChallengeEscape");
Statement stmt = conn.createStatement();
log.debug("Gathering result set");
ResultSet resultSet = stmt.executeQuery("SELECT * FROM customers WHERE customerId = '" + aUserId + "'");

```
可见单引号被转义成\', 接下来是一个比较典型的字符型SQL注入需要用到单引号 

## 解题步骤  

本题是研发自己实现的安全过滤，存在考虑不周，如果用户输入 \' 则变成 \\' 使得\失去了转义作用，直接输入 \' or 1=1--  即可 

## 总结  

研发人员第一步需要树立安全意识，对用户提交的参数进行输入检查，当研发人员形成意识后，发现其实是进入了一个安全对抗领域，需要不断提高自身过滤能力，才能完全消灭安全漏洞   

最理想的情况，非不得已不要自己编写复杂的过滤校验，可以使用公开的公认安全的过滤库  