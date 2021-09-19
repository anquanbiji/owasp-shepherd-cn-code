#  Poor Validation Two(失效的数据验证 2)

## 题介绍
需要不花钱购买到第三方商品 

## 功能实现 

请求数据包  
```
POST /challenges/20e8c4bb50180fed9c1c8d1bf6af5eac154e97d3ce97e43257c76e73e3bbe5d5 HTTP/1.1


megustaAmount=0&trollAmount=1&rageAmount=-100&notBadAmount=0
```
对应实现代码   
```
int megustaAmount = validateAmount(Integer.parseInt(request.getParameter("megustaAmount"))); //强制转义 为大于0 
log.debug("megustaAmount - " + megustaAmount);
int trollAmount = validateAmount(Integer.parseInt(request.getParameter("trollAmount")));  //强制转义 为大于0 
log.debug("trollAmount - " + trollAmount);
int rageAmount = validateAmount(Integer.parseInt(request.getParameter("rageAmount"))); //强制转义 为大于0 
log.debug("rageAmount - " + rageAmount);
int notBadAmount = validateAmount(Integer.parseInt(request.getParameter("notBadAmount"))); //强制转义 为大于0 
log.debug("notBadAmount - " + notBadAmount);

//Working out costs
int megustaCost = megustaAmount * 30;
int trollCost = trollAmount * 3000;
int rageCost = rageAmount * 45;
int notBadCost = notBadAmount * 15;

htmlOutput = new String();

//Work Out Final Cost
int finalCost = megustaCost + rageCost + notBadCost + trollCost;

//Output Order
htmlOutput = "<h3 class='title'>" + bundle.getString("poorValidation.orderComplete") + "</h3>"
		+ "<p>" + bundle.getString("poorValidation.orderComplete.message") + "</p><br/>"
		+ "<p>" + bundle.getString("poorValidation.orderTotal") + " <a><strong>$" + finalCost + "</strong></a></p>";
if (finalCost <= 0 && trollAmount > 0) //总金额小于等于0，巨魔数量大于0 
{
	htmlOutput += "<br><p>" + bundle.getString("poorValidation.freeTrolls") + " - " + Hash.generateUserSolution(levelSolution, currentUser) + "</p>";
}

//验证了 输入数据，禁止输入负数
private static int validateAmount (int amount)
	{
		if(amount < 0)
			amount = 0;
		return amount;
	}
```
## 解题步骤  

由于数量只能输入正数，并且总和要小于0 ，是一个数据溢出的问题    
java  int: 4byte =  32 bit 有符号signed范围：2^31-1 ~ -2^31即：2147483647 ~ -2147483648   

所以巨魔输入 21474836 ，在乘以3000 足够溢出  

## 总结  

校验数据时，要严格限制范围，比如10~1000，设置上限和下限 

