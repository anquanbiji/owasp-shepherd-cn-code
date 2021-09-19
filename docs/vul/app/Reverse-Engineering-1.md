#  Reverse Engineering 1(逆向工程 1)

## 题介绍
逆向ReverseEngineer1.apk ，找到App author's name

## 功能实现 


## 解题步骤  

```
jarsigner -verify -verbose -certs ReverseEngineer1.apk
```
原来是用这个命令，获得签名信息 

## 总结  

