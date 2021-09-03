# OWASP shepherd CN 学前基础知识

测试过程中，需要对浏览器发送的HTTP请求进行拦截、修改、重放，需要一个HTTP代理工具    
任何一款HTTP代理工具都可以  
如果你没有使用过类似工具，我们推荐你使用burp suite 这个代理软件 

## http代理工具(burp)使用 



### 介绍
是一个HTTP代理工具  

- 可以拦截浏览器发出的完整HTTP请求包
- 可以对HTTP请求包进行任意修改
- 可以重新发送请求包到服务器 

### 安装
[Burp Suite Releases 社区版下载地址](https://portswigger.net/burp/releases#community)  
下载后直接双击按照提示安装即可  
### 设置  
- burp suite设置
确认代理监听的端口，默认是(127.0.0.1:8080)
![](http://my.cdn.720life.cn/uploads/free/202108/16_42_58_90111.png)

- 浏览器配置 
以firefox为例  
1.在浏览器地址栏输入 about:preferences  打开配置界面  
2.在最下方找到 网络设置 选项，点击 设置 按钮  
3.手动配置代理 填写本地IP(127.0.0.1)和端口(8080),勾选将此代理用户HTTPS,如下图所示
![](http://my.cdn.720life.cn/uploads/free/202108/16_41_13_37767.png)

### 功能介绍
burp有很多功能，本课程主要代理功能和编/解码功能即可，其他功能可以参考[burp 官方文档](https://portswigger.net/burp/documentation/desktop/getting-started)

#### 代理功能
- 拦截浏览器请求 
点击 burp工具的 Proxy->intercept->intercept is off   
![](http://my.cdn.720life.cn/uploads/free/202108/16_45_27_14158.png)

使用浏览器发送http请求，在burp上进行拦截请求 效果如下  
![](http://my.cdn.720life.cn/uploads/free/202108/16_47_15_64816.png)

*在该页面可以对请求内容进行修改 *

然后点击 Forward 按钮，即可将请求发送到服务端 


#### 编码和解码功能
burp 的Decoder功能，可以进行基本的编码操作 
![](http://my.cdn.720life.cn/uploads/free/202108/17_27_17_84342.png)

## OWASP shepherd基础知识

- 系统默认的管理员账号和密码是admin/password
- 每个题做完都会获得一个通关密钥,需要提交到题最上面的输入框中  
- 只有做完当前题, 才允许做下一个题