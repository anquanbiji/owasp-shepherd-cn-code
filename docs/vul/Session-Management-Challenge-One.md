# Session Management Challenge One(会话管理 1)

## 题介绍
需要伪装成管理员权限才能获得通关密钥

## 功能实现 
点击界面按钮 ，抓取数据包 
```
POST /challenges/dfd6bfba1033fa380e378299b6a998c759646bd8aea02511482b8ce5d707f93a HTTP/1.1
Cookie: checksum=dXNlclJvbGU9dXNlcg==; JSESSIONID=A32F61E937D764CDA542BC940323D455; token=66329888183457720076675536392112351575

adminDetected=false&returnPassword=false&upgradeUserToAdmin=false
```

对应后端代码  
```
/src/main/java/servlets/module/challenge/SessionManagement1.java

Cookie userCookies[] = request.getCookies();  //读取cookie 
int i = 0;
Cookie theCookie = null;
for(i = 0; i < userCookies.length; i++)
{
	if(userCookies[i].getName().compareTo("checksum") == 0) //查找cookie中的 checksum 字段
	{
		theCookie = userCookies[i];
		break; //End Loop, because we found the token
	}
}
String htmlOutput = null;
if(theCookie != null)
{
	log.debug("Cookie value: " + theCookie.getValue());
	byte[] decodedCookieBytes = Base64.decodeBase64(theCookie.getValue()); // base64解码
	String decodedCookie = new String(decodedCookieBytes, "UTF-8");
	log.debug("Decoded Cookie: " + decodedCookie);
	
	if(decodedCookie.equals("userRole=administrator")) // 判断 cookie中的checksum值是否是 userRole=administrator 的base64编码后内容，如果是 则返回通关密钥 
	{
		log.debug("Challenge Complete");
```

## 解题步骤  
将 userRole=administrator  进行base64编码，替换掉cookie中的 checksum 

## 总结  

用户端提交的数据都是可以被篡改的，核心判断信息，如权限等，建议保存到服务端，如session

## 汉化说明
添加文件 
```
src/main/resources/i18n/challenges/sessionManagement/dfd6bfba1033fa380e378299b6a998c759646bd8aea02511482b8ce5d707f93a_zh.properties
```
