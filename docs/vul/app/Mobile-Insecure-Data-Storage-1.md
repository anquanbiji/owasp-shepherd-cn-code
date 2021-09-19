# Mobile Insecure Data Storage 1(不安全的数据存储 1)

## 题介绍
base64编码的管理员密码保存在了数据库中

## 功能实现 
```

public void insertData(){

	Users.execSQL("DELETE FROM Users;");

	Users.execSQL("INSERT INTO Users (name, password) VALUES ('Tyrkyr','ZG9jaGRvY2hkb2No');");
	Users.execSQL("INSERT INTO Users (name, password) VALUES ('ToothBrush','MmNvb2w0dWxvbD8=');");
	Users.execSQL("INSERT INTO Users (name, password) VALUES ('TroolMann','QnJpZGdlcw==');");
	Users.execSQL("INSERT INTO Users (name, password) VALUES ('Patrick','ZGlub3NhdXI=');");
	Users.execSQL("INSERT INTO Users (name, password) VALUES ('bottles','cGFzc3dvcmQxMjM0');");
	Users.execSQL("INSERT INTO Users (name, password) VALUES ('Root','V2Fyc2hpcHNBbmRXcmVuY2hlcw==');");
}
```

## 解题步骤 

读取数据,base64解码 

## 总结  

base64是不安全的