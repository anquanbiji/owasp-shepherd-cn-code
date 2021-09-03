# Security Misconfiguration(安全配置错误)

## 题介绍
需要输入本系统默认的管理员口令，即可获得通关密钥，主要展示如果默认配置不进行修改，危害巨大

## 功能实现 
在界面上输入账号和密码,点击提交

```
POST /lessons/fe04648f43cdf2d523ecf1675f1ade2cde04a7a2e9a7f1a80dbb6dc9f717c833

userName=admin&userPass=password
```
后端实现代码在 /src/main/java/servlets/module/lesson/SecurityMisconfigLesson.java 文件中

```
String userName = request.getParameter("userName");
log.debug("User Name - " + userName);
String userPass = request.getParameter("userPass");
log.debug("User Pass - " + userName);
boolean loggedIn = userName.contentEquals("admin") && userPass.contentEquals("password");
```
判断用户名和密码是否是 admin/password 

## 解题步骤  
直接在页面输入框中输入admin  
密码输入框中输入password  
点击提交即可 

## 总结  
在开始使用任何系统，包括但不限于操作系统、数据库、应用服务、第三方库等应该进行默认操作修改，以提高其安全性，最基础的是修改其默认的账号和密码

类似问题在构建系统时，非常容易出问题，原因:一般系统都复杂和庞大，对于使用者想完全了解系统的功能，安全配置其实是很难的，更多的研发或运维基本都是下载系统，按照网络教程或官方初始化向导进行部署，能用已经万幸，基本不会过多考虑安全性(因为要阅读大量文档)  

对于这类问题要怎样解决?  

- 希望所有的开源/知名/第三方系统，默认配置都是最安全的，如:功能都是默认关闭，使用者按需开放，默认账号和口令，强制第一次登录进行修改，验证密码复杂度等
- 使用者，需要花时间，对系统进行深入了解和配置