# 作为一名合格的web开发人员需要了解哪些安全知识 

经常写出来安全漏洞,被公司安全部通报...  
同级的同事都已经顺利晋升,而我却因为"code不安全 不能晋级"...   
作为一名web开发,怎样快速提高代码安全性?  

## 正确理解web安全   

作为开发人员是容易学习web安全知识的，要对安全知识树立正确的认识   
web安全只是开发中的一小部分，安全是系统的一个基本属性, 需要意识到它的存在  
开发更多是正向思维，按照要求去实现需要的功能, 而安全更多是逆向思维, 确认系统是否实现了不应该实现的功能   
以开发的背景是非常容易理解安全问题,进而更加容易的修复安全问题  

## web安全基础知识

- 用户输入都是不可信的, 一定要进行限制  
能够正确实现这个过滤，基本上解决掉了60%的安全问题   
怎样去实现这个限制?   
比如需要接收用户提交的购买商品的数量count, 就应该限制count一定是整数且是正整数, 防止用户购买到负金额商品, 进一步校验，大部分用户购买一个商品不可能大于1w,因此进一步限制count小于1w，防止后续数据太大导致溢出(在付款金额再次限制，金额为正)  


- 用户的身份进行校验  
一定要对请求增加合适的访问限制，对用户身份，访问范围进行限制，防止出现未授权或越权的访问   
这类问题非常常见，一定要重视起来  

- 数据存储在服务端
用好session这个技术，将涉及到敏感，私有等信息，一定存在服务端session中，防止被篡改  

基本以上3点，就是web安全知识的"铁三角", 理解这3点，基本上就已经入门  


## 应用web安全知识  

应用是最主要的，很多同学听了SQL注入以为就明白了，其实80%的还是不明白，或者只明白了一点点，在具体编写时，仍然还会出现问题   
最主要的原因是没有真正的理解，没有真正的实操过，这个是学习者的最大问题   

