# Broken Authentication and Session Management(失效的身份认证和会话管理)

## 题介绍
本题主要考察不好的会话管理,需要通过点击**完成该课程**按钮,欺骗服务器，让服务器认为我们已经完成了该课程，进而获得通关密钥

## 功能实现 
页面点击 完成该课程 按钮 
```
POST /lessons/b8c19efd1a7cc64301f239f9b9a7a32410a0808138bbefc98986030f9ea83806 HTTP/1.1
Cookie: lessonComplete=lessonNotComplete; JSESSIONID=0B5E5DB01DD31D29205ED99B77ABB56C; token=-127827516577406622538923948450307535312
```
服务端代码: /src/main/java/servlets/module/lesson/SessionManagementLesson.java

```
Cookie userCookies[] = request.getCookies();
int i = 0;
Cookie theCookie = null;
for(i = 0; i < userCookies.length; i++)
{
	if(userCookies[i].getName().compareTo("lessonComplete") == 0)
	{
		theCookie = userCookies[i];
		break; //End Loop, because we found the token
	}
}
String htmlOutput = null;
if(theCookie != null)
{
	log.debug("Cookie value: " + theCookie.getValue());
	
	if(theCookie.getValue().equals("lessonComplete"))
	{
		log.debug("Lesson Complete");
```
功能实现，读取cookie,判断是否存在cookie的key为 lessonComplete，进而判断lessonComplete 的值 是否为 lessonComplete


## 解题步骤  
- 使用代理工具,拦截请求
- 修改cookie中的 lessonComplete=lessonNotComplete 为 lessonComplete=lessonComplete
- 继续发送请求
- 服务器返回通过密钥

## 总结  

一切客户端传递过来的数据都可以被篡改，所以都是不可信任的，涉及核心验证，敏感操作，状态信息一定要保存到服务端如session中，禁止客户端修改