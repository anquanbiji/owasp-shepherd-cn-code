#   Session Management Challenge Four(会话管理 4)

## 题介绍
需要获得一个管理身份 

## 功能实现 
请求数据包  
```
POST /challenges/ec43ae137b8bf7abb9c85a87cf95c23f7fadcf08a092e05620c9968bd60fcba6 HTTP/1.1

Cookie: current=WjNWbGMzUXhNZz09; SubSessionID=TURBd01EQXdNREF3TURBd01EQXdNUT09; JSESSIONID=AFBC4851A5AC73AFE2032E487ADE0EF5; token=-32864054630884368385257168535939963911

userId=0000000000000001&useSecurity=true
```

对应代码 src/main/java/servlets/module/challenge/SessionManagement4.java

```
Cookie userCookies[] = request.getCookies();
int i = 0;
Cookie theCookie = null;
for(i = 0; i < userCookies.length; i++)
{
	if(userCookies[i].getName().compareTo("SubSessionID") == 0)  //获得cookie中的 SubSessionID 
	{
		theCookie = userCookies[i];
		break; //End Loop, because we found the token
	}
}
String htmlOutput = null;
if(theCookie != null)
{
	log.debug("Cookie value: " + theCookie.getValue());
	//Decode Twice
	byte[] decodedCookieBytes = Base64.decodeBase64(theCookie.getValue()); //两次解码 获得明文 
	String decodedCookie = new String(decodedCookieBytes, "UTF-8");
	decodedCookieBytes = Base64.decodeBase64(decodedCookie.getBytes());
	decodedCookie = new String(decodedCookieBytes, "UTF-8");
	log.debug("Decoded Cookie: " + decodedCookie);
	if(decodedCookie.equals("0000000000000001")) //Guest Session
	{
		log.debug("Guest Session Detected");
	}
	else if (decodedCookie.equals("0000000000000021")) //Admin Session //判断是否是管理员 
	{
		log.debug("Admin Session Detected: Challenge Complete");
```
## 解题步骤

替换cookie的SubSessionID 值为 base64(base64(0000000000000021))

## 总结  

客户端输入内容是不可信的，不要使用不安全的编码在客户端保存敏感信息 