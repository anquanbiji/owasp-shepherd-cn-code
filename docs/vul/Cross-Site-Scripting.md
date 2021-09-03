# Cross Site Scripting(跨站脚本攻击)

## 题介绍
需要在搜索框中输入一个可以在前端页面弹出alert提示信息的内容

## 功能实现 
页面点击 获得这个用户 按钮

```
POST /lessons/zf8ed52591579339e590e0726c7b24009f3ac54cdff1b81a65db1688d86efb3a 

searchTerm=%3CSCRIPT%3Ealert('XSS')%3C%2FSCRIPT%3E&csrfToken=-127827516577406622538923948450307535312
```

对应实现代码 /src/main/java/servlets/module/lesson/XssLesson.java

```
String searchTerm = request.getParameter("searchTerm");
log.debug("User Submitted - " + searchTerm);
String htmlOutput = new String();
if(FindXSS.search(searchTerm))
{
	log.debug("XSS Lesson Completed!");
```
获得searchTerm参数值,在/src/main/java/utils/FindXSS.java 解析传入内容,进而判断是否存在alert，相关代码  
```
Elements scriptTags = htmlBody.getElementsByTag("script");
for(Element scriptTag: scriptTags)
{
	String tagContents = scriptTag.html();
	//log.debug("tagContents: " + tagContents);
	if(tagContents.contains("alert"))
	{
		log.debug("Script Tags detected");
		xssDetected = true;
		break;
	}
}
```

## 解题步骤
在页面输入框输入如下javascript代码即可
```
<SCRIPT>alert('XSS')</SCRIPT>
```  

## 总结  

如果系统存在 这样功能:

- 接收客户端提交内容
- 存储客户端内容(可以没有该功能)
- 返回客户端内容到前端渲染展示 

那么需要对用户提交的内容进行html或javascript过滤，禁止出现一些可以浏览器执行的脚本,如javascript或html代码

如果您的系统没有富文本(包含标签的用户提交，如<a 等),可以直接采用编码输出的方法，将<, > 等进行编码输出  
如果您的系统需要富文本，则只能在接收到用户提交内容时进行白名单标签校验  

这类攻击的防御是非常复杂的，原因是前端页面的输出点太多，如html、javascript、css等等，有些输入点完全不需要任何特殊字符，因此在做好基础防御(输入与验证和编码输出)基础上，也要针对特定场景进行特殊验证



