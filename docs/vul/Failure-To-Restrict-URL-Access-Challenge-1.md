#  Failure To Restrict URL Access Challenge 1(没有限制URL访问 1)

## 题介绍
需要以管理员的权限获得通关密钥 

## 功能实现 

点击页面按钮获得请求包 
```
POST /challenges/4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c99 HTTP/1.1


userData=4816283
```

对应后端文件 /src/main/java/servlets/module/challenge/UrlAccess1.java

```
String userData = request.getParameter("userData");
boolean tamperedRequest = !userData.equalsIgnoreCase("4816283");
if(!tamperedRequest)
	log.debug("No request tampering detected");
else
	log.debug("User Submitted - " + userData);

if(!tamperedRequest)
	htmlOutput = "<h2 class='title'>" + bundle.getString("response.status") + "</h2>"
		+ "<p>" + bundle.getString("response.status.message") + "</p>";
else
	htmlOutput = "<h2 class='title'>" + bundle.getString("response.statusFail") + "</h2>"
			+ "<p>" + bundle.getString("response.statusFail.message") + "</p>"
			+ "<!-- " + Encode.forHtml(userData) + " -->";
```
代码很简单，只需要输入userData=4816283就可以，但是却没有通关密钥  

又研究前端代码发现下面的javascript代码
```
$("#leAdminForm").submit(function(){
		$("#submitButton").hide("fast");
		$("#loadingSign").show("slow");
		$("#resultsDiv").hide("slow", function(){
			var ajaxCall = $.ajax({
				type: "POST",
				url: "4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c992",
				data: {
					userData: "4816283", 
				},
				async: false
			}
```
请求的路径是 4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c992 比原来请求的路径 4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c99 多了一个2 

查看 /challenges/4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c992 对应的后端代码 

```
src/main/java/servlets/module/challenge/UrlAccess1Admin.java

String userData = request.getParameter("userData");
boolean tamperedRequest = !userData.equalsIgnoreCase("4816283"); //只需要userData=4816283 
if(!tamperedRequest)
	log.debug("No request tampering detected");
else
	log.debug("User Submitted - " + userData);

if(!tamperedRequest)
{
	String userKey = Hash.generateUserSolution(levelResult, (String)ses.getAttribute("userName")); //生成通关密钥的代码 
	htmlOutput = "<h2 class='title'>" + bundle.getString("response.status") + "</h2>"
		+ "<p>" + bundle.getString("result.keyMessage.1") + "<br />"
		+ "<a>" + userKey + "</a><br /> " 
		+ bundle.getString("result.keyMessage.2") + "</p>";
```

## 解题步骤  
将post请求的地址 /challenges/4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c99 修改为 /challenges/4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c992 即可


## 总结  
任何URL都需要添加正确的访问限制

## 汉化说明

```
src/main/resources/i18n/challenges/urlAccess/4a1bc73dd68f64107db3bbc7ee74e3f1336d350c4e1e51d4eda5b52dddf86c99_zh.properties
```