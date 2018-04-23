<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<div class="lst">
   <ul class="tabs-lst j_container">
	   <c:forEach items="${productList }" var="product" varStatus="status">
	       <li>
	           <a href="<%=Configuration.getContextProperty("detailPath_WAP")%>${product.skuId}.html">
	               <div class="list-thumb">
	               	<c:if test="${product.promotion == 2}">
               		<span class="list-img-label">预售</span>
               		</c:if>
               		<img src="<%=Configuration.getContextProperty("picPath_WAP")%>${product.image}" onerror="this.src='<%=Configuration.getContextProperty("CSS_JS_PATH_WAP")%>images/default__logo_err280_280.jpg';this.onerror=null;">
	               </div>
	               <div class="list-descriptions">
	               	<div class="list-descriptions-wrapper">
	                   <div class="product-name">${product.title} ${product.attrVals }</div>
	                   <div class="price-spot">
	                   	<del>￥<fmt:formatNumber type="number" value="${product.mprice }" pattern="0.00" /></del>
                        <span class="product-price">￥<fmt:formatNumber type="number" value="${product.price}" pattern="0.00" /></span>
	                   </div>
	               	</div>
	              </div>
	          </a>
	      </li>
	   </c:forEach>
   </ul>
</div>
<div id="loading" class="listpic-tit" data-count="${pageCount }">
	<div class="listpic-tit-c">
		<a class="bottom">加载中...</a>
	</div>
</div>
