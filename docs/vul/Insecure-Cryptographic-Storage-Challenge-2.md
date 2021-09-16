#  Insecure Cryptographic Storage Challenge 2(不安全加密存储 2)

## 题介绍
本题使用了不安全的加密函数，直接可以获得明文数据 

## 功能实现 
发现本题并没有发送网络请求，因此查看html源代码，发现是javascript实现的加密  
```
	var input = $("#resultKeyAttempt").val();
	theKey = "kpoisaijdieyjaf";
	var theAlphabet =   "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz";

	// Validate theKey:
	theKey = theKey.toUpperCase();
	var theKeysLength = theKey.length;
	var i;
	var adjustedKey = "";
	for(i = 0; i < theKeysLength; i ++)
	{
		var currentKeyChar = theAlphabet.indexOf(theKey.charAt(i));
		if(currentKeyChar < 0)
			continue;
		adjustedKey += theAlphabet.charAt(currentKeyChar);
	}
	theKey = adjustedKey;
	theKeysLength = theKey.length;

	// Transform input:
	var inputLength = input.length;
	var output = "";
	var theKeysCurrentIndex = 0;
	for(i = 0; i < inputLength; i ++)
	{
		var currentChar = input.charAt(i);
		var currentCharValue = theAlphabet.indexOf(currentChar);
		if(currentCharValue < 0)
		{
			output += currentChar;
			continue;
		}
		var lowercase = currentCharValue >= 26 ? true : false;
		currentCharValue += theAlphabet.indexOf(theKey.charAt(theKeysCurrentIndex));
		currentCharValue += 26;
		if(lowercase)
			currentCharValue = currentCharValue % 26 + 26;
		else
			currentCharValue %= 26;
		output += theAlphabet.charAt(currentCharValue);
		theKeysCurrentIndex =(theKeysCurrentIndex + 1) % theKeysLength;
	}
	// Check result for validity
	$("#resultDiv").hide("fast", function(){
		if(output == "DwsDagmwhziArpmogWaSmmckwhMoEsmgmxlivpDttfjbjdxqBwxbKbCwgwgUyam")
			$('#resultDiv').html("<p>Yeah, that's correct</p>");
		else
			$('#resultDiv').html("<p>No, that's not correct</p>");
		$("#resultDiv").show("slow");
	});
```
从代码知道，最终得到的输出结果为 DwsDagmwhziArpmogWaSmmckwhMoEsmgmxlivpDttfjbjdxqBwxbKbCwgwgUyam  
密钥是 theKey 变量值 


## 解题步骤  

把密文变成明文输入，将 
```
currentCharValue += theAlphabet.indexOf(theKey.charAt(theKeysCurrentIndex));
```
修改为 
```
currentCharValue -= theAlphabet.indexOf(theKey.charAt(theKeysCurrentIndex));
```
即可  

## 总结  
不要使用不安全加密算法，密钥一定要妥善保管  

