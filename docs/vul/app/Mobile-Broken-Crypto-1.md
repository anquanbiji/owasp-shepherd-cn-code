# Mobile Broken Crypto 1(失效加密 1)

## 题介绍

应用程序使用了一种不推荐使用的加密算法（DES），打破了密钥管理的一条重要规则。关键在于对话。解密聊天以获取密钥。


## 功能实现 


## 解题步骤 

密钥是保存在 /data/data/com.mobshep.brokencrypto1/encrypt/desKey 中  
密钥是 123456789abcde

密文串  
```
<string name="message1">mJv7SjMKHUotNI1W1rZgeMkPY5YB6PQzi8oSwBOmEJ58FL3x1lTY0j9Yx6iqK4+i51ApyYLamzbIbL84lN/AIEyrKbnbaJ0yupysVDCXMmHu7Sivr6766ah7MpbPPGdZGpbn+QZ7pdkUi8x4VqX6zsC5XMPAbKQc</string>
<string name="message2">g915WutGOXkVflgYAOALfpumYUMFKIMUqmWDF0JJkD1EkEq2aNFIBHKg/sPquHMDxpaE5t+rqcmQREh2KtHyxz71qqlL3G54tUflHYDalSiMbxXkKbCnwVvLRu9y/CMB</string>
<string name="message3">XJygdr1j/z2eiNHwJwn7okCDJlpZ5JxHjER1mJhveOOaxjBtwkUMQOJPb0Sdnu2Yw3OGpxV+3OfqYL+o3n9JVOwIKCVJc3d0pdNGmHspCgT5kXYeaegzRWLj0ClVPznpeH0cuF43em6wLnKW6Yvfaa4EqJ9tmWMZKl1nex+fTaP8tHM/3MWV+w==</string>
```

## 总结  

