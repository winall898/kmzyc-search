<%@ page language="java"  import="com.kmzyc.search.facade.config.Configuration"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE HTML>
<html>
	<head>
	    <jsp:include page="/WEB-INF/jsp/common/b2bTemplate.jsp"></jsp:include>
	</head>
	<body>
        <jsp:include page="/WEB-INF/jsp/common/b2bHeadTemplate.jsp"></jsp:include>
		<!-- 面包屑 -->
		<tiles:insertDefinition name="b2bBreadcrumb"/>
		<!-- breadcrumb END -->
		<!-- 二级分类 -->
		<div class="l-w i-Search-new fn-clear">
			<div>
                <!--商品筛选-->
			  	<tiles:insertDefinition name="b2bSearchFiltrate"/>
                <!--搜索排序-->
				<tiles:insertDefinition name="b2bSearchSort"/>
				<!--产品列表-->
			    <c:choose>
				<c:when test="${productList == null || fn:length(productList) < 1}">
					<jsp:include page="./template/notMatch.jsp"></jsp:include>
			    </c:when>
				<c:otherwise>
					<tiles:insertDefinition name="b2bSearchProductList"/>
			    </c:otherwise>
				</c:choose>
				<!--产品分页-->
				<tiles:insertDefinition name="b2bSearchPagination"/>
			</div>
		</div>		
		<!-- oneclassify END -->	
										
		<!-- 底部导航链接 -->
		<jsp:include page="/WEB-INF/jsp/common/b2bFootTemplate.jsp"></jsp:include>
	</body>
</html>