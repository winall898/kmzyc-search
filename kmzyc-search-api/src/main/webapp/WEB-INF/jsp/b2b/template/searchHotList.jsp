<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!--热门搜索-->
<!--热门搜索结束-->
<!--常见症状-->

<!--常见症状结束-->
<!--热销商品-->
<c:if test="${!empty hotSearchPaoduct && fn:length(hotSearchPaoduct) > 1 }">
	<div class="m-w m-w-noborder">
		<div class="wh"><h3>热销商品</h3></div>
		<div class="hot-goods">
			<c:forEach items="${hotSearchPaoduct }" var="product" varStatus="status">
				<dl>
		         <dt>
		         	<a href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml">
		         		<img width=50 height=50 src="<%=Configuration.getContextProperty("picPath_B2B")%>${product.image}" onerror="this.src='<%=Configuration.getContextProperty("CSS_JS_PATH_B2B")%>images/default__logo_err60_60.jpg'">
		         	</a>
		         </dt>
		         <dd><a href="<%=Configuration.getContextProperty("detailPath_B2B")%>${product.skuId}.shtml">${product.name}</a></dd>
		         <dd class="price">￥<fmt:formatNumber type="number" value="${product.price}" pattern="0.00" /></dd>
		       </dl>
			</c:forEach>
	    </div>
	</div>
</c:if>
<!--热销商品结束-->