# Insecure Direct Object References(不安全的直接对象引用)

## 题介绍
本题显示了一个游客用户的个人资料(年龄、住址、邮箱等), 要求我们获得管理员个人资料的中保存的通关密钥信息

## 功能实现  

- 点击 刷新个人资料 按钮  
通过代理 抓到请求数据包:  

```
POST /lessons/fdb94122d0f032821019c7edf09dc62ea21e25ca619ed9107bcc50e4a8dbc100

username=guest
```

后端响应代码在文件  
src/main/java/servlets/module/lesson/DirectObjectLesson.java
具体实现比较简单，读取username值，如果是guest则返回guest相关信息，如果是admin则返回通过密钥

```
String userName = request.getParameter("username");
if(userName.equalsIgnoreCase("guest"))
{
	log.debug("Guest Profile Found");
	htmlOutput = htmlGuest(bundle);
}
else if(userName.equalsIgnoreCase("admin"))
{
	// Get key and add it to the output
	String userKey = Hash.generateUserSolution(levelResult, (String)ses.getAttribute("userName"));
	log.debug("Admin Profile Found");
	htmlOutput = htmlAdmin(bundle, userKey);
}
```

## 解题步骤  

- 使用代理工具,拦截请求
- 修改username=guest为username=admin
- 继续发送请求
- 服务器返回通过密钥

## 总结  
完全依赖客户端提交的数据进行操作 ，在日常的系统中比较多
安全开发:需要将核心功能,如权限管理相关功能的记录信息,保存到session中  
正常流程  

- 用户登录后，将用户唯一标识如userid或username保存到session中
- 当查询用户信息时，直接从session中读取




