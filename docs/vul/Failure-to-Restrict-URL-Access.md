# Failure to Restrict URL Access(没有限制URL访问)

## 题介绍
通关密钥保存在只有管理员才能访问的页面中，需要找到这个页面

## 功能实现 
在打开页面中，在html包含一个注释信息  
```
<div id="hiddenDiv" style="display: none;">
		<!-- 这仅显示给管理员 -->
		<a href="adminOnly/resultKey.jsp">
			管理员结果页面
		</a>
	</div>
```
从注释看，这个是只有管理员才能访问的页面

## 解题步骤  

- 需要访问 adminOnly/resultKey.jsp 这个路径，由于是相对路径，当前路径是 /lessons/oed23498d53ad1d965a589e257d8366d74eb52ef955e103c813b592dba0477e3.jsp  
- 所以最终需要访问的路径是 域名/lessons/adminOnly/resultKey.jsp  
- 打开后是空白页面，需要查看页面源码 找到通关密钥

## 总结  

系统中没有URL地址都应该设置适当的访问权限，不要仅仅依靠隐藏URL地址,这个是非常不可靠的