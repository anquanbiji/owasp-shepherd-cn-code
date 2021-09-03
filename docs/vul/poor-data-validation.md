# Poor Data Validation(失效的数据验证)

## 题介绍
本题需要在输入框中输入一个负数,并且提交到服务端,才能获得通关密钥

## 功能实现  
在输入框中输入1,点击 提交数字  
使用代理 抓到请求包  
```
POST /lessons/4d8d50a458ca5f1f7e2506dd5557ae1f7da21282795d0ed86c55fefe41eb874f

userdata=1
```

根据web.xml配置,知道服务端对应的代码servlets.module.lesson.PoorValidationLesson,即src/main/java/servlets/module/lesson/PoorValidationLesson.java
```
web.xml 
<servlet>
		<servlet-name>/lessons/4d8d50a458ca5f1f7e2506dd5557ae1f7da21282795d0ed86c55fefe41eb874f</servlet-name>
		<servlet-class>servlets.module.lesson.PoorValidationLesson</servlet-class>
	</servlet>
```
对应实现代码:
```
String userData = request.getParameter("userdata");
log.debug("User Submitted - " + userData);
String htmlOutput = new String();
int userNumber = Integer.parseInt(userData);
if(userNumber < 0)
{ //通关
}
```

如果传入的变量userdata为负数,则可以通关

但直接在页面输入框输入-1,点击提交按钮 发现返回 "产生一个错误:无效数字: 数字必须大于 0",发现是在前端javascript进行了校验 
 
src/main/webapp/lessons/4d8d50a458ca5f1f7e2506dd5557ae1f7da21282795d0ed86c55fefe41eb874f.jsp  line:127~139  进行了客户端校验，如果提交数据小于0,则拒绝提交
```
counter = counter + 1;
console.log("Counter: " + counter);
var number = $("#numberBox").val();
var theError = "";
if(number.length == 0)
{
    console.log("No Number Submitted");
    theError = "<%= bundle.getString("error.noNumber") %>";		
}
else if (number < 0) 
{
    console.log("Invalid Number Submitted");
    theError = "<%= bundle.getString("error.badNumber") %>";
}
```

## 解题步骤   

- 使用代理工具,拦截请求
- 修改userdata=1 为 userdata=-1
- 继续发送请求
- 服务器返回通关密钥

## 总结  

对请求参数进行合法性校验，一定要在服务端进行校验，否则通过代理抓包是可以篡改前端校验

前端校验的收益，仅仅是在异常输入时，减少客户端请求，对安全性没有提高
