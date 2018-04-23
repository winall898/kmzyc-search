<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.kmb2b.com/functions" prefix="km" %>
<c:if test="${!empty productList && fn:length(productList) > 0}">
<div class="l-w i-breadcrumb i-Search-new fn-clear">
	<c:set var="tempValue" scope="request" value=" "/>
	<c:forEach items="${breadMap }" var="item" varStatus="status">
		<c:choose>
			<c:when test="${status.index == 0}">
				<span style="font-size: 18px;font-weight: bold;font-family: microsoft yahei"><a href='${item.key}' style="text-decoration:none">${item.value}</a></span>
		    	<c:set var="tempValue" scope="request" value="${item.value}"/>
		    </c:when>
		    <c:otherwise>
		        <!-- 重复类目名称不显示 -->
		        <c:if test="${item.value != tempValue}">
				<span>&gt;<a style="text-decoration:none" href='${item.key}'>${item.value}</a></span>
				</c:if>
				<c:set var="tempValue" scope="request" value="${item.value}"/>
		    </c:otherwise>
		</c:choose>
	</c:forEach>
    <c:if test="${showKeyword != null && showKeyword != ''}">
    	<span class="search-title">&gt;
	    	<a href='javascript:void(0)' style="text-decoration:none">"${showKeyword}"</a>
    	</span>
    </c:if>
    <div class="label-all">
    	<c:forEach items="${selectedFilter}" var="filter" varStatus="status">
			<a href="${km:fqDel(km:qsRemove(km:qsRemove(searchURL, 'sort'), 'pn'), filter.code)}" class="label"><span>${filter.name}：</span><span class="l-2">${filter.value}</span><i></i></a>
		</c:forEach>
    </div>
    <span style="float: right;">共<i style="color: #d0111f;">${count}</i>条
	    <h1 class="search-title-r" style="float: none;display:inline-block;color: #d0111f;font-weight: normal;">
	    	<c:choose>
		   		<c:when test="${showKeyword != null && showKeyword != ''}">
		   			${showKeyword}
		   		</c:when>
		   		<c:otherwise>
		   			${tempValue}
		   		</c:otherwise>
		   	</c:choose>
	    </h1>  商品
    </span>
</div>
</c:if>