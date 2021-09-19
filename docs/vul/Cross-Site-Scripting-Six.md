#  Cross Site Scripting Six(跨站脚本攻击 6)

## 题介绍
利用xss注入点，实现一个弹框

## 功能实现 
请求数据包  
```
POST /challenges/d330dea1acf21886b685184ee222ea8e0a60589c3940afd6ebf433469e997caf HTTP/1.1


searchTerm=test&csrfToken=50163458658349300026575546979087684293
```

功能实现代码   
```
String searchTerm = request.getParameter("searchTerm");
log.debug("User Submitted - " + searchTerm);
searchTerm = XssFilter.anotherBadUrlValidate(searchTerm);
userPost = "<a href=\"" + searchTerm + "\">Your HTTP Link!</a>";
log.debug("After Sanitising - " + searchTerm);

boolean xssDetected = FindXSS.search(userPost);
if(xssDetected)
{
	htmlOutput = "<h2 class='title'>" + bundle.getString("result.wellDone") + "</h2>" +
			"<p>" + bundle.getString("result.youDidIt") + "<br />" +
			bundle.getString("result.resultKey") + " <a>" +
				Hash.generateUserSolution(
						Getter.getModuleResultFromHash(getServletContext().getRealPath(""), levelHash),
					(String)ses.getAttribute("userName")
				)
			 + "</a>";
}
public static String XssFilter.anotherBadUrlValidate (String input)
{
	String howToMakeAUrlUrl = new String("https://www.google.com/search?q=What+does+a+HTTP+link+look+like");
	input = input.toLowerCase();
	if (input.startsWith("http"))
	{
		try 
		{
			URL theUrl = new URL(input.replaceAll("#", "&#x23;").replaceFirst("<", "&#x3c;").replaceFirst(">", "&#x3e;").replaceFirst("\"", "&quot;")); //只替换了第一个 双引号 
			input = theUrl.toString();
		} 
		catch (MalformedURLException e) 
		{
			log.debug("Could not Cast URL from input: " + e.toString());
			input = howToMakeAUrlUrl;
		}
	}
	else
	{
		log.debug("Was not a HTTP URL");
		input = howToMakeAUrlUrl;
	}
	return input;
}
```

## 解题步骤  

输入两个双引号 即可绕过过滤 

## 总结  

不安全的过滤非常的危险  

