#  Poor Validation One(失效的数据认证 1)

## 题介绍
需要实现空手套白狼，不花钱购买到第三个商品(巨魔)

## 功能实现 
点击页面购买按钮 ，获得请求包 

```
POST /challenges/ca0e89caf3c50dbf9239a0b3c6f6c17869b2a1e2edc3aa6f029fd30925d66c7e HTTP/1.1


megustaAmount=0&trollAmount=1&rageAmount=-100&notBadAmount=0
```
对应后端实现代码  src/main/java/servlets/module/challenge/PoorValidation1.java

```
int megustaAmount = Integer.parseInt(request.getParameter("megustaAmount")); //所有输入进行了强制转换成int  但没有校验是否是正数
int trollAmount = Integer.parseInt(request.getParameter("trollAmount"));  //购买巨魔的商品的数量
int rageAmount = Integer.parseInt(request.getParameter("rageAmount"));
int notBadAmount = Integer.parseInt(request.getParameter("notBadAmount"));

//Working out costs
int megustaCost = megustaAmount * 30;
int trollCost = trollAmount * 3000;
int rageCost = rageAmount * 45;
int notBadCost = notBadAmount * 15;

htmlOutput = new String();

//Work Out Final Cost
int finalCost = megustaCost + rageCost + notBadCost + trollCost;  //计算最终的费用

//Output Order
htmlOutput = "<h3 class='title'>" + bundle.getString("poorValidation.orderComplete")+ "</h3>"
		+ "<p>" + bundle.getString("poorValidation.orderComplete.message")+ "<br></p>"
		+ "<p>" + bundle.getString("poorValidation.orderTotal")+ " <a><strong>$" + finalCost + "</strong></a></p>";
if (finalCost <= 0 && trollAmount > 0) //最终费用小于等于0, 并且巨魔的费用大于1 则表示通关
{
	htmlOutput += "<br><p>" + bundle.getString("poorValidation.freeTrolls")+ " - " + Hash.generateUserSolution(levelSolution, currentUser) + "</p>";
}

```

## 解题步骤  
购买的数量没有校验是否是负数,因此只需要保证巨魔商品为正数，其他商品数量为负数，总价格小于0即可

## 总结  

对用户收入的数据进行校验是非常重要的，本题已经进行了部分校验，但由于校验不充分，没有考虑到负数情况，进而造成漏洞  
校验一定要充分，确保输入数据，在系统可用情况下的最小集合

## 汉化说明

添加汉化文件 
```
src/main/resources/i18n/challenges/poorValidation/poorValidationStrings_zh.properties
```