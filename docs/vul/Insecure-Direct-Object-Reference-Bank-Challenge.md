#  Insecure Direct Object Reference Bank Challenge(银行不安全的直接对象引用)

## 题介绍
这个题需要注册一个个人账户，并且把个人账户存款增加到大于5000000  

## 功能实现 
这个题有很多过程，注册、登录都没有发现安全问题 

在查看个人存款功能时  
```
POST /challenges/1f0935baec6ba69d79cfb2eba5fdfa6ac5d77fadee08585eb98b130ec524d00cCurrentBalance HTTP/1.1


accountNumber=2
```

发现当前账户id是2，自然想到还有个账号id=1，发现id=1金额比较多   

在看转账功能 
```
POST /challenges/1f0935baec6ba69d79cfb2eba5fdfa6ac5d77fadee08585eb98b130ec524d00cTransfer HTTP/1.1


senderAccountNumber=2&recieverAccountNumber=1&transferAmount=500000
```

校验了转账的金额必须大于0 ，发送者余额必须大于转账金额
```
String senderAccountNumber = request.getParameter("senderAccountNumber");
log.debug("Sender Account Number - " + senderAccountNumber);
String recieverAccountNumber = request.getParameter("recieverAccountNumber");
log.debug("Reciever Account Number - " + recieverAccountNumber);
String transferAmountString = request.getParameter("transferAmount");
log.debug("Transfer Amount - " + transferAmountString);
float tranferAmount = Float.parseFloat(transferAmountString);

//Data Validation
//Positive Transfer Amount?
if(tranferAmount > 0)  //转账金额为正 
{
	//Sender Account Has necessary funds?
	float senderFunds = DirectObjectBankLogin.getAccountBalance(senderAccountNumber, applicationRoot);
	if((senderFunds-tranferAmount) > 0) //余额不能为负
	{
		//Check Receiver Account Exists
		try 
		{
			float recieverAccountBalanace = DirectObjectBankLogin.getAccountBalance(recieverAccountNumber, applicationRoot);
			if(recieverAccountBalanace >= 0)
				performTransfer = true;
		}
		catch(Exception e)
		{
			log.debug("Reciever Account does not exist. Cancelling");
			errorMessage = bundle.getString("transfer.error.recieverNotFound");
		}
	}
	else
		errorMessage = bundle.getString("transfer.error.notEnoughCash");
}
else
	errorMessage = bundle.getString("transfer.error.moreThanZero");


```

问题是没有校验转账发送者和接收者，导致任意添加转账的发送者和接收者 

## 解题步骤  
将接收者recieverAccountNumber 修改为自己id 即可 

## 总结  

不要信任客户端传递的数据 