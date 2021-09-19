#  Insecure Data Storage(移动不安全的数据存储)

## 题介绍
研发人员将敏感信息比如管理员账号和密码保存到app中,如果当手机丢失或者被root的设备上,将会导致敏感信息泄露  


## 功能实现 
app源代码  
```
 protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ids);
        createDatabase();
        insertKey();
    }

    public void createDatabase() {
        try {
            Members = this.openOrCreateDatabase("Members", MODE_PRIVATE, null);
            Members.execSQL("CREATE TABLE IF NOT EXISTS Members " +
                            "(id integer primary key, name VARCHAR, password VARCHAR);"
            );

            File database = getApplication().getDatabasePath("Members.db");

            if (!database.exists()) {
                Toast.makeText(this, "Database Created", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Database Missing", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("DB ERROR", "Error Creating Database");
        }
    }

    public void insertKey(){
        Members.execSQL("DELETE FROM Members;");
        Members.execSQL("INSERT INTO Members (name, password) VALUES ('Admin','Battery777');");
    }

```

创建一个Members数据库, 插入账号和密码  


## 解题步骤  

连接MobileShepherdVM3.2.3虚拟机   
Alt + F1  进入命令行   
cat  /data/data/com.mobshep.insecuredata/databases/Members  

查看敏感信息 

## 总结  

敏感信息不应该保存到app端 