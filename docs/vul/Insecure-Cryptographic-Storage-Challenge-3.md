#  Insecure Cryptographic Storage Challenge 3(不安全加密存储 3)
 
## 题介绍

通关密钥是加密密钥的大写形式

## 功能实现 
通过提示知道需要输入的应该是一个base64编码数据   

请求数据包  
```
POST /challenges/2da053b4afb1530a500120a49a14d422ea56705a7e3fc405a77bc269948ccae1 HTTP/1.1

userData=IAAAAEkQBhEVBwpDHAFJGhYHSBYEGgocAw%3D%3D
```
对应代码   
```
try
{
	String userData = request.getParameter("userData"); //获得用户输入 
	log.debug("User Submitted - " + userData);
	
	log.debug("Decrypting user input");
	//Using level key as encryption key
	String decryptedUserData = decrypt(userData, levelResult); //解密数据  ，levelResult 是密钥 
	log.debug("Decrypted to: " + decryptedUserData);
	
	htmlOutput = "<h2 class='title'>" + bundle.getString("insecureCyrptoStorage.3.plaintextResult") + "</h2><p>" + bundle.getString("insecureCyrptoStorage.3.plaintextResult.message") + "<br/><br/><em>"
			+ Encode.forHtml(decryptedUserData)
			+ "</em></p>";
}
catch(Exception e)
{
	log.fatal(levelName + " - " + e.toString());
	htmlOutput = errors.getString("error.funky");
}
out.write(htmlOutput);

//具体解密函数 
 public static String decrypt(String hash, String key) throws Exception 
{
	try 
	{
		return new String(xor(org.apache.commons.codec.binary.Base64.decodeBase64(hash.getBytes()), key), "UTF-8");
	} 
	catch (java.io.UnsupportedEncodingException ex) 
	{
	  throw new IllegalStateException(ex);
	}
}

/**
* XOR Function
* @param input Byte array to be XOR'd
* @param key Encryption Key
* @return
*/
private static byte[] xor(final byte[] input, String theKey) 
{
final byte[] output = new byte[input.length];
final byte[] secret = theKey.getBytes();
int spos = 0;
for (int pos = 0; pos < input.length; pos += 1) 
{
  output[pos] = (byte) (input[pos] ^ secret[spos]);
  spos += 1;
  if (spos >= secret.length) {
	spos = 0;
  }
}
return output;
}
```


## 解题步骤  

加密是xor,只有0 xor X = X, 希望获得密钥，通关提示密钥是大写字母  
[查看ASCII表](http://ascii.911cha.com/)，大写字母的ASCII, 如 0101 1010(从左到右 第6和8位都是0)，小写字母的ASCII，如0111 1010(从左到右 第6是1,第8位是0) 所以大写字母 xor 对应小写字母 = 空格 ，即z xor Z = 空格  

因此将空格做base64编码提交即可， 空格长度，需要不断尝试  

## 总结  
如果不公开加密算法，黑盒测试还是有一些难度的，加密尽可能使用标准算法如AES，不要依赖加密算法不泄露，来保证安全 