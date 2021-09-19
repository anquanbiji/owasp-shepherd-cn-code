#  Content Provider Leakage(内容提供者泄漏)

## 题介绍

一个内容提供商是安卓用于提供进入到在一个中央存储库中的一个结构化数据集的.内容提供者的目的是由其他应用程序访问,然而,使用安卓调试桥,他们可以通过任何人访问的设备进行访问.

要在不通过应用程序的情况下查询一个内容提供者,可执行以下 adb 命令:

    adb devices
    adb connect [device IP]
    adb shell content query --uri [Content Provider URI]


## 功能实现 


## 解题步骤  

在虚拟机命令行 执行   
```
adb shell content query --uri content://com.somewhere.hidden.SecretProvider/data
```

## 总结  

合理使用Content Provider, 将其设置为私有  
对传入的参数进行安全检查  

