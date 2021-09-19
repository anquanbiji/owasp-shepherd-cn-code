#  Client Side Injection(移动客户端注入)

## 题介绍
移动客户端如果接收用户输入，并且进行SQL查询，可能导致SQL注入  
原题  
```
客户端登录注入发生在当用户可以通过应用程序输入可执行的,可改变一个 App 运行查询的SQLite命令. 该课的 APK 使用 AES 加密它的数据库. 要通过攻击加密系统去窃取登录信息会相当困难,如若使用 SQL 注入来绕过登录将会更加简单.

过滤用户输入被认为是一种无效的防御客户端注入方法.然后替换掉关键词像 SELECT, WHERE, FROM 和点或空格. 过滤将只能使攻击者更难攻破注入缺陷,但却不能阻止入侵.

一个安全系统只是取决于其最薄弱的环节. 正如谚语一环软弱,全链不强.我们不需要知道用户的凭证. 我们只需要知道 APK 使用运行一条 SQL 查询语句作为经典的例子来验证一个用户名和密码是否存在, 通过在一条 SELECT 申明中附加用户名和密码到一个 String. 如果该申明返回 true,则该用户已登录. 

```

## 功能实现 

```
try {
			String dbPath = this.getDatabasePath("Members.db").getPath();

			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
					dbPass, null);

			String query = ("SELECT * FROM MEMBERS WHERE memName='" + username
					+ "' AND memPass = '" + password + "';");  //登录界面进行典型的SQL查询

			Cursor cursor = db.rawQuery(query, null);
			if (cursor != null) {
				if (cursor.getCount() <= 0) {
					cursor.close();
					return false;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast error = Toast.makeText(CSInjection.this,
					"An error occurred.", Toast.LENGTH_LONG);
			error.show();
			key.getText().clear();
			key.setHint("The key is only shown to authenticated users.");
			return false;
		}

```

## 解题步骤  

经典的万能密码 可以登录  
```
用户名 ' OR 1=1 or '1'='1  
密码  除单引号外 任意字符 
```

## 总结  

需要过滤用户输入 