哪里去实际操作?   
这个大家可以使用[shepherd.anquanbiji.com](http://shepherd.anquanbiji.com/)这个项目, 里面有各种web安全基础问题，帮助零基础同学快速掌握web安全基础知识   
这套课程有很多等级,适合不同需求的同学进行学习和使用  

## 持续的学习和提高  
不要以为学完就已经很厉害了，其实还是在入门与门外之间徘徊，具体的场景下，仍然需要具体问题，一个不留神就会编写出安全漏洞, 持续的学习和提高才是永恒的  



## 课程完整列表 
[Insecure Direct Object References(不安全的直接对象引用)](http://shepherd.anquanbiji.com/vul/insecure-derect-object-references.html)  
[Poor Data Validation(失效的数据验证)](http://shepherd.anquanbiji.com/vul/poor-data-validation.html)  
[Security Misconfiguration(安全配置错误)](http://shepherd.anquanbiji.com/vul/security-misconfiguration.html)  
[Broken Authentication and Session Management(失效的身份认证和会话管理)](http://shepherd.anquanbiji.com/vul/Broken-Authentication-and-Session-Management.html)  
[Failure to Restrict URL Access(没有限制URL访问)](http://shepherd.anquanbiji.com/vul/Failure-to-Restrict-URL-Access.html)  
[Cross Site Scripting(跨站脚本攻击)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting.html)  
[Cross Site Scripting One(跨站脚本攻击 1)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-One.html)  
[Insecure Cryptographic Storage(不安全的加密存储)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage.html)  
[SQL Injection Lesson(SQL 注入)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Lesson.html)  
[Insecure Cryptographic Storage Challenge 1(不安全的加密存储 1)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage-Challenge-1.html)  
[Insecure Direct Object References Challenge One(不安全的直接对象引用 1)](http://shepherd.anquanbiji.com/vul/Insecure-Direct-Object-References-Challenge-One.html)  
[Poor Validation One(失效的数据认证 1)](http://shepherd.anquanbiji.com/vul/Poor-Validation-One.html)  
[SQL Injection Challenge One(SQL 注入 1)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-One.html)  
[Session Management Challenge One(会话管理 1)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-One.html)  
[Failure To Restrict URL Access Challenge 1(没有限制URL访问 1)](http://shepherd.anquanbiji.com/vul/Failure-To-Restrict-URL-Access-Challenge-1.html)  
[Cross-Site Request Forgery(跨站请求伪造)](http://shepherd.anquanbiji.com/vul/Cross-Site-Request-Forgery.html)  
[Unvalidated Redirects and Forwards(未验证的重定向和转发)](http://shepherd.anquanbiji.com/vul/Unvalidated-Redirects-and-Forwards.html)  
[SQL Injection Challenge Two(SQL注入挑战 2)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-Two.html)   
[SQL Injection Escaping Challenge(SQL 注入转义)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Escaping-Challenge.html)  
[Session Management Challenge Two(会话管理 2)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Two.html)   
[Cross Site Request Forgery Challenge One(跨站请求伪造 (CSRF) 1)](http://shepherd.anquanbiji.com/vul/Cross-Site-Request-Forgery-Challenge-One.html)  
[Session Management Challenge Three(会话管理 3)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Three.html)  
[Cross Site Scripting Two(跨站脚本攻击 2)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-Two.html)  
[Insecure Cryptographic Storage Challenge 2(不安全加密存储 2)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage-Challenge-2.html)  
[Insecure Direct Object References Challenge Two(不安全的直接对象引用 2)](http://shepherd.anquanbiji.com/vul/Insecure-Direct-Object-References-Challenge-Two.html)  
[Cross Site Scripting 3(跨站脚本 (XSS)3)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-3.html)   
[Insecure Direct Object Reference Bank Challenge(银行不安全的直接对象引用)](http://shepherd.anquanbiji.com/vul/Insecure-Direct-Object-Reference-Bank-Challenge.html)  
[SQL Injection Challenge Three(SQL 注入 3 )](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-Three.html)  
[Session Management Challenge Four(会话管理 4)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Four.html)   
[Cross Site Scripting 4(跨站脚本攻击 4)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-4.html)   
[SQL Injection 4(SQL 注入 4)](http://shepherd.anquanbiji.com/vul/SQL-Injection-4.html)  
[Insecure Cryptographic Storage Challenge 3(不安全加密存储 3)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage-Challenge-3.html)  
[Poor Validation Two(失效的数据验证 2)](http://shepherd.anquanbiji.com/vul/Poor-Validation-Two.html)   
[Failure to Restrict URL Access Challenge 2(没有限制URL访问 2)](http://shepherd.anquanbiji.com/vul/Failure-to-Restrict-URL-Access-Challenge-2.html)   
[Cross Site Scripting 5(跨站脚本攻击 5)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-5.html)  
[SQL Injection Challenge 5(SQL 注入 5)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-5.html)  
[Cross Site Scripting Six(跨站脚本攻击 6)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-Six.html)   
[SQL Injection Challenge 6(SQL 注入 6)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-6.html)  
[Failure to Restrict URL Access Challenge 3(没有限制URL访问 3)](http://shepherd.anquanbiji.com/vul/Failure-to-Restrict-URL-Access-Challenge-3.html)   
[Session Management Challenge Six(会话管理 6)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Six.html)  
[Session Management Challenge Seven(会话管理 7)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Seven.html)  
[SQL Injection Challenge 7(SQL 注入 7)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-7.html)  

[Insecure Data Storage(移动不安全的数据存储)](http://shepherd.anquanbiji.com/vul/app/Insecure-Data-Storage.html)  
[Mobile Reverse Engineering(移动逆向工程)](http://shepherd.anquanbiji.com/vul/app/Mobile-Reverse-Engineering.html)  
[Unintended Data Leakage(移动数据意外泄漏)](http://shepherd.anquanbiji.com/vul/app/Unintended-Data-Leakage.html)  
[Content Provider Leakage(内容提供者泄漏)](http://shepherd.anquanbiji.com/vul/app/Content-Provider-Leakage.html)  
[Client Side Injection(移动客户端注入)](http://shepherd.anquanbiji.com/vul/app/Client-Side-Injection.html)    
[Poor Authentication(失效的身份认证)](http://shepherd.anquanbiji.com/vul/app/Poor-Authentication.html)    
[Broken Crypto(失效的加密)](http://shepherd.anquanbiji.com/vul/app/Broken-Crypto.html)  
  





# 作为一名合格的web开发人员需要了解哪些安全知识 

经常写出来安全漏洞,被公司安全部通报...  
同级的同事都已经顺利晋升,而我却因为"code不安全 不能晋级"...   
作为一名web开发,怎样快速提高代码安全性?  

## 正确理解web安全   

作为开发人员是容易学习web安全知识的，要对安全知识树立正确的认识   
web安全只是开发中的一小部分，安全是系统的一个基本属性, 需要意识到它的存在  
开发更多是正向思维，按照要求去实现需要的功能, 而安全更多是逆向思维, 确认系统是否实现了不应该实现的功能   
以开发的背景是非常容易理解安全问题,进而更加容易的修复安全问题  

## web安全基础知识

- 用户输入都是不可信的, 一定要进行限制  
能够正确实现这个过滤，基本上解决掉了60%的安全问题   
怎样去实现这个限制?   
比如需要接收用户提交的购买商品的数量count, 就应该限制count一定是整数且是正整数, 防止用户购买到负金额商品, 进一步校验，大部分用户购买一个商品不可能大于1w,因此进一步限制count小于1w，防止后续数据太大导致溢出(在付款金额再次限制，金额为正)  


- 用户的身份进行校验  
一定要对请求增加合适的访问限制，对用户身份，访问范围进行限制，防止出现未授权或越权的访问   
这类问题非常常见，一定要重视起来  

- 数据存储在服务端
用好session这个技术，将涉及到敏感，私有等信息，一定存在服务端session中，防止被篡改  

基本以上3点，就是web安全知识的"铁三角", 理解这3点，基本上就已经入门  


## 应用web安全知识  

应用是最主要的，很多同学听了SQL注入以为就明白了，其实80%的还是不明白，或者只明白了一点点，在具体编写时，仍然还会出现问题   
最主要的原因是没有真正的理解，没有真正的实操过，这个是学习者的最大问题   

哪里去实际操作?   
这个大家可以使用[shepherd.anquanbiji.com](http://shepherd.anquanbiji.com/)这个项目, 里面有各种web安全基础问题，帮助零基础同学快速掌握web安全基础知识   
这套课程有很多等级,适合不同需求的同学进行学习和使用  

## 持续的学习和提高  
不要以为学完就已经很厉害了，其实还是在入门与门外之间徘徊，具体的场景下，仍然需要具体问题，一个不留神就会编写出安全漏洞, 持续的学习和提高才是永恒的  



## 课程完整列表 
[Insecure Direct Object References(不安全的直接对象引用)](http://shepherd.anquanbiji.com/vul/insecure-derect-object-references.html)  
[Poor Data Validation(失效的数据验证)](http://shepherd.anquanbiji.com/vul/poor-data-validation.html)  
[Security Misconfiguration(安全配置错误)](http://shepherd.anquanbiji.com/vul/security-misconfiguration.html)  
[Broken Authentication and Session Management(失效的身份认证和会话管理)](http://shepherd.anquanbiji.com/vul/Broken-Authentication-and-Session-Management.html)  
[Failure to Restrict URL Access(没有限制URL访问)](http://shepherd.anquanbiji.com/vul/Failure-to-Restrict-URL-Access.html)  
[Cross Site Scripting(跨站脚本攻击)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting.html)  
[Cross Site Scripting One(跨站脚本攻击 1)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-One.html)  
[Insecure Cryptographic Storage(不安全的加密存储)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage.html)  
[SQL Injection Lesson(SQL 注入)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Lesson.html)  
[Insecure Cryptographic Storage Challenge 1(不安全的加密存储 1)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage-Challenge-1.html)  
[Insecure Direct Object References Challenge One(不安全的直接对象引用 1)](http://shepherd.anquanbiji.com/vul/Insecure-Direct-Object-References-Challenge-One.html)  
[Poor Validation One(失效的数据认证 1)](http://shepherd.anquanbiji.com/vul/Poor-Validation-One.html)  
[SQL Injection Challenge One(SQL 注入 1)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-One.html)  
[Session Management Challenge One(会话管理 1)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-One.html)  
[Failure To Restrict URL Access Challenge 1(没有限制URL访问 1)](http://shepherd.anquanbiji.com/vul/Failure-To-Restrict-URL-Access-Challenge-1.html)  
[Cross-Site Request Forgery(跨站请求伪造)](http://shepherd.anquanbiji.com/vul/Cross-Site-Request-Forgery.html)  
[Unvalidated Redirects and Forwards(未验证的重定向和转发)](http://shepherd.anquanbiji.com/vul/Unvalidated-Redirects-and-Forwards.html)  
[SQL Injection Challenge Two(SQL注入挑战 2)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-Two.html)   
[SQL Injection Escaping Challenge(SQL 注入转义)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Escaping-Challenge.html)  
[Session Management Challenge Two(会话管理 2)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Two.html)   
[Cross Site Request Forgery Challenge One(跨站请求伪造 (CSRF) 1)](http://shepherd.anquanbiji.com/vul/Cross-Site-Request-Forgery-Challenge-One.html)  
[Session Management Challenge Three(会话管理 3)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Three.html)  
[Cross Site Scripting Two(跨站脚本攻击 2)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-Two.html)  
[Insecure Cryptographic Storage Challenge 2(不安全加密存储 2)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage-Challenge-2.html)  
[Insecure Direct Object References Challenge Two(不安全的直接对象引用 2)](http://shepherd.anquanbiji.com/vul/Insecure-Direct-Object-References-Challenge-Two.html)  
[Cross Site Scripting 3(跨站脚本 (XSS)3)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-3.html)   
[Insecure Direct Object Reference Bank Challenge(银行不安全的直接对象引用)](http://shepherd.anquanbiji.com/vul/Insecure-Direct-Object-Reference-Bank-Challenge.html)  
[SQL Injection Challenge Three(SQL 注入 3 )](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-Three.html)  
[Session Management Challenge Four(会话管理 4)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Four.html)   
[Cross Site Scripting 4(跨站脚本攻击 4)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-4.html)   
[SQL Injection 4(SQL 注入 4)](http://shepherd.anquanbiji.com/vul/SQL-Injection-4.html)  
[Insecure Cryptographic Storage Challenge 3(不安全加密存储 3)](http://shepherd.anquanbiji.com/vul/Insecure-Cryptographic-Storage-Challenge-3.html)  
[Poor Validation Two(失效的数据验证 2)](http://shepherd.anquanbiji.com/vul/Poor-Validation-Two.html)   
[Failure to Restrict URL Access Challenge 2(没有限制URL访问 2)](http://shepherd.anquanbiji.com/vul/Failure-to-Restrict-URL-Access-Challenge-2.html)   
[Cross Site Scripting 5(跨站脚本攻击 5)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-5.html)  
[SQL Injection Challenge 5(SQL 注入 5)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-5.html)  
[Cross Site Scripting Six(跨站脚本攻击 6)](http://shepherd.anquanbiji.com/vul/Cross-Site-Scripting-Six.html)   
[SQL Injection Challenge 6(SQL 注入 6)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-6.html)  
[Failure to Restrict URL Access Challenge 3(没有限制URL访问 3)](http://shepherd.anquanbiji.com/vul/Failure-to-Restrict-URL-Access-Challenge-3.html)   
[Session Management Challenge Six(会话管理 6)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Six.html)  
[Session Management Challenge Seven(会话管理 7)](http://shepherd.anquanbiji.com/vul/Session-Management-Challenge-Seven.html)  
[SQL Injection Challenge 7(SQL 注入 7)](http://shepherd.anquanbiji.com/vul/SQL-Injection-Challenge-7.html)  

[Insecure Data Storage(移动不安全的数据存储)](http://shepherd.anquanbiji.com/vul/app/Insecure-Data-Storage.html)  
[Mobile Reverse Engineering(移动逆向工程)](http://shepherd.anquanbiji.com/vul/app/Mobile-Reverse-Engineering.html)  
[Unintended Data Leakage(移动数据意外泄漏)](http://shepherd.anquanbiji.com/vul/app/Unintended-Data-Leakage.html)  
[Content Provider Leakage(内容提供者泄漏)](http://shepherd.anquanbiji.com/vul/app/Content-Provider-Leakage.html)  
[Client Side Injection(移动客户端注入)](http://shepherd.anquanbiji.com/vul/app/Client-Side-Injection.html)    
[Poor Authentication(失效的身份认证)](http://shepherd.anquanbiji.com/vul/app/Poor-Authentication.html)    
[Broken Crypto(失效的加密)](http://shepherd.anquanbiji.com/vul/app/Broken-Crypto.html)  
  





