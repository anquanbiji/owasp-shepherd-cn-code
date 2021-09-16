#  Session Management Challenge Three(会话管理 3)

## 题介绍
需要输入管理员账号和密码才能获得通关密钥

## 功能实现 
输入任意账号和密码，点击提交   
```
POST /challenges/t193c6634f049bcf65cdcac72269eeac25dbb2a6887bdb38873e57d0ef447bc3 HTTP/1.1


subUserName=admin&subUserPassword=password
```
对应实现代码  /src/main/java/servlets/module/challenge/SessionManagement3.java  

```
Object nameObj = request.getParameter("subUserName");      //获得用户名
Object passObj = request.getParameter("subUserPassword");  //获得密码 
String subName = new String();
String subPass = new String();
String userAddress = new String();
if(nameObj != null)
	subName = (String) nameObj;
if(passObj != null)
	subPass = (String) passObj;
log.debug("subName = " + subName);
log.debug("subPass = " + subPass);

log.debug("Getting ApplicationRoot");
String ApplicationRoot = getServletContext().getRealPath("");
log.debug("Servlet root = " + ApplicationRoot );

Connection conn = Database.getChallengeConnection(ApplicationRoot, "BrokenAuthAndSessMangChalThree");  //连接  BrokenAuthAndSessMangChalThree 数据库
log.debug("Checking credentials");
PreparedStatement callstmt;

log.debug("Committing changes made to database");
callstmt = conn.prepareStatement("COMMIT");
callstmt.execute();
log.debug("Changes committed.");

callstmt = conn.prepareStatement("SELECT userName, userAddress, userRole FROM users WHERE userName = ?"); //查询用户名 判断用户角色是否是 admin 
callstmt.setString(1, subName);
log.debug("Executing findUser");
ResultSet resultSet = callstmt.executeQuery();
if(resultSet.next())
{
	log.debug("User found");
	if(resultSet.getString(3).equalsIgnoreCase("admin"))
	{
		log.debug("Admin Detected");
		callstmt = conn.prepareStatement("SELECT userName, userAddress, userRole FROM users WHERE userName = ? AND userPassword = SHA(?)");  //进而判断用户名和密码是否正确 
		callstmt.setString(1, subName);
		callstmt.setString(2, subPass);
		log.debug("Executing authUser");
		ResultSet resultSet2 = callstmt.executeQuery();
		if(resultSet2.next())
		{
			log.debug("Successful Admin Login");
			// Get key and add it to the output

```
从实现上来看，没有发现明显漏洞，数据库查询是预编译的    

界面上还有个功能，是修改密码，猜测可能是这个地方有问题   
```
POST /challenges/b467dbe3cd61babc0ec599fd0c67e359e6fe04e8cdc618d537808cbb693fee8a HTTP/1.1

Cookie: current=WjNWbGMzUXhNZz09; JSESSIONID=A2479A1D9F44509E20B35106989DFFAF; token=22792033228396859635296159069930202705

newPassword=test1%40qq.com
```
对应实现代码  src/main/java/servlets/module/challenge/SessionManagement3ChangePassword.java  

```
Cookie userCookies[] = request.getCookies();
int i = 0;
Cookie theCookie = null;
for(i = 0; i < userCookies.length; i++)
{
	if(userCookies[i].getName().compareTo("current") == 0)  //获得cookie中的current字段值 
	{
		theCookie = userCookies[i];
		break; //End Loop, because we found the token
	}
}
Object passNewObj = request.getParameter("newPassword");
String subName = new String();
String subNewPass = new String();
if(theCookie != null)
	subName = theCookie.getValue();
if(passNewObj != null)
	subNewPass = (String) passNewObj;
log.debug("subName = " + subName);
//Base 64 Decode
try
{
	byte[] decodedName = Base64.decodeBase64(subName);  // current进行两次base64解码 
	subName = new String(decodedName, "UTF-8");
	decodedName = Base64.decodeBase64(subName);
	subName = new String(decodedName, "UTF-8");
}

if(subNewPass.length() >= 6)  //新密码长度 要大于等于6位
{
	log.debug("Getting ApplicationRoot");
	String ApplicationRoot = getServletContext().getRealPath("");
	
	Connection conn = Database.getChallengeConnection(ApplicationRoot, "BrokenAuthAndSessMangChalThree");
	log.debug("Changing password for user: " + subName);
	log.debug("Changing password to: " + subNewPass);
	PreparedStatement callstmt;
	
	callstmt = conn.prepareStatement("UPDATE users SET userPassword = SHA(?) WHERE userName = ?"); //更新用户名(来源cookie)和密码 
	callstmt.setString(1, subNewPass);
	callstmt.setString(2, subName);
	log.debug("Executing changePassword");
	callstmt.execute();

```
## 解题步骤  

通过修改密码功能，修改cookie中的current值为admin

## 总结  

永远不能信任客户端传递的数据，使用强加密替代base64编码等 