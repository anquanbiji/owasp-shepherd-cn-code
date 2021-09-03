# Insecure Direct Object References Challenge One(不安全的直接对象引用 1)

## 题介绍
通关密钥保存在一个用户的个人配置中，但这个用户没有在界面上展示 

## 功能实现 
点击页面按钮查看请求
```
POST /challenges/o9a450a64cc2a196f55878e2bd9a27a72daea0f17017253f87e7ebd98c71c98c HTTP/1.1


userId%5B%5D=3
```
对应后端实现代码 src/main/java/servlets/module/challenge/DirectObject1.java 

```
String userId = request.getParameter("userId[]");  //读取参数
log.debug("User Submitted - " + userId);
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );
String htmlOutput = new String();

Connection conn = Database.getChallengeConnection(ApplicationRoot, "directObjectRefChalOne");
PreparedStatement prepstmt = conn.prepareStatement("SELECT userName, privateMessage FROM users WHERE userId = ?"); //进行参数化查询 无SQL注入
prepstmt.setString(1, userId);
ResultSet resultSet = prepstmt.executeQuery();
if(resultSet.next()) //读取到信息 则返回到前端
{
	log.debug("Found user: " + resultSet.getString(1));
	String userName = resultSet.getString(1);
	String privateMessage = resultSet.getString(2);
	htmlOutput = "<h2 class='title'>" + userName + "'s " + bundle.getString("response.message") + "</h2>" +
			"<p>" + privateMessage + "</p>";
}
else
```
从代码看，没有SQL注入了，只是不断的遍历参数userId[] 

## 解题步骤  
从userId%5B%5D=1 开始遍历，直到获得通关密钥为止


## 总结  

完全依赖客户端提交的数据进行操作 ，在日常的系统中比较常见  
安全的开发:需要将核心功能,如权限管理相关功能的记录信息,保存到session中  
**正确流程**  
用户登录后，将用户唯一标识如userid或username保存到session中  
当查询用户信息时，直接从session中读取

如针对userid这样字段，可以将其修改为字符串，值为uuid，达到无法被遍历的效果，也是一种有效解决方案  


## 汉化说明
添加文件  
```
/src/main/resources/i18n/challenges/directObject/o9a450a64cc2a196f55878e2bd9a27a72daea0f17017253f87e7ebd98c71c98c_zh.properties
```