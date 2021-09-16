# Insecure Direct Object References Challenge Two(不安全的直接对象引用 2)

## 题介绍
需要遍历一个不在列表中的用户，获得通关密钥 

## 功能实现 
获得到的请求数据包  
```
POST /challenges/vc9b78627df2c032ceaf7375df1d847e47ed7abac2a4ce4cb6086646e0f313a4 HTTP/1.1


userId%5B%5D=eccbc87e4b5ce2fe28308fd9f2a7baf3
```

对应实现代码比较简单  
```
String userId = request.getParameter("userId[]");
log.debug("User Submitted - " + userId);
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );
String htmlOutput = new String();

Connection conn = Database.getChallengeConnection(ApplicationRoot, "directObjectRefChalTwo");
PreparedStatement prepstmt = conn.prepareStatement("SELECT userName, privateMessage FROM users WHERE userId = ?");
prepstmt.setString(1, userId);
ResultSet resultSet = prepstmt.executeQuery();
if(resultSet.next())
{
	log.debug("Found user: " + resultSet.getString(1));
	String userName = resultSet.getString(1);
	String privateMessage = resultSet.getString(2);
	htmlOutput = "<h2 class='title'>" + userName + "'s " + bundle.getString("response.message") + "</h2>" +
			"<p>" + privateMessage + "</p>";
}

```
直接查询库，没有任何其他操作，提交的数据  eccbc87e4b5ce2fe28308fd9f2a7baf3 一看像md5,使用cmd5解密后是3 ，并且将所有显示的用户id进行解密，获得的值是 2,3,5,7,11  
按照题提示，非上述用户列表，所以直接遍历 1,4,6,8,9,10,12,13...即可

## 解题步骤  

将数字1,4,6,8,9,10,12,13...使用md5加密后，进行重放即可 

## 总结  

md5存在撞库的问题，如果要使用建议添加hash值  