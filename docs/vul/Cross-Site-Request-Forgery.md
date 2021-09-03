#  Cross-Site Request Forgery(跨站请求伪造)

## 题介绍

需要利用跨站请求伪造，让管理员访问一个URL地址 

## 功能实现 

点击页面提交数据 
```
POST /lessons/ed4182af119d97728b2afca6da7cdbe270a9e9dd714065f0f775cd40dc296bc7 HTTP/1.1


messageForAdmin=https%3A%2F%2Fip%2Froot%2FgrantComplete%2FcsrfLesson%3FuserId%3D598717876&csrfToken=66329888183457720076675536392112351575
```
对应后端实现代码 /src/main/java/servlets/module/lesson/CsrfLesson.java

```
String falseId = (String) ses.getAttribute("falseId");
log.debug("falseId = " + falseId);
String messageForAdmin = request.getParameter("messageForAdmin").toLowerCase();
log.debug("User Submitted - " + messageForAdmin);

String htmlOutput = new String();
boolean validLessonAttack = FindXSS.findCsrfAttackUrl(messageForAdmin, "/root/grantComplete/csrflesson", "userId", falseId); //核心验证操作 

if(validLessonAttack)

// FindXSS.findCsrfAttackUrl

public static boolean findCsrfAttackUrl (String theUrl, String csrfAttackPath, String userIdParameterName, String userIdParameterValue ) 
	{
		boolean validAttack = false;
		try
		{
			URL theAttack = new URL(theUrl);
			log.debug("csrfAttackPath: " + csrfAttackPath);
			log.debug("theAttack Host: " + theAttack.getHost());
			log.debug("theAttack Port: " + theAttack.getPort());
			log.debug("theAttack Path: " + theAttack.getPath());
			log.debug("theAttack Query: " + theAttack.getQuery());
			boolean validPath = theAttack.getPath().toLowerCase().endsWith(csrfAttackPath.toLowerCase());  // 判断提交的URL是否正确
			if(!validPath)
				log.debug("Invalid Solution: Bad Path submitted. Expected:" + csrfAttackPath.toLowerCase());
			else
			{
				boolean validQuery = theAttack.getQuery().toLowerCase().equalsIgnoreCase((userIdParameterName + "=" + userIdParameterValue).toLowerCase()); // 判断参数是否正确
				if(!validQuery)
					log.debug("Invalid Solution: Bad Query. Expected: " + (userIdParameterName + "=" + userIdParameterValue).toLowerCase());
				else
				{
					validAttack = true;
				}
			}
		}
		catch(MalformedURLException e)
		{
			log.debug("Invalid URL Submitted: " + e.toString());
			validAttack = false;
		}
		catch(Exception e)
		{
			log.error("FindCSRF Failed: " + e.toString());
			validAttack = false;
		}
		return validAttack;
	}

```
## 解题步骤  

这个只是一个模拟csrf操作，因此直接输入题目要求的 url地址即可 


## 总结  

CSRF是一类比较常见的安全漏洞，核心点是借用受害者的cookie,达到绕过系统权限校验  
具体危害取决漏洞页面实现的功能，如修改管理员密码功能

常见的解决方案:  
- 校验请求的refer 是否是本域  
- 每个请求添加随机token 

