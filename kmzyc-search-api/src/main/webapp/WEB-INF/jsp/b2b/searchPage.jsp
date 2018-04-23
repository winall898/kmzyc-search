<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML>
<html>
	<head>
	    <jsp:include flush="true" page="/WEB-INF/jsp/common/b2bTemplate.jsp"/>
	</head>
	<body>
        <jsp:include flush="true" page="/html/common/b2b/search-head.jsp"/>
		<!-- 面包屑 -->
		<tiles:insertDefinition name="b2bBreadcrumb"/>
		<!-- 二级分类 -->
		<div class="l-w i-Search-new fn-clear">
			<div class="l-w">
                <!--商品筛选-->
			  	<tiles:insertDefinition name="b2bSearchFiltrate"/>
                <!--搜索排序-->
				<tiles:insertDefinition name="b2bSearchSort"/>
				<!--产品列表-->
				<c:choose>
				<c:when test="${productList == null || fn:length(productList) < 1}">
					<jsp:include flush="true" page="./template/notMatch.jsp"/>
			    </c:when>
				<c:otherwise>
					<tiles:insertDefinition name="b2bSearchProductList"/>
			    </c:otherwise>
				</c:choose>
				<!--产品分页-->
				<tiles:insertDefinition name="b2bSearchPagination"/>
			</div>
		</div>		
		<!-- 底部导航链接 -->		
		<jsp:include flush="true" page="/WEB-INF/jsp/common/b2bFootTemplate.jsp"/>
	</body>
</html>