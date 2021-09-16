# Unvalidated Redirects and Forwards(未验证的重定向和转发)

## 题介绍
需要构造一个url重定向/转发的功能,通过提交一个URL,进而跳转到另一个URL地址 

## 功能实现 
点击 发送信息 按钮，抓取到数据包 
```
POST /lessons/f15f2766c971e16e68aa26043e6016a0a7f6879283c873d9476a8e7e94ea736f HTTP/1.1

messageForAdmin=dd&csrfToken=57966045510818911837005793670200998761
```
对应实现代码在文件:  
```
src/main/java/servlets/module/lesson/UnvalidatedForwardsLesson.java
```
对应实现代码如下:
```
String messageForAdmin = request.getParameter("messageForAdmin").toLowerCase();
log.debug("User Submitted - " + messageForAdmin);
String htmlOutput = new String();
boolean validUrl = true;
boolean validSolution = false;
boolean validAttack = false;
try
{
	URL csrfUrl = new URL(messageForAdmin);
	log.debug("Url Host: " + csrfUrl.getHost());
	log.debug("Url Port: " + csrfUrl.getPort());
	log.debug("Url Path: " + csrfUrl.getPath());
	log.debug("Url Query: " + csrfUrl.getQuery());
	validSolution = csrfUrl.getPath().toLowerCase().equalsIgnoreCase("/user/redirect");
	if(!validSolution)
		log.debug("Invalid Solution: Bad Path or Above");
	validSolution = csrfUrl.getQuery().toLowerCase().startsWith(("to=").toLowerCase()) && validSolution;
	if(!validSolution)
		log.debug("Invalid Solution: Bad Query or Above");
	if(validSolution)
	{
		log.debug("Redirect URL Correct: Now checking the Redirected URL for valid CSRF Attack");
		int csrfStart = 0;
		int csrfEnd = 0;
		csrfStart = csrfUrl.getQuery().indexOf("to=") + 3;
		csrfEnd = csrfUrl.getQuery().indexOf("&");
		if(csrfEnd == -1)
		{
			csrfEnd = csrfUrl.getQuery().length();
		}
		String csrfAttack = csrfUrl.getQuery().substring(csrfStart, csrfEnd);
		log.debug("csrfAttack Found to be: " + csrfAttack);
		validAttack = FindXSS.findCsrfAttackUrl(csrfAttack, "/root/grantComplete/unvalidatedredirectlesson", "userId", tempId);
```
messageForAdmin参数获得到一个完整url地址，经过URL类进行解析，判断路径是否是 /usr/redirect ,提取提交路径的to参数值，通过FindXSS.findCsrfAttackUrl 进一步进行判断路径和userId等参数 

## 解题步骤  
本题比较简单，构造一个满足条件的合法url即可 

## 总结  

对客户端提交的URL地址要进行严格校验，否则很容易出现跳转到任意第三方地址 

