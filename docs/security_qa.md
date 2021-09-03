# 基础安全测试 - owasp shepherd CN项目
作为开发和测试人员,如果想构建安全的系统，首先需要在实际web项目能够中查找到对应漏洞，进一步加深对漏洞的理解。  
owasp shepherd CN 项目就是一套集合了常见安全漏洞的web系统，希望结合真实存在的安全漏洞，让开发和测试同学提高安全意识，进而打造安全系统    

**owasp shepherd CN  是一套集成了常见web漏洞的系统，每个页面是对应一个安全漏洞，包括安全漏洞的介绍，用户需要根据提示信息利用漏洞，找到通关密钥, 提交到系统，如果提交正确，表示对该漏洞已经完整掌握，然后进入到下一个页面/漏洞知识点。**    

## 项目介绍
owasp shepherd CN 是owasp shepherd 项目的中文版本, 致力于帮助研发和测试同学提高web安全意识, 开发安全的web系统
对原项目修改如下:  

- 对shepherd未汉化的题说明信息，进行汉化
- 对shepherd理解起来比较困难的题,进行修改,让使用者更加容易理解题意
- 仅保留了适合从事或将来从事开发和测试同学的基础web安全知识
- 限制新注册账号使用时间,督促参与者尽快完成学习过程
- 提供一套完整的学习笔记记录,包括漏洞产生的原因，源代码解析，漏洞修复建议等

## 项目对象
本项目是最基本的常见web安全漏洞实例，适合有开发基础的任何同学使用，包括:  

- 在职web系统开发及测试同学
- 在校学生
- 对安全基础知识感兴趣的任何同学

## 项目目标
- 结合具体实例系统，帮助开发、测试同学掌握常见的web安全漏洞，提升安全意识
- 真正明白安全漏洞产生原因及防护方法
- 将安全开发知识应用到日常工作中,打造安全系统

## 项目收益  

- 深刻理解"一切用户的输入都可能是有害的", 学习使用代理工具, 修改浏览器发送的HTTP请求包  
- 掌握常见的web安全漏洞(基本涵盖了OWASP十大web应用风险)的危害及测试方法
- 结合示例项目源代码,加深理解安全漏洞产生原因,及修复方案,进而打造安全系统

## 项目课程
主要使用 Field Training 和 Private两个等级的安全漏洞，列表如下

- SQL注入(SQL Injection)
- SQL注入中级版(SQL Injection Challenge One)
- 跨站脚本漏洞(Cross Site Scripting)
- 跨站脚本初级版(Cross Site Scripting 1)
- 安全错误配置(Security Misconfiguration)
- 没有限制URL访问(Failure to Restrict URL Access)
- 没有限制URL访问中级版(Failure To Restrict URL Access Challenge 1)
- 失效的身份验证和会话管理（Broken Authentication and Session Management)
- 会话管理中级版(Session Management Challenge One)
- 跨站请求伪造(Cross Site Request Forgery)
- 不安全加密存储(Insecure Cryptographic Storage)
- 不安全加密存储中级(Insecure Cryptographic Storage Challenge 1)
- 不安全直接对象引用(Insecure Direct Object References)
- 不不安全直接对象引用中级版(Insecure Direct Object References Challenge One)
- 失效的数据验证(Poor Data Validation)
- 失效的数据验证中级版(Poor Validation One)


每个课程，都有详细介绍，帮助参与者学习知识点(适合零基础同学)  
当参与者多次闯关失败，则会显示提示信息，进一步帮助参与者学习和理解课程  

## 参与项目
本项目是**半收费**项目,具体说明如下:  

- 课程开始前,参与学员需要交 49.9元,获得注册账号  
- 学员在一周内完成所有课程(通关), 则表示学有所成, 将**全额退费**
- 学员在二周内完成所有课程(通关), 则表示学有所成, 将退还90%费用
- 学员在三周内完成所有课程(通关), 则表示学有所成, 将退还50%费用
- 学员在三周以后完成所有课程,则不退还任何费用

一个账号的有效期为一个月,重新申请则认为是新成员

**为什么半收费**  
收费课程更有利学员学习   
同时考虑到本项目并非为了盈利,因此本项目采用半收费的形式  
**真心的希望各位参与同学真正的学习到安全知识,应用到将来的开发项目中** 

*如果您是学生或者觉得本项目参与门槛过高,可在联系客服微信时说明,并给出您希望的费用,我们认为完全免费很难达到学习的目的，所以我们拒绝完全免费学习，感谢大家理解*

**具体参加流程**  
联系客服->付费->获得账号和密码->登录后台进行学习->完成学习->联系客服退费->账号在一个月后过期

**客服企业微信**  
加客服微信时,备注:付费学习  
![shepherd CN客服](http://my.cdn.720life.cn/uploads/free/202108/16_16_30_77086.png)


## 项目支持  
该项目是优秀的学习材料，非常适合开发和测试同学进行学习，使用场景包括但不限于  

- 各大高校软件开发相关专业同学
- 各公司开发和测试同学

如果您是老师/公司技术人员想本地化部署本项目，请联系我们  
如果您对本项目有任何建议，请联系我们

owasp社区在持续的更新和维护，本工作室致力于维护该项目的中文版本!

## 项目网址 
[owasp shepherd cn online](http://shepherd-cn.anquanbiji.com) 

## 致谢
感谢owasp shepherd 项目组，开发和维护该套学习材料  
[owasp shepherd github](https://github.com/OWASP/SecurityShepherd "owasp shepherd 源代码")  
[owasp shepherd project](https://wiki.owasp.org/index.php/OWASP_Security_Shepherd "owasp shepherd 项目介绍")

