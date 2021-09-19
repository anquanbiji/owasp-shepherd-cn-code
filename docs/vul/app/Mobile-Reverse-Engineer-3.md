#  Mobile Reverse Engineer 3(逆向工程 3)

## 题介绍

通过逆向获得密钥  

## 功能实现 
具体逆向方法与[逆向1](Mobile-Reverse-Engineering.md)相同   
打开Reverse_Engineering4.class  即可看到 具体算法  
```
paramView = new StringBuilder("Z");
paramView.append("Y");
paramView.append("4399e0f52227dbab4d74bbd3dd2e3c4c".substring(0, "4399e0f52227dbab4d74bbd3dd2e3c4c".length() / 2));
paramView.append("1");
paramView.append("C");
paramView.reverse();
paramView.append("8");
if (this.etKeyCheck.getText().toString().contentEquals(paramView))
{
  Toast.makeText(this, "Valid Key", 1).show();
  return;
}
```

## 解题步骤  

如上

## 总结  