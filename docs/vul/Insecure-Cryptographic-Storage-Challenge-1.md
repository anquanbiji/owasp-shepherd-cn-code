# Insecure Cryptographic Storage Challenge 1(不安全的加密存储 1

## 题介绍
这个原题感觉比较别扭，因此将其修改的更加容易理解.   
需要获得展示的密文的明文信息即可, 密文是使用凯撒密码进行加密,位移是5

## 功能实现 
无后端实现 

## 解题步骤  
主要理解凯撒密码是一个比较弱的加密方法，是将每个字符向后移动固定位数,即位移,如第一个字符r,解密时向前5个字符为m,其他字符一样计算即可

## 总结 

加密是常用而且非常有效的防御方法，但不安全的加密算法、不当的密钥管理，无法达到真正的安全

需要选择被验证过的加密算法: 如AES256,RSA等  
密钥禁止明文保存,如果能定期更换就更好了

## 汉化说明

- 添加汉化文件 
```
src/main/resources/i18n/challenges/insecureCryptoStorage/insecureCryptoStorage_zh.properties 
```
- 修改 
```
/src/main/webapp/challenges/x9c408d23e75ec92495e0caf9a544edb2ee8f624249f3e920663edb733f15cd7.jsp
```
删除 Ymj wjxzqy pjd ktw ymnx qjxxts nx ymj ktqqtbnsl xywnsl; 


